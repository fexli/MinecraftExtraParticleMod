package com.fexli.particle.dumped.futil;


public class Matrix extends Number {
    private static final long serialVersionUID = 7729902854802325412L;

    public static Matrix E3 = new Matrix();

    public static Matrix N0 = new Matrix(0.0D);

    private double[][] data;

    private int rows;

    private int cols;

    public Matrix() {
        this((String)null);
    }

    public Matrix(String str) {
        if (str == null || str.length() == 0 || str.equals("E3")) {
            this.data = new double[][] { { 1.0D, 0.0D, 0.0D }, { 0.0D, 1.0D, 0.0D }, { 0.0D, 0.0D, 1.0D } };
            this.rows = 3;
            this.cols = 3;
            return;
        }
        if (str.equals("N0")) {
            this.rows = 1;
            this.cols = 1;
            this.data = new double[1][1];
            this.data[0][0] = 0.0D;
            return;
        }
        String[] rowstr = str.split(",,");
        this.rows = rowstr.length;
        this.data = new double[this.rows][];
        this.cols = -1;
        for (int i = 0; i < this.rows; i++) {
            String[] colstr = rowstr[i].split(",");
            if (this.cols == -1) {
                this.cols = colstr.length;
            } else if (this.cols != colstr.length) {
                throw new RuntimeException("Matrix Create Error:" + str);
            }
            this.data[i] = new double[this.cols];
            for (int j = 0; j < this.cols; j++)
                this.data[i][j] = Double.parseDouble(colstr[j]);
        }
    }

    public Matrix(double[][] matrix) {
        this.rows = matrix.length;
        this.cols = -1;
        for (int i = 0; i < this.rows; i++) {
            if (this.cols == -1) {
                this.cols = (matrix[i]).length;
            } else if (this.cols != (matrix[i]).length) {
                throw new RuntimeException("Matrix Create Error");
            }
        }
        this.data = matrix;
    }

    public Matrix(double number) {
        this.rows = 1;
        this.cols = 1;
        this.data = new double[1][1];
        this.data[0][0] = number;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public double get(int row, int col) {
        return this.data[row][col];
    }

    public Matrix getMatrix(int row, int col) {
        return new Matrix(this.data[row][col]);
    }

    public boolean isNumber() {
        return (this.rows == 1 && this.cols == 1);
    }

    public boolean isVector() {
        return (this.cols == 1);
    }

    public boolean isRowVector() {
        return (this.rows == 1);
    }

    public double getNumber() {
        if (!isNumber()) {
            throw new RuntimeException();
        }
        return this.data[0][0];
    }

    public double[] getVector() {
        if (!isVector()) {
            throw new RuntimeException();
        }
        double[] result = new double[this.rows];
        for (int i = 0; i < this.rows; i++)
            result[i] = this.data[i][0];
        return result;
    }

    public double[] getRowVector() {
        if (!isRowVector()) {
            throw new RuntimeException();
        }
        double[] result = new double[this.cols];
        for (int i = 0; i < this.cols; i++)
            result[i] = this.data[0][i];
        return result;
    }

    public double[] transform(double... pos) {
        if (!canTransform(pos)) {
            throw new RuntimeException();
        }
        double[] result = new double[this.rows];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++)
                result[i] = result[i] + pos[j] * this.data[i][j];
        }
        return result;
    }

    public Matrix transform(Matrix matrix) {
        if (!canTransform(matrix)) {
            throw new RuntimeException();
        }
        if (matrix.isNumber()) {
            if (isNumber())
                return new Matrix(getNumber() * matrix.getNumber());
            double[][] arrayOfDouble = new double[this.rows][this.cols];
            for (int j = 0; j < this.rows; j++) {
                for (int k = 0; k < this.cols; k++)
                    arrayOfDouble[j][k] = this.data[j][k] * matrix.getNumber();
            }
            return new Matrix(arrayOfDouble);
        }
        double[][] result = new double[this.rows][matrix.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                for (int k = 0; k < this.cols; k++)
                    result[i][j] = result[i][j] + this.data[i][k] * matrix.data[k][j];
            }
        }
        return new Matrix(result);
    }

    public boolean canTransform(double... pos) {
        return (pos.length == this.cols);
    }

    public boolean canTransform(Matrix matrix) {
        return (this.cols == matrix.rows || matrix.isNumber());
    }

    public Matrix add(Matrix matrix) {
        if (!canOperation(matrix)) {
            throw new RuntimeException();
        }
        if (matrix.isNumber()) {
            if (isNumber())
                return new Matrix(getNumber() + matrix.getNumber());
            double[][] arrayOfDouble = new double[this.rows][this.cols];
            for (int j = 0; j < this.rows; j++) {
                for (int k = 0; k < this.cols; k++)
                    arrayOfDouble[j][k] = this.data[j][k] + matrix.getNumber();
            }
            return new Matrix(arrayOfDouble);
        }
        double[][] result = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++)
                result[i][j] = this.data[i][j] + matrix.data[i][j];
        }
        return new Matrix(result);
    }

    public Matrix sub(Matrix matrix) {
        if (!canOperation(matrix)) {
            throw new RuntimeException();
        }
        if (matrix.isNumber()) {
            if (isNumber())
                return new Matrix(getNumber() - matrix.getNumber());
            double[][] arrayOfDouble = new double[this.rows][this.cols];
            for (int j = 0; j < this.rows; j++) {
                for (int k = 0; k < this.cols; k++)
                    arrayOfDouble[j][k] = this.data[j][k] - matrix.getNumber();
            }
            return new Matrix(arrayOfDouble);
        }
        double[][] result = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++)
                result[i][j] = this.data[i][j] - matrix.data[i][j];
        }
        return new Matrix(result);
    }

    public Matrix neg() {
        if (isNumber())
            return new Matrix(-getNumber());
        double[][] result = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++)
                result[i][j] = -this.data[i][j];
        }
        return new Matrix(result);
    }

    public boolean canOperation(Matrix matrix) {
        return ((this.rows == matrix.rows && this.cols == matrix.cols) || matrix.isNumber());
    }

    public Matrix getConfactor(int h, int v) {
        double[][] result = new double[this.rows - 1][this.cols - 1];
        for (int i = 0; i < this.rows - 1; i++) {
            if (i < h - 1) {
                for (int j = 0; j < this.cols - 1; j++) {
                    if (j < v - 1) {
                        result[i][j] = this.data[i][j];
                    } else {
                        result[i][j] = this.data[i][j + 1];
                    }
                }
            } else {
                for (int j = 0; j < (result[i]).length; j++) {
                    if (j < v - 1) {
                        result[i][j] = this.data[i + 1][j];
                    } else {
                        result[i][j] = this.data[i + 1][j + 1];
                    }
                }
            }
        }
        return new Matrix(result);
    }

    public double getMatrixResult() {
        if (isNumber())
            return getNumber();
        return getMatrixResult(this);
    }

    private double getMatrixResult(Matrix matrix) {
        if (matrix.rows == 2 && matrix.cols == 2)
            return matrix.data[0][0] * matrix.data[1][1] - matrix.data[0][1] * matrix.data[1][0];
        double result = 0.0D;
        double[] nums = new double[matrix.rows];
        int i;
        for (i = 0; i < matrix.rows; i++) {
            if (i % 2 == 0) {
                nums[i] = matrix.data[0][i] * getMatrixResult(getConfactor(1, i + 1));
            } else {
                nums[i] = -matrix.data[0][i] * getMatrixResult(getConfactor(1, i + 1));
            }
        }
        for (i = 0; i < matrix.rows; i++)
            result += nums[i];
        return result;
    }

    public Matrix getReverseMartrix() {
        if (!canReverse()) {
            throw new RuntimeException();
        }
        if (isNumber())
            return new Matrix(1.0D / getNumber());
        double[][] result = new double[this.rows][this.cols];
        double abs = getMatrixResult(this);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if ((i + j) % 2 == 0) {
                    result[i][j] = getMatrixResult(getConfactor(i + 1, j + 1)) / abs;
                } else {
                    result[i][j] = -getMatrixResult(getConfactor(i + 1, j + 1)) / abs;
                }
            }
        }
        result = trans(result);
        return new Matrix(result);
    }

    public boolean canReverse() {
        return (this.rows == this.cols);
    }

    private double[][] trans(double[][] data) {
        double[][] result = new double[(data[0]).length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < (data[0]).length; j++)
                result[j][i] = data[i][j];
        }
        return result;
    }

    public Matrix pow(int n) {
        Matrix result = this;
        for (int i = 0; i < n; i++)
            result = result.transform(this);
        return result;
    }

    public Matrix pow(double n) {
        return new Matrix(Math.pow(getNumber(), n));
    }

    public Matrix pow(Matrix n) {
        return pow(n.getNumber());
    }

    public boolean gre(Matrix matrix) {
        if (isNumber() && matrix.isNumber())
            return (getNumber() > matrix.getNumber());
        return false;
    }

    public boolean les(Matrix matrix) {
        if (isNumber() && matrix.isNumber())
            return (getNumber() < matrix.getNumber());
        return false;
    }

    public boolean greequ(Matrix matrix) {
        if (isNumber() && matrix.isNumber())
            return (getNumber() >= matrix.getNumber());
        return false;
    }

    public boolean lesequ(Matrix matrix) {
        if (isNumber() && matrix.isNumber())
            return (getNumber() <= matrix.getNumber());
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                sb.append(this.data[i][j]);
                if (j != this.cols - 1)
                    sb.append(",");
            }
            if (i != this.rows - 1)
                sb.append(",,");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            Matrix matrix = (Matrix)obj;
            if (isNumber() && matrix.isNumber())
                return (getNumber() == matrix.getNumber());
            if (canOperation(matrix)) {
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        if (this.data[i][j] != matrix.data[i][j])
                            return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public double doubleValue() {
        if (isNumber())
            return getNumber();
        return 0.0D;
    }

    public float floatValue() {
        if (isNumber())
            return (float)getNumber();
        return 0.0F;
    }

    public int intValue() {
        if (isNumber())
            return (int)getNumber();
        return 0;
    }

    public long longValue() {
        if (isNumber())
            return (long)getNumber();
        return 0L;
    }
}
