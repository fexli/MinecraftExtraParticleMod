package com.fexli.particle.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableText;

public class ChatUtil {
    public static MinecraftServer server = null;

    public static void setServer(MinecraftServer serv){
        server = serv;
    }
    public static void addChatMessage(String m){
        if (server != null) server.sendMessage(new TranslatableText(m));
    }
}
