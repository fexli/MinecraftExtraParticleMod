package com.fexli.particle.particles.effectlib;

import com.fexli.particle.particles.DotChangeParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.PacketByteBuf;

import java.util.Locale;

public class DotChangeParticleEffect implements ParticleEffect {
    public static final Factory<DotChangeParticleEffect> PARAMETERS_FACTORY = new Factory<DotChangeParticleEffect>() {
        @Override
        public DotChangeParticleEffect read(ParticleType<DotChangeParticleEffect> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            int r = stringReader.readInt();
            stringReader.expect(' ');
            int g = stringReader.readInt();
            stringReader.expect(' ');
            int b = stringReader.readInt();
            stringReader.expect(' ');
            boolean srand = stringReader.readBoolean();
            stringReader.expect(' ');
            int tr = stringReader.readInt();
            stringReader.expect(' ');
            int tg = stringReader.readInt();
            stringReader.expect(' ');
            int tb = stringReader.readInt();
            stringReader.expect(' ');
            boolean trand = stringReader.readBoolean();
            stringReader.expect(' ');
            boolean disapp = stringReader.readBoolean();
            stringReader.expect(' ');
            float scale = stringReader.readFloat();
            stringReader.expect(' ');
            int age = stringReader.readInt();
            stringReader.expect(' ');
            double vx = stringReader.readDouble();
            stringReader.expect(' ');
            double vy = stringReader.readDouble();
            stringReader.expect(' ');
            double vz = stringReader.readDouble();
            return new DotChangeParticleEffect(r,g,b,srand,tr,tg,tb,trand,disapp,scale,age,vx,vy,vz);
        }

        @Override
        public DotChangeParticleEffect read(ParticleType<DotChangeParticleEffect> particleType, PacketByteBuf p) {
            return new DotChangeParticleEffect(p.readInt(),p.readInt(),p.readInt(),p.readBoolean(),p.readInt(),p.readInt(),p.readInt(),p.readBoolean(),p.readBoolean(),p.readFloat(),p.readInt(),p.readDouble(),p.readDouble(),p.readDouble());
        }
    };
    private final int r;
    private final int g;
    private final int b;
    private final boolean srand;
    private final int tr;
    private final int tg;
    private final int tb;
    private final boolean trand;
    private final boolean disappear;
    private final float scale;
    private final int age_;
    private final double vx;
    private final double vy;
    private final double vz;

    public DotChangeParticleEffect(int r, int g, int b, boolean srand, int tr, int tg, int tb, boolean trand,boolean disap, float scale, int age,double vx,double vy, double vz) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.srand = srand;
        this.tr = tr;
        this.tg = tg;
        this.tb = tb;
        this.trand = trand;
        this.disappear = disap;
        this.scale = scale;
        this.age_ = age;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }
    public int getR(){return this.r;}
    public int getG(){return this.g;}
    public int getB(){return this.b;}
    public boolean isSRand(){return this.srand;}
    public int getTR(){return this.tr;}
    public int getTG(){return this.tg;}
    public int getTB(){return this.tb;}
    public boolean isTRand(){return this.trand;}
    public boolean needDisappear(){return this.disappear;}
    public float getScale(){return this.scale;}
    public int getAge(){return this.age_;}
    public double getVx(){return this.vx;}
    public double getVy(){return this.vy;}
    public double getVz(){return this.vz;}

    @Override
    public ParticleType<?> getType() {
        return DotChangeParticle.DOTCHANGE;
    }

    @Override
    public void write(PacketByteBuf p) {
        p.writeInt(this.r);
        p.writeInt(this.g);
        p.writeInt(this.b);
        p.writeBoolean(this.srand);
        p.writeInt(this.tr);
        p.writeInt(this.tg);
        p.writeInt(this.tb);
        p.writeBoolean(this.trand);
        p.writeBoolean(this.disappear);
        p.writeFloat(this.scale);
        p.writeInt(this.age_);
        p.writeDouble(this.vx);
        p.writeDouble(this.vy);
        p.writeDouble(this.vz);
    }

    @Override
    public String asString() {
        String s = "fexli:dot_change ";
        if (this.srand) {
            s += "RandomColor ->";
        } else {
            s += this.r + " "+ this.g+" "+this.b+" ->";
        }
        if (this.trand) {
            s += "RandomColor ";
        } else {
            s += this.tr + " "+ this.tg+" "+this.tb+" ";
        }
        if (this.disappear){
            s += "Disappear ";
        }
        return String.format(Locale.ROOT, "%s scale:%.3f age:%d ac(%.3f %.3f %.3f)",s,this.scale,this.age_,this.vx,this.vy,this.vz);
    }
}
