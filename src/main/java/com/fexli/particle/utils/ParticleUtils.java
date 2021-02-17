package com.fexli.particle.utils;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Utility class for spawning particles.
 */
public final class ParticleUtils {
    private ParticleUtils() { }

    /**
     * Adds a particle to the world with a pre-determined position and velocity.
     *
     * @param w             The world
     * @param effect        Data packet describing the particle to spawn
     * @param pos           Position
     * @param vel           Velocity
     */
    public static void spawnParticle(World w, ParticleEffect effect, Vec3d pos, Vec3d vel) {
        spawnParticle(w, effect, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
    }

    /**
     * Adds a particle to the world with a pre-determined position and velocity.
     */
    public static void spawnParticle(World w, ParticleEffect particleId, Vec3d pos, double speedX, double speedY, double speedZ) {
        spawnParticle(w, particleId, pos.x, pos.y, pos.z, speedX, speedY, speedZ);
    }

    /**
     * Adds a particle to the world with a pre-determined position and velocity.
     */
    public static void spawnParticle(World w, ParticleEffect particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ) {
        w.addParticle(particleId, posX, posY, posZ, speedX, speedY, speedZ);
    }

    public static int rgb2Int(int r,int g,int b){
        return r+g*256+b*256*256;
    }
}
