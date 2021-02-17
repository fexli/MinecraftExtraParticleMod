package com.fexli.particle.future;

import com.fexli.particle.future.JyProcessing;
import com.fexli.particle.utils.FileUtils;
import com.fexli.particle.utils.TextStyleManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class pythonCommand {
    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("python");
        literal.then(CommandManager.argument("file", StringArgumentType.word()).suggests((c,d) -> {
            return suggestFile(d);
        }).executes(context -> execute(context.getSource(),StringArgumentType.getString(context,"file")))
        .then(CommandManager.argument("args",StringArgumentType.greedyString()).executes(context -> execute(context.getSource(),StringArgumentType.getString(context,"file"),StringArgumentType.getString(context,"args")))));

        commandDispatcher.register(literal);
    }

    private static CompletableFuture<Suggestions> suggestFile(SuggestionsBuilder d) {
//        Set set = CustomParticleSettingManager.settingHashMap.keySet();
//        for (Object o : set) {
//            d.suggest((String) o);
//        }
        List<String> files = JyProcessing.pyList;
        if (files != null){
            for (String s : files){
                d.suggest(s);
            }
            return d.buildFuture();
        } else return d.buildFuture();
    }

    private static int execute(ServerCommandSource source) {
        source.sendFeedback(new TranslatableText("commands.python.help"),false);
        return 0;
    }

    private static int execute(ServerCommandSource source, String file) throws CommandSyntaxException {
        source.sendFeedback(new TranslatableText("commands.python.warn").setStyle(TextStyleManager.Red),false);
        JyProcessing.newThread(file,source, "");
        return 0;
    }

    private static int execute(ServerCommandSource source, String file,String args) throws CommandSyntaxException {
        source.sendFeedback(new TranslatableText("commands.python.warn").setStyle(TextStyleManager.Red),false);
        JyProcessing.newThread(file,source, args);
        return 0;
    }
}
