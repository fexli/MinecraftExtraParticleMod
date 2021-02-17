package com.fexli.particle.patchs;


import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.world.World;

public class ShineAnimatedParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;
    public final float upwardsAcceleration;
    public float resistance = 0.99F;
    public float targetColorRed;
    public float targetColorGreen;
    public float targetColorBlue;
    public boolean changesColor;

    protected ShineAnimatedParticle(World world, double d, double e, double f, SpriteProvider spriteProvider, float g) {
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
            this.setRandomAlpha();
            if (this.age > this.maxAge / 3) {
//                this.setColorAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
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
                this.velocityX *= 0.976D;
                this.velocityZ *= 0.976D;
            }

        }
//        System.out.println("D:"+this.colorRed+" "+this.colorGreen+" "+this.colorBlue+" "+this.colorAlpha);
    }

    private void setRandomAlpha() {
        if (this.colorAlpha >= 0.75F) {
            this.colorAlpha -= random.nextFloat()*0.25;
        } else if (this.colorAlpha <= 0.25F) {
            this.colorAlpha += random.nextFloat()*0.25;
        } else {
            this.colorAlpha += -0.25+random.nextFloat()*0.5;
        }
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

    public int getColorMultiplier(float f) {
        return 15728880;
    }

    protected void setResistance(float f) {
        this.resistance = f;
    }
}
