package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/3/9.
 * 服药时间信息项适配器
 */
public class TakeDrugTimeInfoItemAdapter extends BaseAdapter implements SectionIndexer {

    ArrayList<TakeDrugTimeItem> list;
    Context context;
    LinearLayout linearLayout;

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public TakeDrugTimeItem getItem(int position) {

        TakeDrugTimeItem takeDrugTimeItem = list.get(position);

        return takeDrugTimeItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.take_drug_time_info_item, null);
        viewHolder.time = (TextView) convertView.findViewById(R.id.time);
        viewHolder.drug_name_1 = (TextView) convertView.findViewById(R.id.drug_name_1);
        viewHolder.drug_desc_1 = (TextView) convertView.findViewById(R.id.drug_desc_1);
        viewHolder.begin_time_1 = (TextView) convertView.findViewById(R.id.begin_time_1);
        viewHolder.end_time_1 = (TextView) convertView.findViewById(R.id.end_time_1);
        String time = viewHolder.time.getText().toString();
        String timeArray[] = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        int timeValue = hour * 100 + minute;
        //根据时间决定时间旁边的图标
        if (timeValue < 1000) {
            viewHolder.time.setCompoundDrawables(null, null, convertView.getResources().getDrawable(R.drawable.lvzao), null);
        }
        if (timeValue >= 1000 && timeValue < 1600) {
            viewHolder.time.setCompoundDrawables(null, null, convertView.getResources().getDrawable(R.drawable.lvzhong), null);
        }
        if (timeValue >= 1600 && timeValue <= 2359) {
            viewHolder.time.setCompoundDrawables(null, null, convertView.getResources().getDrawable(R.drawable.lvwan), null);
        }
        viewHolder.time.setText(this.list.get(position).getTake_drug_time());
        viewHolder.drug_name_1.setText(this.list.get(position).getDrug_name_1());
        viewHolder.drug_desc_1.setText(this.list.get(position).getDrug_desc_1());
        viewHolder.begin_time_1.setText(this.list.get(position).getBegin_date_1());
        viewHolder.end_time_1.setText(this.list.get(position).getEnd_date_1());

        return convertView;
    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    final class ViewHolder {

        TextView time;

        TextView drug_name_1;
        TextView drug_desc_1;
        TextView begin_time_1;
        TextView end_time_1;
        View line_1;
        RelativeLayout content_1;

        TextView drug_name_2;
        TextView drug_desc_2;
        TextView begin_time_2;
        TextView end_time_2;
        View line_2;
        RelativeLayout content_2;

        TextView drug_name_3;
        TextView drug_desc_3;
        TextView begin_time_3;
        TextView end_time_3;
        View line_3;
        RelativeLayout content_3;

        TextView drug_name_4;
        TextView drug_desc_4;
        TextView begin_time_4;
        TextView end_time_4;
        View line_4;
        RelativeLayout content_4;

        TextView drug_name_5;
        TextView drug_desc_5;
        TextView begin_time_5;
        TextView end_time_5;
        View line_5;
        RelativeLayout content_5;

        TextView drug_name_6;
        TextView drug_desc_6;
        TextView begin_time_6;
        TextView end_time_6;
        View line_6;
        RelativeLayout content_6;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList<TakeDrugTimeItem> list) {
        this.list = list;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int section) {

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void updateListView(ArrayList<TakeDrugTimeItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
