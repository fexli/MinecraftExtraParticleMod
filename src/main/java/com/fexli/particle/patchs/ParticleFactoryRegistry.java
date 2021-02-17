package com.fexli.particle.patchs;

//import com.fexli.particle.patchs.ParticleManagerAccessor;
//import net.fabricmc.loader.api.FabricLoader;
//import net.minecraft.client.particle.ParticleFactory;
//import net.minecraft.client.particle.ParticleManager;
//import net.minecraft.client.particle.SpriteProvider;
//import net.minecraft.particle.ParticleEffect;
//import net.minecraft.particle.ParticleType;
//import net.minecraft.util.registry.Registry;
//
//import java.lang.reflect.Constructor;
//
//public class ParticleFactoryRegistry {
//    private static final Constructor<? extends SpriteProvider> SIMPLE_SPRITE_PROVIDER_CONSTRUCTOR;
//    static {
//        try {
//            String intermediaryClassName = "net.minecraft.class_702$class_4090";
//            String currentClassName = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", intermediaryClassName);
//            @SuppressWarnings("unchecked")
//            Class<? extends SpriteProvider> clazz = (Class<? extends SpriteProvider>) Class.forName(currentClassName);
//            SIMPLE_SPRITE_PROVIDER_CONSTRUCTOR = clazz.getDeclaredConstructor(ParticleManager.class);
//            SIMPLE_SPRITE_PROVIDER_CONSTRUCTOR.setAccessible(true);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to register particles", e);
//        }
//    }
//
//    public static <T extends ParticleEffect> void register(ParticleManager particleManager, ParticleType<T> particleType, SpriteAwareFactory<T> factory) {
//        SpriteProvider spriteProvider;
//        try {
//            spriteProvider = SIMPLE_SPRITE_PROVIDER_CONSTRUCTOR.newInstance(particleManager);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to register particle", e);
//        }
//        ParticleManagerAccessor accessor = (ParticleManagerAccessor) particleManager;
//        accessor.getFactories().put(Registry.PARTICLE_TYPE.getRawId(particleType), factory.create(spriteProvider));
//        accessor.getSpriteAwareFactories().put(Registry.PARTICLE_TYPE.getId(particleType), spriteProvider);
//    }
//
//    @FunctionalInterface
//    private interface SpriteAwareFactory <T extends ParticleEffect> {
//        ParticleFactory<T> create(SpriteProvider spriteProvider);
//    }
//}


import com.fexli.particle.imlp.ParticleFactoryRegistryImpl;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

/**
 * Registry for adding particle factories on the client.
 */
public interface ParticleFactoryRegistry {

    static ParticleFactoryRegistry getInstance() {
        return ParticleFactoryRegistryImpl.INSTANCE;
    }

    /**
     * Registers a factory for constructing particles of the given type.
     */
    <T extends ParticleEffect> void register(ParticleType<T> type, ParticleFactory<T> factory);

    /**
     * Registers a delayed factory for constructing particles of the given type.
     *
     * The factory method will be called with a sprite provider to use for that particle when it comes time.
     *
     * Particle sprites will be loaded from domain:/particles/particle_name.json as per vanilla minecraft behaviour.
     */
    <T extends ParticleEffect> void register(ParticleType<T> type, PendingParticleFactory<T> constructor);


    /**
     * A pending particle factory.
     *
     * @param <T> The type of particle effects this factory deals with.
     */
    @FunctionalInterface
    public interface PendingParticleFactory<T extends ParticleEffect> {
        /**
         * Called to create a new particle factory.
         *
         * Particle sprites will be loaded from domain:/particles/particle_name.json as per vanilla minecraft behaviour.
         *
         * @param provider The sprite provider used to supply sprite textures when drawing the mod's particle.
         *
         * @return A new particle factory.
         */
        ParticleFactory<T> create(SpriteProvider provider);
    }
}