package net.trainingsoase.bedwars.item.specials.tntsheep;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Optional;

public class Sophiaee {

    private Player player = null;
    private ITNTSheep sheep = null;

    private final Bedwars bedwars;

    public Sophiaee(Bedwars bedwars) {
        this.bedwars = bedwars;
    }

    private Player findTargetPlayer(Player player) {
        Player foundPlayer = null;

        double distance = Double.MAX_VALUE;

        Optional<BedwarsTeam> team = bedwars.getTeamService().getTeam(player);

        if(team.isPresent()) {
            ArrayList<Player> possibleTargets = new ArrayList<>(Bukkit.getOnlinePlayers());
            possibleTargets.removeAll(team.get().getPlayers());
            possibleTargets.removeAll(((IngamePhase)bedwars.getLinearPhaseSeries().getCurrentPhase()).getSpectatorService().getPlayers());

            for (Player p : possibleTargets) {
                if (player.getWorld() != p.getWorld()) {
                    continue;
                }
                double dist = player.getLocation().distance(p.getLocation());
                if (dist < distance) {
                    foundPlayer = p;
                    distance = dist;
                }
            }
        }

        return foundPlayer;
    }

    public Material getActivatedMaterial() {
        return null;
    }

    public int getEntityTypeId() {
        return 91;
    }

    public Material getItemMaterial() {
        return Material.MONSTER_EGG;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ITNTSheep getSheep() {
        return this.sheep;
    }

    public void run(Location startLocation) {

        ItemStack usedStack = null;

        usedStack = player.getInventory().getItemInHand();
        if (((SpawnEgg) usedStack.getData()).getSpawnedType() != EntityType.SHEEP) {
            return;
        }
        usedStack.setAmount(usedStack.getAmount() - 1);
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), usedStack);

        player.updateInventory();

        Optional<BedwarsTeam> team = bedwars.getTeamService().getTeam(player);

        if(team.isPresent()) {
            Player targetPlayer = this.findTargetPlayer(this.player);
            if (targetPlayer == null) {
                System.out.println("No target found");
                return;
            }

            TNTSheepPlaceEvent event = new TNTSheepPlaceEvent(this.player, targetPlayer, startLocation);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            final Player target = event.getTargetPlayer();
            final Location start = event.getStartLocation();

            // as task
            new BukkitRunnable() {

                @Override
                public void run() {
                    final Sophiaee that = Sophiaee.this;

                    try {
                        // register entity
                        Class<?> tntRegisterClass = TNTSheepRegister.class;
                        ITNTSheepRegister register = (ITNTSheepRegister) tntRegisterClass.newInstance();
                        Sophiaee.this.sheep = register.spawnCreature(bedwars, that, start, Sophiaee.this.player, target,
                                team.get().getColorData().getDyeColor());

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                that.getSheep().remove();
                            }
                        }.runTaskLater(bedwars, 200);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.runTask(bedwars);
        }
    }

    public void updateTNT() {
        new BukkitRunnable() {

            @Override
            public void run() {
                final Sophiaee that = Sophiaee.this;

                if (that.sheep == null) {
                    return;
                }

                TNTPrimed old = that.sheep.getTNT();
                final int fuse = old.getFuseTicks();

                if (fuse <= 0) {
                    return;
                }

                final Entity source = old.getSource();
                final Location oldLoc = old.getLocation();
                final float yield = old.getYield();
                old.leaveVehicle();
                old.remove();

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        TNTPrimed primed = (TNTPrimed) player.getWorld().spawnEntity(oldLoc,
                                EntityType.PRIMED_TNT);
                        primed.setFuseTicks(fuse);
                        primed.setYield(yield);
                        primed.setIsIncendiary(false);
                        that.sheep.setPassenger(primed);
                        that.sheep.setTNT(primed);
                        that.sheep.setTNTSource(source);

                        if (primed.getFuseTicks() >= 60) {
                            that.updateTNT();
                        }
                    }
                }.runTaskLater(bedwars, 3L);
            }

        }.runTaskLater(bedwars, 60L);
    }
}
