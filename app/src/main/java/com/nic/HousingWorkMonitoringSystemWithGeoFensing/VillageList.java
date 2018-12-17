package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import Util.NetworkUtil;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VillageList extends Activity implements View.OnClickListener {

	static final String KEY_VCODE = "vcode";
	static final String KEY_VNAME_ENG = "vnameEng";
	static final String KEY_VNAME_TAMIL = "vnameTamil";
	
	static final String KEY_ID = "id";
	static final String KEY_WORK_ID = "workid";
	static final String KEY_SCHEME_GROUP_NAME = "schemegrouppname";
	static final String KEY_SCHEME = "scheme";
	static final String KEY_SCHEME_ID = "schemeid";
	static final String KEY_FINANCIAL_YEAR = "financialyear";
	static final String KEY_AGENCY_NAME = "agencyname";
	static final String KEY_WORK_GROUP_NAME = "workgroupname";
	static final String KEY_WORK_GROUP_ID = "workgroupid";
	static final String KEY_WORK_NAME = "workname";
	static final String KEY_WORK_TYPE_ID = "worktypeid";
	static final String KEY_BLOCK = "block";
	static final String KEY_VILLAGE = "village";	
	static final String KEY_VILLAGE_CODE = "villagecode";
	static final String KEY_STAGE_NAME = "stagename";
	static final String KEY_CURRENT_STAGE_OF_WORK = "currentstageofwork";
	static final String KEY_BENEFICIARY_NAME = "beneficiaryname";
	static final String KEY_BENEFICIARY_FATHER_NAME = "beneficiaryfhname";
	static final String KEY_WORK_TYPE_NAME = "worktypname";
	static final String KEY_BENEFICIARY_GENDER = "beneficiarygender";
	static final String KEY_BENEFICIARY_COMMUNITY = "beneficiarycommunity";
	static final String KEY_INITIAL_AMOUNT = "initalamount";
	static final String KEY_AMOUNT_SPENT_SO_FAR = "amountspentsofar";
	static final String KEY_LAST_VISITED_DATE = "upddate";
	
	ListView list_view;
	ImageView home_img,back_img;
	TextView service_provider,title;
	EditText ed_searchVillage;
	ArrayList<HashMap<String, String>> menuItems;
	SitesListPendingWorkList sitesListPendingWorkList;
	String response1;
	InputSource is;
	String d,b,pv;
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	ArrayList<String> al_param = new ArrayList<String>();
	ArrayList<String> al_param_value = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.village_list);

		service_provider = (TextView) findViewById(R.id.footertxt);
		ed_searchVillage = (EditText) findViewById(R.id.ed_searchVillage);
		ed_searchVillage.addTextChangedListener(new offTextWatcher(ed_searchVillage));
		home_img = (ImageView) findViewById(R.id.homeimg);
		home_img.setOnClickListener(this);
		back_img = (ImageView)findViewById(R.id.backimg);
		title = (TextView)findViewById(R.id.title_tv);
		title.setText("VillageList");
		back_img.setOnClickListener(this);

		if(NetworkUtil.isNetworkAvailable(getApplicationContext())){
			viewVillageList();
		}else{
			Cursor cursors1 = getRawEvents("select workid from workIdForOffLine", null);
			if(cursors1.getCount() != 0){
				menuItems = new ArrayList<HashMap<String, String>>();
				menuItems.clear();
				villageListOfflineMode();
			}else{
				Toast.makeText(getApplicationContext(), "Internet Connection is not Available..", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void villageListOfflineMode() {
		Cursor cursors1 = getRawEvents("SELECT DISTINCT(vcode) FROM workIdForOffLine", null);
		while (cursors1.moveToNext()) {
			String pvcode = cursors1.getString(0);
			
			Cursor cursors2 = getRawEvents1("SELECT * FROM village WHERE villagecode=?", pvcode);
			cursors2.moveToFirst();
			String vcode = cursors2.getString(0);
			String vname_eng = cursors2.getString(1);
			String vname_tamil = cursors2.getString(2);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_VCODE, vcode);
			map.put(KEY_VNAME_ENG, vname_eng);
			map.put(KEY_VNAME_TAMIL, vname_tamil);

			// adding HashList to ArrayList
			menuItems.add(map);
		}
		cursors1.close();
		listView();
	}

	private void viewVillageList() {

		Cursor cursors1 = getRawEvents("select * from village", null);
		menuItems = new ArrayList<HashMap<String, String>>();
		while (cursors1.moveToNext()) {

			String vcode = cursors1.getString(0);
			String vname_eng = cursors1.getString(1);
			String vname_tamil = cursors1.getString(2);

			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(KEY_VCODE, vcode);
			map.put(KEY_VNAME_ENG, vname_eng);
			map.put(KEY_VNAME_TAMIL, vname_tamil);

			// adding HashList to ArrayList
			menuItems.add(map);
		}
		cursors1.close();

		listView();
	}

	private void listView() {
		list_view = (ListView) findViewById(R.id.list);


		ColorDrawable divcolor = new ColorDrawable(Color.DKGRAY);
		list_view.setDivider(getResources().getDrawable(R.drawable.divider));
		list_view.setDividerHeight(1);
		list_view.setSmoothScrollbarEnabled(true);
		list_view.setFooterDividersEnabled(true);

		VillageListAdapter lia = new VillageListAdapter(this, menuItems);
		list_view.setAdapter(lia);

		list_view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {				
				pv = menuItems.get(position).get(KEY_VCODE).toString();
				Cursor cursors1 = getRawEvents1("SELECT * FROM pendingworks WHERE villagecode=?", pv);
				if(cursors1.getCount() != 0){
					Intent in = new Intent(VillageList.this, PendingWorkList.class);
					in.putExtra("vcode", pv);
					in.putExtra("mode", "manual");
					startActivity(in);
				}else{
					if(NetworkUtil.isNetworkAvailable(getApplicationContext())){
						PostVillageId(d,b,pv);
					}else{
						Toast.makeText(getApplicationContext(), "Internet Connection is not Available..", Toast.LENGTH_LONG).show();
					}
				}
				//Log.i("CODE", d+" : "+b+" : "+pv);
			}
		});

		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {
			String ser = cursors.getString(6);
			
			d = cursors.getString(3);
			b = cursors.getString(4);
			service_provider.setText(ser);
		}
		cursors.close();
		
	}

	protected void PostVillageId(String d,String b,String pv) {

		try{
			LoginScreen.db.delete("upcomingstage", null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		LoginScreen.preferences = PreferenceManager.getDefaultSharedPreferences(VillageList.this);
		String user_name = LoginScreen.preferences.getString("user_name","");
		
		SitesListPendingWorkList.Clear();
		al_param.clear();
		al_param_value.clear();
		
		al_param.add("d"); //District code
		al_param.add("b"); //block code
		al_param.add("pv");//village code
		al_param.add("user");//user id
		
		al_param_value.add(d);
		al_param_value.add(b);
		al_param_value.add(pv);
		al_param_value.add(user_name);
		
		PostMethod.param = new String[al_param.size()];
		PostMethod.paramValue = new String[al_param.size()];
		
		for(int i=0;i<al_param.size();i++){
			
			PostMethod.param[i] = al_param.get(i).toString();
			PostMethod.paramValue[i] = al_param_value.get(i).toString();
		}
		
		PoastVillageIdTask task = new PoastVillageIdTask();
		task.execute();
	}

	private class PoastVillageIdTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		protected String doInBackground(String... urls) {

			try {

				PostMethod po = new PostMethod(getApplicationContext(),
						"https://www.tnrd.gov.in/project/webservices_forms/scheme_monitoring_system.php");

				/*PostMethod po = new PostMethod(getApplicationContext(),
						"http://10.163.14.137/rdwebtraining/project_new/webservices_forms/scheme_monitoring_system.php");*/
				response1 = po.post();
//				System.out.println("Response is : " + response1);
				Log.v("Res:", ""+response1);
				if(response1.equals("DB Error: no such field")){
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}else{
					is = new InputSource();
					is.setCharacterStream(new StringReader(response1));

					try {
						/** Handling XML */
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader xr = sp.getXMLReader();

						MyXMLHandlerPendingWorkList myMarkMyXmlHandler = new MyXMLHandlerPendingWorkList();
						xr.setContentHandler(myMarkMyXmlHandler);
						xr.parse(is);

					} catch (Exception e) {
						System.out.println("XML Pasing Excpetion = " + e);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("LOG_TAG", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			
			if(response1.equals("DB Error: no such field")){
				Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();	
			}else if(response1.equals("No record Found")){
				Toast.makeText(getApplicationContext(), response1, Toast.LENGTH_LONG).show();
			}else{
				String id = "",
						workid = "", 
						schemeid="",
						schemegrouppname = "", 
						scheme = "", 
						financialyear = "", 
						agencyname = "", 
						workgroupname = "", 
						workgroupid = "",
						workname = "", 
						worktypeid = "", 
						block = "", 
						village = "", 
						villagecode = "", 
						stagename = "", 
						currentstageofwork = "",
						beneficiaryname = "",
						beneficiaryfhname = "", 
						worktypname = "",
						beneficiarygender = "",
						beneficiarycommunity = "",
						initalamount = "",
						amountspentsofar = "",
						updated_date="";
				
				try{
					sitesListPendingWorkList = MyXMLHandlerPendingWorkList.sitesListPendingWorkList;
					if(sitesListPendingWorkList != null) {
						for (int i = 0; i < sitesListPendingWorkList.getId().size(); i++) {
							id = sitesListPendingWorkList.getId().get(i).toString();
							workid = sitesListPendingWorkList.getWorkId().get(i).toString();
							schemeid = sitesListPendingWorkList.getSchemeId().get(i).toString();
							schemegrouppname = sitesListPendingWorkList.getSchemeGroupName().get(i)
									.toString();
							scheme = sitesListPendingWorkList.getScheme().get(i).toString();
							financialyear = sitesListPendingWorkList.getFinancialYear().get(i)
									.toString();
							agencyname = sitesListPendingWorkList.getAgencyName().get(i).toString();
							workgroupname = sitesListPendingWorkList.getWorkGroupName().get(i)
									.toString();
							workgroupid = sitesListPendingWorkList.getWorkGroupId().get(i).toString();
							workname = sitesListPendingWorkList.getWorkName().get(i).toString();
							worktypeid = sitesListPendingWorkList.getWorkTypeId().get(i).toString();
							block = sitesListPendingWorkList.getBlock().get(i).toString();
							village = sitesListPendingWorkList.getVillage().get(i).toString();
							villagecode = sitesListPendingWorkList.getVillageCode().get(i).toString();
							stagename = sitesListPendingWorkList.getStageName().get(i).toString();
							currentstageofwork = sitesListPendingWorkList.getCurrentStageOfWork()
									.get(i).toString();
							beneficiaryname = sitesListPendingWorkList.getBeneficiaryName().get(i).toString();
							beneficiaryfhname = sitesListPendingWorkList.getBeneficiaryFatherName().get(i).toString();
							worktypname = sitesListPendingWorkList.getWorkTypeName().get(i).toString();
							beneficiarygender = sitesListPendingWorkList.getBeneficiarygender().get(i).toString();
							if (beneficiarygender.equals("1")) {
								beneficiarygender = "Male";
							} else {
								beneficiarygender = "Female";
							}
							beneficiarycommunity = sitesListPendingWorkList.getBeneficiarycommunity().get(i).toString();
							initalamount = sitesListPendingWorkList.getInitialAmount().get(i).toString();
							amountspentsofar = sitesListPendingWorkList.getAmountSpentSoFar().get(i).toString();
							updated_date = sitesListPendingWorkList.getUpdateDate().get(i).toString();

							insertDataValue2(id, workid, schemegrouppname, schemeid, scheme,
									financialyear, agencyname, workgroupname,
									workgroupid, workname, worktypeid, block, village,
									villagecode, stagename, currentstageofwork, beneficiaryname,
									beneficiaryfhname, worktypname, beneficiarygender, beneficiarycommunity,
									initalamount, amountspentsofar,
									updated_date);

							insertDataValue3(workid, villagecode);

							Intent in = new Intent(VillageList.this, PendingWorkList.class);
							in.putExtra("vcode", pv);
							in.putExtra("mode", "manual");
							startActivity(in);
						}
					}
				}catch(Exception e){
					Toast.makeText(getApplicationContext(), "No Record", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}	
		}

		private void insertDataValue3(String workid, String villagecode) {
			ContentValues values;
			values = new ContentValues();
			
			values.put("workid", workid);
			values.put("vcode", villagecode);
			LoginScreen.db.insert("workIdForOffLine", null, values);
		}
	}

	private void insertDataValue2(String id, String workid,
			String schemegrouppname,  String schemeid, String scheme, String financialyear,
			String agencyname, String workgroupname, String workgroupid,
			String workname, String worktypeid, String block, String village,
			String villagecode, String stagename,  String currentstageofwork, 
			String beneficiaryname, String beneficiaryfhname, String worktypname,
			String beneficiarygender,String beneficiarycommunity,
			String initalamount,String amountspentsofar,String updated_date) {
		
		//LoginScreen.db = LoginScreen.placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();
		
		values.put(KEY_ID, id);
		values.put(KEY_WORK_ID, workid);
		values.put(KEY_SCHEME_GROUP_NAME, schemegrouppname);
		values.put(KEY_SCHEME_ID, schemeid);
		values.put(KEY_SCHEME, scheme);
		values.put(KEY_FINANCIAL_YEAR, financialyear);
		values.put(KEY_AGENCY_NAME, agencyname);
		values.put(KEY_WORK_GROUP_NAME, workgroupname);
		values.put(KEY_WORK_GROUP_ID, workgroupid);
		values.put(KEY_WORK_NAME, workname);
		values.put(KEY_WORK_TYPE_ID, worktypeid);
		values.put(KEY_BLOCK, block);
		values.put(KEY_VILLAGE, village);
		values.put(KEY_VILLAGE_CODE, villagecode);
		values.put(KEY_STAGE_NAME, stagename);
		values.put(KEY_CURRENT_STAGE_OF_WORK, currentstageofwork);
		values.put(KEY_BENEFICIARY_NAME, beneficiaryname);
		values.put(KEY_BENEFICIARY_FATHER_NAME, beneficiaryfhname);
		values.put(KEY_WORK_TYPE_NAME, worktypname);
		
		values.put(KEY_BENEFICIARY_GENDER, beneficiarygender);
		values.put(KEY_BENEFICIARY_COMMUNITY, beneficiarycommunity);
		values.put(KEY_INITIAL_AMOUNT, initalamount);
		values.put(KEY_AMOUNT_SPENT_SO_FAR, amountspentsofar);
		
		values.put(KEY_LAST_VISITED_DATE, "");

		LoginScreen.db.insert("pendingworks", null, values);
	}

	private Cursor getRawEvents(String sql, String string) {
		
		Cursor cursor = LoginScreen.db.rawQuery(sql, null);
		startManagingCursor(cursor);
		return cursor;
	}

	private Cursor getRawEvents1(String sql, String string) {
		String[] id = { string };
		Cursor cursor = LoginScreen.db.rawQuery(sql, id);

		startManagingCursor(cursor);
		return cursor;
	}

	private class offTextWatcher implements TextWatcher {
		public offTextWatcher(EditText dl) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			try {
				String village = s.toString() + "%";

				// String[] where = {bank};
				menuItems.clear();
				Cursor cursors1 = getRawEvents1(
						"SELECT * FROM village WHERE villagename LIKE ?",
						village);

				menuItems = new ArrayList<HashMap<String, String>>();
				while (cursors1.moveToNext()) {

					String vcode = cursors1.getString(0);
					String vname_eng = cursors1.getString(1);
					String vname_tamil = cursors1.getString(2);

					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put(KEY_VCODE, vcode);
					map.put(KEY_VNAME_ENG, vname_eng);
					map.put(KEY_VNAME_TAMIL, vname_tamil);

					// adding HashList to ArrayList
					menuItems.add(map);
				}
				cursors1.close();

				VillageListAdapter lia = new VillageListAdapter(
						getApplicationContext(), menuItems);
				list_view.setAdapter(lia);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void afterTextChanged(Editable s) {

		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS: // we set this to 0
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog
					.setMessage("Please Wait...");
			mProgressDialog.setIndeterminate(false);
			// mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	public void onClick(View v) {

		 if(v.equals(home_img)) {
			Intent Pendingwork = new Intent(VillageList.this,Dashboard.class);
			 Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(Pendingwork);
			finish();
			super.onBackPressed();
			 overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

		 } if(v.equals(back_img)) {
			onBackPress();
		}


	}
	public void onBackPress(){
		super.onBackPressed();
		setResult(Activity.RESULT_CANCELED);
		overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
	}

	@Override
	public void onBackPressed(){
		onBackPress();
	}
}
