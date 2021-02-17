package com.fexli.particle.future;


import com.fexli.particle.particles.CustomParticle;
import com.fexli.particle.utils.RandomUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class CustomParticleSetting {
    public int version;
    public String name;
    //    public String author;
    private String type_;
    private boolean sType = false;

    public static Random random = new Random();
    private Double velocityX = null;
    private Double velocityY = null;
    private Double velocityZ = null;
    private double[] speed;
    private boolean speedControl = false;

    private Float accX = null;
    private Float accY = null;
    private Float accZ = null;
    private boolean sAcc = false;
    private int r;
    private int g;
    private int b;
    private boolean sColor = false;
    private float[] alpha;
    private boolean sAlpha = false;

    private boolean collidesWithWorld = true;
    private boolean sCWW = false;

    private float[] scale;
    private boolean sScale = false;
    private int[] maxAge;
    private boolean sMaxAge = false;
    private float resistanceX;
    private float resistanceY;
    private float resistanceZ;
    private boolean sResistance = false;

    private String parent = null;
    private Integer targetColorR = null;
    private Integer targetColorG = null;
    private Integer targetColorB = null;
    private boolean sTC = false;
    private boolean targetControl = false;
    private float targetColorA;
    private boolean sTA = false;

    public CustomParticleSetting(){
//        this.velocityX = 0.0D;
//        this.velocityY = 0.0D;
//        this.velocityZ = 0.0D;
//        this.resistanceX = 0.91F;
//        this.resistanceY = 0.91F;
//        this.resistanceZ = 0.91F;
        this.r = 255;
        this.g = 255;
        this.b = 255;
//        this.accX = 0F;
//        this.accY = 0F;
//        this.accZ = 0F;
        this.maxAge = new int[]{50,50};
        this.scale = new float[]{0.1F,0.1F};
        this.alpha = new float[]{1.0F,1.0F};

        this.version = 1;
        this.name = "NoneName";
        this.type_ = "PARTICLE_SHEET_LIT";
    }

    public void load(CustomParticle particle,boolean isparent){
        // if has parent?
        if (this.parent != null){
            CustomParticleSetting parent = CustomParticleSettingManager.settingHashMap.get(this.parent);
            if (parent != null){
                parent.load(particle,true);
//                System.out.println("[Parent]["+parent.name+"]Color"+parent.r+" "+parent.g+" "+parent.b);
            }
        }
        // add here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (this.sColor) particle.setColor(setColorUnsure(this.r),setColorUnsure(this.g),setColorUnsure(this.b));
        if (this.sAlpha) particle.setAlpha(RandomUtils.Float(random,this.alpha[0],this.alpha[1]));
        if (this.sType) particle.setType(this.type_);
        if (this.sCWW) particle.setCollidesWithWorld(this.collidesWithWorld);
        if (this.speedControl) {
            particle.setVelocateWithSpeed(this.speed);
        } else {
            if (this.velocityX != null) particle.setVelocateX(this.velocityX);
            if (this.velocityY != null) particle.setVelocateY(this.velocityY);
            if (this.velocityZ != null) particle.setVelocateZ(this.velocityZ);
//            particle.setVelocate(this.velocityX,this.velocityY,this.velocityZ);
        }
        if (this.sScale) particle.setScale(RandomUtils.Float(random,this.scale[0],this.scale[1]));
        if (this.sMaxAge) particle.setMaxAge(RandomUtils.Int(random,this.maxAge[0],this.maxAge[1]));
        if (this.sResistance) particle.setResistance(this.resistanceX,this.resistanceY,this.resistanceZ);
        if (this.sAcc){
//            particle.setAcceleration(this.accX,this.accY,this.accZ);
            if (this.accX != null) particle.setAccelerationX(this.accX);
            if (this.accY != null) particle.setAccelerationY(this.accY);
            if (this.accZ != null) particle.setAccelerationZ(this.accZ);
        }
        if (this.sTC) {
            if (this.targetColorR != null) particle.setTargetColorRed(this.targetColorR);
            if (this.targetColorG != null) particle.setTargetColorGreen(this.targetColorG);
            if (this.targetColorB != null) particle.setTargetColorBlue(this.targetColorB);
        }
        if (this.sTA) particle.setTargetAlpha(this.targetColorA);
        // parent->TargetColor child->maxAge
        if (!isparent){
            particle.calculateStepAlpha();
            particle.calculateStepColor();
        }
    }
    public void tick(CustomParticle particle){

    }
    public String toString(){
        return "Color()"+this.r+this.g+this.b+" A()"+ Arrays.toString(this.alpha) +" Maxage"+ Arrays.toString(this.maxAge);
    }
    public CustomParticleSetting combine(CustomParticleSetting parentSetting){
        return this;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type_ = type;
        this.sType = true;
    }

    public void setSpeed(double[] speed) {
        this.speed = speed;
        this.speedControl = true;
    }

    public void setColor(int r,int g,int b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.sColor = true;
    }
    public int setColorUnsure(int color){
        if (color <= 255 & color >= 0){
            return color;
        }
        if (color > 255 & color < 511){
            return RandomUtils.Int(random,color-255,255);
        }
        if (color < 0 & color > -256){
            return RandomUtils.Int(random,0,-color);
        }
        return RandomUtils.Int(random,0,255);
    }
    public void setVelocity(double asDouble, double asDouble1, double asDouble2) {
        this.velocityX = asDouble;
        this.velocityY = asDouble1;
        this.velocityZ = asDouble2;
        this.speedControl = false;

    }

    public void setCollidesWithWorld(boolean collidesWithWorld) {
        this.collidesWithWorld = collidesWithWorld;
        this.sCWW = true;

    }

    public void setMaxAge(int[] maxAges) {
        this.maxAge = maxAges;
        this.sMaxAge = true;

    }

    public void setScale(float[] scales) {
        this.scale = scales;
        this.sScale = true;
    }

    public void setAlpha(float[] alphas) {
        this.alpha = alphas;
        this.sAlpha =true;

    }

    public void setResistance(float x, float y, float z) {
        this.resistanceX = x;
        this.resistanceY = y;
        this.resistanceZ = z;
        this.sResistance = true;
    }

    public void setAccelerationX(Float f){
        if (f == null) return;
        this.accX = f;
        this.sAcc = true;
    }

    public void setAccelerationY(Float f){
        if (f == null) return;
        this.accY = f;
        this.sAcc = true;
    }
    public void setAccelerationZ(Float f){
        if (f == null) return;
        this.accZ = f;
        this.sAcc = true;
    }
//    public void setAcceleration(float asFloat, float asFloat1, float asFloat2) {
//        this.accX = asFloat;
//        this.accY = asFloat1;
//        this.accZ = asFloat2;
//        this.sAcc = true;
//    }

    public void setParent(String p_name) {
        this.parent = p_name;
    }

    public void setTargetColorR(Integer i) {
        if (i == null) return;
        this.targetColorR = i;
        this.sTC = true;
    }
    public void setTargetColorG(Integer i) {
        if (i == null) return;
        this.targetColorG = i;
        this.sTC = true;
    }
    public void setTargetColorB(Integer i) {
        if (i == null) return;
        this.targetColorB = i;
        this.sTC = true;
    }

    public void setTargetAlpha(float targetAlpha) {
        this.targetColorA = targetAlpha;
        this.sTA = true;
    }


    public void setVelocityX(Double d) {
        if (d == null) return;
        this.velocityX = d;
        this.speedControl = false;
    }
    public void setVelocityY(Double d) {
        if (d == null) return;
        this.velocityY = d;
        this.speedControl = false;

    }
    public void setVelocityZ(Double d) {
        if (d == null) return;
        this.velocityZ = d;
        this.speedControl = false;

    }
}
