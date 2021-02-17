package com.fexli.particle.dumped.fexpression;

import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.dumped.futil.StringUtil;
import java.util.Map;

public class ConditionExpression implements IExpression<Boolean, Matrix> {
    private static final IExpression.Operator[][] SUCCESSOPERATOR = new IExpression.Operator[][] { { IExpression.Operator.EQU, IExpression.Operator.NEQ, IExpression.Operator.GRE, IExpression.Operator.LES, IExpression.Operator.GREEQU, IExpression.Operator.LESEQU }, { IExpression.Operator.NOT }, { IExpression.Operator.AND }, { IExpression.Operator.OR } };

    private IExpression<Boolean, Matrix> leftcExpression = null;

    private IExpression<Boolean, Matrix> rightcExpression = null;

    private IExpression<Matrix, Matrix> leftExpression = null;

    private IExpression<Matrix, Matrix> rightExpression = null;

    private IExpression.Operator operator = null;

    private String expression;

    public ConditionExpression(String expression) {
        expression.replaceAll(" ", "");
        this.expression = expression;
        parse(expression);
        if (this.operator == null)
            throw new RuntimeException(expression);
    }

    public Boolean invoke(Map<String, Matrix> args) {
        boolean result;
        switch (this.operator) {
            case EQU:
                result = (this.leftExpression.invoke(args)).equals(this.rightExpression.invoke(args));
                return result;
            case NEQ:
                result = !(this.leftExpression.invoke(args)).equals(this.rightExpression.invoke(args));
                return result;
            case GRE:
                result = (this.leftExpression.invoke(args)).gre(this.rightExpression.invoke(args));
                return result;
            case LES:
                result = (this.leftExpression.invoke(args)).les(this.rightExpression.invoke(args));
                return result;
            case GREEQU:
                result = (this.leftExpression.invoke(args)).greequ(this.rightExpression.invoke(args));
                return result;
            case LESEQU:
                result = (this.leftExpression.invoke(args)).lesequ(this.rightExpression.invoke(args));
                return result;
            case NOT:
                result = !this.rightcExpression.invoke(args);
                return result;
            case AND:
                result = ((Boolean) this.leftcExpression.invoke(args) && this.rightcExpression.invoke(args));
                return result;
            case OR:
                result = ((Boolean) this.leftcExpression.invoke(args) || this.rightcExpression.invoke(args));
                return result;
        }
        throw new RuntimeException(this.expression);
    }

    private void parse(String expression) {
        expression = StringUtil.removeOuterBracket(expression);
        char[] chs = expression.toCharArray();
        int operatorIndex = -1;
        int operatorGrade = -1;
        int bracket = 0;
        for (int i = 0; i < chs.length; i++) {
            switch (chs[i]) {
                case '(':
                    bracket++;
                    break;
                case ')':
                    bracket--;
                    break;
                default:
                    if (bracket == 0)
                        for (int j = 0; j < SUCCESSOPERATOR.length; j++) {
                            for (int k = 0; k < (SUCCESSOPERATOR[j]).length; k++) {
                                boolean bool = true;
                                for (int l = 0; l < SUCCESSOPERATOR[j][k].getOperator().length(); l++) {
                                    if (chs[i + l] != SUCCESSOPERATOR[j][k].getOperator().charAt(l)) {
                                        bool = false;
                                        break;
                                    }
                                }
                                if (bool && j >= operatorGrade) {
                                    operatorIndex = i;
                                    operatorGrade = j;
                                    this.operator = SUCCESSOPERATOR[j][k];
                                }
                            }
                        }
                    break;
            }
        }
        if (operatorIndex == -1)
            throw new RuntimeException(expression);
        boolean equal = (expression.charAt(operatorIndex + 1) == '=');
        if (operatorGrade == 0) {
            try {
                this.leftExpression = new NumericalExpression(expression.substring(0, operatorIndex));
            } catch (RuntimeException e1) {
                try {
                    this.leftExpression = new FunctionExpression(expression.substring(0, operatorIndex));
                } catch (RuntimeException e2) {
                    this.leftExpression = new NumberExpression(expression.substring(0, operatorIndex),0);
                }
            }
            try {
                this
                        .rightExpression = new NumericalExpression(expression.substring(operatorIndex + (equal ? 2 : 1), expression.length()));
            } catch (RuntimeException e1) {
                try {
                    this
                            .rightExpression = new FunctionExpression(expression.substring(operatorIndex + (equal ? 2 : 1), expression.length()));
                } catch (RuntimeException e2) {
                    this
                            .rightExpression = new NumberExpression(expression.substring(operatorIndex + (equal ? 2 : 1), expression.length()),0);
                }
            }
        } else {
            this.leftcExpression = (operatorIndex == 0) ? null : new ConditionExpression(expression.substring(0, operatorIndex));
            this.rightcExpression = new ConditionExpression(expression.substring(operatorIndex + (equal ? 2 : 1), expression.length()));
        }
    }
}
