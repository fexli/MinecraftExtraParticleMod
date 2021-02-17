package com.fexli.particle.particles;

import com.fexli.particle.patchs.ParticleTypeRegistry;
import com.fexli.particle.patchs.PatchAnimatedParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class RainbowParticle extends PatchAnimatedParticle {
    public static final DefaultParticleType RAINBOW = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "rainbow"));

    protected RainbowParticle(World world, double d, double e, double f, SpriteProvider spriteProvider, float g) {
        super(world, d, e, f, spriteProvider, g);
    }

    private RainbowParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0F);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.colorRed = 1.0F;
        this.colorGreen = 0.0F;
        this.colorBlue = 0.0F;
        this.scale = 0.023F;
        this.maxAge = 40 + random.nextInt(7);
        this.resistance = 0.7F;
        this.setSpriteForAge(spriteProvider);
    }

    private int[][] ColorMap = {{255, 0, 0}, {255, 31, 0}, {255, 63, 0}, {255, 95, 0}, {255, 127, 0}, {255, 159, 0},
            {255, 191, 0}, {255, 223, 0}, {255, 255, 0}, {223, 255, 0}, {191, 255, 0}, {159, 255, 0}, {127, 255, 0},
            {95, 255, 0}, {63, 255, 0}, {31, 255, 0}, {0, 255, 0}, {0, 255, 31}, {0, 255, 63}, {0, 255, 95},
            {0, 255, 127}, {0, 255, 159}, {0, 255, 191}, {0, 255, 223}, {0, 255, 255}, {0, 223, 255}, {0, 191, 255},
            {0, 159, 255}, {0, 127, 255}, {0, 95, 255}, {0, 63, 255}, {0, 31, 255}, {0, 0, 255}, {31, 0, 255},
            {63, 0, 255}, {95, 0, 255}, {127, 0, 255}, {159, 0, 255}, {191, 0, 255}, {223, 0, 255},{255,0,255}};


    private int[] getColor(int age){
        if(age <= 40){
            return ColorMap[age];
        }
        return null;
    };
    private void setColor(int[] map){
        if(map != null){
            super.setColor(map[0],map[1],map[2]);

        }
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.setColor(this.getColor(this.age));
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= (double)this.resistance;
            this.velocityY *= (double)this.resistance;
            this.velocityZ *= (double)this.resistance;
        }
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
            return new com.fexli.particle.particles.RainbowParticle(world, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
