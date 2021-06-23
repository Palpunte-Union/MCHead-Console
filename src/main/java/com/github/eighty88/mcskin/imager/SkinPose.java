package com.github.eighty88.mcskin.imager;

public class SkinPose {

    private double[][][] values;

    public static final int HEAD = 0;

    public static final int LOCATION = 0;
    public static final int ROTATION = 1;

    public SkinPose() {
        values = new double[6][2][3];
    }

    public SkinPose(double[][][] values) {
        this();
        this.values = values.clone();
    }

    public double[][][] getValues() {
        return values.clone();
    }


    public static SkinPose standing() {
        double[][] headValues = 	{{0, 0, 2.5}, {0, 0, 0}};
        double[][] chestValues = 	{{0, 0, 0}, {0, 0, 0}};
        double[][] leftArmValues = 	{{1.5, 0, 0}, {0, 0, 0}};
        double[][] rightArmValues = {{-1.5, 0, 0}, {0, 0, 0}};
        double[][] leftLegValues = 	{{-0.5, 0, -3}, {0, 0, 0}};
        double[][] rightLegValues = {{0.5, 0, -3}, {0, 0, 0}};

        double[][][] poseValues = {headValues, chestValues, leftArmValues, rightArmValues, leftLegValues, rightLegValues};

        return new SkinPose(poseValues);
    }

}

