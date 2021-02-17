package com.fexli.particle.future;

import com.fexli.particle.utils.FileUtils;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CustomParticleSettingManager {
    public static HashMap<String,CustomParticleSetting> settingHashMap = new HashMap<String, CustomParticleSetting>();
    public static Random random = new Random();
    private static boolean FileExist(String filename) {
        File fileordir = new File(filename);
        return fileordir.exists();
    }



    public static void reload(){
        settingHashMap = new HashMap<String, CustomParticleSetting>();
        register();
    }
    public static void register(){
        List<String> filelist= FileUtils.getFiles(JyProcessing.jsonRootDir,"",".json");
        if (filelist != null){
            for (String filename : filelist) {
                System.out.println("[ExParticle-Custom]Load Json:"+filename);
                try {
                    CustomParticleSetting setting = ParticleJsonReader.parseSetting(filename);
                    if (!settingHashMap.containsKey(setting.name))
                        settingHashMap.put(setting.name,setting);
                } catch (ValueException e){
                    System.out.println("[ExParticle-Json]"+filename+e.toString());
                }
            }
        }
    }
    public static CustomParticleSetting registerSingle(String filename){
        try {
            CustomParticleSetting newsetting = ParticleJsonReader.parseSetting(filename);
            settingHashMap.put(newsetting.name,newsetting);
            return newsetting;
        } catch (ValueException e) {
            System.out.println("[ExParticle-Json]"+filename+e.toString());
            return null;
        }
    }

    public static CustomParticleSetting getSetting(String pjsonname) {
        return settingHashMap.getOrDefault(pjsonname, null);
    }

//    public static CustomParticleSetting getSetting(String filename){
//        String jsonStr = readJson(filename);
//        if (jsonStr != null){
//            return ParticleJsonReader.parseSetting(ParticleJsonReader.parseStr2JObj(filename));
//        } else {
//            return new CustomParticleSetting();
//        }
//    }
    public static String readJson(String filename) {
        if (!FileExist(JyProcessing.jsonRootDir+filename)) {
            System.out.println("[ReadJson]"+filename+" file not exist");
            return null;
        }
        String jsonStr = "";
        try {
            File file = new File(JyProcessing.jsonRootDir+filename);
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            System.out.println("[ReadJson]"+e.toString());
            return null;
        }
    }
}
