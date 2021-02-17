package com.fexli.particle.imlp;


import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleEffect.Factory;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleTypeRegistryImpl implements ParticleTypeRegistry {

    public static final ParticleTypeRegistryImpl INSTANCE = new ParticleTypeRegistryImpl();

    private ParticleTypeRegistryImpl() { }

    @Override
    public DefaultParticleType register(Identifier id, boolean alwaysSpawn) {
        return Registry.register(Registry.PARTICLE_TYPE, id, new Simple(alwaysSpawn));
    }

    @Override
    public <T extends ParticleEffect> ParticleType<T> register(Identifier id, boolean alwaysSpawn, ParticleEffect.Factory<T> factory) {
        return Registry.register(Registry.PARTICLE_TYPE, id, new Complex<>(alwaysSpawn, factory));
    }

    // Constructor is (gasp!) protected
    public static class Simple extends DefaultParticleType {
        public Simple(boolean alwaysSpawn) {
            super(alwaysSpawn);
        }
    }

    // Same for this
    public static class Complex<T extends ParticleEffect> extends ParticleType<T> {
        public Complex(boolean alwaysSpawn, Factory<T> factory) {
            super(alwaysSpawn, factory);
        }
    }
}