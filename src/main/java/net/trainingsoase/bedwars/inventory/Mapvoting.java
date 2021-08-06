package net.trainingsoase.bedwars.inventory;

import de.dytanic.cloudnet.common.collection.Pair;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.bedwars.voting.VoteMap;
import net.trainingsoase.oreo.inventory.InventoryLayout;
import net.trainingsoase.oreo.inventory.InventoryRows;
import net.trainingsoase.oreo.inventory.InventorySlot;
import net.trainingsoase.oreo.inventory.translated.GlobalTranslatedInventoryBuilder;
import net.trainingsoase.oreo.inventory.util.LayoutCalculator;
import net.trainingsoase.oreo.item.builder.ColoredBuilder;
import net.trainingsoase.oreo.item.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

import static net.trainingsoase.bedwars.Bedwars.GLASS_PANE;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class Mapvoting {

    private final GlobalTranslatedInventoryBuilder mapInventoryBuilder;

    private final Bedwars bedwars;

    public final Map<String, VoteMap> mapVotes;

    private String playedMap;

    public Mapvoting(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.mapVotes = new HashMap<>();

        InventoryLayout inventoryLayout = new InventoryLayout(InventoryRows.THREE);

        inventoryLayout.setNonClickItems(LayoutCalculator.quad(0, InventoryRows.THREE.getSize() - 1), GLASS_PANE);

        for (String mapName : MapHelper.getInstance(bedwars).getMapNames()) {
            VoteMap voteMap = new VoteMap(mapName, 0);
            if (!mapVotes.containsKey(mapName)) {
                mapVotes.put(mapName, voteMap);
            }
        }

        mapInventoryBuilder = new GlobalTranslatedInventoryBuilder(InventoryRows.THREE, bedwars.getLanguageProvider());
        mapInventoryBuilder.registerListener(bedwars);
        mapInventoryBuilder.setTitleData("inventory_mapvoting");
        mapInventoryBuilder.setInventoryLayout(inventoryLayout);
        mapInventoryBuilder.setTaskChainFactory(bedwars);
        mapInventoryBuilder.setDataLayoutChainFunction((dataLayoutTaskChain ->
                dataLayoutTaskChain.async(dataLayout -> {
                    dataLayout = dataLayout == null ? new InventoryLayout(InventoryRows.THREE) : dataLayout;

                    int slot = 10;
                    int[] slots = new int[]{10,11,12,13,14,15,16};

                    dataLayout.blank(slots);

                    for (VoteMap voteMap : this.mapVotes.values()) {
                        dataLayout.setItem(slot, new InventorySlot(new ItemBuilder(Material.EMPTY_MAP).setDisplayName(voteMap.getName()).setAmount(voteMap.getVotes())
                                .build()).setClickListener(handleMapClick()));
                        slot++;
                    }

                    return dataLayout;
                })));
    }

    private void updateMapInventory() {
        mapInventoryBuilder.invalidateDataLayout();
    }

    public void removePlayerFromVoting(Player player) {
        for (VoteMap value : mapVotes.values()) {
            if(value.getPlayers().remove(player)) {
                value.removeVote();
                updateMapInventory();
                break;
            }
        }
    }

    private Consumer<InventoryClickEvent> handleMapClick() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();

            for (VoteMap value : mapVotes.values()) {
                if(value.getPlayers().remove(player)) {
                    value.removeVote();
                    break;
                }
            }

            VoteMap voteMap = mapVotes.get(event.getCurrentItem().getItemMeta().getDisplayName());
            voteMap.addPlayer(player);
            voteMap.addVote();

            updateMapInventory();

            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        };
    }

    public synchronized String getVotedMap() {
        Pair<VoteMap, Integer> votes = new Pair<>();

        votes.setFirst(null);
        votes.setSecond(0);

        for (VoteMap map : mapVotes.values()) {
            if (map.getVotes() > votes.getSecond()) {
                votes.setFirst(map);
                votes.setSecond(map.getVotes());
            }
        }

        if (votes.getFirst() == null) {
            playedMap = pickRandomMap();
            return playedMap;
        } else {
            playedMap = votes.getFirst().getName();
            return votes.getFirst().getName();
        }
    }

    private String pickRandomMap() {
        List<String> mapNames = MapHelper.getInstance(bedwars).getMapNames();
        Collections.shuffle(mapNames);
        return mapNames.get(0);
    }

    public Inventory getMapVotingInventory(Locale locale) {
        return mapInventoryBuilder.getInventory(locale);
    }

    public String getPlayedMap() {
        return playedMap;
    }
}
