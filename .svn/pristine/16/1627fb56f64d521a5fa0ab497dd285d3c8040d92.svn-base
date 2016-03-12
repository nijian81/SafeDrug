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
 * 添加药品页面，进入输入药品名称页面，对应的药品显示适配器
 * 
 */
public class AddDrugItemAdapter extends BaseAdapter implements SectionIndexer {

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

		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.add_drug_item, null);
		viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.desc);
		viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
		viewHolder.tvTitle.setText(this.list.get(position).getDrugName());
		viewHolder.tvDesc.setText(this.list.get(position).getDrugDescription());
		
		return convertView;
	}

	final static class ViewHolder {
		TextView tvDesc;
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
