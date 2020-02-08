package com.example.emergncyemail;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    static SQLiteDatabase sql;
    TextView start,end;
    String check="";
    RecyclerView recyclerView;
    static List<Product> productList=new ArrayList<Product>();
    ProductAdapter adapter;
    List<CheckBox> checkBoxList=new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList=ReciveActivity.products;
        adapter=new ProductAdapter(this,productList);
        recyclerView.setAdapter(adapter);

        System.out.print("jjo");
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Bottom b=new Bottom();
              b.show(getSupportFragmentManager(),"bottom");

            }
        });*/
        sql=openOrCreateDatabase("Emergency", MODE_PRIVATE, null);
        sql.execSQL("create table if not exists emergency(day varchar,start varchar,ends varchar)");
    }

    public void save(View v){
        String s=start.getText().toString();
        String e=end.getText().toString();
        String week="";
        for(CheckBox a:checkBoxList){
            if(a.isChecked())
             week=week+a.getText().toString()+" ";
        }
        productList.add(new Product(1234,s,e,week));
        adapter=new ProductAdapter(this,productList);
        recyclerView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(),week,Toast.LENGTH_SHORT).show();
        DatabaseSave(s,e,week);
        ReciveActivity.products=productList;
        EditText et=findViewById(R.id.editText);
        SendEmailTask.email=et.getText().toString();
        checkBoxList=new ArrayList<CheckBox>();
    }

    public void DatabaseSave(String s,String e,String week){
        String saa[]=s.split(" ");
        String eaa[]=e.split(" ");
        String sr[]=saa[0].split(":");
        String er[]=eaa[0].split(":");

        int sa=Integer.parseInt(sr[0]);
        int sb=Integer.parseInt(sr[1]);
        int ea=Integer.parseInt(er[0]);
        int eb=Integer.parseInt(er[1]);
        long adds=(sa*3600)+(sb*60);
        long adde=(ea*3600)+(eb*60);
        Toast.makeText(getApplicationContext(),adds+":"+adde,Toast.LENGTH_SHORT).show();
        for(CheckBox a:checkBoxList){
            if(a.isChecked()){
                sql.execSQL("insert into emergency  values('"+a.getText().toString()+"','"+adds+"','"+adde+"')");
            }
        }
    }

    public void checkbox(View v){

        CheckBox checkBox=(CheckBox) v;
        if(checkBox.isChecked()){
          for(int i=0;i<checkBoxList.size();i++){
              CheckBox box=checkBoxList.get(i);
              if(checkBox.getText().toString()==box.getText().toString()){
                  checkBoxList.add(checkBox);
              }else{
              }
          }
        }

        checkBoxList.add(checkBox);
        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();

    }



    public void openTimePicker(){
        Timepicker tm=new Timepicker();
        tm.show(getSupportFragmentManager(),"pick");

    }

    public void start(View v){
      openTimePicker();
      start=(TextView) v;
      check="start";
    }


    public void end(View v){
        openTimePicker();
        end=(TextView) v;
        check="end";
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Toast.makeText(getApplicationContext(),"poo",Toast.LENGTH_LONG).show();
        String AM_PM;
        if (hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        if(check.equals("start")){

            if (hour < 10 && min < 10) {
                start.setText("0"+hour + ":0" + min + " " + AM_PM);
            } else if (hour < 10) {
                start.setText("0"+hour + ":" + min + " " + AM_PM);
            } else if (min < 10) {
                start.setText(hour + ":0" + min + " " + AM_PM);
            }else {
                start.setText(hour + ":" + min + " " + AM_PM);
            }
        }else{
            if (hour < 10 && min < 10) {
                end.setText("0"+hour + ":0" + min + " " + AM_PM);
            } else if (hour < 10) {
                end.setText("0"+hour + ":" + min + " " + AM_PM);
            } else if (min < 10) {
                end.setText(hour + ":0" + min + " " + AM_PM);
            }else {
                end.setText(hour + ":" + min + " " + AM_PM);
            }
        }
    }

    public Boolean stringsplit(String s,String e) {
        Boolean flag=false;
        String[] a = s.split(":");
        String[] b = e.split(":");


        return flag;
    }

    public void search(){

        Cursor c=sql.rawQuery("select start,ends from emergency where day='Sunday'", null);

        if(c.getCount()!=0){
            c.moveToFirst();
            do{
                String start2=c.getString(c.getColumnIndex("start"));
                String end2=c.getString(c.getColumnIndex("ends"));
                Toast.makeText(getApplicationContext(), start2+"--"+end2, Toast.LENGTH_SHORT).show();
            }while(c.moveToNext());
        }else{
            Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
        }
    }


      static class SendEmailTask extends AsyncTask<Void, Void, Void> {

        static String email;
         String no;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Email sending", "sending start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
                Cursor c=sql.rawQuery("select start,ends from emergency where day='"+weekday_name+"'", null);

                if(c.getCount()!=0){
                    c.moveToFirst();
                    do{
                        String start2=c.getString(c.getColumnIndex("start"));
                        String end2=c.getString(c.getColumnIndex("ends"));
                        int a=Integer.parseInt(start2);
                        int b=Integer.parseInt(end2);

                        Calendar ca=Calendar.getInstance();
                        String sq=""+ca.getTime();
                        String eq=sq.substring(11,16);
                        String srp[]=eq.split(":");
                        int saa=Integer.parseInt(srp[0]);
                        int sbb=Integer.parseInt(srp[1]);
                        int curr=(saa*3600)+(sbb*60);

                        if(curr>a && curr<b){
                            GmailSender sender = new GmailSender("amanyadavwolvrine@gmail.com","amanwolvrine333");
                            //subject, body, sender, to
                            sender.sendMail("Emergency",
                                    "You have Call from this No: "+no,
                                    "amanyadavwolvrine@gmail.com",
                                    email);
                         }

                    }while(c.moveToNext());
                }

                Log.i("Email sending", "send");
            } catch (Exception e) {
                Log.i("Email sending", "cannot send");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}



