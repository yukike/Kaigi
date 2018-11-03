package com.example.yukike.kaigi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReserveLogsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<ReserveLogs> ReserveLogsList;

    public ReserveLogsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setReserveLogsList(ArrayList<ReserveLogs> ReserveLogsList) {
        this.ReserveLogsList = ReserveLogsList;
    }

    @Override
    public int getCount() {
        return ReserveLogsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ReserveLogsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ReserveLogsList.get(position).getId();
    }

    @Override
    public boolean isEnabled(int postion) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.reservelogsrow,parent,false);

        ((TextView)convertView.findViewById(R.id.textViewRoomName)).setText(ReserveLogsList.get(position).getRoomNo());
        ((TextView)convertView.findViewById(R.id.textViewYyyymmdd)).setText(ReserveLogsList.get(position).getYyyymmdd());
        ((TextView)convertView.findViewById(R.id.textViewStartHhmm)).setText(String.valueOf(ReserveLogsList.get(position).getStartHhmm()));
        ((TextView)convertView.findViewById(R.id.textViewEndHhmm)).setText(String.valueOf(ReserveLogsList.get(position).getEndHhmm()));
        ((TextView)convertView.findViewById(R.id.textViewStatus)).setText(String.valueOf(ReserveLogsList.get(position).getStatus()));

        return convertView;
    }
}
