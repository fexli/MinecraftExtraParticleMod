package com.fexli.particle.dumped.fexpression;


import com.fexli.particle.dumped.futil.IExecutable;
import com.fexli.particle.dumped.futil.StringUtil;

public final class ExpressionUtil {
    public static IExecutable prase(String expression, boolean enableReturn) {
        if (!(StringUtil.getCharInString(expression,"(") == StringUtil.getCharInString(expression,")"))) {
            throw new RuntimeException("Expression's Brackets are not equal");
        }
        IExecutable exe;
        if (expression == null || expression.equals("null"))
            return null;
        if (enableReturn) {
            exe = new ParticleExpression(expression);
        } else {
            exe = new ParticleExpressions(expression);
        }
        return exe;
    }
}
