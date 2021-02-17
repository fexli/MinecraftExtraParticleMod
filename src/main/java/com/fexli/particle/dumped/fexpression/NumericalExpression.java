package com.fexli.particle.dumped.fexpression;
//package com.isgk.colorblock.util.expression;

import com.fexli.particle.utils.ChatUtil;
import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.dumped.futil.StringUtil;
import java.util.Map;

public class NumericalExpression implements IExpression<Matrix, Matrix> {
    private static final IExpression.Operator[][] SUCCESSOPERATOR = new IExpression.Operator[][] { { IExpression.Operator.POW }, { IExpression.Operator.MUL, IExpression.Operator.DIV }, { IExpression.Operator.ADD, IExpression.Operator.SUB }, { IExpression.Operator.ASS }, { IExpression.Operator.SPL, IExpression.Operator.SPLLIN } };

    private IExpression<Matrix, Matrix> leftExpression = null;

    private IExpression<Matrix, Matrix> rightExpression = null;

    private IExpression.Operator operator = null;

    private String expression;

    public NumericalExpression(String expression) {
        expression.replaceAll(" ", "");
        this.expression = expression;
        parse(expression);
        if (this.operator == null)
            throw new RuntimeException(expression);
    }

    public Matrix invoke(Map<String, Matrix> args) {
        Matrix result = Matrix.N0;
        if (this.operator == null) {
            ChatUtil.addChatMessage("Operator Error:" + this.expression);
            throw new RuntimeException(this.expression);
        }
        switch (this.operator) {
            case ADD:
                result = ((Matrix)this.leftExpression.invoke(args)).add(this.rightExpression.invoke(args));
                return result;
            case SUB:
                result = ((Matrix)this.leftExpression.invoke(args)).sub(this.rightExpression.invoke(args));
                return result;
            case NEG:
                result = ((Matrix)this.rightExpression.invoke(args)).neg();
                return result;
            case MUL:
                result = ((Matrix)this.leftExpression.invoke(args)).transform(this.rightExpression.invoke(args));
                return result;
            case DIV:
                result = ((Matrix)this.leftExpression.invoke(args)).transform(((Matrix)this.rightExpression.invoke(args)).getReverseMartrix());
                return result;
            case POW:
                result = ((Matrix)this.leftExpression.invoke(args)).pow(this.rightExpression.invoke(args));
                return result;
            case ASS:
                if (this.leftExpression instanceof NumberExpression)
                    return ((NumberExpression)this.leftExpression).setKeyValue(args, this.rightExpression.invoke(args));
                ChatUtil.addChatMessage("Assignment Error:" + this.expression);
                throw new RuntimeException(this.expression);
//            case APPEND:
//                if (this.leftExpression instanceof NumberExpression){
//                    Matrix obj = args.get(this.expression.split("=")[0]);
//                    if (obj != null)
//                        return ((NumberExpression) this.leftExpression).setKeyValue(args, this.rightExpression.invoke(args).add(obj));
//                    ChatUtil.addChatMessage("Assignment Error:" + this.expression);
//                    throw new RuntimeException(this.expression);
//                }

        }
        ChatUtil.addChatMessage("Operator Error:" + this.expression);
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
                                boolean equal = true;
                                for (int l = 0; l < SUCCESSOPERATOR[j][k].getOperator().length(); l++) {
                                    if (chs[i + l] != SUCCESSOPERATOR[j][k].getOperator().charAt(l)) {
                                        equal = false;
                                        break;
                                    }
                                }
                                if (equal && j >= operatorGrade) {
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
        String subStrings = expression.substring(operatorIndex + 1);
        if (operatorIndex == 0 && this.operator == IExpression.Operator.SUB) {
            this.operator = IExpression.Operator.NEG;
            try {
                this.rightExpression = new NumericalExpression(subStrings);
            } catch (RuntimeException e1) {
                try {
                    this.rightExpression = new FunctionExpression(subStrings);
                } catch (RuntimeException e2) {
                    this.rightExpression = new NumberExpression(subStrings,0);
                    if (((NumberExpression) this.rightExpression).getRows() != 1)
                        throw new RuntimeException(expression);
                }
            }
        } else {
            if (this.operator == IExpression.Operator.SPL || this.operator == IExpression.Operator.SPLLIN)
                throw new RuntimeException(expression);
            if (this.operator == IExpression.Operator.SUB && expression.charAt(operatorIndex - 1) == ',')
                throw new RuntimeException(expression);
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
                this.rightExpression = new NumericalExpression(subStrings);
            } catch (RuntimeException e1) {
                try {
                    this.rightExpression = new FunctionExpression(subStrings);
                } catch (RuntimeException e2) {
                    this.rightExpression = new NumberExpression(subStrings,0);
                }
            }
        }
    }
}
