package net.trainingsoase.bedwars.item;

import at.rxcki.strigiformes.TranslatedObjectCache;
import net.trainingsoase.bedwars.map.MapHelper;
import net.trainingsoase.data.i18n.LanguageProvider;
import net.trainingsoase.oreo.item.builder.ItemBuilder;
import net.trainingsoase.oreo.item.builder.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class JoinItems {

    private static JoinItems instance;

    private final TranslatedObjectCache<ItemStack> teamSelectionItem;
    private final TranslatedObjectCache<ItemStack> votingItem;
    private final TranslatedObjectCache<ItemStack> guardianItem;
    private final TranslatedObjectCache<ItemStack> mapVotingItem;
    private final TranslatedObjectCache<ItemStack> lobbyItem;

    public JoinItems(LanguageProvider<CommandSender> languageProvider) {
        instance = this;

        this.teamSelectionItem = new TranslatedObjectCache<>(locale -> new SkullBuilder(SkullBuilder.SkullType.PLAYER)
                .setSkinOverValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThkYWExZTNlZDk0ZmYzZTMzZTFkNGM2ZTQzZjAyNGM0N2Q3OGE1N2JhNGQzOGU3NWU3YzkyNjQxMDYifX19")
                .setDisplayName(languageProvider.getTextProvider().getString("item_teamselector", locale))
                .build());

        this.votingItem = new TranslatedObjectCache<>(locale -> new SkullBuilder(SkullBuilder.SkullType.PLAYER)
                .setSkinOverValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZWI3N2Q0ZDI1NzI0YTljYWYyYzdjZGYyZDg4Mzk5YjE0MTdjNmI5ZmY1MjEzNjU5YjY1M2JlNDM3NmUzIn19fQ==")
                .setDisplayName(languageProvider.getTextProvider().getString("item_voting", locale))
                .build());

        this.guardianItem = new TranslatedObjectCache<>(locale -> new ItemBuilder(Material.STONE_SWORD)
                .setDisplayName(languageProvider.getTextProvider().getString("item_guardian", locale))
                .setUnbreakable(true)
                .build());

        this.mapVotingItem = new TranslatedObjectCache<>(locale -> new SkullBuilder(SkullBuilder.SkullType.PLAYER)
                .setSkinOverValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU1NzkzZjBjYzQwYTkzNjgyNTI3MTRiYzUyNjNhNWMzZGYyMjMzYmRkZjhhNTdlM2Q4ZDNmNTRhZjY3MjZjIn19fQ==")
                .setDisplayName(languageProvider.getTextProvider().getString("item_mapvoting", locale))
                .build());

        this.lobbyItem = new TranslatedObjectCache<>(locale -> new SkullBuilder(SkullBuilder.SkullType.PLAYER)
                .setSkinOverValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjk3MjBhMTgyYzAzZmQyZDQ2NGY2MzczOTNjNmE3ZmI1MTQ1ZmUyZDJlZGMwZmVlODVkYWU0Yzk3YjRhZTgyMyJ9fX0=")
                .setDisplayName(languageProvider.getTextProvider().getString("item_lobby", locale))
                .build());
    }

    public TranslatedObjectCache<ItemStack> getTeamSelectionItem() {
        return teamSelectionItem;
    }

    public TranslatedObjectCache<ItemStack> getVotingItem() {
        return votingItem;
    }

    public TranslatedObjectCache<ItemStack> getGuardianItem() {
        return guardianItem;
    }

    public TranslatedObjectCache<ItemStack> getMapVotingItem() {
        return mapVotingItem;
    }

    public TranslatedObjectCache<ItemStack> getLobbyItem() {
        return lobbyItem;
    }

    public static synchronized JoinItems getInstance(LanguageProvider<CommandSender> languageProvider) {
        if(instance == null) {
            new JoinItems(languageProvider);
        }
        return instance;
    }
}
