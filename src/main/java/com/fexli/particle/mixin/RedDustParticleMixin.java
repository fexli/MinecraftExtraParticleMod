package com.fexli.particle.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(net.minecraft.client.particle.RedDustParticle.class)
public abstract class RedDustParticleMixin extends SpriteBillboardParticle {

    public RedDustParticleMixin(World world_r, double x, double y, double z, double velocityX, double velocityY, double velocityZ, DustParticleEffect dustParticleEffect, SpriteProvider spriteProvider){
        super(world_r, x, y, z, velocityX, velocityY, velocityZ);
    }

//    @Inject(at = @At("RETURN"), method = "RedDustParticle(Lnet/minecraft/world/World;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Lnet/minecraft/particle/DustParticleEffect;Lnet/minecraft/client/particle/SpriteProvider;)V")
    @Inject(at = @At("RETURN"), method = "<init>")
    public void RedDustParticle(World world_r, double x, double y, double z, double velocityX, double velocityY, double velocityZ, DustParticleEffect dustParticleEffect, SpriteProvider spriteProvider,CallbackInfo info){
        this.colorRed = dustParticleEffect.getRed() * 0.74f;
        this.colorGreen = dustParticleEffect.getGreen() * 0.74f;
        this.colorBlue = dustParticleEffect.getBlue() * 0.74f;
//        System.out.println("RGB:" + this.colorRed+ this.colorGreen+ this.colorBlue);
//        System.out.println("Max age:"+this.maxAge);
        this.maxAge += 100;
    }

//    @Inject(at = @At("RETURN"), method = "tick()V")
//    public void tick(CallbackInfo info) {
//        this.colorRed = 0.0f;
//        this.colorBlue = 0.0f;
//        this.colorGreen = 0.0f;
//    }
}