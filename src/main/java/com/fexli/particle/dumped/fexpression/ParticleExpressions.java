package com.fexli.particle.dumped.fexpression;


import com.fexli.particle.dumped.futil.IExecutable;
import com.google.common.collect.Maps;
import com.fexli.particle.utils.ChatUtil;
import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.dumped.futil.StringUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ParticleExpressions implements IExecutable {
    private static final Map<String, NumericalExpression[]> numericalExpressionsBuf = Maps.newHashMap();

    public IExpression<?, Matrix>[] expressions;

    private Map<String, Matrix> args;

    private Double defaultV = null;

    public ParticleExpressions(String expression) {
        NumericalExpression[] nes;
        if (numericalExpressionsBuf.containsKey(expression)) {
            nes = numericalExpressionsBuf.get(expression);
        } else {
            String[] expressionsString = expression.split(";");
            nes = new NumericalExpression[expressionsString.length];
            for (int i = 0; i < expressionsString.length; i++){
                nes[i] = new NumericalExpression(expressionsString[i]);
            }
            numericalExpressionsBuf.put(expression, nes);
        }
        this.expressions = (IExpression<?, Matrix>[])nes;
        this.args = new HashMap<>();
    }

    public ParticleExpressions(IExpression<?, Matrix>[] expressions) {
        this.expressions = expressions;
        this.args = new HashMap<>();
    }

    public void put(String key, Object value) {
        if (value instanceof Number) {
            this.args.put(key, new Matrix(((Number)value).doubleValue()));
        } else if (value instanceof Matrix) {
            this.args.put(key, (Matrix)value);
        } else {
            ChatUtil.addChatMessage("Put Args Error:" + value.toString());
            throw new RuntimeException();
        }
    }

    public Object get(String key) {
        Object obj = this.args.get(key);
        if (obj instanceof Matrix)
            return ((Matrix) obj).getNumber();
        if (obj instanceof Number)
            return ((Number) obj).doubleValue();
        return this.defaultV;
//        ChatUtil.addChatMessage("Get Args Error:" + obj.toString());
//        throw new RuntimeException();
    }
    public void setDefault(Double defaultValue){
        this.defaultV = defaultValue;
    }
    public Object invoke() {
        // TODO: check prev()
        for (IExpression<?, Matrix> expression : this.expressions){
            expression.invoke(this.args);

        }
        return null;
    }
}