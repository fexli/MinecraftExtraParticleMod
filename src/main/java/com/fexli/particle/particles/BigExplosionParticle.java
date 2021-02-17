package com.fexli.particle.particles;

import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BigExplosionParticle extends SpriteBillboardParticle {
    private final SpriteProvider field_17815;

    public static final DefaultParticleType Explosion = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "explosion"));

    private BigExplosionParticle(World world, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.maxAge = 6 + this.random.nextInt(4);
//        float f = this.random.nextFloat() * 0.6F + 0.4F;
//        this.maxAge = 14;
        float f = 1.0F;
        this.colorRed = f;
        this.colorGreen = f;
        this.colorBlue = f;
//        this.scale = 2.0F * (1.0F - (float)d * 0.5F);
        this.scale = 16.5F;
        this.field_17815 = spriteProvider;
        this.setSpriteForAge(spriteProvider);
    }

    public int getColorMultiplier(float tint) {
        return 15728880;
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.field_17815);
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider field_17816;

        public Factory(SpriteProvider spriteProvider) {
            this.field_17816 = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, World world, double d, double e, double f, double g, double h, double i) {
            return new com.fexli.particle.particles.BigExplosionParticle(world, d, e, f, g, this.field_17816);
        }
    }
}
