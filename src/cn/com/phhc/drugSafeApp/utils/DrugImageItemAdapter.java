package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import cn.com.phhc.drugSafeApp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/3/9.
 * 药品查询界面用于药品项展示的适配器
 */
public class DrugImageItemAdapter extends BaseAdapter implements SectionIndexer {

    ArrayList<DrugImageItem> list;
    Context context;

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public DrugImageItem getItem(int position) {

        DrugImageItem drugItem = list.get(position);

        return drugItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.drug_image_item, null);
        viewHolder.drugImage = (ImageView) convertView.findViewById(R.id.image);

        String flag = this.list.get(position).getFlag();
        if (flag.equals("1")) {
            viewHolder.drugImage.setImageResource(R.drawable.xz);
        } else {
            viewHolder.drugImage.setImageResource(R.drawable.wxz);
        }

        return convertView;
    }

    final static class ViewHolder {
        ImageView drugImage;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList<DrugImageItem> list) {
        this.list = list;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

//    @Override
//    public int getPositionForSection(int section) {
//        for (int i = 0; i < getCount(); i++) {
//            String sortStr = list.get(i).getSortLetters();
//            char firstChar = sortStr.charAt(0);
//            if (firstChar == section) {
//                return i;
//            }
//        }
//
//        return -1;
//    }

//    public int getSectionForPosition(int position) {
//        return list.get(position).getSortLetters().charAt(0);
//    }

//    public void updateListView(ArrayList<DrugItem> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
