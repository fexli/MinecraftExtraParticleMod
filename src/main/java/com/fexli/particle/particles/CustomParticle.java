package com.fexli.particle.particles;


import com.fexli.particle.future.CustomParticleSetting;
import com.fexli.particle.particles.effectlib.CustomParticleEffect;
import com.fexli.particle.patchs.ParticleTypeRegistry;
import com.fexli.particle.utils.Messenger;
import com.fexli.particle.utils.RandomUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CustomParticle extends SpriteBillboardParticle {
    public static final ParticleType<CustomParticleEffect> CUSTOM = ParticleTypeRegistry.getTnstance().register(new Identifier("fexli", "custom"), CustomParticleEffect.PARAMETERS_FACTORY);
    protected final SpriteProvider spriteProvider;
    public float accelerationX = 0.0F;
    public float accelerationY = 0.0F;
    public float accelerationZ = 0.0F;
    public float resistanceX = 0.91F;
    public float resistanceY = 0.91F;
    public float resistanceZ = 0.91F;
    public float targetColorRed;
    public float targetColorGreen;
    public float targetColorBlue;
    private float targetColorAlpha;
    public boolean changesColor;
    /*
     * ALL PROTECT PARTICLE-BASE value
     * FINAL VALUE
     *private static final Box EMPTY_BOUNDING_BOX = new Box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
     *protected final World world;
     *protected final Random random;
     *
     *protected double prevPosX;
     *protected double prevPosY;
     *protected double prevPosZ;
     *protected double x;
     *protected double y;
     *protected double z;
     *protected double velocityX;
     *protected double velocityY;
     *protected double velocityZ;
     *private Box boundingBox;
     *protected boolean onGround;
     *protected boolean collidesWithWorld;
     *protected boolean dead;
     *protected float spacingXZ;
     *protected float spacingY;
     *protected int age;
     *protected int maxAge;
     *protected float gravityStrength;
     *protected float colorRed;
     *protected float colorGreen;
     *protected float colorBlue;
     *protected float colorAlpha;
     *protected float angle;
     *protected float prevAngle;
     *public static double cameraX;
     *public static double cameraY;
     *public static double cameraZ;
     * Method vect3d
     *
     * Vec3d vec3d = Entity.calculateMotionVector((Entity)null, new Vec3d(d, e, f), this.getBoundingBox(), this.world, EntityContext.absent(), new ReusableStream(Stream.empty()));
     * d = vec3d.x;
     * e = vec3d.y;
     * f = vec3d.z;
     */
    private CustomParticleSetting customsettings;
    private ParticleTextureSheet particleType = ParticleTextureSheet.PARTICLE_SHEET_LIT;

    private CustomParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CustomParticleEffect customParticleEffect, SpriteProvider spriteProvider) {
        super(world,x,y,z);
        this.customsettings = customParticleEffect.getSetting();
        this.spriteProvider = spriteProvider;
        this.applySetting();
//        System.out.println("[Debug]Color("+this.colorRed+" "+this.colorGreen+" "+this.colorBlue+" "+this.colorAlpha+") Age("+this.maxAge+") Scale("+this.scale+")");
        this.setSpriteForAge(spriteProvider);
    }
    public void setColor(int r,int g,int b) {
        this.colorRed = (float)r/255F;
        this.colorGreen = (float)g/255F;
        this.colorBlue = (float)b/255F;
    }

    public void setTargetColorRed(int r) {
        this.targetColorRed = (float)r/255.0F;
        this.changesColor = true;
    }
    public void setTargetColorGreen(int g) {
        this.targetColorGreen = (float)g/255.0F;
        this.changesColor = true;
    }
    public void setTargetColorBlue(int b) {
        this.targetColorBlue = (float)b/255.0F;
        this.changesColor = true;
    }
    public void setTargetColor(int r,int g,int b) {
        this.targetColorRed = (float)r / 255.0F;
        this.targetColorGreen = (float)g / 255.0F;
        this.targetColorBlue = (float)b / 255.0F;
        this.changesColor = true;
    }
    public void setVelocateX(double d){ this.velocityX = d;}
    public void setVelocateY(double d){ this.velocityY = d;}
    public void setVelocateZ(double d){ this.velocityZ = d;}
    public void setVelocate(double x,double y,double z){
         this.velocityX = x;
         this.velocityY = y;
         this.velocityZ = z;
    }
    private float stepR = 0.0F;
    private float stepG = 0.0F;
    private float stepB = 0.0F;

    public void calculateStepColor(){
        this.stepR = (this.targetColorRed- this.colorRed) / (float) ((2*this.maxAge+2)/3);
        this.stepG = (this.targetColorGreen - this.colorGreen) / (float) ((2*this.maxAge+2)/3);
        this.stepB = (this.targetColorBlue - this.colorBlue) / (float) ((2*this.maxAge+2)/3);
    }
    private float stepA = 0.0F;
    private boolean disappear = false;

    public void setDisappear(boolean d){this.disappear = d;}

    public void calculateStepAlpha(){
        if (disappear){
            try {
                this.stepA = (this.colorAlpha-this.targetColorAlpha)/(((float)2*this.maxAge/3)+2);
            } catch (Exception e) {
                this.stepA = 0.0F;
            }
        } else {
            this.stepA = 0.0F;
        }
    }
    private void applySetting(){
        if (this.customsettings == null) {
            System.out.println("[ExParticle][ERROR]Particle Settings is null !");
        }
        this.customsettings.load(this,false);
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
                this.colorAlpha -=this.stepA;
                if (this.changesColor) {
                    this.colorRed += this.stepR;
                    this.colorGreen += this.stepG;
                    this.colorBlue += this.stepB;
                }
            }

            this.velocityY += this.accelerationY;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= this.resistanceX;
            this.velocityY *= this.resistanceY;
            this.velocityZ *= this.resistanceZ;
            if (this.onGround & this.collidesWithWorld) {
//                this.velocityX *= 0.699999988079071D;
//                this.velocityZ *= 0.699999988079071D;
                this.velocityY *= 0.6;
            }

        }
    }
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    public ParticleTextureSheet getType() {
        return this.particleType;
    }

    public int getColorMultiplier(float f) {
        return 15728880;
    }

    public void setResistance(float x,float y,float z) {
        this.resistanceX = x;
        this.resistanceY = y;
        this.resistanceZ = z;
    }

    public void setType(String type_) {
        switch (type_) {
            case "PARTICLE_SHEET_LIT":
                this.particleType = ParticleTextureSheet.PARTICLE_SHEET_LIT;
                break;
            case "PARTICLE_SHEET_OPAQUE":
                this.particleType = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
                break;
            case "PARTICLE_SHEET_TRANSLUCENT":
                this.particleType = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
                break;
            case "CUSTOM":
                this.particleType = ParticleTextureSheet.CUSTOM;
                break;
            case "NO_RENDER":
                this.particleType = ParticleTextureSheet.NO_RENDER;
                break;
            case "TERRAIN_SHEET":
                this.particleType = ParticleTextureSheet.TERRAIN_SHEET;
                break;
        }
    }

    public void setAlpha(float alpha) {
        this.colorAlpha = alpha;
    }

    public void setCollidesWithWorld(boolean collidesWithWorld) {
        this.collidesWithWorld = collidesWithWorld;
    }

    public void setVelocateWithSpeed(double[] speed) {
        this.velocityX = random.nextGaussian()* RandomUtils.Double(random,speed[0],speed[1]);
        this.velocityY = random.nextGaussian()* RandomUtils.Double(random,speed[0],speed[1]);
        this.velocityZ = random.nextGaussian()* RandomUtils.Double(random,speed[0],speed[1]);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setAcceleration(float accX, float accY, float accZ) {
        this.accelerationX = accX;
        this.accelerationY = accY;
        this.accelerationZ = accZ;
    }

    public void setTargetAlpha(float targetColorA) {
        this.targetColorAlpha = targetColorA;
        this.disappear = true;
    }

    public void setAccelerationX(Float accX) {
        this.accelerationX = accX;
    }

    public void setAccelerationY(Float accY) {
        this.accelerationY = accY;
    }

    public void setAccelerationZ(Float accZ) {
        this.accelerationZ = accZ;
    }


    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<CustomParticleEffect> {
         private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(CustomParticleEffect customParticleEffect, World world, double d, double e, double f, double g, double h, double i) {
            return new CustomParticle(world, d, e, f, g, h, i, customParticleEffect, this.spriteProvider);
        }
    }
}
