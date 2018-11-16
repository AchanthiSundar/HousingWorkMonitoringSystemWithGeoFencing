package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VillageListAdapter extends BaseAdapter {

	Context ladp;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>> mList;
	static final String KEY_VCODE = "vcode";
	static final String KEY_VNAME_ENG = "vnameEng";
	static final String KEY_VNAME_TAMIL = "vnameTamil";

	public VillageListAdapter(Context context,
			ArrayList<HashMap<String, String>> menuItems) {
		mInflater = LayoutInflater.from(context);
		mList = menuItems;
		this.ladp = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.village_list_item,
						null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.block);
				holder.text1 = (TextView) convertView
						.findViewById(R.id.list_villageName_English);
				holder.text2 = (TextView) convertView
						.findViewById(R.id.list_villageName_Tamil);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Typeface fontface = Typeface.createFromAsset(ladp.getAssets(),
					"fonts/mylai.ttf");

			holder.text.setText(mList.get(position).get(KEY_VCODE).toString());
			holder.text.setTypeface(Typeface.SERIF);

			holder.text1.setText(mList.get(position).get(KEY_VNAME_ENG)
					.toString());
			holder.text.setTypeface(Typeface.SERIF);

			holder.text2.setTypeface(fontface, Typeface.BOLD);
			holder.text2.setText(UnicodeUtil.unicode2tsc(mList.get(position)
					.get(KEY_VNAME_TAMIL).toString()));

			return convertView;
		}

		catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return null;
	}

	private class ViewHolder {
		TextView text;
		TextView text1;
		TextView text2;
	}

}
