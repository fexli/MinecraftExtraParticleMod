package com.fexli.particle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.CommandManager;
//import net.minecraft.server.ServerTask
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unchecked"})
public class ParticleHelpCommand {

    private static final SimpleCommandExceptionType FAILED_EXCPETION = new SimpleCommandExceptionType(new TranslatableText("commands.particle.failed", new Object[0]));

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> exparticleHelper = CommandManager.literal("particlehelper");
//        exparticleHelper.executes(context -> {return execute(context.getSource());});
        exparticleHelper.then(CommandManager.argument("particle_name", StringArgumentType.greedyString())
                .suggests((c, d)->{return suggestMatching((d));})
                .executes(context -> {return execute(context.getSource(),StringArgumentType.getString(context,"particle_name"));}));

        commandDispatcher.register(exparticleHelper);
    }

    private static CompletableFuture<Suggestions> suggestMatching(SuggestionsBuilder suggestionsBuilder) {
        suggestionsBuilder.suggest("fexli:dumo");
        suggestionsBuilder.suggest("fexli:dot");
        suggestionsBuilder.suggest("fexli:dot_change");
        suggestionsBuilder.suggest("fexli:rainbow");
        suggestionsBuilder.suggest("fexli:star");
        suggestionsBuilder.suggest("fexli:explosion");

        return suggestionsBuilder.buildFuture();
    }

//    private static int execute(ServerCommandSource source) {
//        source.sendFeedback(new TranslatableText("commands.particlehelper.success"),false);
//        return 0;
//    }

    private static int execute(ServerCommandSource source, String str) throws CommandSyntaxException {
        System.out.println(str);
        if (str.equals("fexli:dumo")){
            source.sendFeedback(new TranslatableText("commands.particlehelper.success.dumo"),false);
            return 1;
        }
        if (str.equals("fexli:dot")){
            source.sendFeedback(new TranslatableText("commands.particlehelper.success.dot"),false);
            return 1;
        }
        if (str.equals("fexli:dot_change")){
            source.sendFeedback(new TranslatableText("commands.particlehelper.success.dot_change"),false);
            return 1;
        }
        source.sendFeedback(new TranslatableText("commands.particlehelper.success.other"),false);
        return 1;
    }
}
