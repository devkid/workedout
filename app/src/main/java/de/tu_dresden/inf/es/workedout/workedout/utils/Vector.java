package de.tu_dresden.inf.es.workedout.workedout.utils;


public class Vector extends Object {
    public double[] mValues;

    public Vector() {
        mValues = new double[3];
    }

    public Vector(double x, double y, double z) {
        mValues = new double[3];
        mValues[0] = x;
        mValues[1] = y;
        mValues[2] = z;
    }

    public Vector(Vector v) {
        mValues = new double[3];
        System.arraycopy(v.mValues, 0, mValues, 0, 3);
    }

    /*
        Arithmetic operations
     */

    public Vector added(Vector v) {
        return new Vector(
                mValues[0] + v.mValues[0],
                mValues[1] + v.mValues[1],
                mValues[2] + v.mValues[2]);
    }
    public Vector added(double a) { return added(new Vector(a, a, a)); }

    public void add(Vector v) {
        mValues[0] += v.mValues[0];
        mValues[1] += v.mValues[1];
        mValues[2] += v.mValues[2];
    }
    public void add(double a) { add(new Vector(a, a, a)); }

    public Vector subtracted(Vector v) {
        return new Vector(
                mValues[0] - v.mValues[0],
                mValues[1] - v.mValues[1],
                mValues[2] - v.mValues[2]);
    }
    public Vector subtracted(double a) { return subtracted(new Vector(a, a, a)); }

    public void subtract(Vector v) {
        mValues[0] -= v.mValues[0];
        mValues[1] -= v.mValues[1];
        mValues[2] -= v.mValues[2];
    }
    public void subtract(double a) { subtract(new Vector(a, a, a)); }

    public Vector multiplied(Vector v) {
        return new Vector(
                mValues[0] * v.mValues[0],
                mValues[1] * v.mValues[1],
                mValues[2] * v.mValues[2]);
    }
    public Vector multiplied(double a) { return multiplied(new Vector(a, a, a)); }

    public void multiply(Vector v) {
        mValues[0] *= v.mValues[0];
        mValues[1] *= v.mValues[1];
        mValues[2] *= v.mValues[2];
    }
    public void multiply(double a) { multiply(new Vector(a, a, a)); }

    public Vector divided(Vector v) {
        return new Vector(
                mValues[0] / v.mValues[0],
                mValues[1] / v.mValues[1],
                mValues[2] / v.mValues[2]);
    }
    public Vector divided(double a) { return divided(new Vector(a, a, a)); }

    public void divide(Vector v) {
        mValues[0] /= v.mValues[0];
        mValues[1] /= v.mValues[1];
        mValues[2] /= v.mValues[2];
    }
    public void divide(double a) { divide(new Vector(a, a, a)); }


    /*

     */

    public double length() {
        return Math.sqrt(
                Math.pow(mValues[0], 2) +
                        Math.pow(mValues[1], 2) +
                        Math.pow(mValues[2], 2)
        );
    }

    public Vector normalized() {
        double length = length();
        return new Vector(
                mValues[0] / length,
                mValues[1] / length,
                mValues[2] / length);
    }

    public void normalize() {
        double length = length();
        mValues[0] /= length;
        mValues[1] /= length;
        mValues[2] /= length;
    }

    public String toString() {
        return toString(", ");
    }
    public String toString(String delim) {
        return String.format("%+07.3f%s%+07.3f%s%+07.3f", mValues[0], delim, mValues[1], delim, mValues[2]);
    }

    public double scalarProduct(Vector v) {
        return mValues[0] * v.mValues[0] + mValues[1] * v.mValues[1] + mValues[2] * v.mValues[2];
    }
}
