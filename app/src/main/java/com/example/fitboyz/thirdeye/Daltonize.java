package com.example.fitboyz.thirdeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Daltonize {


    static int[][][] precomputeProtanopia;
//    int[][][] precomputeDeutranopia =  new int[256][256][256];
//    int[][][] precomputeTritanopia=  new int[256][256][256];

    double [][] convertRGB = {{0.08094444, -0.1305044, 0.116721066}, {-0.010248533514, 0.05401932663599884, -0.11361470821404349}, {-0.0003652969378610491, -0.004121614685876285, 0.6935114048608589}};
    double [][] convertLMS = {{17.8824, 43.5161, 4.11935}, {3.45565, 27.1554, 3.86714}, {0.0299566, 0.184309, 1.46709}};


    double [][] prot = {{0, 2.02344, -2.52581}, {0, 1, 0}, {0, 0, 1}};
    double [][] deut = {{1, 0, 0}, {0.494207, 0, 1.24827}, {0, 0, 1}};
    double [][] trit = {{1, 0, 0}, {0, 1, 0}, {-0.395913, 0.801109, 0}};

    double [][] correction = {{0, 0, 0}, {0.7, 1, 0}, {0.7, 0, 1}};

    private static Daltonize instance = null;

    public Daltonize(){
        /*initializePrecompute();*/
//        initializePrecompute();
    }

    public static Daltonize getInstance() {
        precomputeProtanopia = new int[256][256][256];
        if (instance == null) {
            return new Daltonize();
        }
        return instance;
    }

    public void setPrecompute(int[][][] precompute) {
        precomputeProtanopia = precompute;
    }



    public static double [][] matrixMultiply (double[][] matrix1, double[][] matrix2) {
        int col1 = matrix1[0].length;
        int row2 = matrix2.length;

        int row1 = matrix1.length;
        int col2 = matrix2[0].length;

        if (col1 != row2){
            return null;
        }

        double [][] completed = new double [row1][col2];

        for (int i = 0; i < row1; i++){
            for (int j = 0; j < col2; j++){
                for (int k = 0; k < col1; k++){
                    completed[i][j] = completed[i][j] + matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return completed;
    }


    public int[][][] initializePrecompute() {


//        precomputeDeutranopia = new int[256][256][256];
//        precomputeTritanopia = new int[256][256][256];
//        precomputeProtanopia = new int[256][256][256];


        for (int i = 0; i < 256; i++){
            for (int j = 0; j < 256; j++){
                for (int k = 0; k < 256; k++){
                    precomputeProtanopia[i][j][k] = daltonizeColor((double) i, (double) j, (double) k, 1);
//                    precomputeDeutranopia[i][j][k] = daltonizeColor((double) i, (double) j, (double) k, 1);
//                    precomputeTritanopia[i][j][k] = daltonizeColor((double) i, (double) j, (double) k, 2);

                }
            }
        }

        return precomputeProtanopia;
    }

    public Bitmap daltonizeImage(Bitmap bmp, int option) {

        bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Bitmap toReturn = bmp;

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                int pixel = bmp.getPixel(i,j);
                int color = 0;
//                if (option == 0){
//                    color = precomputeProtanopia[Color.red(pixel)][Color.green(pixel)][Color.blue(pixel)];
                color = daltonizeColor(Color.red(pixel), Color.green(pixel), Color.blue(pixel), 1);

//                } else if (option == 1){
//                    color = precomputeDeutranopia[Color.red(pixel)][Color.green(pixel)][Color.blue(pixel)];
//                } else if (option == 2) {
//                    color = precomputeTritanopia[Color.red(pixel)][Color.green(pixel)][Color.blue(pixel)];
//                }
                toReturn.setPixel(i, j, color);
            }
        }
            return toReturn;
    }

    public int daltonizeColor(double red, double green, double blue, int option) {

        double [][] current = new double[3][1];
        current[0][0] = red;
        current[1][0] = green;
        current[2][0] = blue;

        current = matrixMultiply(convertLMS, current);

//        if (option == 1){
            current = matrixMultiply(prot, current);
//        } else if (option == 2){
//            current = matrixMultiply(deut, current);
//        } else if (option == 3){
//            current = matrixMultiply(trit, current);
//        }

        current = matrixMultiply(convertRGB,current);

        for (int k = 0; k < 3; k++){
            if (current[k][0] > 252){
                current[k][0] = 252;
            }
            if (current[k][0] < 0){
                current[k][0] = 0;
            }
        }

        current[0][0] = red - current[0][0];
        current[1][0] = green - current[1][0];
        current[2][0] = blue - current[2][0];

        current = matrixMultiply(correction, current);

        current[0][0] = red + current[0][0];
        current[1][0] = green + current[1][0];
        current[2][0] = blue + current[2][0];

        for (int k = 0; k < 3; k++){
            if (current[k][0] > 252){
                current[k][0] = 252;
            }
            if (current[k][0] < 0){
                current[k][0] = 0;
            }
        }

        return Color.rgb((int) current[0][0], (int) current[1][0], (int) current[2][0]);

    }

    public static Bitmap bitmapFromArray(int[][] pixels2d){
        int width = pixels2d.length;
        int height = pixels2d[0].length;
        int[] pixels = new int[width * height];
        int pixelsIndex = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixels[pixelsIndex] = pixels2d[i][j];
                pixelsIndex ++;
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

}
