package com.example.sealmodel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sealmodel.JavaClass.Center_Point;
import com.example.sealmodel.JavaClass.Certification;
import com.example.sealmodel.SQL.SQLite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    Center_Point Center_Point = new Center_Point();
    private com.example.sealmodel.JavaClass.Certification Certification=new Certification();//辨認方法的class
    private ImageView screenImage;//讀取螢幕觸控
    private int screenWidth,screenHeight;
    private ArrayList<Double> Distance;//存放你當前距離
    private ArrayList<Integer> sealid = new ArrayList<Integer>();//儲存印章樣式
    private boolean stampCheck = true;//為了不重覆判定的互斥鎖
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME="MyTable";
    private final int DB_VERSION = 2;//sqltie的基本參數設定
    private ImageView BackGround;
    SQLite sqlStampCounter;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();//取得所有資料
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();//取得被選中的項目資料
    ImageView stamp1,stamp2,stamp3,stamp4,stamp5,stamp6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clear();
        //getSupportActionBar().hide();
        //設定隱藏狀態
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        BackGround = findViewById(R.id.background);
        getScreenSize();//獲取手機螢幕大小

        Picasso.with(this).load(R.drawable.background).resize(screenWidth,screenHeight).into(BackGround);//設定背景
        sealid.add(1);//設定樣式編號，因為只有一個，所以先設定一號

        sqlOrin();
        findStampId();
        makeSetPointCard();
        oriImage(Integer.parseInt(arrayList.get(0).get("stampCount")));//開啟程式時的初始化，包含資料庫跟imageview的Findid和已經蓋過的章設定

        setListeners();//抓取觸控判定
    }

    private void setListeners() {

        //呼叫一個新的class
        screenImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {

                ArrayList<Float> Point= new ArrayList();
                ArrayList<Float> CenterPoint = new ArrayList();
                int numPtrs = e.getPointerCount();
                for (int ptr = 0; ptr < numPtrs; ptr++) {
                    if(ptr==7 && stampCheck){
                        Point.add(e.getX(0));
                        Point.add(e.getY(0));
                        Point.add(e.getX(1));
                        Point.add(e.getY(1));
                        Point.add(e.getX(2));
                        Point.add(e.getY(2));
                        Point.add(e.getX(3));
                        Point.add(e.getY(3));
                        Point.add(e.getX(4));
                        Point.add(e.getY(4));
                        Point.add(e.getX(5));
                        Point.add(e.getY(5));
                        Point.add(e.getX(6));
                        Point.add(e.getY(6));
                        Point.add(e.getX(7));
                        Point.add(e.getY(7));

                        CenterPoint=Center_Point.GetCenterPoint(Point);
                        Distance=Center_Point.GetDistance();
                        int colum = 0;

                        for(;colum < sealid.size();colum += 1){
                            int dist = 0;
                            ArrayList<Double> alldistance = new ArrayList<>();
                            alldistance.clear();
                            alldistance.add(0.0);
                            alldistance.add(0.0);
                            alldistance.add(0.0);//自行輸入你的印章驗證距離
                            //if(Certification.Certification(Distance,alldistance)) { //比對答案，如果寫好想判定的距離即可使用
                                if(setImage(Integer.parseInt(arrayList.get(0).get("stampCount"))+1,"exampleseal.jpg")){
                                    useStamp("exampleseal.jpg");
                                    break;
                                }//stampstyle是儲存當前樣式，現在因為只有一種，所以直接寫定植
                            //}
                        }
                        CenterPoint.clear();
                        Point.clear();
                    }
                }
                return true;
            }
        });
    }

    private void getScreenSize(){//畫面大小辦定
        DisplayMetrics dm = new DisplayMetrics();
        //取得視窗屬性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //視窗的寬度
        screenWidth = dm.widthPixels;
        //視窗高度
        screenHeight = dm.heightPixels;
    }

    private void oriImage(int stampCount){//開程式時需要設定多少印章
        if(stampCount> 0){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp1);
            stamp1.setAlpha(1.0F);
        }
        if(stampCount > 1){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp2);
            stamp2.setAlpha(1.0F);
        }
        if(stampCount> 2){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp3);
           stamp3.setAlpha(1.0F);
        }
        if(stampCount> 3){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp4);
            stamp4.setAlpha(1.0F);
        }
        if(stampCount> 4){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp5);
            stamp5.setAlpha(1.0F);
        }
        if(stampCount> 5){
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp6);
            stamp6.setAlpha(1.0F);
        }
    }

    private boolean setImage(int stampCount,String stampStyle){//蓋章時設定的印章
        boolean ImageExist = false;
        if(stampCount==1){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp1);
            stamp1.setAlpha(1.0F);
        }
        else if(stampCount==2){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp2);
            stamp2.setAlpha(1.0F);
        }
        else if( stampCount==3){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp3);
            stamp3.setAlpha(1.0F);
        }
        else if( stampCount==4){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp4);
            stamp4.setAlpha(1.0F);
        }
        else if( stampCount==5){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp5);
            stamp5.setAlpha(1.0F);
        }
        else if( stampCount==6){
            ImageExist = true;
            Picasso.with(this).load(R.drawable.exampleseal).resize(screenWidth/2,screenHeight/3).into(stamp6);
            stamp6.setAlpha(1.0F);
        }
        return ImageExist;
    }

    private void findStampId(){
        stamp1=findViewById(R.id.stamp1);
        stamp2=findViewById(R.id.stamp2);
        stamp3=findViewById(R.id.stamp3);
        stamp4=findViewById(R.id.stamp4);
        stamp5=findViewById(R.id.stamp5);
        stamp6=findViewById(R.id.stamp6);
        stamp1.setPadding(5,25,5,5);
        stamp2.setPadding(5,25,5,5);
        stamp3.setPadding(5,25,5,5);
        stamp4.setPadding(5,25,5,5);
        stamp5.setPadding(5,25,5,5);
        stamp6.setPadding(5,25,5,5);
        screenImage=findViewById(R.id.screenImage);//這個不用設置圖片
    }

    private void sqlOrin(){//sqlite的初始設定
        sqlStampCounter = new SQLite(this, DB_NAME, null, DB_VERSION, TABLE_NAME);//初始化資料庫
        //sqlStampCounter.deleteAll();
        sqlStampCounter.chickTable();//確認是否存在資料表，沒有則新增
        arrayList = sqlStampCounter.showAll();
    }

    private void makeSetPointCard(){//沒資料時創第一筆資料，並且抓取所有資料
        if(arrayList.size()==0){
            sqlStampCounter.addData("0","0","0","0","0","0","0");
        }
        arrayList = sqlStampCounter.showAll();//撈取資料表內所有資料
    }

    public void useStamp(String stampStyle){//蓋章時，把印章資訊存進sqlite
        stampCheck=false;
        getNowArray = sqlStampCounter.searchById(arrayList.get(0).get("id"));
        int stampCount=Integer.parseInt(getNowArray.get(0).get("stampCount"))+1;
        if(stampCount==1){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    stampStyle,
                    "0",
                    "0",
                    "0",
                    "0",
                    "0");
        }
        else if(stampCount==2){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    getNowArray.get(0).get("stampId1"),
                    stampStyle,
                    "0",
                    "0",
                    "0",
                    "0");
        }
        else if(stampCount==3){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    getNowArray.get(0).get("stampId1"),
                    getNowArray.get(0).get("stampId2"),
                    stampStyle,
                    "0",
                    "0",
                    "0");
        }
        else if(stampCount==4){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    getNowArray.get(0).get("stampId1"),
                    getNowArray.get(0).get("stampId2"),
                    getNowArray.get(0).get("stampId3"),
                    stampStyle,
                    "0",
                    "0");
        }
        else if(stampCount==5){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    getNowArray.get(0).get("stampId1"),
                    getNowArray.get(0).get("stampId2"),
                    getNowArray.get(0).get("stampId3"),
                    getNowArray.get(0).get("stampId4"),
                    stampStyle,
                    "0");
        }
        else if(stampCount==6){
            sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),Integer.toString(stampCount),
                    getNowArray.get(0).get("stampId1"),
                    getNowArray.get(0).get("stampId2"),
                    getNowArray.get(0).get("stampId3"),
                    getNowArray.get(0).get("stampId4"),
                    getNowArray.get(0).get("stampId5"),
                    stampStyle);
        }
        arrayList = sqlStampCounter.showAll();
        getNowArray = sqlStampCounter.searchById(arrayList.get(0).get("id"));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("蓋章成功");
        alertDialog.setMessage("目前已有" + getNowArray.get(0).get("stampCount")+"個點數");
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stampCheck=true;
            }

        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        makeSetPointCard();
    }

    public void toZero(View V){//如果要兌換獎絣就出現提示，確定後消除sqlite的資料
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("兌換獎品");
        alertDialog.setMessage("是否確定兌換?\n(兌換後不可要求退貨)");
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getNowArray = sqlStampCounter.searchById(arrayList.get(0).get("id"));
                sqlStampCounter.modifyEZ(getNowArray.get(0).get("id"),"0","0","0","0","0","0","0");
                arrayList = sqlStampCounter.showAll();
                getNowArray = sqlStampCounter.searchById(arrayList.get(0).get("id"));

                Toast.makeText(getBaseContext(),"兌換成功", Toast.LENGTH_LONG).show();

                stamp1.setImageResource(R.drawable.white_new2);
                stamp2.setImageResource(R.drawable.white_new2);
                stamp3.setImageResource(R.drawable.white_new2);
                stamp4.setImageResource(R.drawable.white_new2);
                stamp5.setImageResource(R.drawable.white_new2);
                stamp6.setImageResource(R.drawable.white_new2);
                stamp1.setAlpha(0.75f);
                stamp2.setAlpha(0.75f);
                stamp3.setAlpha(0.75f);
                stamp4.setAlpha(0.75f);
                stamp5.setAlpha(0.75f);
                stamp6.setAlpha(0.75f);//把畫面上的imageview變回白色背景並且設定半透明
            }
        });
        alertDialog.setNeutralButton("我反悔了", new DialogInterface.OnClickListener()
        { //設定取消按鈕
            @Override
            public void onClick(DialogInterface dialog, int which)
            {}
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void clear(){//初始化變數
        arrayList.clear();//取得所有資料
        getNowArray.clear();//取得被選中的項目資料
    }
}