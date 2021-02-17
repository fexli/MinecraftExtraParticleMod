package com.fexli.particle.patchs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PatchAnimatedParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;
    public final float upwardsAcceleration;
    public float resistance = 0.91F;
    public float targetColorRed;
    public float targetColorGreen;
    public float targetColorBlue;
    public boolean changesColor;

    protected PatchAnimatedParticle(World world, double d, double e, double f, SpriteProvider spriteProvider, float g) {
        super(world, d, e, f);
        this.spriteProvider = spriteProvider;
        this.upwardsAcceleration = g;
    }

    public void setColor(int r,int g,int b) {
        this.colorRed = (float)r/255F;
        this.colorGreen = (float)g/255F;
        this.colorBlue = (float)b/255F;
    }

    public void setTargetColor(int r,int g,int b) {
        this.targetColorRed = (float)r / 255.0F;
        this.targetColorGreen = (float)g / 255.0F;
        this.targetColorBlue = (float)b / 255.0F;
        this.changesColor = true;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            if (this.age > this.maxAge / 2) {
                this.setColorAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
                if (this.changesColor) {
                    this.colorRed += (this.targetColorRed - this.colorRed) * 0.2F;
                    this.colorGreen += (this.targetColorGreen - this.colorGreen) * 0.2F;
                    this.colorBlue += (this.targetColorBlue - this.colorBlue) * 0.2F;
                }
            }

            this.velocityY += (double)this.upwardsAcceleration;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= (double)this.resistance;
            this.velocityY *= (double)this.resistance;
            this.velocityZ *= (double)this.resistance;
            if (this.onGround) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
            }

        }
    }

    public int getColorMultiplier(float f) {
        return 15728880;
    }

    public void setResistance(float f) {
        this.resistance = f;
    }
}
