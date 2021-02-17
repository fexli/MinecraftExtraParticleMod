package com.fexli.particle.future;


import com.fexli.particle.utils.FileUtils;
import com.fexli.particle.utils.TextStyleManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class JyProcessing {
    public static final String pyRootDir = "./exparticles/python/";
    public static final String jsonRootDir = "./exparticles/p_json/";
    public static List<String> pyList;
    private static HashMap<String, Thread> pool;


    public static void registerDir() {
        File pydir = new File(pyRootDir);
        if (!pydir.exists()) {
            pydir.mkdirs();
        }
        File jsondir = new File(jsonRootDir);
        if (!jsondir.exists()) {
            jsondir.mkdirs();
        }

    }
    public static void loadDirs(){
        pyList = FileUtils.getFiles(JyProcessing.pyRootDir,"");
    }
    public static void newThread(String filename, ServerCommandSource source, String args) throws CommandSyntaxException {
        try {
            PyThread thr = new PyThread(filename, filename, source, args);
//            pool.put(filename,thr);
            thr.run();
        } catch (Exception e) {
            source.sendFeedback(new TranslatableText("commands.python.execute.failure", e.toString()).setStyle(TextStyleManager.Red), false);
            System.out.println(e.getMessage());
        }

    }
}