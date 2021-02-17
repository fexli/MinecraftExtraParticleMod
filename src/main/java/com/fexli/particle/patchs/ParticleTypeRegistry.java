package com.fexli.particle.patchs;


import com.fexli.particle.imlp.ParticleTypeRegistryImpl;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;

/**
 * Registry for adding new particle types to the game.
 */
public interface ParticleTypeRegistry {

    static ParticleTypeRegistry getTnstance() {
        return ParticleTypeRegistryImpl.INSTANCE;
    }

    /**
     * Creates a new, default particle type for the given id.
     *
     * @param id The particle id.
     */
    default DefaultParticleType register(Identifier id) {
        return register(id, false);
    }

    /**
     * Creates a new, default particle type for the given id.
     *
     * @param id The particle id.
     * @param alwaysSpawn True to always spawn the particle regardless of distance.
     */
    DefaultParticleType register(Identifier id, boolean alwaysSpawn);

    /**
     * Creates a new particle type with a custom factory for packet/data serialization.
     *
     * @param id The particle id.
     * @param factory     A factory for serializing packet data and string command parameters into a particle effect.
     */
    default <T extends ParticleEffect> ParticleType<T> register(Identifier id, ParticleEffect.Factory<T> factory) {
        return register(id, false, factory);
    }

    /**
     * Creates a new particle type with a custom factory for packet/data serialization.
     *
     * @param id The particle id.
     * @param alwaysSpawn True to always spawn the particle regardless of distance.
     * @param factory     A factory for serializing packet data and string command parameters into a particle effect.
     */
    <T extends ParticleEffect> ParticleType<T> register(Identifier id, boolean alwaysSpawn, ParticleEffect.Factory<T> factory);
}