package com.fexli.particle.mixin;

import com.google.common.collect.Lists;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

import java.util.*;

@Mixin(ParticleManager.class)
public class ParticleLimitMixin {
    @Mutable
    @Shadow
    @Final
    private final Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Shadow
    protected World world;

    @Mutable
    @Shadow
    @Final
    private final Queue<EmitterParticle> newEmitterParticles;

    @Shadow
    @Final
    private Queue<Particle> newParticles;

    public ParticleLimitMixin(Map<ParticleTextureSheet, Queue<Particle>> particles, Queue<EmitterParticle> newEmitterParticles) {
        this.particles = particles;
        this.newEmitterParticles = newEmitterParticles;
    }


    /**
     * @author fe_x_li
     * @reason remove limit max of 16384 particles
     */
    @Overwrite
    private void tickParticles(Collection<Particle> collection){
        if (!collection.isEmpty()){
            Iterator<Particle> iterator = collection.iterator();
            while (iterator.hasNext()){
                Particle particle = iterator.next();
                tickParticle(particle);
                if (!particle.isAlive()){
                    iterator.remove();
                }
            }
        }
    }

    @Shadow
    private void tickParticle(Particle particle){

    }

    /**
     * @author fe_x_li
     * @reason remove limit max of 16384 particles
     */
    @Overwrite
    public void tick(){
        this.particles.forEach((particleTextureSheet, queue) -> {
            this.world.getProfiler().push(particleTextureSheet.toString());
            this.tickParticles(queue);
            this.world.getProfiler().pop();
        });
        if (!this.newEmitterParticles.isEmpty()) {
            List<EmitterParticle> list = Lists.newArrayList();
            Iterator<EmitterParticle> var2 = this.newEmitterParticles.iterator();

            while(var2.hasNext()) {
                EmitterParticle emitterParticle = var2.next();
                emitterParticle.tick();
                if (!emitterParticle.isAlive()) {
                    list.add(emitterParticle);
                }
            }

            this.newEmitterParticles.removeAll(list);
        }


        if (!this.newParticles.isEmpty()) {
            Particle particle;
            while((particle = (Particle)this.newParticles.poll()) != null) {
                ((Queue<Particle>)this.particles.computeIfAbsent(particle.getType(), e ->new LinkedList())).add(particle);
//                ((Queue)this.particles.computeIfAbsent(particle.getType(), (particleTextureSheet) -> {
//                    return EvictingQueue.create(16384);
//                })).add(particle);
            }
        }
    }
}
