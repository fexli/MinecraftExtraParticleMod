package com.fexli.particle.mixin;

import java.util.Map;
import com.fexli.particle.imlp.ParticleFactoryRegistryImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
//NEW METHOD

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Shadow @Final
    //    private Map<Identifier, ParticleManager.SimpleSpriteProvider> spriteAwareFactories = Maps.newHashMap();
    // DO NOT CHANGE THIS !!!
    private Map field_18300;


    @Shadow @Final
    private Int2ObjectMap<ParticleFactory<?>> factories;

    @Inject(method = "registerDefaultFactories()V", at = @At("RETURN"))
    private void onRegisterDefaultFactories(CallbackInfo info) {
        ParticleFactoryRegistryImpl.INSTANCE.injectValues(factories, field_18300);
    }
}