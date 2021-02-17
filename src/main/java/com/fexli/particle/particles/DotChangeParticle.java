package com.fexli.particle.particles;

import com.fexli.particle.particles.effectlib.DotChangeParticleEffect;
import com.fexli.particle.patchs.MovedAnimatedParticle;
import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Random;

public class DotChangeParticle extends MovedAnimatedParticle {
    public static final ParticleType<DotChangeParticleEffect> DOTCHANGE = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "dot_change"),DotChangeParticleEffect.PARAMETERS_FACTORY);

    private static Random rand_ = new Random();

    protected DotChangeParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, DotChangeParticleEffect dp,SpriteProvider spriteProvider) {
        super(world, x,y,z, spriteProvider, 0.0F);
        this.velocityX = dp.getVx();
        this.velocityY = dp.getVy();
        this.velocityZ = dp.getVz();
        this.setDisappear(dp.needDisappear());
        this.maxAge = dp.getAge();
        this.calculateStepAlpha();
        if(dp.isSRand()){
            this.colorRed = this.random.nextFloat() * 1.0F;
            this.colorGreen = this.random.nextFloat() * 1.0F;
            this.colorBlue = this.random.nextFloat() * 1.0F;
        } else {
            this.colorRed = (float) dp.getR()/255;
            this.colorGreen = (float)dp.getG()/255;
            this.colorBlue = (float)dp.getB()/255;
        }
        if(dp.isTRand()) {
            this.setTargetColor(rand_.nextInt(256),rand_.nextInt(256),rand_.nextInt(256));
        } else {
            this.setTargetColor(dp.getTR(),dp.getTG(),dp.getTB());
        }
        this.scale = dp.getScale();
        this.setSpriteForAge(spriteProvider);
    }


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DotChangeParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DotChangeParticleEffect dotChangeParticleEffect, World world, double d, double e, double f, double g, double h, double i) {
            return new com.fexli.particle.particles.DotChangeParticle(world, d, e, f, g, h, i, dotChangeParticleEffect, this.spriteProvider);
        }

    }

}
