package com.fexli.particle.particles;

import com.fexli.particle.patchs.ParticleTypeRegistry;
import com.fexli.particle.patchs.ShineAnimatedParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class StarParticle extends ShineAnimatedParticle {

    public static final DefaultParticleType STAR = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "star"));

    private StarParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.colorRed = this.random.nextFloat() * 1.0F;
        this.colorGreen = this.random.nextFloat() * 1.0F;
        this.colorBlue = this.random.nextFloat() * 1.0F;
        this.setTargetColor(this.random.nextInt(255),this.random.nextInt(255),this.random.nextInt(255));
//        System.out.println("DUMOParticle:r"+this.colorRed+"g"+this.colorGreen+"b"+this.colorBlue);
        this.scale = 0.0028F;
        this.maxAge = 120 + this.random.nextInt(40);
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
            return new StarParticle(world, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
