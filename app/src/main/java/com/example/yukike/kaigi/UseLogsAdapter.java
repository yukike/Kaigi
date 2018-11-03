package com.example.yukike.kaigi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UseLogsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<UseLogs> UseLogsList;

    public UseLogsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUseLogsList(ArrayList<UseLogs> UseLogsList) {
        this.UseLogsList = UseLogsList;
    }

    @Override
    public int getCount() {
        return UseLogsList.size();
    }

    @Override
    public Object getItem(int position) {
        return UseLogsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return UseLogsList.get(position).getId();
    }

    @Override
    public boolean isEnabled(int postion) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.uselogsrow,parent,false);

        ((TextView)convertView.findViewById(R.id.textViewRoomName)).setText(UseLogsList.get(position).getRoomNo());
        ((TextView)convertView.findViewById(R.id.textViewYyyymmdd)).setText(UseLogsList.get(position).getYyyymmdd());
        ((TextView)convertView.findViewById(R.id.textViewStartHhmm)).setText(String.valueOf(UseLogsList.get(position).getStartHhmm()));
        //((TextView)convertView.findViewById(R.id.textViewEndHhmm)).setText(String.valueOf(ReserveLogsList.get(position).getEndHhmm()));

        return convertView;
    }
}
