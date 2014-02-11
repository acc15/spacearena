package ru.spacearena.game;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class Matrix {

    private float[] values;
    private int cols;

    public Matrix(float[][] arr) {
        this.values = new float[arr.length * arr[0].length];
        this.cols = arr[0].length;
        for (int i=0; i<arr.length; i++) {
            final float[] row = arr[i];
            System.arraycopy(row, 0, values, i * row.length, row.length);
        }
    }

    public Matrix(int rows, int cols) {
        this.values = new float[rows*cols];
        this.cols = cols;
    }

    public Matrix(int cols, float[] values) {
        this.values = values;
        this.cols = cols;
    }

    public int getRows() {
        return values.length / cols;
    }

    public int getCols() {
        return cols;
    }

    private int getCellIndex(int row, int col) {
        if (row < 0 || row >= getRows()) {
            throw new IllegalArgumentException("Row is out of bounds: " + row + " of " + getRows());
        }
        if (col < 0 || col >= getCols()) {
            throw new IllegalArgumentException("Col is out of bounds: " + col + " of " + getCols());
        }
        return row * cols + col;
    }

    public float getValue(int row, int col) {
        return values[getCellIndex(row, col)];
    }

    public void setValue(int row, int col, float value) {
        values[getCellIndex(row,col)] = value;
    }

    public String toPrintableString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<values.length; i++) {
            if (i!=0 && i%cols==0) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(String.format("%8.2f", values[i]));
        }
        return stringBuilder.toString();
    }

    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<getRows(); i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append('{');
            for (int j=0; j<getCols(); j++) {
                if (j > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(String.format("%.2f", getValue(i, j)));
            }
            stringBuilder.append('}');
        }
        return stringBuilder.toString();
    }

    public Matrix mul(Matrix mx) {

        if (getCols() != mx.getRows()) {
            throw new IllegalArgumentException("Cols should be equal to rows: " + getCols() + "x" + mx.getRows());
        }

        final Matrix result = new Matrix(getRows(), mx.getCols());
        for (int row = 0; row < result.getRows(); row++) {
            for (int col = 0; col < result.getCols(); col++) {

                float sum = 0;
                for (int v = 0; v < cols; v++) {
                    final float product = getValue(row, v) * mx.getValue(v, col);
                    sum += product;
                }

                result.setValue(row, col, sum);
            }
        }
        return result;
    }

    public static Matrix identity() {
        return new Matrix(new float[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1}
        });
    }

    public static Matrix translate(float dx, float dy) {
        return new Matrix(new float[][] {
                {1,0,dx},
                {0,1,dy},
                {0,0,1}
        });
    }

    public static Matrix rotate(float angle) {
        return new Matrix(new float[][] {
                {(float)Math.cos(angle),(float)-Math.sin(angle),0},
                {(float)Math.sin(angle), (float)Math.cos(angle),0},
                {0,0,1},
        });
    }

    public static Matrix scale(float sx, float sy) {
        return new Matrix(new float[][] {
                {sx, 0, 0},
                {0, sy, 0},
                {0,0,1},
        });
    }
}
