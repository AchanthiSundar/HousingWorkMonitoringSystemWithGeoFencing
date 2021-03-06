package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import Util.NetworkUtil;
import Util.PlaceDataSQL;
import Util.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends Activity implements OnClickListener {

    //Used to check internet Connection..
    ConnectivityManager cm;
    NetworkInfo networkInfo;

    /* Location Manager for GPS */
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    AlertDialog.Builder alert;

    /* To get Mobile Details */
    String imei;
    String sb;

    //To get Mobile details
    TelephonyManager telephonyManager;
    PhoneStateListener listener;

    EditText username, password;
    Button login;
    String name, pass;

    //To Show Progressbar While Logging in
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    //add parameter and its corresponding value for postmethod
    ArrayList<String> al_param = new ArrayList<String>();
    ArrayList<String> al_param_value = new ArrayList<String>();

    public static PlaceDataSQL placeData;
    public static SQLiteDatabase db;
    InputSource is;

    String login_url = "https://www.tnrd.gov.in/project/webservices_forms/scheme_monitoring_login_services.php";
    //String login_url = "http://10.163.14.137/rdwebtraining/project_new/webservices_forms/scheme_monitoring_login_services.php";

    static SharedPreferences preferences;
    Context context;

    static SharedPreferences sharedpreferences;
    static String pref_username;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private Handler myhandler = new Handler();
    private Handler handler = new Handler();
    private Runnable runnable;


    //static String pref_password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        runnable = new Runnable() {
            @Override
            public void run() {
                getMobileDetails();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                PERMISSIONS_REQUEST_READ_PHONE_STATE);

                    } else {
                        getMobileDetails();
                    }
                }else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            getMobileDetails();
                        }
                    };myhandler.postDelayed(runnable,2000);

                }
            }
        };

        context = this;
        try {
            placeData = new PlaceDataSQL(this);
            db = placeData.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = getSharedPreferences("mypreferences", Context.MODE_PRIVATE);
        pref_username = sharedpreferences.getString("username", null);
        initialise();
    }

    private void getMobileDetails() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imei = telephonyManager.getDeviceId();
    }

    public void onClick(View v) {

        if (v.equals(login)) {
            try {
                loginMethod(username.getText().toString(), password.getText().toString());
                getMobileDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loginMethod(String usrname, String passwrd) {
        getMobileDetails();
        getUsernameAndPassword(usrname, passwrd);
        if ((name.equals("") && pass.equals(""))) {
            username.setFocusableInTouchMode(true);
            Utils.showAlert(LoginScreen.this,"Please Enter UserName");
//            username.requestFocus();
//            username.setError(Html.fromHtml("<font color='#FF0000'>Please Enter UserName</font>"));
        } else if (name.equals("")) {
            username.setFocusableInTouchMode(true);
            Utils.showAlert(LoginScreen.this,"Please Enter UserName");

//            username.requestFocus();
//            username.setError(Html.fromHtml("<font color='#FF0000'>Please Enter UserName</font>"));
        } else if (pass.equals("")) {
            password.setFocusableInTouchMode(true);
            Utils.showAlert(LoginScreen.this,"Please Enter Password");

//            password.requestFocus();
//            password.setError(Html.fromHtml("<font color='#FF0000'>Please Enter Password</font>"));
        } else {
            if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                alert = new AlertDialog.Builder(LoginScreen.this);
                alert.setCancelable(true);

                mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocListener = new MyLocationListener();
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
////					return;
//                }
//                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        callLoginWebService();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    alert.setTitle("GPS");
					alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..");
					alert.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent I = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(I);
								}
							});
					alert.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

                                }
                            });
                    alert.show();
                }

            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(
                        LoginScreen.this);
                ab.setMessage(Html
                        .fromHtml("<font color=#A52A2A>Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..</font>"));
                ab.setPositiveButton("Setting Internet Connection",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Intent I = new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(I);
                            }
                        });
                ab.setNegativeButton("Continue With Off-Line",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                offline_mode();
                            }
                        });
                ab.show();
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        try {
                            callLoginWebService();
                            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Permission was granted, do your thing!
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }


            // other 'case' lines to check for other
            // permissions this app might request
            if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMobileDetails();
            }


        }
    }


    protected void offline_mode() {
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
        String user_name = preferences.getString("user_name", "").replaceAll(" +$","");
        String pwd = preferences.getString("pwd", "").replaceAll(" +$","");

        if (user_name.equals(name) && pwd.equals(pass)) {
            Cursor cursors1 = getRawEvents("select workid from workIdForOffLine", null);
            if (cursors1.getCount() != 0) {
                startActivity(new Intent(LoginScreen.this, Dashboard.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "No Records Found..", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password.", Toast.LENGTH_LONG).show();
        }
    }

    private Cursor getRawEvents(String sql, String string) {

        Cursor cursor = LoginScreen.db.rawQuery(sql, null);
        startManagingCursor(cursor);
        return cursor;
    }

    private void callLoginWebService() {
        al_param.add("mdn");
        al_param.add("pwdtxt");
        al_param.add("imei");
        al_param.add("command");
        al_param.add("ver");
        al_param.add("appcode");

        al_param_value.add(name.trim());
        al_param_value.add(pass.trim());
        Log.d("imei",""+imei);
//        Log.d("imeitrim",""+imei.trim());
        try {
            if (!imei.equalsIgnoreCase("null") || imei!= null) {
                al_param_value.add(imei.trim());
            } else {

                getMobileDetails();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        al_param_value.add("authentication");
        al_param_value.add("1");
        al_param_value.add("H");


        PostMethod.param = new String[al_param.size()];
        PostMethod.paramValue = new String[al_param.size()];

        for (int i = 0; i < al_param.size(); i++) {
            PostMethod.param[i] = al_param.get(i).toString();
            PostMethod.paramValue[i] = al_param_value.get(i).toString();
        }
        try {
            //db.delete("verify", null, null);
            db.delete("details", null, null);
            db.delete("village", null, null);
            db.delete("pendingworks", null, null);
            db.delete("upcomingstage", null, null);
            db.delete("workIdForOffLine", null, null);

            Dashboard.preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
            SharedPreferences.Editor editor = Dashboard.preferences.edit();
            editor.putInt("count", 1);
            editor.commit();

            SitesList.Clear();
            Dashboard.xml_emp_photo.clear();
            Dashboard.xml_emp_name.clear();
            Dashboard.xml_emp_designation.clear();
            Dashboard.xml_districtcode.clear();
            Dashboard.xml_blockcode.clear();
            Dashboard.xml_emp_last_visit_date.clear();
            Dashboard.xml_serviceProvider.clear();
            Dashboard.xml_version.clear();

            Login task = new Login();
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUsernameAndPassword(String username, String password) {
        name = Base64.encodeToString((username).getBytes(), Base64.DEFAULT);
        pass = Base64.encodeToString((password).getBytes(), Base64.DEFAULT);
    }

    private class Login extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        protected String doInBackground(String... urls) {

            try {
                PostMethod po = new PostMethod(getApplicationContext(), login_url);
                sb = po.post1();
                Log.d("Response_Of_Login", sb);

                if (sb.startsWith("<rd>")) {
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(sb.toString()));

                    try {
                        /** Handling XML */
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();

                        MyXMLHandler myMarkMyXmlHandler = new MyXMLHandler(context);
                        xr.setContentHandler(myMarkMyXmlHandler);
                        xr.parse(is);

                    } catch (SAXException e) {
                        System.out.println("XML = " + e);
                    } catch (IOException e) {
                        System.out.println("IOn = " + e);
                    }
                }
//				else{
//					sb = "N";
//				}

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(String result) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            sharedpreferences = getSharedPreferences("mypreferences", Context.MODE_PRIVATE);
            pref_username = sharedpreferences.getString("username", null);
            //	Log.i("RES", sb);
            if (sb.startsWith("<rd>")) {

                preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user_name", name);
                editor.putString("pwd", pass);
                editor.commit();

                Editor edtr = sharedpreferences.edit();
                edtr.putString("username", username.getText().toString());
                edtr.commit();

                startActivity(new Intent(LoginScreen.this, Dashboard.class));
                finish();
            } else if (sb.contains("invalid")) {


                Utils.showAlert(LoginScreen.this,"Invalid UserName OR Password ");
//       	   	if(pref_username!=null){
//				SharedPreferences settings = getSharedPreferences("mypreferences", Context.MODE_PRIVATE);
//				settings.edit().putString("username", username.getText().toString()).commit();
//			}

            } else if (sb.contains("Message")) {
                downloadDialog(sb);
            }
        }
    }

    private void initialise() {
        String user_name = sharedpreferences.getString("username", null);
        username = (EditText) findViewById(R.id.username);
        username.setText(user_name);
        if (user_name != null) {
            username.setSelection(user_name.length());
        }
        password = (EditText) findViewById(R.id.password);
        password.requestFocus();
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    loginMethod(username.getText().toString(), password.getText().toString());
                }
                return false;
            }
        });

//        TextView txt_footer = (TextView) findViewById(R.id.headtext1);
//        txt_footer.setText("");
        myhandler.postDelayed(runnable,5000);
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {
                getMobileDetails();

            }
        };myhandler.postDelayed(runnable,2000);


    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS: // we set this to 0
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    public void downloadDialog(String msg) {
        AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
        localAlertDialog.setTitle("Warning");
        localAlertDialog.setMessage("" + msg);
        localAlertDialog.setCancelable(false);
        localAlertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        localAlertDialog.show();
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(LoginScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        }
        return false;
    }

}
