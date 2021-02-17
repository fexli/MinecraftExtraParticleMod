package com.fexli.particle.dumped.futil;


public interface IExecutable {
    void put(String paramString, Object paramObject);

    Object get(String paramString);

    Object invoke();

    void setDefault(Double value);
}
