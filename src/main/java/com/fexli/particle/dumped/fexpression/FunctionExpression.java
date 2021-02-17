package com.fexli.particle.dumped.fexpression;
//package com.isgk.colorblock.util.expression;

import com.fexli.particle.utils.ChatUtil;
import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.dumped.futil.StringUtil;

import java.util.Map;

public class FunctionExpression implements IExpression<Matrix, Matrix> {
    private IExpression<Matrix, Matrix> expression1 = null;

    private IExpression<Matrix, Matrix> expression2 = null;

    private IExpression<Matrix, Matrix> expression3 = null;

    private IExpression.Function function = null;

    private String expressionStr;

    public FunctionExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        this.expressionStr = expression;
        parse(expression);
        if (this.function == null)
            throw new RuntimeException(expression);
    }

    public Matrix invoke(Map<String, Matrix> args) {
        if (this.function == null) {
            ChatUtil.addChatMessage("Function Error:" + this.expressionStr);
            throw new RuntimeException(this.expressionStr);
        }
        try {
            if (this.expression2 == null) {
//                return new Matrix(this.function.invoke(new double[] { ((Matrix)this.expression1.invoke((Map)args)).getNumber() }));
                return new Matrix(this.function.invoke((this.expression1.invoke(args)).getNumber()));
            }
            if (this.expression3 == null) {
//                return new Matrix(this.function.invoke(new double[] { ((Matrix)this.expression1.invoke((Map)args)).getNumber(), ((Matrix)this.expression2.invoke((Map)args)).getNumber() }));
                return new Matrix(this.function.invoke((this.expression1.invoke(args)).getNumber(), (this.expression2.invoke(args)).getNumber()));
            }
            return new Matrix(this.function.invoke((this.expression1.invoke(args)).getNumber(), (this.expression2.invoke(args)).getNumber(), (this.expression3.invoke(args)).getNumber()));
        } catch (RuntimeException e) {
            ChatUtil.addChatMessage(e.getMessage());
            throw new RuntimeException(this.expressionStr);
        }
    }

    private void parse(String expression) {
        expression = StringUtil.removeOuterBracket(expression);
        int bracketBegin = expression.indexOf('(');
        int bracketEnd = expression.lastIndexOf(')');
        if (bracketBegin == -1 || bracketEnd == -1)
            throw new RuntimeException(expression);
        String funName = expression.substring(0, bracketBegin);
        boolean isFunc = false;
        for (IExpression.Function fun : IExpression.Function.values()) {
            if (fun.getName().equals(funName)) {
                this.function = fun;
                isFunc = true;
                break;
            }
        }
        if (!isFunc)
            throw new RuntimeException(expression);
        int index1 = -1;
        int index2 = -1;
        int bracket = 0;
        for (int i = bracketBegin + 1; i < bracketEnd; i++) {
            char ch = expression.charAt(i);
            switch (ch) {
                case '(':
                    bracket++;
                    break;
                case ')':
                    bracket--;
                    break;
                case ',':
                    if (bracket == 0 && index1 != -1) {
                        index2 = i;
                        break;
                    }
                    if (bracket == 0) index1 = i;
                    break;
            }
            if (bracket < 0)
                throw new RuntimeException(expression);
        }
        if (index1 == -1) {
            try {
                this.expression1 = new NumericalExpression(expression.substring(bracketBegin + 1, bracketEnd));
            } catch (RuntimeException e1) {
                try {
                    this.expression1 = new FunctionExpression(expression.substring(bracketBegin + 1, bracketEnd));
                } catch (RuntimeException e2) {
                    this.expression1 = new NumberExpression(expression.substring(bracketBegin + 1, bracketEnd), 0);
                }
            }
        } else if (index2 == -1) {
            try {
                this.expression1 = new NumericalExpression(expression.substring(bracketBegin + 1, index1));
            } catch (RuntimeException e1) {
                try {
                    this.expression1 = new FunctionExpression(expression.substring(bracketBegin + 1, index1));
                } catch (RuntimeException e2) {
                    this.expression1 = new NumberExpression(expression.substring(bracketBegin + 1, index1), 0);
                }
            }
            try {
                this.expression2 = new NumericalExpression(expression.substring(index1 + 1, bracketEnd));
            } catch (RuntimeException e1) {
                try {
                    this.expression2 = new FunctionExpression(expression.substring(index1 + 1, bracketEnd));
                } catch (RuntimeException e2) {
                    this.expression2 = new NumberExpression(expression.substring(index1 + 1, bracketEnd), 0);
                }
            }
        } else {
            try {
                this.expression1 = new NumericalExpression(expression.substring(bracketBegin + 1, index1));
            } catch (RuntimeException e1) {
                try {
                    this.expression1 = new FunctionExpression(expression.substring(bracketBegin + 1, index1));
                } catch (RuntimeException e2) {
                    this.expression1 = new NumberExpression(expression.substring(bracketBegin + 1, index1), 0);
                }
            }
            try {
                this.expression2 = new NumericalExpression(expression.substring(index1 + 1, index2));
            } catch (RuntimeException e1) {
                try {
                    this.expression2 = new FunctionExpression(expression.substring(index1 + 1, index2));
                } catch (RuntimeException e2) {
                    this.expression2 = new NumberExpression(expression.substring(index1 + 1, index2), 0);
                }
            }
            try {
                this.expression3 = new NumericalExpression(expression.substring(index2 + 1, bracketEnd));
            } catch (RuntimeException e1) {
                try {
                    this.expression3 = new FunctionExpression(expression.substring(index2 + 1, bracketEnd));
                } catch (RuntimeException e2) {
                    this.expression3 = new NumberExpression(expression.substring(index2 + 1, bracketEnd), 0);
                }
            }
        }
    }
}
