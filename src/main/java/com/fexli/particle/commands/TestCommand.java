package com.fexli.particle.commands;

import com.fexli.particle.dumped.fexpression.ExpressionUtil;
import com.fexli.particle.dumped.futil.IExecutable;
import com.fexli.particle.particles.effectlib.DotParticleEffect;
import com.fexli.particle.utils.TextStyleManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.entity.player.PlayerEntity;
import com.fexli.particle.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class TestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("fxltest").
                executes((context) -> listLog(context.getSource()))
                .then(CommandManager.literal("expr").then(CommandManager.argument("expr", StringArgumentType.greedyString()).executes(context -> {
                    return exprInvoke(context.getSource(), StringArgumentType.getString(context, "expr"));
                })))
                .then(CommandManager.literal("draw2D").then(CommandManager.literal("coordinate").then(CommandManager.argument("expr", StringArgumentType.greedyString()).executes(context -> {
                    return exprDraw(context, context.getSource(), StringArgumentType.getString(context, "expr"),true);
                }))).then(CommandManager.literal("polar").then(CommandManager.argument("expr", StringArgumentType.greedyString()).executes(context -> {
                    return exprDraw(context, context.getSource(), StringArgumentType.getString(context, "expr"),false);
                }))))
                .then(CommandManager.literal("drawT").then(CommandManager.literal("coordinate").then(CommandManager.argument("expr", StringArgumentType.greedyString()).executes(context -> {
                    return exprDrawTime(context, context.getSource(), StringArgumentType.getString(context, "expr"),true);
                }))).then(CommandManager.literal("polar").then(CommandManager.argument("expr", StringArgumentType.greedyString()).executes(context -> {
                    return exprDrawTime(context, context.getSource(), StringArgumentType.getString(context, "expr"),false);
                }))));

        dispatcher.register(literalargumentbuilder);
    }

    private static int exprDrawTime(CommandContext<ServerCommandSource> context, ServerCommandSource source, String expr,boolean coor) {
        try {
            if (coor){
                long milli = System.currentTimeMillis();
                Vec3d pos = source.getPosition();
                for (int i = 0; i < 400; i++){
                    for (ServerPlayerEntity serverPlayerEntity : source.getMinecraftServer().getPlayerManager().getPlayerList()) {
                        if (i % 40 == 0){
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(0, 0, 0, 0.008F, false), true, pos.x, pos.y - (float)(i-200)/40F, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(0, 0, 0, 0.008F, false), true, pos.x - (float)(i-200)/40F, pos.y, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(0, 0, 0, 0.008F, false), true, pos.x, pos.y, pos.z - (float)(i-200)/40F, 1, 0, 0, 0, 0);
                        } else{
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(255, 255, 255, 0.005F, false), true, pos.x - (float)(i-200)/40F, pos.y, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(255, 255, 255, 0.005F, false), true, pos.x, pos.y - (float)(i-200)/40F, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(255, 255, 255, 0.005F, false), true, pos.x, pos.y, pos.z - (float)(i-200)/40F, 1, 0, 0, 0, 0);
                        }
                    }
                }
                IExecutable exe = ExpressionUtil.prase(expr, false);
                if (exe == null) throw new RuntimeException("Invalid Expression");
                exe.setDefault(0.0D);

                exe.put("pi", Math.PI);
                exe.put("e", Math.E);
                exe.put("prevx",0D);
                exe.put("prevy",0D);
                exe.put("prevz",0D);
                exe.put("prevt",0D);

                exe.put("t",0D);
                exe.invoke();

                double ts = (Double) exe.get("ts");
                double te = (Double) exe.get("te");
                double step = (Double) exe.get("step");
                if (step == 0) step = Math.PI*0.001;
                if (ts == 0) ts = 0;
                if (te == 0) te = 2*Math.PI;
                if (ts < te && step < 0) throw new ValueException("step must be positive when ts<te");
                if (ts > te && step > 0) throw new ValueException("step must be negative when ts>te");

                for (double t = ts;t < te;t += step){
                    exe.put("t",t);
                    exe.invoke();
                    double x = (double)exe.get("x");
                    double y = (double)exe.get("y");
                    double z = (double)exe.get("z");
                    for (ServerPlayerEntity serverPlayerEntity : source.getMinecraftServer().getPlayerManager().getPlayerList()) {
                        source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(111, 243, 255, 0.006F, false), true, pos.x + x, pos.y + y, pos.z + z, 1, 0, 0, 0, 0);
                    }
                    exe.put("prevx",x);
                    exe.put("prevy",y);
                    exe.put("prevz",z);
                    exe.put("prevt",t);
                }
                source.sendFeedback(new TranslatableText(" =successful("+(System.currentTimeMillis()-milli)+"ms)").setStyle(TextStyleManager.Aqua), false);
            } else {
                return 0;
                // 极坐标 以后再写
            }

            return 1;
        } catch (Exception e) {
            source.sendFeedback(new TranslatableText(" =failure("+e.toString()+")").setStyle(TextStyleManager.Red),false);
            return 0;
        }
    }
    private static int exprDraw(CommandContext<ServerCommandSource> context, ServerCommandSource source, String expr,boolean coor) {
        try {
            if (coor){
                long milli = System.currentTimeMillis();
                Vec3d pos = source.getPosition();
                for (int i = 0; i < 400; i++){
                    for (ServerPlayerEntity serverPlayerEntity : source.getMinecraftServer().getPlayerManager().getPlayerList()) {
                        if (i % 40 == 0){
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(0, 0, 0, 0.008F, false), true, pos.x, pos.y - (float)(i-200)/40F, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(0, 0, 0, 0.008F, false), true, pos.x - (float)(i-200)/40F, pos.y, pos.z, 1, 0, 0, 0, 0);
                        } else{
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(255, 255, 255, 0.005F, false), true, pos.x - (float)(i-200)/40F, pos.y, pos.z, 1, 0, 0, 0, 0);
                            source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(255, 255, 255, 0.005F, false), true, pos.x, pos.y - (float)(i-200)/40F, pos.z, 1, 0, 0, 0, 0);
                        }
                    }
                }
                IExecutable exe = ExpressionUtil.prase(expr, false);
                if (exe == null) throw new RuntimeException("Invalid Expression");
                exe.setDefault(0.0D);
                exe.put("pi", Math.PI);
                exe.put("e", Math.E);
                for (float x = -5;x <= 5;x += 0.002){
                    exe.put("x",(double)x);
                    exe.invoke();
                    double y = (double)exe.get("y");
                    for (ServerPlayerEntity serverPlayerEntity : source.getMinecraftServer().getPlayerManager().getPlayerList()) {
                        source.getWorld().spawnParticles(serverPlayerEntity, new DotParticleEffect(111, 243, 255, 0.006F, false), true, pos.x + x, pos.y + y, pos.z, 1, 0, 0, 0, 0);
                    }
                }
                source.sendFeedback(new TranslatableText(" =successful("+(System.currentTimeMillis()-milli)+"ms)").setStyle(TextStyleManager.Aqua), false);
            } else {
                return 0;
                // 极坐标 以后再写
            }

            return 1;
        } catch (Exception e) {
//            source.sendFeedback(new TranslatableText(" =failure(" + e.toString() + ")").setStyle(TextStyleManager.Red), false);
            source.sendFeedback(new TranslatableText(" =failure("+e.toString()+")").setStyle(TextStyleManager.Red),false);
            return 0;
        }
    }

    private static int exprInvoke(ServerCommandSource source, String expr) throws CommandSyntaxException {
        try {
            IExecutable exe = ExpressionUtil.prase(expr, false);
            if (exe == null) throw new RuntimeException("Invalid Expression");
            exe.put("pi", Math.PI);
            exe.put("e", Math.E);
            exe.invoke();
            double x = (Double) exe.get("x");
            source.sendFeedback(new TranslatableText(" =successful(" + x + ")").setStyle(TextStyleManager.Aqua), false);
            return 1;
        } catch (Exception e) {
            source.sendFeedback(new TranslatableText(" =failure(" + e.toString() + ")").setStyle(TextStyleManager.Red), false);
            return 0;
        }
    }

    private static int listLog(ServerCommandSource source) {
        PlayerEntity player;
        try {
            player = source.getPlayer();
            Messenger.m(player, "w Hello! Fexli's Test Command Player");
            return 1;
        } catch (CommandSyntaxException e) {
            Messenger.m(source, "Hello! Fexli's Test Command Server!");
            return 0;
        }
    }
}
