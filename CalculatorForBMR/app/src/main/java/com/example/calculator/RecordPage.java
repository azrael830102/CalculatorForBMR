package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RecordPage extends AppCompatActivity {
    private Button btn_cancel;
    private Button btn_send;

    private Record selected_record;

    private EditText et_name;
    private RadioGroup rg_gender;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_page);
        initPageElement();
        Intent it = this.getIntent();
        Bundle bundle = it.getExtras();
        int action = bundle.getInt("action");
        TextView tv_RecordPageTitle = (TextView) findViewById(R.id.record_page_title);
        switch (action) {
            case R.string.btn_event_create:
                tv_RecordPageTitle.setText("Creating Record");
                selected_record = new Record();
                break;
            case R.string.btn_event_modify:
                tv_RecordPageTitle.setText("Modifying Record");
                selected_record = (Record) bundle.getParcelable("selectedRecord");
                initRecordFields(selected_record);
                break;
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(RecordPage.this, MainPage.class);
                startActivity(it);
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            int _action;
            @Override
            public void onClick(View view) {
                sendDataToResultPage(_action);
            }
            public View.OnClickListener init(int action) {
                _action = action;
                return this;
            }
        }.init(action));

        setHideKeyboardEvent();
    }

    private void sendDataToResultPage(int action) {
        selected_record.set_name(et_name == null ? "" : et_name.getText().toString());

        if (rg_gender.getCheckedRadioButtonId() == R.id.record_page_gender_0) {
            selected_record.set_gender(getResources().getString(R.string.record_gender_male));
        } else {
            selected_record.set_gender(getResources().getString(R.string.record_gender_female));
        }
        selected_record.set_age(et_age == null ? "" : et_age.getText().toString());
        selected_record.set_height(et_height == null ? "" : et_height.getText().toString());
        selected_record.set_weight(et_weight == null ? "" : et_weight.getText().toString());

        Bundle bundle = new Bundle();
        switch (action) {
            case R.string.btn_event_create:
                bundle.putInt("action", R.string.btn_event_create);
                break;
            case R.string.btn_event_modify:
                bundle.putInt("action", R.string.btn_event_modify);
                initRecordFields(selected_record);
                break;
        }
        bundle.putParcelable("selectedRecord", selected_record);

        Intent it = new Intent() ;
        it.putExtras(bundle);
        it.setClass(RecordPage.this , OutputPage.class ) ;

        startActivity(it) ;
    }
    private void initPageElement(){
        btn_send = (Button) findViewById(R.id.record_page_btn_send);
        btn_cancel = (Button) findViewById(R.id.record_page_btn_cancel);
        et_name = findViewById(R.id.record_page_name);
        rg_gender = findViewById(R.id.record_page_gender);
        et_age = findViewById(R.id.record_page_age);
        et_height = findViewById(R.id.record_page_height);
        et_weight = findViewById(R.id.record_page_weight);
    }

    private void initRecordFields(Record selected_record) {
        if (selected_record != null) {
            et_name.setText(selected_record.get_name());

            if (selected_record.get_gender().equals(getResources().getString(R.string.record_gender_male))) {
                rg_gender.check(R.id.record_page_gender_0);//Male
            } else {
                rg_gender.check(R.id.record_page_gender_1);//Female
            }

            et_age.setText(selected_record.get_age());
            et_height.setText(selected_record.get_height());
            et_weight.setText(selected_record.get_weight());
        }
    }



    public void setHideKeyboardEvent() {
        setHideKeyboardEvent(et_name);
        setHideKeyboardEvent(et_age);
        setHideKeyboardEvent(et_height);
        setHideKeyboardEvent(et_weight);
    }

    public void setHideKeyboardEvent(View view) {
        View.OnFocusChangeListener ofcl = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };
        view.setOnFocusChangeListener(ofcl);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}