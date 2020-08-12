package com.example.calculator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Record implements Parcelable {
    private String _uid;
    private String _name = "";
    private String _gender = "";
    private String _age = "";
    private String _height = "";
    private String _weight = "";
    private String _bmrValue = "";
    private String _bmiValue = "";

    public Record() {
        UUID uuid = UUID.randomUUID();
        _uid = uuid.toString().replace("-", "");
    }

    public Record(String name, String gender, String age, String height, String weight, String bmrValue, String bmiValue) {
        UUID uuid = UUID.randomUUID();
        _uid = uuid.toString().replace("-", "");
        set_name(name);
        set_gender(gender);
        set_age(age);
        set_height(height);
        set_weight(weight);
        set_bmrValue(bmrValue);
        set_bmiValue(bmiValue);
    }

    public Record(String uid, String name, String gender, String age, String height, String weight, String bmrValue, String bmiValue) {
        set_uid(uid);
        set_name(name);
        set_gender(gender);
        set_age(age);
        set_height(height);
        set_weight(weight);
        set_bmrValue(bmrValue);
        set_bmiValue(bmiValue);
    }

    public Record(JSONObject recordJsonObj) throws JSONException {
        set_uid(recordJsonObj.getString(RecordDao.COL_ID));
        set_name(recordJsonObj.getString(RecordDao.COL_NAME));
        set_gender(recordJsonObj.getString(RecordDao.COL_GENDER));
        set_age(recordJsonObj.getString(RecordDao.COL_AGE));
        set_height(recordJsonObj.getString(RecordDao.COL_HEIGHT));
        set_weight(recordJsonObj.getString(RecordDao.COL_WEIGHT));
        set_bmrValue(recordJsonObj.getString(RecordDao.COL_BMR));
        set_bmiValue(recordJsonObj.getString(RecordDao.COL_BMI));
    }

    protected Record(Parcel in) {
        _uid = in.readString();
        _name = in.readString();
        _gender = in.readString();
        _age = in.readString();
        _height = in.readString();
        _weight = in.readString();
        _bmrValue = in.readString();
        _bmiValue = in.readString();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };


    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_height() {
        return _height;
    }

    public void set_height(String _height) {
        this._height = _height;
    }

    public String get_weight() {
        return _weight;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }

    public String get_bmrValue() {
        return _bmrValue;
    }

    public void set_bmrValue(String _bmrValue) {
        this._bmrValue = _bmrValue;
    }

    public String get_bmiValue() {
        return _bmiValue;
    }

    public void set_bmiValue(String _bmiValue) {
        this._bmiValue = _bmiValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_uid);
        parcel.writeString(_name);
        parcel.writeString(_gender);
        parcel.writeString(_age);
        parcel.writeString(_height);
        parcel.writeString(_weight);
        parcel.writeString(_bmrValue);
        parcel.writeString(_bmiValue);

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid = " + _uid);
        sb.append(" name = " + _name);
        sb.append(" gender = " + _gender);
        sb.append(" age = " + _age);
        sb.append(" height = " + _height);
        sb.append(" weight = " + _weight);
        sb.append(" bmr_value = " + _bmrValue);
        sb.append(" bmi_value = " + _bmiValue);
        return sb.toString();
    }
}
