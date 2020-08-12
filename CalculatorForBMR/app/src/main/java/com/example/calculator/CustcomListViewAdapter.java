package com.example.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustcomListViewAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private ArrayList<Record> _records;
    private String _titleFieldWidth;
    private Context _mcon;

    public CustcomListViewAdapter(Context context, ArrayList<Record> records, String titleFieldWidth) {
        _mcon = context;
        myInflater = LayoutInflater.from(context);
        this._records = records;
        this._titleFieldWidth = titleFieldWidth;
    }

    public CustcomListViewAdapter(Context context, String titleFieldWidth) {
        _mcon = context;
        myInflater = LayoutInflater.from(context);
        this._titleFieldWidth = titleFieldWidth;
    }

    /*private view holder class*/
    private class ViewHolder {
        //        TextView _item_id;
        TextView _item_name;
        TextView _item_divider;
        TextView _item_bmr;

        public ViewHolder(/*TextView item_id,*/TextView item_name, TextView item_divider, TextView item_bmr) {
//            this._item_id = item_id;
            this._item_name = item_name;
            this._item_divider = item_divider;
            this._item_bmr = item_bmr;
        }
    }
    public void setRecords(ArrayList<Record> records){_records = records;};
    @Override
    public int getCount() {
        return _records.size();
    }

    @Override
    public Object getItem(int arg0) {
        return _records.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return _records.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String[] itemFieldWidth = _titleFieldWidth.split(",");// 0:Name ; 1:| ; 2:BMR
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.custom_listview, null);
            holder = new ViewHolder(
//                    (TextView) convertView.findViewById(R.id.item_id),
                    (TextView) convertView.findViewById(R.id.item_name),
                    (TextView) convertView.findViewById(R.id.item_divider),
                    (TextView) convertView.findViewById(R.id.item_bmr)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Record record = (Record) getItem(position);
        try {
//            holder._item_id.setText(Long.toString(record.get_uid()));

            holder._item_name.setText(record.get_name());
            holder._item_name.setWidth(Integer.parseInt(itemFieldWidth[0]));

            holder._item_divider.setWidth(Integer.parseInt(itemFieldWidth[1]));

            holder._item_bmr.setText(record.get_bmrValue());
            holder._item_bmr.setWidth(Integer.parseInt(itemFieldWidth[2]));


        } catch (Exception e) {
            throw e;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            private Record _selected_record;
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("action", R.string.btn_event_modify);
                bundle.putParcelable("selectedRecord", _selected_record);
                Intent it = new Intent(_mcon, RecordPage.class ) ;
                it.putExtras(bundle);
                _mcon.startActivity(it) ;
            }
            private View.OnClickListener init(Record selected_record) {
                _selected_record = selected_record;
                return this;
            }
        }.init(record));

        return convertView;
    }

}


