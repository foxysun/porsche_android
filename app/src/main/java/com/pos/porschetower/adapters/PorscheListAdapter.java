package com.pos.porschetower.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.porschetower.R;
import com.pos.porschetower.customview.PorscheTextView;

/**
 * Created by coala on 10/19/2020.
 */

public class PorscheListAdapter extends BaseAdapter {

    String[] listdata;

    public PorscheListAdapter(String[] listdata) {
        this.listdata = listdata;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return listdata.size();
        return listdata.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();
        final String item = listdata[position];

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) inflater.inflate(R.layout.menu_list_textview, parent, false);

        holder.txtTitle	= (PorscheTextView) view.findViewById(R.id.item_textview);

        holder.txtTitle.setText(item);

        return view;
    }

    public String[] getData() {
        return this.listdata;
    }

    public void setData(String[] data) {
        this.listdata = data;
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView txtTitle;
    }
}