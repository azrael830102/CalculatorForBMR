package com.example.calculator;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class RecordDao {

    private static final String API_URL = "http://10.0.2.2/bmr_calculator";
    private static final String INIT_PHP = "/initDB.php";
    private static final String QUERY_PHP = "/query_record.php";
    private static final String INSERT_PHP = "/insert_record.php";
    private static final String UPDATE_PHP = "/update_record.php";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_GENDER = "gender";
    public static final String COL_AGE = "age";
    public static final String COL_HEIGHT = "height";
    public static final String COL_WEIGHT = "weight";
    public static final String COL_BMR = "bmr_value";
    public static final String COL_BMI = "bmi_value";


    private Record _record;

    private ListView _list_records;
    private String _titleFieldWidth;
    private Context _mcon;

    public RecordDao() {

    }

    // insert record using
    public RecordDao(Record record) {
        _record = record;
    }

    // query record using
    public RecordDao(Context mcon, ListView list_records, String titleFieldWidth) {
        _mcon = mcon;
        _list_records = list_records;
        _titleFieldWidth = titleFieldWidth;
    }

    public void initDBSetting() {
        String path = API_URL + INIT_PHP;
        new Thread(
                new Runnable() {
                    String _path;

                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = executeHttpGet(_path);
                            if (jsonObject != null) {
                                String msg = jsonObject.getString("msg");
                                Log.v("initDBSetting() : ", msg);
                            } else {
                                Log.v("initDBSetting() : ", "DB init failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    public Runnable init(String path) {
                        _path = path;
                        return this;
                    }
                }.init(path)
        ).start();
    }

    public ArrayList<Record> queryAllRecords() {
        try {
            ArrayList<Record> lst = queryRecords();
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public synchronized ArrayList<Record> queryRecords() {
        String path = API_URL + QUERY_PHP;
        RunnableCallable rct = new RunnableCallable(path);
        new Thread(rct).start();
        Log.d("queryRecords()", "End");
       return rct.call();
    }

//    public synchronized void queryRecords() {
//        String path = API_URL + QUERY_PHP;
//        new Thread(
//                new Runnable() {
//                    String _path;
//                    private ListView _list_records;
//                    private String _titleFieldWidth;
//                    private Context _mcon;
//
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject jsonObject = executeHttpGet(_path);
//                            ArrayList<Record> record_list = new ArrayList<>();
//                            if (jsonObject != null) {
//                                String msg = jsonObject.getString("msg");
//                                JSONArray array = new JSONArray(jsonObject.getString("records"));
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject recordJsonObj = array.getJSONObject(i);
//                                    Record record = new Record(recordJsonObj);
//                                    record_list.add(record);
//                                }
//                                CustcomListViewAdapter adapter;
//                                adapter = new CustcomListViewAdapter(_mcon, record_list, _titleFieldWidth);
//                                _list_records.setAdapter(adapter);
//                                Log.v("initDBSetting() : ", msg);
//                            } else {
//                                Log.v("initDBSetting() : ", "DB init failed");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    public Runnable init(String path, Context mcon, ListView list_records, String titleFieldWidth) {
//                        _path = path;
//                        _mcon = mcon;
//                        _list_records = list_records;
//                        _titleFieldWidth = titleFieldWidth;
//                        return this;
//                    }
//
//                }.init(path, _mcon, _list_records, _titleFieldWidth)
//        ).start();
//    }

    public boolean updateRecord() {
        try {
            updateRecord(recordToMap());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized void updateRecord(HashMap<String, String> map) {
        new Thread(new Runnable() {
            HashMap<String, String> _map;
            @Override
            public void run() {
                String path = API_URL + UPDATE_PHP;
                executeHttpPost(path,_map);
            }
            public Runnable init(HashMap<String, String> map) {
                _map = map;
                return this;
            }
        }.init(map)).start();
        Log.d("updateRecord()", "End");
    }

    public boolean insertRecord() {
        try {
            insertRecord(recordToMap());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized void insertRecord(HashMap<String, String> map) {

        new Thread(new Runnable() {
            HashMap<String, String> _map;
            @Override
            public void run() {
                String path = API_URL + INSERT_PHP;
                executeHttpPost(path,_map);
            }
            public Runnable init(HashMap<String, String> map) {
                _map = map;
                return this;
            }
        }.init(map)).start();
        Log.d("insertRecord()", "End");
    }

    private void executeHttpPost(String path,HashMap<String, String> map) {
        try {
            // request method is POST

            URL urlObj = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(mapToString(map));
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("Save Record", "result: " + result.toString());
            conn.disconnect();
        } catch (IOException e) {
            Log.v("Save Record", "Record saved failed");
            e.printStackTrace();
        }
    }

    public static JSONObject executeHttpGet(String path) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(path).openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return new JSONObject(parseInfo(in));
            } else {
                Log.v("executeHttpGet()", con.getResponseMessage());
            }
        } catch (IOException | JSONException e) {
            if (e instanceof IOException) {
                ((IOException) e).printStackTrace();
            } else if (e instanceof JSONException) {
                ((JSONException) e).printStackTrace();
                Log.v("executeHttpGet()", ((JSONException) e).getMessage());
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String parseInfo(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        Log.v("parseInfo() : ", sb.toString());
        return sb.toString();
    }


    private  String mapToString(HashMap<String, String> map) {
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : map.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(map.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        return sbParams.toString();
    }
    private  HashMap<String, String> recordToMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(COL_ID, _record.get_uid());
        map.put(COL_NAME, _record.get_name());
        map.put(COL_GENDER, _record.get_gender());
        map.put(COL_AGE, _record.get_age());
        map.put(COL_HEIGHT, _record.get_height());
        map.put(COL_WEIGHT, _record.get_weight());
        map.put(COL_BMR, _record.get_bmrValue());
        map.put(COL_BMI, _record.get_bmiValue());
        return map;
    }


}




class RunnableCallable implements Callable<ArrayList<Record>>, Runnable {
    String _path;
    private ArrayList<Record> record_list = new ArrayList<>();
    @Override
    public void run() {
        try {
            JSONObject jsonObject = RecordDao.executeHttpGet(_path);

            if (jsonObject != null) {
                String msg = jsonObject.getString("msg");
                JSONArray array = new JSONArray(jsonObject.getString("records"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject recordJsonObj = array.getJSONObject(i);
                    Record record = new Record(recordJsonObj);
                    record_list.add(record);
                }
//                 CustcomListViewAdapter adapter;
//                 adapter = new CustcomListViewAdapter(_mcon, record_list, _titleFieldWidth);
//                 _list_records.setAdapter(adapter);
                Log.v("initDBSetting() : ", msg);
            } else {
                Log.v("initDBSetting() : ", "DB init failed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public RunnableCallable (String path) {
        _path = path;
    }
    @Override
    public ArrayList<Record> call(){
        return record_list;
    }
}