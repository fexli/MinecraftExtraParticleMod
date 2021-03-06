package com.fexli.particle.particles.bulletscreen.ertype;
import com.fexli.particle.particles.bulletscreen.ExplosionOParticle;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.PacketByteBuf;
import java.util.Locale;
public class JinOParticleEffect implements ParticleEffect {
    public static final ParticleEffect.Factory<JinOParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<JinOParticleEffect>() {
        @Override
        public JinOParticleEffect read(ParticleType<JinOParticleEffect> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            double vx = stringReader.readDouble();
            stringReader.expect(' ');
            double vy = stringReader.readDouble();
            stringReader.expect(' ');
            double vz = stringReader.readDouble();
            stringReader.expect(' ');
            float scale = (float) stringReader.readFloat();
            stringReader.expect(' ');
            int age = stringReader.readInt();
            stringReader.expect(' ');
            float alpha = stringReader.readFloat();
            return new JinOParticleEffect(vx, vy, vz, scale, age, alpha);
        }
        @Override
        public JinOParticleEffect read(ParticleType<JinOParticleEffect> type, PacketByteBuf packetByteBuf) {
            return new JinOParticleEffect(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readFloat(), packetByteBuf.readInt(), packetByteBuf.readFloat());
        }
    };
    private final double vx;
    private final double vy;
    private final double vz;
    private final float scale;
    private final int mAge_;
    private final float alpha;
    public JinOParticleEffect(double vx, double vy, double vz, float scale, int age, float alpha) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.scale = scale;
        this.mAge_ = age;
        this.alpha = alpha;
    }
    public double getVx() {
        return this.vx;
    }
    public double getVy() {
        return this.vy;
    }
    public double getVz() {
        return this.vz;
    }
    public float getScale() {
        return this.scale;
    }
    public int getmAge_() {
        return this.mAge_;
    }
    @Override
    public ParticleType<?> getType() {
        return ExplosionOParticle.ExplosionO;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeDouble(this.vx);
        buf.writeDouble(this.vy);
        buf.writeDouble(this.vz);
        buf.writeFloat(this.scale);
        buf.writeInt(this.mAge_);
        buf.writeFloat(this.alpha);
    }
    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.3f %d", "p:o", this.vx, this.vy, this.vz, this.scale, this.mAge_);
    }
    public float getAlpha() {
        return this.alpha;
    }
}
