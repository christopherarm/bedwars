package net.trainingsoase.bedwars.utils.effects;

import com.comphenix.protocol.wrappers.EnumWrappers;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.Location;

public class EffectStorage {

    private final EffectManager effectManager;

    public EffectStorage() {
        this.effectManager = new EffectManager(EffectLib.instance());
    }

    public Effect playTeleportEffect(Location loc) {
        final HelixEffect effect = new HelixEffect(this.effectManager);
        effect.particles = 20;
        effect.strands = 20;
        effect.particle = ParticleEffect.SPELL_INSTANT;
        effect.iterations = -1;
        effect.radius = 1;
        effect.setLocation(loc);
        effect.start();
        return effect;
    }

    public void playGoldSpawn(Location loc) {
        final HelixEffect effect = new HelixEffect(this.effectManager);
        effect.radius = 1;
        effect.strands = 1;
        effect.iterations = 1;
        effect.particle = ParticleEffect.SPELL_MOB;
        effect.color = Color.YELLOW;
        effect.setLocation(loc);
        effect.start();
    }

    public void playIronSpawn(Location loc) {
        final HelixEffect effect = new HelixEffect(this.effectManager);
        effect.radius = 1;
        effect.strands = 1;
        effect.iterations = 1;
        effect.particle = ParticleEffect.SPELL_MOB;
        effect.color = Color.SILVER;
        effect.setLocation(loc);
        effect.start();
    }

    public Effect startEnderChestEffect(Location loc, Color color) {
        final HelixEffect effect = new HelixEffect(this.effectManager);
        effect.radius = 0.5F;
        effect.iterations = -1;
        effect.strands = 2;
        effect.particle = ParticleEffect.SPELL_MOB;
        effect.particles = 10;
        effect.color = color;
        effect.period = 15;
        effect.setLocation(loc);
        effect.start();

        return effect;
    }
}
