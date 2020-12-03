package com.example.sealmodel.JavaClass;

import android.util.Log;

import java.util.ArrayList;

public class Certification {


    public boolean Certification(ArrayList<Double> gotDistance,ArrayList<Double> checkDistance){


        Double check1=Math.abs( gotDistance.get(0)-checkDistance.get(0));
        Double check2=Math.abs(gotDistance.get(1)-checkDistance.get(1));
        Double check3=Math.abs(gotDistance.get(2)-checkDistance.get(2));

        Log.d("logcat1:",Double.toString(check1));
        Log.d("logcat2:",Double.toString(check2));
        Log.d("logcat3:",Double.toString(check3));
        if(check1<=30&&check2<=30&&check3<=30){

            return true;

        }
        else
            return false;







    }



}
