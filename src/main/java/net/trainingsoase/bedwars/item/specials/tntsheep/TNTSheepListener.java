package net.trainingsoase.bedwars.item.specials.tntsheep;

import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.phase.IngamePhase;
import net.trainingsoase.bedwars.team.BedwarsTeam;
import net.trainingsoase.hopjes.api.phase.Phase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class TNTSheepListener implements Listener {

    private final Bedwars bedwars;

    public TNTSheepListener(Bedwars bedwars) {
        this.bedwars = bedwars;

        try {
            // register entities
            Class<?> tntRegisterClass = TNTSheepRegister.class;
            ITNTSheepRegister register = (ITNTSheepRegister) tntRegisterClass.newInstance();
            register.registerEntities(91);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ITNTSheep || event.getEntity() instanceof Sheep) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM) || event.getCause().equals(EntityDamageEvent.DamageCause.VOID)
                    || event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                event.setDamage(0.0);
                event.setCancelled(true);
            }

            if (event.getEntity() instanceof ITNTSheep || event.getEntity() instanceof Sheep) {
                event.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onExplode(final EntityExplodeEvent event) {
        if(bedwars.getLinearPhaseSeries().getCurrentPhase() instanceof IngamePhase) {
            IngamePhase ingamePhase = (IngamePhase) bedwars.getLinearPhaseSeries().getCurrentPhase();
            event.blockList().removeIf(block -> !ingamePhase.getBreakBlocks().contains(block));
            /*if(event.getEntity().getVehicle() instanceof Sheep) {
                ((Sheep) event.getEntity().getVehicle()).damage(100);
            }*/
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();

        Sophiaee creature = new Sophiaee(bedwars);

        if (event.getMaterial() != creature.getItemMaterial()) {
            return;
        }

        if (((IngamePhase)bedwars.getLinearPhaseSeries().getCurrentPhase()).getSpectatorService().isSpectator(player)) {
            return;
        }

        Location startLocation = null;
        if (event.getClickedBlock() == null
                || event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) {
            startLocation = player.getLocation().getBlock()
                    .getRelative(getCardinalDirection(player.getLocation())).getLocation();
        } else {
            startLocation = event.getClickedBlock().getRelative(BlockFace.UP).getLocation();
        }

        creature.setPlayer(player);
        creature.run(startLocation);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractOtherUser(PlayerInteractEntityEvent event) {
        if (event.getPlayer() == null) {
            return;
        }

        if (event.getRightClicked() == null) {
            return;
        }

        if (event.getRightClicked() instanceof ITNTSheep) {
            event.setCancelled(true);
            return;
        }

        if (event.getRightClicked() instanceof Sheep) {
            event.setCancelled(true);
            return;
        }

        if (event.getRightClicked().getVehicle() != null
                && event.getRightClicked().getVehicle() instanceof ITNTSheep) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() instanceof Sheep) {
            event.setCancelled(true);
        }

        if(event.getRightClicked() instanceof ITNTSheep) {
            event.setCancelled(true);
        }
    }

    private BlockFace getCardinalDirection(Location location) {
        double rotation = (location.getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_EAST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.SOUTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_WEST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.NORTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.NORTH;
        }
    }
}
