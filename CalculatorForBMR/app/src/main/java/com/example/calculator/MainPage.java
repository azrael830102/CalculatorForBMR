package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {
    private ListView list_records;


    private Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        initViewElement();
        //set element event
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("action", R.string.btn_event_create);

                Intent it = new Intent() ;
                it.putExtras(bundle);
                it.setClass(MainPage.this , RecordPage.class ) ;

                startActivity(it) ;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //query records in DB
        RecordDao recordDao = new RecordDao(this,list_records, getTitleFieldWidth());
//        recordDao.initDBSetting();
        ArrayList<Record> record_list= null;
        record_list = recordDao.queryAllRecords();
        if(record_list!= null){
            CustcomListViewAdapter adapter;
            adapter = new CustcomListViewAdapter(this, record_list, getTitleFieldWidth());
            list_records.setAdapter(adapter);
        }
    }

    private String getTitleFieldWidth() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView title_divider = (TextView) findViewById(R.id.title_divider);
        TextView title_bmr = (TextView) findViewById(R.id.title_bmr);
        return title_name.getWidth() + "," + title_divider.getWidth() + "," + title_bmr.getWidth();
    }
    private void initViewElement(){
        list_records = (ListView) findViewById(R.id.list_records);
        btn_create = (Button) findViewById(R.id.btn_create);
    }
}