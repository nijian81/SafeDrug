package cn.com.phhc.drugSafeApp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import cn.com.phhc.drugSafeApp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/3/9. 药品查询页面，进入药品详情页面，点击选择更多之后，弹出的公司展示项
 */
public class CompanyItemAdapter extends BaseAdapter implements SectionIndexer {

	ArrayList<CompanyItem> list;
	Context context;

	@Override
	public int getCount() {

		return list.size();

	}

	@Override
	public CompanyItem getItem(int position) {

		CompanyItem companyItem = list.get(position);

		return companyItem;
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
				R.layout.company_item, null);
		viewHolder.tvName = (TextView) convertView.findViewById(R.id.company);
		viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.desc);
		viewHolder.tvRelativeLayout = (RelativeLayout)convertView.findViewById(R.id.relative);
		if(this.list.get(position).getFlag() == 1){
			viewHolder.tvRelativeLayout.setBackgroundColor(Color.rgb(23, 107, 92));
		}
		viewHolder.tvName.setText(this.list.get(position).getName());
		viewHolder.tvDesc.setText(this.list.get(position).getDesc());

		return convertView;
	}

	final static class ViewHolder {
		TextView tvName;
		TextView tvDesc;
		RelativeLayout tvRelativeLayout;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setArrayList(ArrayList<CompanyItem> list) {
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

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}
