package com.notbytes.barcodereader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TI A1 on 14-06-2017.
 */
public class CustomAdapterDetails extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataClass> Items;
    DataHelper dbHelper;
    ArrayList<DataClass> arrDataFilter;

    public CustomAdapterDetails(Activity activity, List<DataClass> Items) {
        this.activity = activity;
        this.Items = Items;
        arrDataFilter = new ArrayList<DataClass>();
        arrDataFilter.addAll(Items);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DataHelper(activity);
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView tvitemno;
        public TextView tvweight;
        public Button btndeleterow;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row, null);

            holder.tvitemno = (TextView) convertView.findViewById(R.id.tvitemno);
            holder.tvweight = (TextView) convertView.findViewById(R.id.tvweight);
            holder.btndeleterow = (Button) convertView.findViewById(R.id.btndeleterow);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        final DataClass data = Items.get(position);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#E9EBE9"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.tvitemno.setText(data.getItem_no()+"");
        holder.tvweight.setText(data.getGram()+"");
        holder.btndeleterow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.openToWrite();
                dbHelper.removeFromList(data.getId());
                Items.remove(position);
                HistoryActivity.tvtotal.setText("Total Weight = "+dbHelper.retriveTotal());
                dbHelper.close();

                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
