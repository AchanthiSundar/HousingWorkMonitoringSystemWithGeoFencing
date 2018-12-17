package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import Util.PlaceDataSQL;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedPendingWork extends Activity implements OnClickListener{
	
	ImageView home, back;
	TextView service_provider;
	String  workId,schemeId,finYear,workGroupId,workTypeId,currentStageOfWork;
	private static PlaceDataSQL placeData;
	
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.detailed_pending_work);
	    
	    
	    placeData = new PlaceDataSQL(this);
		SQLiteDatabase db = placeData.getWritableDatabase();
		
		
		Button clickButton = (Button) findViewById(R.id.button1);
		clickButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(DetailedPendingWork.this,
						PendingWorkDetailSpinner.class);
				in.putExtra("workId", workId);
				in.putExtra("schemeId", schemeId);
				in.putExtra("fin_year", finYear);
				in.putExtra("workGroupId", workGroupId);
				in.putExtra("workTypeId", workTypeId);
				in.putExtra("currentStageOfWork", currentStageOfWork);
				startActivity(in);
				// finish();
			}
		});
        
	    initialize();
	    getPendingWork();
	    setServiceProvider();
	   
	}

	private void setServiceProvider() {
		
		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {
			String ser = cursors.getString(6);
			// d = cursors.getString(3);
			// b = cursors.getString(4);
			service_provider.setText(ser);
		}
		//cursors.close();
	}
	private void getPendingWork() {

		Intent in = getIntent();       
	     // Get XML values from previous intent
		 workId = in.getStringExtra(VillageList.KEY_WORK_ID);
		 String schemeGroupName = in.getStringExtra(VillageList.KEY_SCHEME_GROUP_NAME);
		 schemeId = in.getStringExtra(VillageList.KEY_SCHEME_ID);
		 String scheme = in.getStringExtra(VillageList.KEY_SCHEME);
		 finYear = in.getStringExtra(VillageList.KEY_FINANCIAL_YEAR);
		 String agencyName = in.getStringExtra(VillageList.KEY_AGENCY_NAME);
		 String workGroupName = in.getStringExtra(VillageList.KEY_WORK_GROUP_NAME);
		 workGroupId = in.getStringExtra(VillageList.KEY_WORK_GROUP_ID);
		 String WorkName = in.getStringExtra(VillageList.KEY_WORK_NAME);
		 workTypeId = in.getStringExtra(VillageList.KEY_WORK_TYPE_ID);
		 String block = in.getStringExtra(VillageList.KEY_BLOCK);
		 String village = in.getStringExtra(VillageList.KEY_VILLAGE);
		 String stageName = in.getStringExtra(VillageList.KEY_STAGE_NAME);
		 currentStageOfWork = in.getStringExtra(VillageList.KEY_CURRENT_STAGE_OF_WORK);
		 String lastvisiteddate = in.getStringExtra(VillageList.KEY_LAST_VISITED_DATE);
		 
		 
		TextView tv_work_id = (TextView) findViewById(R.id.tv_workId);
		TextView tv_schemeGroupName = (TextView) findViewById(R.id.tv_schemeGroupNmae);
		TextView tv_scheme = (TextView) findViewById(R.id.tv_scheme);
		TextView tv_financialYear = (TextView) findViewById(R.id.tv_financialYear);
		TextView tv_agencyName = (TextView) findViewById(R.id.tv_agencyName);
		TextView tv_workGroupName = (TextView) findViewById(R.id.tv_workGroupName);
		TextView tv_workName = (TextView) findViewById(R.id.tv_workName);
		TextView tv_block = (TextView) findViewById(R.id.tv_block);
		TextView tv_village = (TextView) findViewById(R.id.tv_village);
		TextView tv_stageName = (TextView) findViewById(R.id.tv_currentStage);
		TextView tv_lastVistedDate = (TextView) findViewById(R.id.tv_lastVisitedDate);
		
		tv_work_id.setText(workId);
		tv_schemeGroupName.setText(schemeGroupName);
		tv_scheme.setText(scheme);
		tv_financialYear.setText(finYear);
		tv_agencyName.setText(agencyName);
		tv_workGroupName.setText(workGroupName);
		tv_workName.setText(WorkName);
		tv_block.setText(block);
		tv_village.setText(village);
		tv_stageName.setText(stageName);
		tv_lastVistedDate.setText(lastvisiteddate);
		
	}

	private void initialize() {
		home = (ImageView) findViewById(R.id.homeimg);
		home.setOnClickListener(this);

		back = (ImageView) findViewById(R.id.backimg);
		back.setOnClickListener(this);

		service_provider = (TextView) findViewById(R.id.footertxt);

	}

	public void onClick(View v) {
		if (v.equals(home)) {
			Intent Pendingwork = new Intent(DetailedPendingWork.this,
					Dashboard.class);
			Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(Pendingwork);
			finish();
			super.onBackPressed();
			overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
		}

		if (v.equals(back)) {
			onBackPress();
		}
	}

	public void onBackPress(){
		super.onBackPressed();
		setResult(Activity.RESULT_CANCELED);
		overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
	}
	private Cursor getRawEvents(String sql, String string) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
