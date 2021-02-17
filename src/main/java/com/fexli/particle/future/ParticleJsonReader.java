package com.fexli.particle.future;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParticleJsonReader {
    public static JsonObject parseStr2JObj(String jsonStr){
        JsonParser jsonParser = new JsonParser();
        /*
         *JsonElement parse(String json)
         * 如果被解析的字符串不符合 json 格式，则抛出异常
         */
        if (jsonStr == null) return null;
        return jsonParser.parse(jsonStr).getAsJsonObject();

    }
    public static void set(CustomParticleSetting setting,String key,int value){

    }
    public static CustomParticleSetting parseSetting(String str){
        return parseSetting(parseStr2JObj(CustomParticleSettingManager.readJson(str)));
    }

    public static CustomParticleSetting parseSetting(JsonObject jsonObject){
        if (jsonObject == null) throw new ValueException("jsonObject is null!");
        CustomParticleSetting setting = new CustomParticleSetting();
        // https://blog.csdn.net/wangmx1993328/article/details/84385548
        // https://www.cnblogs.com/sjjg/p/4714221.html
        // https://recomm.cnblogs.com/blogpost/11929207
        // get Parent
        getParentSetting(setting,jsonObject);
        // override new seting self
        getBaseSetting(setting,jsonObject);
        // parse load
        // -> Finished:
        // speed speedControl velocity
        // collidesWithWorld
        // color alpha
        // maxAge scale
        // resistance
        getLoadSetting(setting,jsonObject);
        return setting;
    }

    private static void getLoadSetting(CustomParticleSetting setting, JsonObject jsonObject) {
        if (jsonObject.has("load")){
            JsonObject load = jsonObject.get("load").getAsJsonObject();
            if (load.has("speedControl")){
                if (load.get("speedControl").getAsBoolean()){
                    setting.setSpeed(getAsRandomDouble(load.get("speed")));
                } else LoadVelocity(setting,load);
            } else if (load.has("speed")) {
                setting.setSpeed(getAsRandomDouble(load.get("speed")));
            } else{
                LoadVelocity(setting, load);
            }

            if (load.has("collidesWithWorld")){
                setting.setCollidesWithWorld(load.get("collidesWithWorld").getAsBoolean());
            }

            if (load.has("color")){
                JsonArray colorArray = load.get("color").getAsJsonArray();
                if (colorArray.size() != 3){
                    throw new ValueException("load.color[] size must be 3, got "+colorArray.size());
                }
                setting.setColor(colorArray.get(0).getAsInt(),colorArray.get(1).getAsInt(),colorArray.get(2).getAsInt());
            }
            if (load.has("alpha")){
                setting.setAlpha(getAsRandomFloat(load.get("alpha")));
            }
            if (load.has("maxAge")){
                setting.setMaxAge(getAsRandomInt(load.get("maxAge")));
            }
            if (load.has("scale")){
                setting.setScale(getAsRandomFloat(load.get("scale")));
            }
            if (load.has("resistance")){
                LoadResistance(setting,load.get("resistance").getAsJsonArray());
            }
            if (load.has("acceleration")){
                LoadAcceleration(setting,load.get("acceleration").getAsJsonArray());
            }
            if (load.has("targetControl")){
                if (load.has("targetColor")){
                    JsonArray colorArray = load.get("targetColor").getAsJsonArray();
                    if (colorArray.size() != 3){
                        throw new ValueException("load.targetColor[] size must be 3, got "+colorArray.size());
                    }
                    setting.setTargetColorR(getAsIntOrNull(colorArray.get(0)));
                    setting.setTargetColorG(getAsIntOrNull(colorArray.get(1)));
                    setting.setTargetColorB(getAsIntOrNull(colorArray.get(2)));
//                    setting.setTargetColor(colorArray.get(0).getAsInt(),colorArray.get(1).getAsInt(),colorArray.get(2).getAsInt());
                }
                if (load.has("targetAlpha")){
                    setting.setTargetAlpha(load.get("targetAlpha").getAsFloat());
                }
//                throw new ValueException("targetControl Must contain targetColor[] or targetAlpha!");
            }

            // here now
        } else {
            throw new ValueException("ParticleJson Must Contain load{}");
        }
    }

    private static Integer getAsIntOrNull(JsonElement jsonElement) {
        try {
            return jsonElement.getAsInt();
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            return null;
        }
    }


    private static float[] getAsRandomFloat(JsonElement floatArray) {
        try {
            float i = floatArray.getAsFloat();
            return new float[]{i,0};
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            try {
                JsonObject rand = floatArray.getAsJsonObject();
                float min = rand.get("base").getAsFloat();
                float max = rand.get("bonus").getAsFloat();
                return new float[]{min,max};
            } catch (ClassCastException|IllegalStateException|UnsupportedOperationException ee){
                throw new ValueException("Err on parsing Random-Type Float:"+ee.toString());
            }
        }
    }

    private static void LoadAcceleration(CustomParticleSetting setting, JsonArray accArray) {
        if (accArray.size() != 3) throw new ValueException("acceleration[] size must be 3,got "+accArray.size());
//        setting.setAcceleration(accArray.get(0).getAsFloat(),accArray.get(1).getAsFloat(),accArray.get(2).getAsFloat());
        setting.setAccelerationX(getasFloatOrNull(accArray.get(0)));
        setting.setAccelerationY(getasFloatOrNull(accArray.get(1)));
        setting.setAccelerationZ(getasFloatOrNull(accArray.get(2)));
    }

    private static Float getasFloatOrNull(JsonElement jsonElement) {
        try {
            return jsonElement.getAsFloat();
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            return null;
        }
    }

    private static void LoadResistance(CustomParticleSetting setting, JsonArray regArray){
        if (regArray.size() != 3) throw new ValueException("resistance[] size must be 3,got "+regArray.size());
        setting.setResistance(regArray.get(0).getAsFloat(),regArray.get(1).getAsFloat(),regArray.get(2).getAsFloat());
    }
    private static void LoadVelocity(CustomParticleSetting setting, JsonObject load) {
        if (load.has("velocity")){
            JsonArray velocity = load.get("velocity").getAsJsonArray();
            if (velocity.size() != 3) throw new ValueException("velocity[] size must be 3, got "+velocity.size());
            setting.setVelocityX(getAsDoubleOrNull(velocity.get(0)));
            setting.setVelocityY(getAsDoubleOrNull(velocity.get(1)));
            setting.setVelocityZ(getAsDoubleOrNull(velocity.get(2)));
//            setting.setVelocity(velocity.get(0).getAsDouble(),velocity.get(1).getAsDouble(),velocity.get(2).getAsDouble());

        } else {
            throw new ValueException("ParticleJson Must Contain load.speed or load.velocity");
        }
    }

    private static Double getAsDoubleOrNull(JsonElement jsonElement) {
        try {
            return jsonElement.getAsDouble();
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            return null;
        }
    }

    private static int[] getAsRandomInt(JsonElement intObj) {
        try {
            int i = intObj.getAsInt();
            return new int[]{i,0};
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            try {
                JsonObject rand = intObj.getAsJsonObject();
                int min = rand.get("base").getAsInt();
                int max = rand.get("bonus").getAsInt();
                return new int[]{min,max};
            } catch (ClassCastException|IllegalStateException|UnsupportedOperationException ee){
                throw new ValueException("Err on parsing Random-Type Int:"+ee.toString());
            }
        }
    }
    private static double[] getAsRandomDouble(JsonElement speed) {
        try {
            double d =  speed.getAsDouble();
            return new double[]{d,0};
        } catch (ClassCastException|IllegalStateException|UnsupportedOperationException e){
            try {
                JsonObject rand = speed.getAsJsonObject();
                double min = rand.get("base").getAsDouble();
                double max = rand.get("bonus").getAsDouble();
                return new double[]{min,max};
            } catch (ClassCastException|IllegalStateException|UnsupportedOperationException ee){
                throw new ValueException("Err on parsing Random-Type Double:"+ee.toString());
            }
        }
    }

    private static void getBaseSetting(CustomParticleSetting setting, JsonObject jsonObject) {
        if (jsonObject.has("version")){
            setting.setVersion(jsonObject.get("version").getAsInt());
        }
        if (jsonObject.has("name")){
            setting.setName(jsonObject.get("name").getAsString());
        } else {
            throw new ValueException("ParticleJson Must Contain a name!");
        }
        if (jsonObject.has("type")){
            setting.setType(jsonObject.get("type").getAsString());
        }
    }

    private static void getParentSetting(CustomParticleSetting setting, JsonObject jsonObject) {
        if (jsonObject.has("parent")){
            try {
                String p_name = jsonObject.get("parent").getAsString();
                setting.setParent(p_name);
            } catch (UnsupportedOperationException ignored){

            }

        }

//        if (jsonObject.has("parent")){
//            if (!jsonObject.get("parent").isJsonNull()) {
//                String p_name = jsonObject.get("parent").getAsString();
//                if (CustomParticleSettingManager.settingHashMap.containsKey(p_name)){
//                    // read from settingMap
//                    setting.combine(CustomParticleSettingManager.settingHashMap.get(p_name));
//                } else {
//                    CustomParticleSetting p_setting = CustomParticleSettingManager.registerSingle(p_name);
//                    if (p_setting != null) {setting.combine(p_setting);} else {
//                        throw new ValueException("Parsing Parent "+p_name+" Setting Failed!");
//                    }
//                }
//            }
//        }
    }


}