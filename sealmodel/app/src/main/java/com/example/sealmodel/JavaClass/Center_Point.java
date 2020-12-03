package com.example.sealmodel.JavaClass;

import android.util.Log;

import java.util.ArrayList;

public class Center_Point {

    private ArrayList<Float> Point= new ArrayList();
    private ArrayList<Float> FirstPoint= new ArrayList();
    private ArrayList<Float> FinalCenterPoint= new ArrayList();
    private ArrayList<Double> Distance= new ArrayList();

    public ArrayList<Float> GetCenterPoint(ArrayList<Float> point){
    Clear();
    Point=point;
        Log.d("Point:", "Pointx:"+Point.get(0)+" y:"+Point.get(1));
        Log.d("Point:", "Pointx:"+Point.get(2)+" y:"+Point.get(3));
        Log.d("Point:", "Pointx:"+Point.get(4)+" y:"+Point.get(5));
        Log.d("Point:", "Pointx:"+Point.get(6)+" y:"+Point.get(7));
        Log.d("Point:", "Pointx:"+Point.get(8)+" y:"+Point.get(9));
        Log.d("Point:", "Pointx:"+Point.get(10)+" y:"+Point.get(11));
        Log.d("Point:", "Pointx:"+Point.get(12)+" y:"+Point.get(13));
        Log.d("Point:", "Pointx:"+Point.get(14)+" y:"+Point.get(15));

    CompareCenter();
    FirstPoint();
        Log.d("FirstPoint:", "FirstPointx:"+FirstPoint.get(0)+" y:"+FirstPoint.get(1));
    Certification();
    return FinalCenterPoint;
    }

    private void CompareCenter(){
        //找出距離最大者並將該中點存進FinalCenterPoint中
        Double Max=0.0;
        for(int i=0;i<8;i=i+2){
            for(int j=i+2;j<10;j=j+2){
                Double Distance=CountDistance(Point.get(i),Point.get(i+1),Point.get(j),Point.get(j+1));
                if(Distance>Max){
                    Max=Distance;
                    FinalCenterPoint.set(0,(Point.get(i)+Point.get(j))/2);
                    FinalCenterPoint.set(1,(Point.get(i+1)+Point.get(j+1))/2);
                }
            }
        }
    }


    private void FirstPoint(){
        //辨別第一點
        Double Min=99999.0;
        for(int i=0;i<10;i=i+2){
                Double DistanceFirst=CountDistance(FinalCenterPoint.get(0),FinalCenterPoint.get(1),Point.get(i),Point.get(i+1));
                if(DistanceFirst<Min){
                    Min=DistanceFirst;
                    FirstPoint.set(0,Point.get(i));
                    FirstPoint.set(1,Point.get(i+1));
                }
            }
        }

    private void Certification(){
        Distance.add(CountDistance(FirstPoint.get(0),FirstPoint.get(1),Point.get(10),Point.get(11)));
        Distance.add(CountDistance(FirstPoint.get(0),FirstPoint.get(1),Point.get(12),Point.get(13)));
        Distance.add(CountDistance(FirstPoint.get(0),FirstPoint.get(1),Point.get(14),Point.get(15)));

        Sort();
    }
private void Sort(){

        for(int i=0;i<Distance.size()-1;i++) {
            for (int j = 0; j < Distance.size()-1; j++) {
                if (Distance.get(j) > Distance.get(j + 1)) {
                    Double temp = Distance.get(j);
                    Distance.set(j, Distance.get(j + 1));
                    Distance.set(j + 1, temp);

                }

            }
        }
    Log.d("Distance:", "Distance1:"+Distance.get(0));
    Log.d("Distance:", "Distance2:"+Distance.get(1));
    Log.d("Distance:", "Distance3:"+Distance.get(2));


}
    public ArrayList<Double> GetDistance(){
        return Distance;

    }

    private Double CountDistance(float x1,float y1,float x2,float y2){
    //計算兩點的距離
       Double Result=Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        //Log.d("Distance12:", "Distance"+Distance.get(0));
        return Result;
    }


    private void Clear(){
        //初始化變數
        Point.clear();
        FirstPoint.clear();
        FirstPoint.add((float)0);
        FirstPoint.add((float)0);
        FinalCenterPoint.clear();
        FinalCenterPoint.add((float)0);
        FinalCenterPoint.add((float)0);
        Distance.clear();
    }


}
