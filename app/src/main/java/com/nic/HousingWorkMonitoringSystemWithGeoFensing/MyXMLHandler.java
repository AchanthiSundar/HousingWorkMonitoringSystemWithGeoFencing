package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static SitesList sitesList = null;
	Context conte;
	public MyXMLHandler(Context context) {
		// TODO Auto-generated constructor stub
		conte = context;
	}

	public static SitesList getSitesList() {
		return sitesList;
	}

	public static void setSitesList(SitesList sitesList) {
		MyXMLHandler.sitesList = sitesList;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("rd")) {
			/** Start */
			sitesList = new SitesList();
		} else if (localName.equals("name")) {
			/** Get attribute value */
			String attr = attributes.getValue("designation");
			sitesList.setEmpDesignation(attr);
		} else if (localName.equals("vlist")) {
			String vcode = "";
			String vname = "";
			String vname1 = "";

			try {
				vcode = attributes.getValue("villagecode");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				vname = attributes.getValue("villagename");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				vname1 = attributes.getValue("villagename1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			insertDataValue1(vcode, vname, vname1);	
		} else if (localName.equals("data1")) {
			
			String workgroupcode = "";
			String workcode = "";
			String workstageorder = "";
			String workstagecode = "";
			String workstagename = "";
			try {
				workgroupcode = attributes.getValue("workgroupcode");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				workcode = attributes.getValue("workcode");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				workstageorder = attributes.getValue("workstageorder");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				workstagecode = attributes.getValue("workstagecode");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				workstagename = attributes.getValue("workstagename");
			} catch (Exception e) {
				e.printStackTrace();
			}
			insertDataValue3(workgroupcode, workcode, workstageorder,
					workstagecode, workstagename);
		}
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = false;

		/** set value */
		if (localName.equalsIgnoreCase("name"))
			sitesList.setEmpName(currentValue);

		else if (localName.equalsIgnoreCase("districtcode"))
			sitesList.setDistrictCode(currentValue);
		
		else if (localName.equalsIgnoreCase("blockcode"))
			sitesList.setBlockCode(currentValue);
		
		else if (localName.equalsIgnoreCase("provider"))
			sitesList.setServiceProvider(currentValue);

		else if (localName.equalsIgnoreCase("last_field_visitdate"))
			sitesList.setLastVisitDate(currentValue);

		else if (localName.equalsIgnoreCase("logo"))
			sitesList.setPhoto(currentValue);
		
		else if (localName.equalsIgnoreCase("version")){
			sitesList.setgetVersion(currentValue);
		}
			
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}

	private void insertDataValue1(String vcode, String vname, String vname1) {
		
		SQLiteDatabase db = LoginScreen.placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();

		values.put("villagecode", vcode);
		values.put("villagename", vname);
		values.put("villagename1", vname1);
		
		db.insert("village", null, values);	
	}
	
	private void insertDataValue3(String workgroupcode, String workcode,
			String workstageorder, String workstagecode, String workstagename) {
		SQLiteDatabase db = LoginScreen.placeData.getWritableDatabase();
		Log.i("Called", "Called");
		ContentValues values;
		values = new ContentValues();

		 values.put("workgroupcode", workgroupcode);
		 values.put("workcode", workcode);
		 values.put("workstageorder", workstageorder);
		 values.put("workstagecode", workstagecode);
		 values.put("workstagename", workstagename);
		 
		 db.insert("upcomingstage", null, values);
	}
}
