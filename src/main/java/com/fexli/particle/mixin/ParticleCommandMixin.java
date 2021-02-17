package com.fexli.particle.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.command.ParticleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;

@Mixin(ParticleCommand.class)
public class ParticleCommandMixin {

//    @Shadow @Final private static SimpleCommandExceptionType FAILED_EXCPETION;
//
//    /**
//     * @author fe_x_li
//     * @reason get raw return of Speed
//     */
//    @Overwrite
//    private static int execute(ServerCommandSource source, ParticleEffect parameters, Vec3d pos, Vec3d delta, float speed, int count, boolean force, Collection<ServerPlayerEntity> viewers) throws CommandSyntaxException {
//        int i = 0;
//        System.out.println("speed:"+ speed + " double:"+ (double)speed);
//        Iterator var9 = viewers.iterator();
//
//        while(var9.hasNext()) {
//            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var9.next();
//            if (source.getWorld().spawnParticles(serverPlayerEntity, parameters, force, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, (double)speed)) {
//                ++i;
//            }
//        }
//
//        if (i == 0) {
//            throw FAILED_EXCPETION.create();
//        } else {
//            source.sendFeedback(new TranslatableText("commands.particle.success", new Object[]{Registry.PARTICLE_TYPE.getId(parameters.getType()).toString()}), true);
//            return i;
//        }
//    }
}
