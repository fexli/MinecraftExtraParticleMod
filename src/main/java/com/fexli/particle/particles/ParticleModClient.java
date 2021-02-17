package com.fexli.particle.particles;

import com.fexli.particle.particles.bulletscreen.*;
import com.fexli.particle.patchs.ParticleFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;


public class ParticleModClient implements ClientModInitializer {

//    public static final DefaultParticleType SIMPLE_TEST_PARTICLE = ParticleTypeRegistry.getTnstance().register(new Identifier("testmod", "simple"));
//    public static final DefaultParticleType CUSTOM_TEST_PARTICLE = ParticleTypeRegistry.getTnstance().register(new Identifier("testmod", "custom"));

    @Override
    public void onInitializeClient() {
//        ParticleFactoryRegistry.getInstance().register(SIMPLE_TEST_PARTICLE, SimpleTestParticle::new);
//        ParticleFactoryRegistry.getInstance().register(CUSTOM_TEST_PARTICLE, CustomTestParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DUMOParticle.DUMO,DUMOParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(BigExplosionParticle.Explosion,BigExplosionParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DotParticle.DOT,DotParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(StarParticle.STAR,StarParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(RainbowParticle.RAINBOW,RainbowParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DotChangeParticle.DOTCHANGE,DotChangeParticle.Factory::new);
        // regist custom particles
        ParticleFactoryRegistry.getInstance().register(CustomParticle.CUSTOM,CustomParticle.Factory::new);
        // for Jinjin's Explosion particle
        ParticleFactoryRegistry.getInstance().register(ExplosionAParticle.ExplosionA,ExplosionAParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionBParticle.ExplosionB,ExplosionBParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionCParticle.ExplosionC,ExplosionCParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionDParticle.ExplosionD,ExplosionDParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionEParticle.ExplosionE,ExplosionEParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionFParticle.ExplosionF,ExplosionFParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionGParticle.ExplosionG,ExplosionGParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionHParticle.ExplosionH,ExplosionHParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ExplosionIParticle.ExplosionI,ExplosionIParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionJParticle.ExplosionJ,ExplosionJParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionKParticle.ExplosionK,ExplosionKParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionLParticle.ExplosionL,ExplosionLParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionMParticle.ExplosionM,ExplosionMParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionNParticle.ExplosionN,ExplosionNParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionOParticle.ExplosionO,ExplosionOParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ExplosionPParticle.ExplosionP,ExplosionPParticle.Factory::new);
        System.out.println("Extra Particle Package Initialized!");
    }


//    @Environment(EnvType.CLIENT)
//    static class SimpleTestParticle extends SpriteBillboardParticle {
//
//        public SimpleTestParticle(ParticleEffect effect, World world, double x, double y, double z, double velX, double velY, double velZ) {
//            super(world, x, y, z, velX, velY, velZ);
//
//            setSprite(MinecraftClient.getInstance().getItemRenderer().getModels().getSprite(Items.BARRIER));
//        }
//
//        @Override
//        public ParticleTextureSheet getType() {
//            return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
//        }
//    }
//
//    @Environment(EnvType.CLIENT)
//    static class CustomTestParticle extends AnimatedParticle {
//
//        protected CustomTestParticle(World world, double x, double y, double z, SpriteProvider sprites) {
//            super(world, x, y, z, sprites, 1);
//
//            setSprite(sprites.getSprite(world.random));
//        }
//
//        @Environment(EnvType.CLIENT)
//        public static class Factory implements ParticleFactory<DefaultParticleType> {
//
//            private final SpriteProvider sprites;
//
//            public Factory(SpriteProvider sprites) {
//                this.sprites = sprites;
//            }
//
//            @Override
//            public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
//                return new CustomTestParticle(world, x, y, z, sprites);
//            }
//        }
//    }
}