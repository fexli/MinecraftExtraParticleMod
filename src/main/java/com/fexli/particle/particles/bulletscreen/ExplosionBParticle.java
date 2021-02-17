package com.fexli.particle.particles.bulletscreen;

import com.fexli.particle.particles.bulletscreen.ertype.JinBParticleEffect;
import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ExplosionBParticle extends SpriteBillboardParticle {
    private final SpriteProvider field_17815;

    public static final ParticleType<JinBParticleEffect> ExplosionB = ParticleTypeRegistry.getTnstance().register(new Identifier("p", "b"), JinBParticleEffect.PARAMETERS_FACTORY);

    private ExplosionBParticle(World world, JinBParticleEffect jinParticleEffect, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
//        this.maxAge = 6 + this.random.nextInt(4);
        this.maxAge = jinParticleEffect.getmAge_();
        float f = 1.0F;
        this.colorRed = f;
        this.colorGreen = f;
        this.colorBlue = f;
        this.colorAlpha = jinParticleEffect.getAlpha();
        this.scale = jinParticleEffect.getScale();
        this.velocityX = jinParticleEffect.getVx();
        this.velocityY = jinParticleEffect.getVy();
        this.velocityZ = jinParticleEffect.getVz();
        this.field_17815 = spriteProvider;
        this.setSpriteForAge(spriteProvider);
    }

    public int getColorMultiplier(float tint) {
        return 15728880;
    }

    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.setSpriteForAge(this.field_17815);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<JinBParticleEffect> {
        private final SpriteProvider field_17816;

        public Factory(SpriteProvider spriteProvider) {
            this.field_17816 = spriteProvider;
        }


        public Particle createParticle(JinBParticleEffect particleEffect, World world, double d, double e, double f, double g, double h, double i) {
            return new ExplosionBParticle(world,particleEffect, d, e, f, g, this.field_17816);
        }
    }
}
