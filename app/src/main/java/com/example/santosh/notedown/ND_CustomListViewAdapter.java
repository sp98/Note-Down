/* Class Name: ND_CustomListViewAdapter
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */


package com.example.santosh.notedown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_CustomListViewAdapter extends ArrayAdapter {

    Context context;
    List titleArray = new ArrayList();
    List descArray= new ArrayList();
    List truncatedDescArray = new ArrayList();
    List timeArray= new ArrayList();
    // List ActualTimeArray = new ArrayList();



    ND_CustomListViewAdapter(Context context, List titles, List desc, List time) {
        super(context, R.layout.nd_singlerow_layout, R.id.Title, titles);
        this.context=context;
        this.titleArray = titles;
        this.descArray = desc;
        this.timeArray = time;
        // changeDataFormat(timeArray);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        MyViewHolder holder = null;

        if( row == null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.nd_singlerow_layout, parent, false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }

        else {
            holder = (MyViewHolder) row.getTag();
        }

        for (int i= 0; i<descArray.size();i++) {
            String text = descArray.get(i).toString();
            if (text.length() > 23) {
                String truncated = text.substring(0, 22) + "...";
                truncatedDescArray.add(truncated);
            } else
                truncatedDescArray.add(text);
        }

        TextView myTitle = (TextView) row.findViewById(R.id.Title);
        TextView myDesc = (TextView) row.findViewById(R.id.Text);
        TextView myTime = (TextView) row.findViewById(R.id.Time);

        myTitle.setText(titleArray.get(position).toString());
        myDesc.setText(truncatedDescArray.get(position).toString());
        myTime.setText(timeArray.get(position).toString());

        return row;
    }


}


class MyViewHolder {

    TextView txtView1;
    TextView txtView2;
    TextView txtView3;

    MyViewHolder(View v) {

        this.txtView1 = (TextView) v.findViewById(R.id.Title);
        this.txtView2 = (TextView) v.findViewById(R.id.Text);
        this.txtView3 = (TextView) v.findViewById(R.id.Time);
    }
}
