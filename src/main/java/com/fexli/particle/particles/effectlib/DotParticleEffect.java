package com.fexli.particle.particles.effectlib;

import com.fexli.particle.particles.DotParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.PacketByteBuf;

import java.util.Locale;

public class DotParticleEffect implements ParticleEffect {

    public static final Factory<DotParticleEffect> PARAMETERS_FACTORY = new Factory<DotParticleEffect>() {
        @Override
        public DotParticleEffect read(ParticleType<DotParticleEffect> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            int r = (int)stringReader.readInt();
            stringReader.expect(' ');
            int g = (int)stringReader.readInt();
            stringReader.expect(' ');
            int b = (int)stringReader.readInt();
            stringReader.expect(' ');
            float scale = (float)stringReader.readFloat();
            stringReader.expect(' ');
            boolean random = stringReader.readBoolean();
            return new DotParticleEffect(r,g,b,scale,random);
        }

        @Override
        public DotParticleEffect read(ParticleType<DotParticleEffect> type, PacketByteBuf packetByteBuf) {
            return new DotParticleEffect(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readFloat(),packetByteBuf.readBoolean());
        }
    };
    private final int red;
    private final int green;
    private final int blue;
    private final float scale;
    private final boolean rand;

    public DotParticleEffect(int r, int g, int b, float s,boolean rand) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.scale = s;
        this.rand = rand;
    }

    public int getRed(){return this.red;}
    public int getGreen(){return this.green;}
    public int getBlue(){return this.blue;}
    public float getScale(){return this.scale;}
    public boolean isRand(){return this.rand;}

    @Override
    public ParticleType<?> getType() {
        return DotParticle.DOT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.red);
        buf.writeInt(this.green);
        buf.writeInt(this.blue);
        buf.writeFloat(this.scale);
        buf.writeBoolean(this.rand);
    }

    @Override
    public String asString() {
        if (this.rand){
            return String.format(Locale.ROOT,"%s %d %d %d %.3f","fexli:dot",this.red,this.green,this.blue,this.scale);
        } else{
            return String.format(Locale.ROOT, "%s RandomColor %.3f", "fexli:dot", this.scale);
        }
    }
}
