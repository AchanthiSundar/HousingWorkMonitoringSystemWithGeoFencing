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
import android.widget.SectionIndexer;
import android.widget.TextView;

public class PendingWorkListAdapter extends BaseAdapter implements SectionIndexer{
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Context ladp;
	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, String>> mList;
	public PendingWorkListAdapter(Context context,
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

				convertView = mInflater.inflate(R.layout.pending_work_listitem,null);
				holder = new ViewHolder();

				//holder.id = (TextView) convertView.findViewById(R.id.id);
				holder.workid = (TextView) convertView.findViewById(R.id.workid);
				holder.schemegrouppname = (TextView) convertView.findViewById(R.id.tv_schemeGroupName);

				//holder.schemeid = (TextView) convertView.findViewById(R.id.schemeid);
				holder.scheme = (TextView) convertView.findViewById(R.id.tv_scheme);
				holder.financialyear = (TextView) convertView.findViewById(R.id.tv_financialYear);
				holder.agencyname = (TextView) convertView.findViewById(R.id.tv_agencyName);

				holder.workgroupname = (TextView) convertView.findViewById(R.id.tv_workGroupName);
				//holder.workgroupid = (TextView) convertView.findViewById(R.id.workgroupid);
				holder.workname = (TextView) convertView.findViewById(R.id.tv_workName);

				//holder.worktypeid = (TextView) convertView.findViewById(R.id.worktypeid);
				holder.block = (TextView) convertView.findViewById(R.id.tv_block);
				holder.village = (TextView) convertView.findViewById(R.id.tv_village);

				//holder.villagecode = (TextView) convertView.findViewById(R.id.villagecode);
				//holder.stagename = (TextView) convertView.findViewById(R.id.stagename);
				
				holder.currentstageofwork = (TextView) convertView.findViewById(R.id.tv_currentStage);
				
				//holder.beneficiaryname = (TextView) convertView.findViewById(R.id.beneficiaryName);
				holder.beneficiaryfhname = (TextView) convertView.findViewById(R.id.beneficiaryFatherName);
				//holder.worktypname = (TextView) convertView.findViewById(R.id.workTypeName);
				
				holder.beneficiarygender = (TextView) convertView.findViewById(R.id.tv_gender);
				holder.beneficiarycommunity = (TextView) convertView.findViewById(R.id.tv_community);
				holder.initalamount = (TextView) convertView.findViewById(R.id.tv_initialAmount);
				holder.amountspentsofar = (TextView) convertView.findViewById(R.id.tv_amountSpentSoFar);
				
				holder.lastvisitdate = (TextView) convertView.findViewById(R.id.last_visited_date);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

//			holder.id.setText(mList.get(position).get(VillageList.KEY_ID).toString());
//			holder.id.setTypeface(Typeface.SERIF);

			holder.workid.setText(mList.get(position).get(VillageList.KEY_WORK_ID).toString()+" ("+
					mList.get(position).get(VillageList.KEY_BENEFICIARY_NAME).toString()+")");
			holder.workid.setTypeface(Typeface.SERIF);

			holder.schemegrouppname.setText(mList.get(position).get(VillageList.KEY_SCHEME_GROUP_NAME).toString());
			holder.schemegrouppname.setTypeface(Typeface.SERIF);

			//holder.schemeid.setText(mList.get(position).get(VillageList.KEY_SCHEME_ID).toString());
			holder.scheme.setText(mList.get(position).get(VillageList.KEY_SCHEME).toString());
			holder.scheme.setTypeface(Typeface.SERIF);

			holder.financialyear.setText(mList.get(position).get(VillageList.KEY_FINANCIAL_YEAR).toString());
			holder.financialyear.setTypeface(Typeface.SERIF);

			holder.agencyname.setText(mList.get(position).get(VillageList.KEY_AGENCY_NAME).toString());
			holder.agencyname.setTypeface(Typeface.SERIF);

			holder.workgroupname.setText(mList.get(position).get(VillageList.KEY_WORK_GROUP_NAME).toString());
			holder.workgroupname.setTypeface(Typeface.SERIF);

//			holder.workgroupid.setText(mList.get(position).get(VillageList.KEY_WORK_GROUP_ID).toString());
//			holder.workgroupid.setTypeface(Typeface.SERIF);

			holder.workname.setText(mList.get(position).get(VillageList.KEY_WORK_NAME).toString());
			holder.workname.setTypeface(Typeface.SERIF);

//			holder.worktypeid.setText(mList.get(position).get(VillageList.KEY_WORK_TYPE_ID).toString());
//			holder.worktypeid.setTypeface(Typeface.SERIF);

			holder.block.setText(mList.get(position).get(VillageList.KEY_BLOCK).toString());
			holder.block.setTypeface(Typeface.SERIF);

			holder.village.setText(mList.get(position).get(VillageList.KEY_VILLAGE).toString());
			holder.village.setTypeface(Typeface.SERIF);

//			holder.villagecode.setText(mList.get(position).get(VillageList.KEY_VILLAGE_CODE).toString());
//			holder.villagecode.setTypeface(Typeface.SERIF);

//			holder.stagename.setText(mList.get(position).get(VillageList.KEY_STAGE_NAME).toString());
//			holder.stagename.setTypeface(Typeface.SERIF);

			holder.currentstageofwork.setText(mList.get(position).get(VillageList.KEY_STAGE_NAME).toString());
			holder.currentstageofwork.setTypeface(Typeface.SERIF);
			
//			holder.beneficiaryname.setText(mList.get(position).get(VillageList.KEY_BENEFICIARY_NAME).toString());
//			holder.beneficiaryname.setTypeface(Typeface.SERIF);
			String benficiaryName = mList.get(position).get(VillageList.KEY_BENEFICIARY_NAME).toString()+"  "+
					                mList.get(position).get(VillageList.KEY_BENEFICIARY_FATHER_NAME).toString();
			holder.beneficiaryfhname.setText(benficiaryName);
			holder.beneficiaryfhname.setTypeface(Typeface.SERIF);
			
//			holder.worktypname.setText(mList.get(position).get(VillageList.KEY_WORK_TYPE_NAME).toString());
//			holder.worktypname.setTypeface(Typeface.SERIF);
			
			holder.beneficiarygender.setText(mList.get(position).get(VillageList.KEY_BENEFICIARY_GENDER).toString());
			holder.beneficiarygender.setTypeface(Typeface.SERIF);
			
			holder.beneficiarycommunity.setText(mList.get(position).get(VillageList.KEY_BENEFICIARY_COMMUNITY).toString());
			holder.beneficiarycommunity.setTypeface(Typeface.SERIF);
			
			holder.initalamount.setText("Rs."+mList.get(position).get(VillageList.KEY_INITIAL_AMOUNT).toString());
			holder.initalamount.setTypeface(Typeface.SERIF);
			
			holder.amountspentsofar.setText("Rs."+mList.get(position).get(VillageList.KEY_AMOUNT_SPENT_SO_FAR).toString());
			holder.amountspentsofar.setTypeface(Typeface.SERIF);
			
			holder.lastvisitdate.setText(mList.get(position).get(VillageList.KEY_LAST_VISITED_DATE).toString());
			holder.lastvisitdate.setTypeface(Typeface.SERIF);

			return convertView;
		}

		catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return null;
	}

	private class ViewHolder {
	//	TextView id;
		TextView workid;
		TextView schemegrouppname;
	//	TextView schemeid;
		TextView scheme;
		TextView financialyear;
		TextView agencyname;
		TextView workgroupname;
	//	TextView workgroupid;
		TextView workname;
	//	TextView worktypeid;
		TextView block;
		TextView village;
	//	TextView villagecode;
	//	TextView stagename;
		TextView currentstageofwork;
	//	TextView beneficiaryname;
		TextView beneficiaryfhname;
		//TextView worktypname;
		TextView beneficiarygender;
		TextView beneficiarycommunity;
		TextView initalamount;
		TextView amountspentsofar;
		TextView lastvisitdate;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		String[] sections = new String[mSections.length()];

		for (int i = 0; i < mSections.length(); i++)

		sections[i] = String.valueOf(mSections.charAt(i));

		return sections;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		// If there is no item for current section, previous section will be

		// selected

		for (int i = section; i >= 0; i--) {

		for (int j = 0; j < getCount(); j++) {

		if (i == 0) {

		// For numeric section

		for (int k = 0; k <= 9; k++) {

		if (StringMatcher.match(String.valueOf(mList.get(j).get(VillageList.KEY_BENEFICIARY_NAME).charAt(0)), String.valueOf(k)))

		return j;

		}

		} else {if (StringMatcher.match(String.valueOf(mList.get(j).get(VillageList.KEY_BENEFICIARY_NAME).charAt(0)), String.valueOf(mSections.charAt(i))))

	return j;

	}

	}

	}

	return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
