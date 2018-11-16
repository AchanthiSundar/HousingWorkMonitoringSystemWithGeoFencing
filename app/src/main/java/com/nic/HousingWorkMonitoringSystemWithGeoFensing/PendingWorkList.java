package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PendingWorkList extends Activity {
	
	ArrayList<HashMap<String, String>> menuItems;
	IndexableListView list_view;
	TextView service_provider;
	Spinner sp_scheme,sp_finYear;
	String arr_scheme[],arr_financial_year[];
	String selectedScheme="",selectedfinYear="";
	String mode="";
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pending_work_list);

		initialise();
		viewScheme();
		viewFinyear();
		ViewPendingWorkList();
		
		sp_scheme.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {				
				if (position >= 1) {
					selectedScheme = arr_scheme[position];
					ViewPendingWorkList();
				}else {
					ViewPendingWorkList();
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		sp_finYear.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {				
				if (position >= 1) {
					selectedfinYear = arr_financial_year[position];
					ViewPendingWorkList();
					
				}else {
					ViewPendingWorkList();
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	}

	
	private void viewScheme() {
		Cursor cursors = getRawEvents("SELECT DISTINCT(scheme) FROM pendingworks",null);
		arr_scheme = new String[cursors.getCount()+1];
		arr_scheme[0] = "-- All Schemes --";
		int i=1;
		while (cursors.moveToNext()) {
			String scheme = cursors.getString(0);
			arr_scheme[i] = scheme;
			i++;
		}
		sp_scheme.setAdapter(new MyAdapter(PendingWorkList.this, R.layout.spinner_value,arr_scheme));
	}

	private void viewFinyear() {
		Cursor cursors = getRawEvents("SELECT DISTINCT(financialyear) FROM pendingworks",null);
		arr_financial_year = new String[cursors.getCount()+1];
		arr_financial_year[0] = "-- All Financial Year --";
		int i=1;
		while (cursors.moveToNext()) {
			String finyear = cursors.getString(0);
			arr_financial_year[i] = finyear;
			i++;
		}
		sp_finYear.setAdapter(new MyAdapter(PendingWorkList.this, R.layout.spinner_value,arr_financial_year));
	}
	
	private void initialise() {
		service_provider = (TextView) findViewById(R.id.footertxt);
		sp_scheme = (Spinner)findViewById(R.id.sp_scheme);
		sp_finYear = (Spinner)findViewById(R.id.sp_finYear);
		list_view = (IndexableListView) findViewById(R.id.list);
		
		Intent in = getIntent();
		mode = in.getStringExtra("mode");
		Log.i("mode", mode);
	}

	private void ViewPendingWorkList() {
		Intent in = getIntent();
		String vcode = in.getStringExtra("vcode");
		Cursor cursors1 = null;
		if(selectedScheme.equals("") && selectedfinYear.equals("")){
			String whereArgs[] = {vcode};
			cursors1 = getRawEventWhere("SELECT * FROM pendingworks WHERE villagecode=?", whereArgs);
		} else if(!selectedScheme.equals("") && selectedfinYear.equals("")){
			String whereArgs[] = {vcode,selectedScheme};
			cursors1 = getRawEventWhere("SELECT * FROM pendingworks WHERE villagecode=? AND scheme = ?", whereArgs);
		} else if(selectedScheme.equals("") && !selectedfinYear.equals("")){
			String whereArgs[] = {vcode,selectedfinYear};
			cursors1 = getRawEventWhere("SELECT * FROM pendingworks WHERE villagecode=? AND financialyear = ?", whereArgs);
		}else if(!(selectedScheme.equals("") && selectedfinYear.equals(""))){
			String whereArgs[] = {vcode,selectedScheme,selectedfinYear};
			cursors1 = getRawEventWhere("SELECT * FROM pendingworks WHERE villagecode=? AND scheme = ? AND financialyear = ?", whereArgs);
		}
		menuItems = new ArrayList<HashMap<String, String>>();
		menuItems.clear();
		while (cursors1.moveToNext()) {
			String id = cursors1.getString(0);
			String workid = cursors1.getString(1);
			String schemegroupname = cursors1.getString(2);
			String schemeid = cursors1.getString(3);
			String scheme = cursors1.getString(4);
			String fyear = cursors1.getString(5);
			String agencyname = cursors1.getString(6);
			String workgroupname = cursors1.getString(7);
			String workgroupid = cursors1.getString(8);
			String workname = cursors1.getString(9);
			String worktypeid = cursors1.getString(10);
			String block = cursors1.getString(11);
			String village = cursors1.getString(12);
			String villagecode = cursors1.getString(13);
			String stagename = cursors1.getString(14);
			String currentstageofwork = cursors1.getString(15);
			String beneficiaryname = cursors1.getString(16);
			String beneficiaryfhname = cursors1.getString(17);
			String worktypname = cursors1.getString(18);
			
			String beneficiarygender = cursors1.getString(19);
			String beneficiarycommunity = cursors1.getString(20);
			String initalamount = cursors1.getString(21);
			String amountspentsofar = cursors1.getString(22);
			
			String update = cursors1.getString(23);

			
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(VillageList.KEY_ID, id);
			map.put(VillageList.KEY_WORK_ID, workid);
			map.put(VillageList.KEY_SCHEME_GROUP_NAME, schemegroupname);
			map.put(VillageList.KEY_SCHEME_ID, schemeid);
			map.put(VillageList.KEY_SCHEME, scheme);
			map.put(VillageList.KEY_FINANCIAL_YEAR, fyear);
			map.put(VillageList.KEY_AGENCY_NAME, agencyname);
			map.put(VillageList.KEY_WORK_GROUP_NAME, workgroupname);
			map.put(VillageList.KEY_WORK_GROUP_ID, workgroupid);
			map.put(VillageList.KEY_WORK_NAME, workname);
			map.put(VillageList.KEY_WORK_TYPE_ID, worktypeid);
			map.put(VillageList.KEY_BLOCK, block);
			map.put(VillageList.KEY_VILLAGE, village);
			map.put(VillageList.KEY_VILLAGE_CODE, villagecode);
			map.put(VillageList.KEY_STAGE_NAME, stagename);
			map.put(VillageList.KEY_CURRENT_STAGE_OF_WORK, currentstageofwork);
			map.put(VillageList.KEY_BENEFICIARY_NAME, beneficiaryname);
			map.put(VillageList.KEY_BENEFICIARY_FATHER_NAME, beneficiaryfhname);
			map.put(VillageList.KEY_WORK_TYPE_NAME, worktypname);
			
			map.put(VillageList.KEY_BENEFICIARY_GENDER, beneficiarygender);
			map.put(VillageList.KEY_BENEFICIARY_COMMUNITY, beneficiarycommunity);
			map.put(VillageList.KEY_INITIAL_AMOUNT, initalamount);
			map.put(VillageList.KEY_AMOUNT_SPENT_SO_FAR, amountspentsofar);
			
			map.put(VillageList.KEY_LAST_VISITED_DATE, update);

			// adding HashList to ArrayList
			
			menuItems.add(map);
		}
		//cursors1.close();
		 Collections.sort(menuItems, new Comparator<HashMap< String,String >>() {

		        @Override
		        public int compare(HashMap<String, String> lhs,
		                HashMap<String, String> rhs) {
		            // Do your comparison logic here and retrn accordingly.
		            return lhs.get(VillageList.KEY_BENEFICIARY_NAME).compareTo(rhs.get(VillageList.KEY_BENEFICIARY_NAME));
		        }
		    });
		listView();

	}
	
   
	private void listView() {
//		ColorDrawable divcolor = new ColorDrawable(Color.DKGRAY);
//		list_view.setDivider(divcolor);
//		list_view.setDividerHeight(0);
//		list_view.setSmoothScrollbarEnabled(true);
//		list_view.setFooterDividersEnabled(true);
        list_view.setFastScrollEnabled(true);
		PendingWorkListAdapter lia = new PendingWorkListAdapter(this, menuItems);
		list_view.setAdapter(lia);

		list_view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
/*
				TextView tv_id = (TextView)v.findViewById(R.id.id);
				TextView tv_work_id = (TextView)v.findViewById(R.id.workid);
				TextView tv_schemeGroupName = (TextView)v.findViewById(R.id.schemegrouppname);
				TextView tv_scheme = (TextView)v.findViewById(R.id.scheme);
				TextView tv_financialYear = (TextView)v.findViewById(R.id.fyear);
				TextView tv_agencyName = (TextView)v.findViewById(R.id.agency);
				TextView tv_workGroupName = (TextView)v.findViewById(R.id.workgroupname);
				TextView tv_workGroupId = (TextView)v.findViewById(R.id.workgroupid);
				TextView tv_workName = (TextView)v.findViewById(R.id.workname);
				TextView tv_workTypeId = (TextView)v.findViewById(R.id.worktypeid);
				TextView tv_block = (TextView)v.findViewById(R.id.block);
				TextView tv_village = (TextView)v.findViewById(R.id.village);
				TextView tv_villageCode = (TextView)v.findViewById(R.id.villagecode);
				TextView tv_stageName = (TextView)v.findViewById(R.id.stagename);
				TextView tv_currentStageOfWork = (TextView)v.findViewById(R.id.currentstageofwork);
				TextView tv_beneficiaryname = (TextView)v.findViewById(R.id.beneficiaryName);
				TextView tv_beneficiaryfhname = (TextView)v.findViewById(R.id.beneficiaryFatherName);
				TextView tv_worktypname = (TextView)v.findViewById(R.id.workTypeName);
				
				TextView tv_gender = (TextView)v.findViewById(R.id.tv_gender);
				TextView tv_community = (TextView)v.findViewById(R.id.tv_community);
				TextView tv_initialAmount = (TextView)v.findViewById(R.id.tv_initialAmount);
				TextView tv_amountSpentSofar = (TextView)v.findViewById(R.id.tv_amountSpentSoFar);
				
				TextView tv_lastVisitedDate = (TextView)v.findViewById(R.id.last_visited_date);*/

				String work_id = menuItems.get(position).get(VillageList.KEY_WORK_ID).toString();//tv_work_id.getText().toString();
			//	String schemeGroupName = tv_schemeGroupName.getText().toString();
				String schemeid = menuItems.get(position).get(VillageList.KEY_SCHEME_ID).toString();//tv_schemeid.getText().toString();
			//	String scheme = tv_scheme.getText().toString();
				String financialYear = menuItems.get(position).get(VillageList.KEY_FINANCIAL_YEAR).toString();//tv_financialYear.getText().toString();
//				String agencyName = tv_agencyName.getText().toString();
//				String workGroupName = tv_workGroupName.getText().toString();
				String workGroupId = menuItems.get(position).get(VillageList.KEY_WORK_GROUP_ID).toString();//tv_workGroupId.getText().toString();
				//String workName = tv_workName.getText().toString();
				String workTypeId = menuItems.get(position).get(VillageList.KEY_WORK_TYPE_ID).toString();//tv_workTypeId.getText().toString();
//				String block = tv_block.getText().toString();
//				String village = tv_village.getText().toString();
//				String stageName = tv_stageName.getText().toString();
				String currentStageOfWork = menuItems.get(position).get(VillageList.KEY_CURRENT_STAGE_OF_WORK).toString();//tv_currentStageOfWork.getText().toString();
//				String beneficiaryname = tv_beneficiaryname.getText().toString();
//				String beneficiaryfhname = tv_beneficiaryfhname.getText().toString();
//				String worktypname = tv_worktypname.getText().toString();
				
//				String gender = tv_gender.getText().toString();
//				String community = tv_community.getText().toString();
//				String initialAmount = tv_initialAmount.getText().toString();
//				String amountSpentSoFar = tv_amountSpentSofar.getText().toString();
//				
//				String lastvisiteddate = tv_lastVisitedDate.getText().toString();


		
				Intent in = new Intent(PendingWorkList.this,PendingWorkDetailSpinner.class);
				in.putExtra("workId", work_id);
				in.putExtra("schemeId", schemeid);
				in.putExtra("fin_year", financialYear);
				in.putExtra("workGroupId", workGroupId);
				in.putExtra("workTypeId", workTypeId);
				in.putExtra("currentStageOfWork", currentStageOfWork);
				startActivity(in);
				
				
				/*Intent in = new Intent(getApplicationContext(),DetailedPendingWork.class);
				
				in.putExtra(VillageList.KEY_WORK_ID, work_id);
				in.putExtra(VillageList.KEY_SCHEME_GROUP_NAME, schemeGroupName);
				in.putExtra(VillageList.KEY_SCHEME_ID, schemeid);
				in.putExtra(VillageList.KEY_SCHEME, scheme);
				in.putExtra(VillageList.KEY_FINANCIAL_YEAR, financialYear);
				in.putExtra(VillageList.KEY_AGENCY_NAME, agencyName);
				in.putExtra(VillageList.KEY_WORK_GROUP_NAME, workGroupName);
				in.putExtra(VillageList.KEY_WORK_GROUP_ID, workGroupId);
				in.putExtra(VillageList.KEY_WORK_NAME, workName);
				in.putExtra(VillageList.KEY_WORK_TYPE_ID, workTypeId);
				in.putExtra(VillageList.KEY_BLOCK, block);
				in.putExtra(VillageList.KEY_VILLAGE, village);
				in.putExtra(VillageList.KEY_STAGE_NAME, stageName);
				in.putExtra(VillageList.KEY_CURRENT_STAGE_OF_WORK, currentStageOfWork);
				in.putExtra(VillageList.KEY_BENEFICIARY_NAME, beneficiaryname);
				in.putExtra(VillageList.KEY_BENEFICIARY_FATHER_NAME, beneficiaryfhname);
				in.putExtra(VillageList.KEY_WORK_TYPE_NAME, worktypname);
				
				in.putExtra(VillageList.KEY_BENEFICIARY_GENDER, gender);
				in.putExtra(VillageList.KEY_BENEFICIARY_COMMUNITY, community);
				in.putExtra(VillageList.KEY_INITIAL_AMOUNT, initialAmount);
				in.putExtra(VillageList.KEY_AMOUNT_SPENT_SO_FAR, amountSpentSoFar);
				
				in.putExtra(VillageList.KEY_LAST_VISITED_DATE, lastvisiteddate);

				startActivity(in);*/
				//finish();

			}
		});
		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {
			String ser = cursors.getString(6);
			// d = cursors.getString(3);
			// b = cursors.getString(4);
			service_provider.setText(ser);
		}
		//cursors.close();
	}


	private Cursor getRawEvents(String sql, String string) {
		Cursor cursor = LoginScreen.db.rawQuery(sql, null);
		startManagingCursor(cursor);
		return cursor;
	}
	
	private Cursor getRawEventWhere(String sql, String[] string) {
		Cursor cursor = LoginScreen.db.rawQuery(sql, string);

		startManagingCursor(cursor);
		return cursor;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mode.equalsIgnoreCase("manual")){
				Intent Pendingwork = new Intent(PendingWorkList.this,
						VillageList.class);
				Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(Pendingwork);
				finish();
				
			}else{
				finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
}
