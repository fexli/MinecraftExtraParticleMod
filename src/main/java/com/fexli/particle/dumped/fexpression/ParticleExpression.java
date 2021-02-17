package com.fexli.particle.dumped.fexpression;

import com.fexli.particle.dumped.futil.IExecutable;
import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.utils.ChatUtil;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class ParticleExpression implements IExecutable {
    private static final Map<String, ConditionExpression> conditionExpressionBuf = Maps.newHashMap();

    IExpression<?, Matrix> expression;

    Map<String, Matrix> args;

    Double defaultV = 0.0D;
    public ParticleExpression(String expression) {
        ConditionExpression ce;
        if (conditionExpressionBuf.containsKey(expression)) {
            ce = conditionExpressionBuf.get(expression);
        } else {
            ce = new ConditionExpression(expression);
            conditionExpressionBuf.put(expression, ce);
        }
        this.expression = ce;
        this.args = new HashMap<>();
    }

    public ParticleExpression(IExpression<?, Matrix> expression) {
        this.expression = expression;
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
            return Double.valueOf(((Matrix)obj).getNumber());
        if (obj instanceof Number)
            return Double.valueOf(((Number)obj).doubleValue());
        return this.defaultV;
//        ChatUtil.addChatMessage("Get Args Error:" + obj.toString());
//        throw new RuntimeException();
    }

    public Object invoke() {
        return this.expression.invoke(this.args);
    }

    @Override
    public void setDefault(Double value) {
        this.defaultV = value;
    }


}
