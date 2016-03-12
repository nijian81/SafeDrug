package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/3/9.
 */
public class DrugItemAdapter extends BaseAdapter implements SectionIndexer {

	ArrayList<DrugItem> list;
	Context context;

	@Override
	public int getCount() {

		return list.size();

	}

	@Override
	public DrugItem getItem(int position) {

		DrugItem drugItem = list.get(position);

		return drugItem;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.drug_item, null);
			viewHolder.tvLetter = (TextView) convertView
					.findViewById(R.id.catalog);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DrugItem drugItem = getItem(position);
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			if (!drugItem.getSortLetters().matches("[0-9]")) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(drugItem.getSortLetters());
			}else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getDrugName());

		return convertView;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setArrayList(ArrayList<DrugItem> list) {
		this.list = list;
	}

	@Override
	public Object[] getSections() {
		return new Object[0];
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	public void updateListView(ArrayList<DrugItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}