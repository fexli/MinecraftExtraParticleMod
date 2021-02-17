package com.fexli.particle.imlp;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.fexli.particle.patchs.ParticleFactoryRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleFactoryRegistryImpl implements ParticleFactoryRegistry {
    public static final ParticleFactoryRegistryImpl INSTANCE = new ParticleFactoryRegistryImpl();

    private final Int2ObjectMap<ParticleFactory<?>> factories = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<PendingParticleFactory<?>> constructors = new Int2ObjectOpenHashMap<>();

    private ParticleFactoryRegistryImpl() { }

    @Override
    public <T extends ParticleEffect> void register(ParticleType<T> type, ParticleFactory<T> factory) {
        factories.put(Registry.PARTICLE_TYPE.getRawId(type), factory);
    }

    @Override
    public <T extends ParticleEffect> void register(ParticleType<T> type, PendingParticleFactory<T> factory) {
        constructors.put(Registry.PARTICLE_TYPE.getRawId(type), factory);
    }

    public void injectValues(Int2ObjectMap<ParticleFactory<?>> factories, Map<Identifier, SpriteProvider> spriteProviders) {
        factories.putAll(this.factories);

        constructors.forEach((id, factory) -> {
            SpriteProvider spriteProvider = AccessHack.createSimpleSpriteProvider();

            spriteProviders.put(Registry.PARTICLE_TYPE.getId(Registry.PARTICLE_TYPE.get(id)), spriteProvider);
            factories.put((int)id, factory.create(spriteProvider));
        });
    }

    /**
     * Uses reflection to obtain new instances of the private inner class `ParticleManager.SimpleSpriteProvider`.
     */
    static final class AccessHack {
        private static Constructor<?> constr;

        private static Constructor<?> getConstructor() throws ReflectiveOperationException {
            if (constr != null) {
                return constr;
            }

            for (Class<?> cls : ParticleManager.class.getDeclaredClasses()) {
                if (!cls.isInterface() && SpriteProvider.class.isAssignableFrom(cls)) {
                    constr = cls.getDeclaredConstructor(ParticleManager.class);
                    constr.setAccessible(true);

                    return constr;
                }
            }

            throw new IllegalStateException("net.minecraft.client.particle.ParticleManager.SimpleSpriteProvider is gone!");
        }

        static SpriteProvider createSimpleSpriteProvider() {
            try {
                return (SpriteProvider)getConstructor().newInstance(MinecraftClient.getInstance().particleManager);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("net.minecraft.client.particle.ParticleManager.SimpleSpriteProvider.<init>() is gone!");
        }
    }
}