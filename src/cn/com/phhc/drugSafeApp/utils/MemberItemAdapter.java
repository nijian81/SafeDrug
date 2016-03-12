package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/3/9.
 */
public class MemberItemAdapter extends BaseAdapter {

    ArrayList<MemberItem> list;
    Context context;

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public MemberItem getItem(int position) {

        MemberItem memberItem = list.get(position);

        return memberItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.member_item, null);
            viewHolder.portrait = (ImageView) convertView.findViewById(R.id.portrait);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String portrait = this.list.get(position).getPortrait();
        switch (portrait) {
            case "tx1":
                viewHolder.portrait.setImageResource(R.drawable.tx1);
                break;
            case "tx2":
                viewHolder.portrait.setImageResource(R.drawable.tx2);
                break;
            case "tx3":
                viewHolder.portrait.setImageResource(R.drawable.tx3);
                break;
            case "tx4":
                viewHolder.portrait.setImageResource(R.drawable.tx4);
                break;
            case "tx5":
                viewHolder.portrait.setImageResource(R.drawable.tx5);
                break;
            case "tx6":
                viewHolder.portrait.setImageResource(R.drawable.tx6);
                break;
            case "tx7":
                viewHolder.portrait.setImageResource(R.drawable.tx7);
                break;
            case "tx8":
                viewHolder.portrait.setImageResource(R.drawable.tx8);
                break;
            default:
                viewHolder.portrait.setImageResource(R.drawable.tx4);
                break;
        }
        viewHolder.name.setText(this.list.get(position).getName());

        return convertView;
    }

    final static class ViewHolder {
        ImageView portrait;
        TextView name;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList<MemberItem> list) {
        this.list = list;
    }

    public void updateListView(ArrayList<MemberItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
