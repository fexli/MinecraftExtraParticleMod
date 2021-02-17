package com.fexli.particle.patchs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class MovedAnimatedParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;
    public final float upwardsAcceleration;
    public float resistance = 0.91F;
    public float targetColorRed;
    public float targetColorGreen;
    public float targetColorBlue;
    public boolean changesColor;

    protected MovedAnimatedParticle(World world, double d, double e, double f, SpriteProvider spriteProvider, float g) {
        super(world, d, e, f);
        this.spriteProvider = spriteProvider;
        this.upwardsAcceleration = g;
    }

    public void setColor(int r,int g,int b) {
        super.setColor((float)r/255,(float)g/255,(float)b/255);
    }

    public void setTargetColor(int tr,int tg,int tb) {
        this.targetColorRed = (float)tr / 255.0F;
        this.targetColorGreen = (float)tg / 255.0F;
        this.targetColorBlue = (float)tb / 255.0F;
        this.changesColor = true;
        calculateStepColor();
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
            if (this.age > this.maxAge / 3) {
//                this.setColorAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
                this.colorAlpha -= this.stepA;
                if (this.changesColor) {
                    this.colorRed += this.stepR;
                    this.colorGreen += this.stepG;
                    this.colorBlue += this.stepB;
                }
            }

            this.velocityY += this.upwardsAcceleration;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= (double)this.resistance;
            this.velocityY *= (double)this.resistance;
            this.velocityZ *= (double)this.resistance;
            if (this.onGround) {
                this.velocityX *= 0.699999988079071D;
                this.velocityZ *= 0.699999988079071D;
            }

        }
//        System.out.println("D:"+this.colorRed+" "+this.colorGreen+" "+this.colorBlue+" "+this.colorAlpha);
    }
    private float stepR;
    private float stepG;
    private float stepB;
    private boolean stepHasCalculated;

    public void calculateStepColor(){
        if(!stepHasCalculated){
//            System.out.println("T:"+targetColorRed+" "+targetColorGreen+" "+targetColorBlue);
//            System.out.println("C:"+colorRed+" "+colorGreen+" "+colorBlue);
//            System.out.println("S:"+(2*this.maxAge/3));
            this.stepR = (this.targetColorRed- this.colorRed) / (float) ((2*this.maxAge+2)/3);
            this.stepG = (this.targetColorGreen - this.colorGreen) / (float) ((2*this.maxAge+2)/3);
            this.stepB = (this.targetColorBlue - this.colorBlue) / (float) ((2*this.maxAge+2)/3);
//            System.out.println("STEP:"+this.stepR+" "+this.stepG+" "+this.stepB);
            this.stepHasCalculated = true;
        }
    }
    private float stepA;
    private boolean disappear = false;

    public void setDisappear(boolean d){this.disappear = d;}

    public void calculateStepAlpha(){
        if (disappear){
            try {
                this.stepA = 1/(((float)2*this.maxAge/3)+2);
            } catch (Exception e) {
                this.stepA = 0.0F;
            }
        } else {
            this.stepA = 0.0F;
        }
    }
    public int getColorMultiplier(float f) {
        return 15728880;
    }

    protected void setResistance(float f) {
        this.resistance = f;
    }
}
