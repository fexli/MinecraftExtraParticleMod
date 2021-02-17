package com.fexli.particle.dumped.fexpression;
//package com.isgk.colorblock.util.expression;

import com.google.common.collect.Lists;
import com.fexli.particle.utils.ChatUtil;
import com.fexli.particle.dumped.futil.Matrix;
import com.fexli.particle.dumped.futil.StringUtil;
import java.util.List;
import java.util.Map;

public class NumberExpression implements IExpression<Matrix, Matrix> {
    private String expression;

    private String[][] var;

    private IExpression<Matrix, Matrix>[][] expressionMatrix;

    private boolean[][] isVar;

    private int rows;

    private int cols;

    private Matrix value;

    private boolean isFinal;

    public NumberExpression(String key,int inner) {
        int innerRound;
        if (inner >= 32) {
            throw new RuntimeException("innerRound Loop Detected:"+key);
        } else{
            innerRound = inner;
        }
        key = StringUtil.removeOuterBracket(key.replaceAll("\\s+", ""));
        this.expression = key;
        try {
            if (key.equals("")) {
                this.value = new Matrix(0.0D);
            } else {
                try {
                    this.value = new Matrix(Double.parseDouble(key));
                } catch (RuntimeException e) {
                    this.value = new Matrix(key);
                }
            }
            this.rows = this.value.getRows();
            this.cols = this.value.getCols();
            this.isFinal = true;
        } catch (RuntimeException e) {
            String[] rowss = key.split(",,");
            this.rows = rowss.length;
            this.cols = -1;
            this.var = new String[this.rows][];
            this.expressionMatrix = (IExpression<Matrix, Matrix>[][])new IExpression[this.rows][];
            this.isVar = new boolean[this.rows][];
            for (int i = 0; i < this.rows; i++) {
                String[] colss = split(rowss[i]);
                if (this.cols == -1) {
                    this.cols = colss.length;
                } else if (this.cols != colss.length) {
                    throw new RuntimeException("NumberExpression Create Error:" + key);
                }
                this.var[i] = new String[this.cols];
                this.expressionMatrix[i] = (IExpression<Matrix, Matrix>[])new IExpression[this.cols];
                this.isVar[i] = new boolean[this.cols];
                for (int j = 0; j < this.cols; j++) {
                    if (tryParse(colss[j])) {
                        this.isVar[i][j] = false;
                        try {
                            this.expressionMatrix[i][j] = new NumericalExpression(colss[j]);
                        } catch (RuntimeException e1) {
                            try {
                                this.expressionMatrix[i][j] = new FunctionExpression(colss[j]);
                            } catch (RuntimeException e2) {
                                this.expressionMatrix[i][j] = new NumberExpression(colss[j], innerRound +1);
                            }
                        }
                    } else {
                        this.isVar[i][j] = true;
                    }
                    this.var[i][j] = colss[j];
                }
            }
            this.isFinal = false;
        }
    }

    private String[] split(String key) {
        List<String> result = Lists.newArrayList();
        int preIndex = 0;
        int bracket = 0;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (ch == '(') {
                bracket++;
            } else if (ch == ')') {
                bracket--;
            } else if (ch == ',' && bracket == 0) {
                result.add(key.substring(preIndex, i));
                preIndex = i + 1;
            }
        }
        result.add(key.substring(preIndex));
        return result.<String>toArray(new String[0]);
    }

    private boolean tryParse(String key) {
        for (IExpression.Operator operator : IExpression.Operator.values()) {
            if (key.contains(operator.getOperator()))
                return true;
        }
        if (key.indexOf('(') != -1 && key.indexOf(')') != -1)
            return true;
        try {
            Double.parseDouble(key);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public Matrix invoke(Map<String, Matrix> args) {
        double[][] result;
        if (this.isFinal)
            return this.value;
        if (this.rows == 1 && this.cols == 1) {
            if (this.isVar[0][0]) {
                if (args.containsKey(this.var[0][0]))
                    return args.get(this.var[0][0]);
                ChatUtil.addChatMessage("Var Undefine:" + this.expression);
                throw new RuntimeException(this.expression);
            }
            return this.expressionMatrix[0][0].invoke(args);
        }
        Matrix[][] matrixs = new Matrix[this.rows][this.cols];
        boolean allIsNumber = true;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.isVar[i][j]) {
                    if (args.containsKey(this.var[i][j])) {
                        matrixs[i][j] = args.get(this.var[i][j]);
                    } else {
                        ChatUtil.addChatMessage("Var Undefine:" + this.var[i][j]);
                        throw new RuntimeException(this.expression);
                    }
                } else {
                    matrixs[i][j] = this.expressionMatrix[i][j].invoke(args);
                }
                if (!matrixs[i][j].isNumber())
                    allIsNumber = false;
            }
        }
        if (allIsNumber) {
            result = new double[this.rows][this.cols];
            for (int j = 0; j < this.rows; j++) {
                for (int k = 0; k < this.cols; k++)
                    result[j][k] = matrixs[j][k].getNumber();
            }
        } else {
            int newCols = 0;
            int newRows = 0;
            for (int m = 0; m < this.rows; m++) {
                int sRows = matrixs[m][0].getRows();
                newRows += sRows;
                for (int n = 1; n < this.cols; n++) {
                    if (matrixs[m][n].getRows() != sRows) {
                        ChatUtil.addChatMessage("Var Type Error:" + this.var[m][n]);
                        throw new RuntimeException(this.expression);
                    }
                }
            }
            for (int j = 0; j < this.cols; j++) {
                int sCols = matrixs[0][j].getCols();
                newCols += sCols;
                for (int n = 1; n < this.rows; n++) {
                    if (matrixs[n][j].getCols() != sCols) {
                        ChatUtil.addChatMessage("Var Type Error:" + this.var[n][j]);
                        throw new RuntimeException(this.expression);
                    }
                }
            }
            result = new double[newRows][newCols];
            for (int k = 0; k < this.rows; k++) {
                for (int n = 0; n < this.cols; n++) {
                    for (int i1 = 0; i1 < matrixs[k][n].getRows(); i1++) {
                        for (int l = 0; l < matrixs[k][n].getCols(); l++)
                            result[k + i1][n + l] = matrixs[k][n].get(i1, l);
                    }
                }
            }
        }
        return new Matrix(result);
    }

    public Matrix setKeyValue(Map<String, Matrix> args, Matrix matrix) {
        if (this.isFinal) {
            ChatUtil.addChatMessage("Not A Var:" + this.expression);
            throw new RuntimeException(this.expression);
        }
        if (this.rows == matrix.getRows() && this.cols == matrix.getCols()) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    if (this.isVar[i][j]) {
                        args.put(this.var[i][j], matrix.getMatrix(i, j));
                    } else {
                        ChatUtil.addChatMessage("Not A Var:" + this.var[i][j]);
                        throw new RuntimeException(this.expression);
                    }
                }
            }
            return matrix;
        }
        if (this.rows == 1 && this.cols == 1) {
            if (this.isVar[0][0]) {
                args.put(this.var[0][0], matrix);
            } else {
                ChatUtil.addChatMessage("Not A Var:" + this.var[0][0]);
                throw new RuntimeException(this.expression);
            }
            return matrix;
        }
        ChatUtil.addChatMessage("Var Type Error:" + this.expression);
        throw new RuntimeException(this.expression);
    }
}
