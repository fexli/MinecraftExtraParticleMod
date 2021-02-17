package com.fexli.particle.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(net.minecraft.client.particle.CampfireSmokeParticle.class)
public abstract class CampfireHigherMixin extends SpriteBillboardParticle {
	public CampfireHigherMixin(World world_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
		super(world_1, double_1, double_2, double_3, double_4, double_5, double_6);
	}

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;DDDDDDZ)V")
	private void CampfireSmokeParticle (World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, boolean bl, CallbackInfo info) {
		if (bl) {
			this.maxAge += 560;
		}
	}

	@Inject(at = @At("RETURN"), method = "tick()V")
	public void tick(CallbackInfo info) {
		this.velocityX -= this.velocityX / 170.0D;
		this.velocityZ -= this.velocityZ / 170.0D;
	}

}

