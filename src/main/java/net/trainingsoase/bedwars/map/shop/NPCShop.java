package net.trainingsoase.bedwars.map.shop;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import net.trainingsoase.bedwars.Bedwars;
import net.trainingsoase.bedwars.map.MapHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class NPCShop {

    private final Bedwars bedwars;

    private final NPCPool npcPool;

    public NPCShop(Bedwars bedwars) {
        this.bedwars = bedwars;
        this.npcPool = NPCPool.builder(bedwars)
                .tabListRemoveTicks(55).actionDistance(20).spawnDistance(5000).build();
    }

    public void spawnNPCs() {
        MapHelper.getInstance(bedwars).getGameMap().getShopLocations().forEach(loc -> {
            Collection<Profile.Property> shopProperties = new HashSet<>();
            shopProperties.add(new Profile.Property("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYyMTcxNTU5MTA2OSwKICAicHJvZmlsZUlkIiA6ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI0YjAyMDRmNWNkMjBkMTAyMDA1MjU3YzdiYWY4ZGJmZWU5ZTNjNzUzOGY1Yzc4MzM1MGQ2YmI5YmI4OGRhZTEiCiAgICB9CiAgfQp9",
                    "o76UBRDt9/erJOaQJyt7x29QAFZGtlxNruocruw/AkofhA01zFZT56Ue0qSqaca4MTbGDuVHe9GYMBy/3PxnXmdf9K09RRf+zTDGlS85PdJuD0N3at1PUyArGV2MkajSax3LGjUjm1blvSuXSnWO1o5R+CC0JTfC0+3PPVmbqoj/xHSAgHMi2qq5mflMzClCVq6FXUF6MdBjjds09NkTqFR6mxYielpnJplCTIUYsr3+58kpKcgpg/ep3KIrp7ehuHzs/daSt5h87+ZefIpIz7WoLtNC2IPJnBe094qyEFmMbyBhXNaLogIgwue1YaeHdFuVHUm/5PCjGB3xqbQ3Lv6Uey98p8eo4ebzZd6nmtB/n0b7YcaHUIGqSZble5RLGVRxpqwVftaoXoABLvbhfhwY31woM8NmMlSDqj+mu15MR7Mz8VufW1YPHUZqqvthaG1FOzZXVUB35+JP8XUWtYzCoXoDeMaMxp6x7NnRbs8XwMMwVUg82gmEPEB8ejUHRIng6lyWq9R3spkNf/7BKZMZkgB2f9zz75lPuldANlGssHdPuR7IdKx+nDhrXP7/DVUJ3eHTP1lH6WaWd0OFqe45rwaybu14I+0yLKRWzoAolc8t8toyMpw0pTVBcLCqYdtNh1yKcWN81z+cLzJFbszpIihIrNvDrIUwZXYO5do="));

            NPC.Builder builder = NPC.builder();

            builder.profile(new Profile(
                    UUID.randomUUID(),
                    "§e§lShop",
                    shopProperties));

            builder.imitatePlayer(false);
            builder.lookAtPlayer(true);
            builder.location(loc.toLocation());
            builder.spawnCustomizer((spawnedNPC, player) -> {
                spawnedNPC.metadata()
                        .queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true)
                        .queue(MetadataModifier.EntityMetadata.SNEAKING, false)
                        .send(player);
            });

            builder.build(npcPool);
        });
    }
}
