package com.fexli.particle.particles;

import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class DUMOParticle extends AnimatedParticle {

    public static final DefaultParticleType DUMO = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "dumo"));

    private DUMOParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, -5.0E-4F);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.colorRed = this.random.nextFloat() * 1.0F;
        this.colorGreen = this.random.nextFloat() * 1.0F;
        this.colorBlue = this.random.nextFloat() * 1.0F;
//        System.out.println("DUMOParticle:r"+this.colorRed+"g"+this.colorGreen+"b"+this.colorBlue);
//        this.scale *= 1.0F;
        this.maxAge = 60 + this.random.nextInt(12);
//        this.setTargetColor(15916745);
        this.setSpriteForAge(spriteProvider);
    }

    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, World world, double d, double e, double f, double g, double h, double i) {
            return new com.fexli.particle.particles.DUMOParticle(world, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
