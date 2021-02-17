package com.fexli.particle.particles;

import com.fexli.particle.particles.effectlib.DotParticleEffect;
import com.fexli.particle.patchs.ParticleTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
@Environment(EnvType.CLIENT)
public class DotParticle extends AnimatedParticle {

    public static final ParticleType<DotParticleEffect> DOT = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "dot"),DotParticleEffect.PARAMETERS_FACTORY);

    private DotParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int r, int g, int b, int targetR, int targetG, int targetB, DotParticleEffect dotParticleEffect, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0.0F);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
//        this.scale = 0.005F;
        this.scale = dotParticleEffect.getScale();
        this.maxAge = 165;
        this.colorRed = this.random.nextFloat() * 1.0F;
        this.colorGreen = this.random.nextFloat() * 1.0F;
        this.colorBlue = this.random.nextFloat() * 1.0F;
//        System.out.println(dotParticleEffect.asString());
        if (!dotParticleEffect.isRand()){
            //            this.setTargetColor(ParticleUtils.rgb2Int(targetR,targetG,targetB));
            if (dotParticleEffect.getBlue() != -1){
                this.colorBlue = (float)dotParticleEffect.getBlue()/255;
            }
            if (dotParticleEffect.getGreen() != -1){
                this.colorGreen = (float)dotParticleEffect.getGreen()/255;
            }
            if (dotParticleEffect.getRed() != -1){
                this.colorRed = (float) dotParticleEffect.getRed()/255;
            }
        }
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
    public static class Factory implements ParticleFactory<DotParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DotParticleEffect dotParticleEffect, World world, double d, double e, double f, double g, double h, double i) {
            return new com.fexli.particle.particles.DotParticle(world, d, e, f, g, h, i, 0,0,0,0,0,0, dotParticleEffect, this.spriteProvider);
        }
//        public Particle createParticle(DotParticleEffect dotParticleEffect, World world, double d, double e, double f, double g, double h, double i, int rr, int gg, int bb, int tr, int tg, int tb){
//            return new com.fexli.particle.particles.DotParticle(world,d,e,f,g,h,i,rr,gg,bb,tr,tg,tb,true,this.spriteProvider);
//        }
    }
}
