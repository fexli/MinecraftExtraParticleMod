package com.fexli.particle.commands;

import com.fexli.particle.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class QuickReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("/r")
                .executes(context -> {
                    context.getSource().getMinecraftServer().reload();
                    context.getSource().sendFeedback(new TranslatableText("commands.reload.success"),true);
                    return 0;
                });

        dispatcher.register(literalargumentbuilder);
    }
}
