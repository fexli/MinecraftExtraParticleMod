package com.fexli.particle.future;

import com.fexli.particle.particles.effectlib.CustomParticleEffect;
import com.fexli.particle.utils.ExpressionArgumentType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.command.arguments.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ExtraParticleCommand {
//    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
//        LiteralCommandNode<ServerCommandSource> literalCommandNode = commandDispatcher.register(CommandManager.literal("exparticle").then(CommandManager.argument("name", StringArgumentType.string()).executes(context -> {return execite(context.getSource());})));
//        LiteralArgumentBuilder<ServerCommandSource> exparticle =  CommandManager.literal("exparticle");
//        exparticle.then(CommandManager.argument("name", StringArgumentType.string()).executes(context -> {return execite(context.getSource());}));
//        exparticle.then(CommandManager.literal("at").then(CommandManager.argument("targets", EntityArgumentType.entities()).fork(literalCommandNode, (commandContext) -> {
//            List<ServerCommandSource> list = Lists.newArrayList();
//            for (Entity entity : EntityArgumentType.getOptionalEntities(commandContext, "targets")) {
//                list.add((commandContext.getSource()).withWorld((ServerWorld) entity.world).withPosition(entity.getPosVector()).withRotation(entity.getRotationClient()));
//            }
//            return list;
//        })));
//        exparticle.then(CommandManager.literal("positioned").then(CommandManager.argument("pos", Vec3ArgumentType.vec3()).redirect(literalCommandNode, (commandContext) -> {
//            return (commandContext.getSource()).withPosition(Vec3ArgumentType.getVec3(commandContext, "pos"));
//        })));
//        exparticle.then(CommandManager.literal("rotated").then(CommandManager.argument("rot", RotationArgumentType.rotation()).redirect(literalCommandNode, (commandContext) -> {
//            return (commandContext.getSource()).withRotation(RotationArgumentType.getRotation(commandContext, "rot").toAbsoluteRotation(commandContext.getSource()));
//        })));
//        exparticle.then(CommandManager.literal("align").then(CommandManager.argument("axes", SwizzleArgumentType.swizzle()).redirect(literalCommandNode, (commandContext) -> {
//            return (commandContext.getSource()).withPosition(((ServerCommandSource) commandContext.getSource()).getPosition().floorAlongAxes(SwizzleArgumentType.getSwizzle(commandContext, "axes")));
//        })));
//        exparticle.then(CommandManager.literal("in").then(CommandManager.argument("dimension", DimensionArgumentType.dimension()).redirect(literalCommandNode, (commandContext) -> {
//            return (commandContext.getSource()).withWorld(((ServerCommandSource) commandContext.getSource()).getMinecraftServer().getWorld(DimensionArgumentType.getDimensionArgument(commandContext, "dimension")));
//        })));
//        commandDispatcher.register(exparticle);
//    }
    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> exParticle = CommandManager.literal("exparticle")
                .then(CommandManager.literal("custom").then(CommandManager.argument("name", StringArgumentType.string()).suggests((c,d)->{ return suggestMatching(d);})
                .executes(context -> {return execite(context.getSource(), StringArgumentType.getString(context,"name"),Vec3d.ZERO,null);})
                        .then(CommandManager.argument("pos",Vec3ArgumentType.vec3())
                        .executes(context -> {return execite(context.getSource(), StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context, "pos"),null);})
                                .then(CommandManager.argument("velocate", Vec3ArgumentType.vec3(false))
                                .executes(context -> {return execite(context.getSource(), StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context, "pos"),Vec3ArgumentType.getVec3(context, "velocate"));})))))
                .then(CommandManager.literal("parameterAll").then(CommandManager.argument("name", ExpressionArgumentType.expression()).suggests((c, d) -> { return suggestMatchingParticleType(d);})
                        .then(CommandManager.argument("center",Vec3ArgumentType.vec3()).then(CommandManager.argument("posexpr",ExpressionArgumentType.expression()).executes(context -> {
                            return parameter(context.getSource(),StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context,"center"),ExpressionArgumentType.getString(context,"posexpr"),false,null);
                        }).then(CommandManager.argument("velocityexpr",ExpressionArgumentType.expression()).executes(context -> {
                            return parameter(context.getSource(),StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context,"center"),ExpressionArgumentType.getString(context,"posexpr"),false,ExpressionArgumentType.getString(context,"velocityexpr"));
                        }))))))
                .then(CommandManager.literal("parameterTick").then(CommandManager.argument("name", ExpressionArgumentType.expression()).suggests((c, d) -> { return suggestMatchingParticleType(d);})
                        .then(CommandManager.argument("center",Vec3ArgumentType.vec3()).then(CommandManager.argument("posexpr",ExpressionArgumentType.expression()).executes(context -> {
                            return parameter(context.getSource(),StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context,"center"),ExpressionArgumentType.getString(context,"posexpr"),true,null);
                        }).then(CommandManager.argument("velocityexpr",ExpressionArgumentType.expression()).executes(context -> {
                            return parameter(context.getSource(),StringArgumentType.getString(context,"name"),Vec3ArgumentType.getVec3(context,"center"),ExpressionArgumentType.getString(context,"posexpr"),true,ExpressionArgumentType.getString(context,"velocityexpr"));
                        }))))));

        LiteralCommandNode<ServerCommandSource> exParticleNode = commandDispatcher.register(exParticle);


    }
    private static int parameter(ServerCommandSource source,String name,Vec3d center,String posexpr,boolean ticking,String velocityexpr){
        return 0;
    }
    private static CompletableFuture<Suggestions> suggestMatchingParticleType(SuggestionsBuilder d) {
        d.suggest("fexli:dot");
        return d.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestMatching(SuggestionsBuilder suggestionsBuilder) {
        Set set = CustomParticleSettingManager.settingHashMap.keySet();
        for (Object o : set) {
            suggestionsBuilder.suggest((String) o);
        }


        return suggestionsBuilder.buildFuture();
    }

    private static int execite(ServerCommandSource source,String name, Vec3d pos,Vec3d velocate) throws CommandSyntaxException {
        Iterator playerList = source.getMinecraftServer().getPlayerManager().getPlayerList().iterator();
        int track = 0;
        while (playerList.hasNext()){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)playerList.next();
            if (velocate == null){
                if (source.getWorld().spawnParticles(serverPlayerEntity, new CustomParticleEffect(name,0F,0F,0F,false), true, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0D)) {
                    ++track;
                }
            } else{
                if (source.getWorld().spawnParticles(serverPlayerEntity, new CustomParticleEffect(name,(float)velocate.x,(float)velocate.y,(float)velocate.z,true), true, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0D)) {
                    ++track;
                }
            }

        }
//        source.sendFeedback(new TranslatableText(source.getPlayer().getUuidAsString()),false);
        source.sendFeedback(new TranslatableText("Successful"),false);
        return track;
    }
}
