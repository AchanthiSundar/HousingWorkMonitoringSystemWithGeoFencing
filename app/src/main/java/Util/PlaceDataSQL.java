package Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class PlaceDataSQL extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "schememonitoringsystems";
	private static final int DATABASE_VERSION = 1;
	private Context context;

	static final String ID_WORK_ID = "txtwkid";
	static final String ID_STAGE = "txtsn";
	static final String ID_REMARKS = "txtremarks";
	static final String ID_LATITUDE = "lat";
	static final String ID_LONGITUDE = "lan";
	static final String ID_IMAGE = "image";
	static final String ID_SCHEME_ID = "schemeid";
	static final String ID_STAGE_ID = "stage_id";
	static final String ID_FIN_YEAR = "fin_year";
	static final String ID_WORK_TYPE_ID = "worktypeid";
	static final String ID_USER = "user";

	public static final String pendingTable = "pendingUpload";

	public PlaceDataSQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE details ("
				+ "employee_photo BLOB," +
				"emp_name varchar(16)," +
				"emp_designation varchar(16)," +
				"districtcode varchar(4),"+ 
				"blockcode varchar(4)," +
				"emp_last_visit_date varchar(16)," +
				"version varchar(4)," +
				"service_provider varchar(8))");

		db.execSQL("CREATE TABLE village ("
				+ "villagecode varchar(4)," +
				"villagename varchar(16)," +
				"villagename1 varchar(16))");

		db.execSQL("CREATE TABLE pendingworks ("
				+ "id varchar(8)," +
				"workid varchar(16)," +
				"schemegrouppname varchar(16)," +
				"schemeid varchar(4)," +
				"scheme varchar(32),"+ 
				"financialyear varchar(16)," +
				"agencyname varchar(32)," +
				"workgroupname varchar(32)," +
				"workgroupid varchar(16),"+ 
				"workname varchar(64)," +
				"worktypeid varchar(16)," +
				"block varchar(16)," +
				"village varchar(16),"+ 
				"villagecode varchar(8)," +
				"stagename varchar(32)," +
				"currentstageofwork varchar(16)," +
				"beneficiaryname varchar(32),"+ 
				"beneficiaryfhname varchar(32)," +
				"worktypname varchar(16)," +
				"beneficiarygender varchar(8)," +
				"beneficiarycommunity varchar(8)," +
				"initalamount varchar(8)," +
				"amountspentsofar varchar(8)," +
				"upddate varchar(16))");

		db.execSQL("CREATE TABLE upcomingstage ("
				+ "workgroupcode NUMERIC," +
				"workcode NUMERIC," +
				"workstageorder NUMERIC," +
				"workstagecode NUMERIC,"+ 
				"workstagename varchar(32))");

		db.execSQL("CREATE TABLE pendingUpload ("
				+ "id INTEGER PRIMARY KEY," +
				"txtwkid NUMERIC," +
				"txtsn TEXT," +
				"txtremarks TEXT," +
				"image BLOB," +
				"lat TEXT, " +
				"lan TEXT,"+ 
				"schemeid TEXT," +
				"fin_year TEXT," +
				"stage_id TEXT," +
				"worktypeid TEXT," +
				"user TEXT)");

		db.execSQL("CREATE TABLE workIdForOffLine ("
				+ "id INTEGER PRIMARY KEY," +
				"workid NUMERIC," +
				"vcode TEXT)");
	}

	private void versionUpdation(SQLiteDatabase db) {

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase(String db) {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = "data/data/com.data.pack/databases/" + db;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		} catch (Exception e) {

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		if (oldVersion == 1) {
			Log.d("New Version", "Datas can be upgraded");
		}
		Log.d("Sample Data", "onUpgrade	: " + newVersion);
	}

	public void DeletePendingUploads(String table) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table, null, null);
	}

	public void AddPendingUploads(String workid, String stage, String remarks,
			String latitude, String longitude, byte[] image, String schemeId,
			String fin_year, String stage_id, String worktypeid, String user_name) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(ID_WORK_ID, workid);
		cv.put(ID_STAGE, stage);
		cv.put(ID_REMARKS, remarks);
		cv.put(ID_IMAGE, image);
		cv.put(ID_LATITUDE, latitude);
		cv.put(ID_LONGITUDE, longitude);
		cv.put(ID_SCHEME_ID, schemeId);
		cv.put(ID_FIN_YEAR, fin_year);
		cv.put(ID_STAGE_ID, stage_id);
		cv.put(ID_WORK_TYPE_ID, worktypeid);
		cv.put(ID_USER, user_name);

		db.insert(pendingTable, null, cv);
		db.close();

		// Log.i("Debug",
		// "DB inserted "+cv.size()+" col id :: "+cv.getAsByteArray(colImgData));
	}

	public int getTotalCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from pendingUpload", null);
		int count = cur.getCount();
		// cur.close();
		// db.close();
		return count;
	}
}
