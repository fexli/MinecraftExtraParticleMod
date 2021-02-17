package com.fexli.particle.particles.effectlib;

import com.fexli.particle.future.CustomParticleSetting;
import com.fexli.particle.future.CustomParticleSettingManager;
import com.fexli.particle.future.JyProcessing;
import com.fexli.particle.particles.CustomParticle;
import com.fexli.particle.particles.DotParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.PacketByteBuf;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class CustomParticleEffect implements ParticleEffect {

    public static final Factory<CustomParticleEffect> PARAMETERS_FACTORY = new Factory<CustomParticleEffect>() {
        @Override
        public CustomParticleEffect read(ParticleType<CustomParticleEffect> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            String r = stringReader.readString();
            return new CustomParticleEffect(r);
        }

        @Override
        public CustomParticleEffect read(ParticleType<CustomParticleEffect> type, PacketByteBuf packetByteBuf) {
            return new CustomParticleEffect(packetByteBuf.readString(),packetByteBuf.readFloat(),packetByteBuf.readFloat(),packetByteBuf.readFloat(),packetByteBuf.readBoolean());
        }
    };
    private final String pjsonname;
    private final Float vx;
    private final Float vy;
    private final Float vz;
    private final Boolean v;

    public CustomParticleEffect(String r) {
        this.pjsonname = r;
        this.vx = 0F;
        this.vy = 0F;
        this.vz = 0F;
        this.v = false;
    }
    public CustomParticleEffect(String r,float vx,float vy,float vz,boolean v){
        this.pjsonname = r;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.v = v;
    }

    public String getJsonFileName(){return this.pjsonname;}

    public CustomParticleSetting getSetting() {
//        System.out.println("[ExParticle][Debug]Loading:"+this.pjsonname);
        CustomParticleSetting s = CustomParticleSettingManager.getSetting(this.pjsonname);
        if (s == null) {
            System.out.println("[ExParticle][Debug]Loading Failed!");
//            Set<String> keys = CustomParticleSettingManager.settingHashMap.keySet();
//            Iterator<String> iterator1 = keys.iterator();
//            StringBuilder str = new StringBuilder();
//            while (iterator1.hasNext()){
//                str.append(iterator1.next()).append(" ");
//            }
//            System.out.println(str.toString());
        }
        if (s == null) return new CustomParticleSetting();
        if (this.v) {
            s.setVelocity(this.vx, this.vy, this.vz);
        }
        return s;
    }
    @Override
    public ParticleType<?> getType() {
        return CustomParticle.CUSTOM;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.pjsonname);
        buf.writeFloat(this.vx);
        buf.writeFloat(this.vy);
        buf.writeFloat(this.vz);
        buf.writeBoolean(this.v);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "Customized Particle: %s", this.pjsonname);
    }
}
