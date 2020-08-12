package com.example.calculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class OutputPage extends AppCompatActivity {
    private Button btn_cancel;
    private Button btn_save;

    private TextView tv_result_name;
    private TextView tv_result_bmi;
    private TextView tv_result_bmr;

    private Record selected_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_page);

        Intent it = this.getIntent();
        Bundle bundle = it.getExtras();
        selected_record = (Record) bundle.getParcelable("selectedRecord");
        int action = bundle.getInt("action");

        initViewElement();
        showResult();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(OutputPage.this, MainPage.class);
                startActivity(it);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            private Context _mcon;
            private int _action;
            @Override
            public void onClick(View view) {
                Log.v("Save Record : ", "Record to save : " + selected_record.toString());
                RecordDao recordDao = new RecordDao(selected_record);
                boolean isSucceed = false;
                switch (_action) {
                    case R.string.btn_event_create:
                        isSucceed = recordDao.insertRecord();
                        break;
                    case R.string.btn_event_modify:
                        isSucceed = recordDao.updateRecord();
                        break;
                }

                if (isSucceed) {
                    // save successes
                    Intent it = new Intent();
                    it.setClass(OutputPage.this, MainPage.class);
                    startActivity(it);
                } else {
                    showMsgBox(_mcon);
                }
            }

            private View.OnClickListener init(Context mcon,int action) {
                _mcon = mcon;
                _action = action;
                return this;
            }
        }.init(this,action));
    }

    private static void showMsgBox(Context mcon) {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(mcon);
        MyAlertDialog.setTitle("Error!");
        MyAlertDialog.setMessage("Record saved failed");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyAlertDialog.setNeutralButton("OK", OkClick);
        MyAlertDialog.show();
    }

    private void showResult() {
        DecimalFormat df = new DecimalFormat("#.00");
        tv_result_name.setText(selected_record.get_name());
        String bmi = calculateBMI(df);
        String bmr = calculateBMR(df);
        tv_result_bmi.setText(bmi);
        tv_result_bmr.setText(bmr);
        selected_record.set_bmiValue(bmi);
        selected_record.set_bmrValue(bmr);
    }

    private void initViewElement() {
        btn_cancel = (Button) findViewById(R.id.result_page_btn_cancel);
        btn_save = (Button) findViewById(R.id.result_page_btn_save);
        tv_result_name = (TextView) findViewById(R.id.result_name);
        tv_result_bmi = (TextView) findViewById(R.id.result_bmi);
        tv_result_bmr = (TextView) findViewById(R.id.result_bmr);
    }

    private String calculateBMI(DecimalFormat df) {
        double height = Double.parseDouble(selected_record.get_height()) / 100;//cm -> m
        double weight = Double.parseDouble(selected_record.get_weight());//kg
        return df.format((weight / (height * height)));
    }

    private String calculateBMR(DecimalFormat df) {
        double height = Double.parseDouble(selected_record.get_height());//cm
        double weight = Double.parseDouble(selected_record.get_weight());//kg
        double age = Double.parseDouble(selected_record.get_age());

        if (selected_record.get_gender().equals(getResources().getString(R.string.record_gender_male))) {
            //Male
            return df.format((13.7 * weight) + (5.0 * height) - (6.8 * age) + 66);
        } else {
            //Female
            return df.format((9.6 * weight) + (1.8 * height) - (4.7 * age) + 655);
        }
    }

}