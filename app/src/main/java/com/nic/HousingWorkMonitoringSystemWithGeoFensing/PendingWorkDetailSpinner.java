package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.ByteArrayOutputStream;

import Util.PlaceDataSQL;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PendingWorkDetailSpinner extends Activity implements OnClickListener {

    Spinner sp_selectStage;
    EditText ed_remarks;
    Button bt_takePhoto;
    TextView tv_workId, service_provider;
    ImageView home, back;

    String work_group_code, work_code, work_stage_code;
    String workGroupId, currentStageOfWork;
    private static PlaceDataSQL placeData;
    String[] workStageName;
    String[] workStageCode;
    int position_spinner;
    static String workId, schemeId, fin_year, selectedStageName, selectedStageCode, workTypeId;
    static String remarks = "No Remarks";
    private static final int CAMERA_PIC_REQUEST = 2500;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    AlertDialog.Builder alert;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_stage);

        placeData = new PlaceDataSQL(this);
        SQLiteDatabase db = placeData.getWritableDatabase();

        initialize();
        getIntentValue();
        setServiceProvider();
        getStage();

        sp_selectStage.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                position_spinner = position;
                if (position >= 1) {

                    selectedStageName = workStageName[position];
                    selectedStageCode = workStageCode[position];
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void getStage() {

        String whereArg[] = {workGroupId, workTypeId, currentStageOfWork};
        Cursor cursors = getRawEventsWhere("SELECT workstagename,workstagecode,workstageorder FROM upcomingstage " +
                "WHERE workgroupcode = ? AND workcode = ? and workstageorder >=" +
                "(select workstageorder from upcomingstage WHERE  workstagecode= ? ) ORDER BY workstageorder", whereArg);
        //Cursor cursors = getRawEventsWhere("select workstagename,workstagecode,workstageorder FROM  upcomingstage where workgroupcode  = ? AND workcode = ? and workstageorder  >= ?  and workstagecode  != 11  ORDER BY workstageorder", whereArg);

        cursors.moveToFirst();
        workStageName = new String[cursors.getCount() + 1];
        workStageName[0] = "-- Select Stage --";

        workStageCode = new String[cursors.getCount() + 1];
        workStageCode[0] = "-- Select Stage --";

        for (int i = 1; i < cursors.getCount() + 1; i++) {

            workStageName[i] = cursors.getString(0);
            workStageCode[i] = cursors.getString(1);
            //Log.i("workstagename", workStageName[i]);

            cursors.moveToNext();
        }
        sp_selectStage.setAdapter(new MyAdapter(PendingWorkDetailSpinner.this, R.layout.spinner_value, workStageName));
    }

    private Cursor getRawEventsWhere(String string, String[] whereArg) {
        SQLiteDatabase db = (placeData).getReadableDatabase();
        Cursor cursor = db.rawQuery(string, whereArg);

        startManagingCursor(cursor);
        return cursor;
    }

    private void setServiceProvider() {

        Cursor cursors = getRawEvents("select * from details", null);
        while (cursors.moveToNext()) {
            String ser = cursors.getString(6);
            service_provider.setText(ser);
        }
    }

    private void getIntentValue() {

        Intent in = getIntent();
        workId = in.getStringExtra("workId");
        schemeId = in.getStringExtra("schemeId");
        fin_year = in.getStringExtra("fin_year");
        workGroupId = in.getStringExtra("workGroupId");
        workTypeId = in.getStringExtra("workTypeId");
        currentStageOfWork = in.getStringExtra("currentStageOfWork");
        tv_workId.setText(workId);
        Log.v("WorkId", workId);
        Log.v("SchemeId", schemeId);
        Log.v("FinYear", fin_year);
        Log.v("WorkGroupId", workGroupId);
        Log.v("workTypeId", workTypeId);
        Log.v("currentStage", currentStageOfWork);
    }

    private void initialize() {

        sp_selectStage = (Spinner) findViewById(R.id.sp_stage);
        ed_remarks = (EditText) findViewById(R.id.ed_remarks);
        bt_takePhoto = (Button) findViewById(R.id.but_photo);
        tv_workId = (TextView) findViewById(R.id.tv_workId);
        service_provider = (TextView) findViewById(R.id.footertxt);
        home = (ImageView) findViewById(R.id.homeimg);
        back = (ImageView) findViewById(R.id.backimg);

        bt_takePhoto.setOnClickListener(this);
        back.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v.equals(bt_takePhoto)) {
            remarks = ed_remarks.getText().toString();
            takePhoto();
        }

        if (v.equals(home)) {

            Intent Pendingwork = new Intent(PendingWorkDetailSpinner.this, Dashboard.class);
            Pendingwork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Pendingwork);
            finish();
        }

        if (v.equals(back)) {

            finish();
        }
    }

    private void takePhoto() {

        alert = new AlertDialog.Builder(PendingWorkDetailSpinner.this);
        alert.setCancelable(true);

        if (position_spinner != 0) {

            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mlocListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

			if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				if(MyLocationListener.latitude>0) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            				} else {
            					alert.setTitle("GPS");
            					alert.setMessage("Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            					alert.setPositiveButton("OK", null);
            					alert.show();
            				}

            			}else {
            				alert.setTitle("GPS");
            				alert.setMessage("GPS is not turned on...");
            				alert.setPositiveButton("OK", null);
            				alert.show();
            			}

        } else {
            Toast.makeText(getApplicationContext(), "Please Select Stage...", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap cameraImage;

    private void setCamaraImage(Bitmap bitmap) {
        cameraImage = bitmap;
    }

    public static Bitmap getCameraBitmap() {
        return cameraImage;
    }

    public static byte[] getCameraImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cameraImage.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] byte_arr = stream.toByteArray();
        return byte_arr;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                setCamaraImage(bitmap);

                Intent upload = new Intent(PendingWorkDetailSpinner.this,
                        PreviewPage.class);
                startActivity(upload);
                finish();
            } catch (Exception e) {
                System.out.println("ooooooooooooooooooooooon " + e.toString());
            }
        }
    }

    private Cursor getRawEvents(String sql, String string) {
        SQLiteDatabase db = (placeData).getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        startManagingCursor(cursor);
        return cursor;
    }
}
