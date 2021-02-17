package com.fexli.particle.utils;

import java.util.Random;

public class RandomUtils {
    public static float Float(Random random){
        return random.nextFloat();
    }
    public static float Float(Random random,float bonus){
        return random.nextFloat() * bonus;
    }
    public static float Float(Random random,float base,float bonus){
        if (bonus == 0) return base;
        return base +random.nextFloat() * bonus;
    }

    public static double Double(Random random){
        return random.nextDouble();
    }
    public static double Double(Random random, double bonus){
        return random.nextDouble() * bonus;
    }
    public static double Double(Random random,double base, double bonus){
        if (bonus == 0) return base;
        return base + random.nextDouble() * bonus;
    }

    public static int Int(Random random, int base, int bonus) {
        if (0 == bonus) return base;
        return base + random.nextInt(bonus);
    }
}
