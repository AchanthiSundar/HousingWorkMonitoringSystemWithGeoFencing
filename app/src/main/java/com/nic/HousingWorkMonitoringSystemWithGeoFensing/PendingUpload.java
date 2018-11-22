package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Util.NetworkUtil;
import Util.PlaceDataSQL;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PendingUpload extends ListActivity implements OnClickListener {
	
	public static PlaceDataSQL placeData;
	public static SQLiteDatabase db;
	TextView service_provider;
	ListView lv;
	AlertDialog alertDialog; 
	int po;
	String listViewPositions_id;
	Button bt_uploadAll;
	AlertDialog.Builder alert;
	String offlatTextValue,offlanTextValue;
	
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	ArrayList<String> al_param = new ArrayList<String>();
	ArrayList<String> al_param_value = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending_upload);

		initialise();
		viewPendingUploads();
		viewServiceProvider();
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				po = position;
				String Workid="", stage="", latitude="", longitude="", remarks="";
				
				listViewPositions_id = ((TextView) view.findViewById(R.id.id)).getText().toString();
				Workid = ((TextView) view.findViewById(R.id.workid)).getText().toString();
				stage = ((TextView) view.findViewById(R.id.stage)).getText().toString();
				latitude = ((TextView) view.findViewById(R.id.tvv_latitude)).getText().toString();
				longitude = ((TextView) view.findViewById(R.id.tvv_longitude)).getText().toString();
				remarks = ((TextView) view.findViewById(R.id.remarks)).getText().toString();
				
				alert  = new AlertDialog.Builder(PendingUpload.this);
				alert.setCancelable(true);
				if(NetworkUtil.isNetworkAvailable(getApplicationContext())){		
					if (MyLocationListener.latitude > 0) {
						offlatTextValue = "" + MyLocationListener.latitude;
						offlanTextValue = "" + MyLocationListener.longitude;
						uploadPhoto();			
					}  else {
						alert.setTitle("GPS");
						alert.setMessage("GPS activation in progress,\n Please press back button to\n retake the photo");
						alert.setPositiveButton("OK", null);
						alert.show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Network Connection Not Available...", Toast.LENGTH_LONG).show();
				}				
			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				listViewPositions_id = ((TextView) arg1.findViewById(R.id.id)).getText().toString();
				return false;
			}
			
		});
	}
	private void uploadPhoto() {
		al_param.add("txtwkid");
		al_param.add("txtremarks");
		al_param.add("image");
		al_param.add("lat");
		al_param.add("lan");
		al_param.add("oflat");
		al_param.add("oflan");
		
		al_param.add("schemeid");
		al_param.add("fin_year");
		al_param.add("stage_id");
		al_param.add("mworkid");
		al_param.add("username");
		
		al_param.add("mode");
		
		
		
		Bitmap bp = BitmapFactory.decodeByteArray(Dashboard.image.get(po), 0, Dashboard.image.get(po).length);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeBytes(byte_arr);
		
		al_param_value.add(Dashboard.workid.get(po));
		al_param_value.add(Dashboard.remarks.get(po));
		al_param_value.add(image_str);
		al_param_value.add(Dashboard.latitude.get(po));
		al_param_value.add(Dashboard.longitude.get(po));
		al_param_value.add(offlatTextValue);
		al_param_value.add(offlanTextValue);
		
		al_param_value.add(Dashboard.schemeid.get(po));
		al_param_value.add(Dashboard.fin_year.get(po));
		al_param_value.add(Dashboard.stage_id.get(po));
		al_param_value.add(Dashboard.worktypeid.get(po));
		al_param_value.add(Dashboard.username.get(po));
		
		al_param_value.add("offline");
		
		PostMethod.param = new String[al_param.size()];
		PostMethod.paramValue = new String[al_param.size()];
		
		for(int i=0;i<al_param.size();i++){
			//Log.i(al_param.get(i).toString()+" : ", al_param_value.get(i).toString());
			PostMethod.param[i] = al_param.get(i).toString();
			PostMethod.paramValue[i] = al_param_value.get(i).toString();	
		}
		try {
			Dashboard.id.clear();
			Dashboard.workid.clear();
			Dashboard.stage.clear();
			Dashboard.remarks.clear();
			Dashboard.latitude.clear();
			Dashboard.longitude.clear();
			Dashboard.image.clear();
			
			Dashboard.schemeid.clear();
			Dashboard.fin_year.clear();
			Dashboard.stage_id.clear();
			Dashboard.worktypeid.clear();
			Dashboard.username.clear();
			
			Upload task = new Upload();
			task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	private class Upload extends AsyncTask<String, String, String> {
		String response ;
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		protected String doInBackground(String... urls) {
			PostMethod po = new PostMethod(getApplicationContext(), "https://www.tnrd.gov.in/project/webservices_forms/sample_photo.php");
			
			//PostMethod po = new PostMethod(getApplicationContext(), "http://10.163.14.137/rdwebtraining/project_new/webservices_forms/sample_photo.php");
			
			response = po.post();
			//Log.d("Response", response);
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("LOG_TAG", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			//alertDialog.dismiss();
			try {
				if (response.equals("Saved")) {
					Toast.makeText(PendingUpload.this,
							"Data Uploaded Successfully" , Toast.LENGTH_LONG)
							.show();
					String[] whereArgs = {listViewPositions_id};
					
					db.delete("pendingUpload", "id = ?", whereArgs);
					
					try {
						Dashboard.tv_pendingUploadCount.setText(""+placeData.getTotalCount());
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					Cursor cursor = getEvents(placeData.pendingTable);
					int count = cursor.getCount();
					//Log.i("Count ", ""+count);
					
					if(count > 0) {
						while (cursor.moveToNext()) {
							int ID = cursor.getInt(0);
							String WORK_ID = cursor.getString(1);	   
							String STAGE = cursor.getString(2);
							String REMARKS = cursor.getString(3);
							byte[] IMAGE = cursor.getBlob(4);
							String LATITUDE = cursor.getString(5);
							String LONGITUDE = cursor.getString(6);
							
							String schemeId = cursor.getString(7);
							String finYear = cursor.getString(8);
							String stageId = cursor.getString(9);
							String mworkId = cursor.getString(10);
							String userName = cursor.getString(11);
							
							Dashboard.id.add(ID);
							Dashboard.workid.add(WORK_ID);
							Dashboard.stage.add(STAGE);
							Dashboard.remarks.add(REMARKS);
							Dashboard.image.add(IMAGE);
							Dashboard.latitude.add(LATITUDE);
							Dashboard.longitude.add(LONGITUDE);
							
							Dashboard.schemeid.add(schemeId);
							Dashboard.fin_year.add(finYear);
							Dashboard.stage_id.add(stageId);
							Dashboard.worktypeid.add(mworkId);
							Dashboard.username.add(userName);
						}
						Dashboard.spinArray = (String[])Dashboard.stage.toArray(new String[Dashboard.stage.size()]);
						ItemsAdapter itemsAdapter = new ItemsAdapter(PendingUpload.this,
								R.layout.pending_upload_list_item, Dashboard.spinArray);
						setListAdapter(itemsAdapter);
					} else {
						finish();
					}
				}
				if (response.equals("Failed")) {
					Toast.makeText(PendingUpload.this, "Error In Data Upload",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void initialise() {
		try {
			placeData = new PlaceDataSQL(this);
			db = placeData.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		service_provider = (TextView) findViewById(R.id.footertxt);	
		bt_uploadAll = (Button)findViewById(R.id.upload_db_data);
	}

	private void viewPendingUploads() {
		ItemsAdapter itemsAdapter = new ItemsAdapter(PendingUpload.this,
				R.layout.pending_upload_list_item, Dashboard.spinArray);
		setListAdapter(itemsAdapter);
		lv = getListView();
		
		registerForContextMenu(lv);
	}

	private void viewServiceProvider() {
		Cursor cursors = getRawEvents("select * from details", null);
		while (cursors.moveToNext()) {
			String ser = cursors.getString(6);
			service_provider.setText(ser);
		}
		cursors.close();
	}
	
	public void onClick(View v) {
		if(v.equals(bt_uploadAll)) {
			
		}
	}
	private Cursor getRawEvents(String sql, String string) {
		String[] id = { string };
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}
	private Cursor getEvents(String table)
	{
		SQLiteDatabase db = placeData.getReadableDatabase();
		Cursor cursor = db.query(table, null, null, null, null, null, null);
		startManagingCursor(cursor);
		return cursor;
	}
	private class ItemsAdapter extends BaseAdapter {
		String[] items;

		public ItemsAdapter(Context context, int textViewResourceId,
				String[] items) {
			this.items = items;
		}

		public View getView(final int POSITION, View convertView,
				ViewGroup parent) {
			TextView id,Workid, stage, latitude, longitude, remarks;
			ImageView img;
			View view = convertView;

			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.pending_upload_list_item, null);
			}

			img = (ImageView) view.findViewById(R.id.image);

			id = (TextView) view.findViewById(R.id.id);
			Workid = (TextView) view.findViewById(R.id.workid);
			stage = (TextView) view.findViewById(R.id.stage);
			latitude = (TextView) view.findViewById(R.id.tvv_latitude);
			longitude = (TextView) view.findViewById(R.id.tvv_longitude);
			remarks = (TextView) view.findViewById(R.id.remarks);

			id.setText(""+Dashboard.id.get(POSITION));
			Workid.setText(Dashboard.workid.get(POSITION));
			stage.setText(Dashboard.stage.get(POSITION));
			latitude.setText(Dashboard.latitude.get(POSITION));
			longitude.setText(Dashboard.longitude.get(POSITION));
			remarks.setText(Dashboard.remarks.get(POSITION));
			img.setImageBitmap(BitmapFactory.decodeByteArray(
					Dashboard.image.get(POSITION), 0,
					Dashboard.image.get(POSITION).length));

			return view;
		}

		public int getCount() {
			return items.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
	}
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS: // we set this to 0
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog
					.setMessage("Please Wait..Your Data Is Getting Uploaded..");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		  super.onCreateContextMenu(menu, v, menuInfo);
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.context_menu, menu);
		}

	public boolean onContextItemSelected(MenuItem item) {
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		  switch(item.getItemId()) {
		  case R.id.delete:
		       deleteSelectedItem();
		        return true;
		  default:
		        return super.onContextItemSelected(item);
		  }
	}
		  
	private void deleteSelectedItem() {
		
		Dashboard.id.clear();
		Dashboard.workid.clear();
		Dashboard.stage.clear();
		Dashboard.remarks.clear();
		Dashboard.latitude.clear();
		Dashboard.longitude.clear();
		Dashboard.image.clear();
		
		String[] whereArgs = {listViewPositions_id};
		
		db.delete("pendingUpload", "id = ?", whereArgs);
		
		try {
			Dashboard.tv_pendingUploadCount.setText(""+placeData.getTotalCount());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Cursor cursor = getEvents(placeData.pendingTable);
		int count = cursor.getCount();
		//Log.i("Count ", ""+count);
		
		if(count > 0) {
			while (cursor.moveToNext()) {
				int ID = cursor.getInt(0);
				String WORK_ID = cursor.getString(1);	   
				String STAGE = cursor.getString(2);
				String REMARKS = cursor.getString(3);
				byte[] IMAGE = cursor.getBlob(4);
				String LATITUDE = cursor.getString(5);
				String LONGITUDE = cursor.getString(6);
				
				Dashboard.id.add(ID);
				Dashboard.workid.add(WORK_ID);
				Dashboard.stage.add(STAGE);
				Dashboard.remarks.add(REMARKS);
				Dashboard.image.add(IMAGE);
				Dashboard.latitude.add(LATITUDE);
				Dashboard.longitude.add(LONGITUDE);
			}
			Dashboard.spinArray = (String[])Dashboard.stage.toArray(new String[Dashboard.stage.size()]);
			ItemsAdapter itemsAdapter = new ItemsAdapter(PendingUpload.this,
					R.layout.pending_upload_list_item, Dashboard.spinArray);
			setListAdapter(itemsAdapter);		
		}else {
			finish();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
