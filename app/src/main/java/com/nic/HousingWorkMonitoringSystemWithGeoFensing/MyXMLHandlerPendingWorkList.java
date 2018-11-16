package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.ContentValues;

public class MyXMLHandlerPendingWorkList extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static SitesListPendingWorkList sitesListPendingWorkList = null;

	public static SitesListPendingWorkList getSitesListPendingWorkList() {
		return sitesListPendingWorkList;
	}

	public static void setSitesListPendingWorkList(SitesListPendingWorkList sitesListPendingWorkList) {
		MyXMLHandlerPendingWorkList.sitesListPendingWorkList = sitesListPendingWorkList;
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
			sitesListPendingWorkList = new SitesListPendingWorkList();
		
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
		if (localName.equalsIgnoreCase("id"))
			sitesListPendingWorkList.setId(currentValue);

		else if (localName.equalsIgnoreCase("workid"))
			sitesListPendingWorkList.setWorkId(currentValue);

		else if (localName.equalsIgnoreCase("schemeid"))
			sitesListPendingWorkList.setSchemeId(currentValue);
		
		else if (localName.equalsIgnoreCase("schemegrouppname"))
			sitesListPendingWorkList.setSchemeGroupName(currentValue);

		else if (localName.equalsIgnoreCase("scheme"))
			sitesListPendingWorkList.setScheme(currentValue);
		
		else if (localName.equalsIgnoreCase("financialyear"))
			sitesListPendingWorkList.setFinancialYear(currentValue);

		else if (localName.equalsIgnoreCase("agencyname"))
			sitesListPendingWorkList.setAgencyName(currentValue);

		else if (localName.equalsIgnoreCase("workgroupname"))
			sitesListPendingWorkList.setWorkGroupName(currentValue);

		else if (localName.equalsIgnoreCase("workgroupid"))
			sitesListPendingWorkList.setWorkGroupId(currentValue);

		else if (localName.equalsIgnoreCase("workname"))
			sitesListPendingWorkList.setWorkName(currentValue);

		else if (localName.equalsIgnoreCase("worktypeid"))
			sitesListPendingWorkList.setWorkTypeId(currentValue);

		else if (localName.equalsIgnoreCase("block"))
			sitesListPendingWorkList.setBlock(currentValue);

		else if (localName.equalsIgnoreCase("village"))
			sitesListPendingWorkList.setVillage(currentValue);

		else if (localName.equalsIgnoreCase("villagecode"))
			sitesListPendingWorkList.setVillageCode(currentValue);

		else if (localName.equalsIgnoreCase("stagename"))
			sitesListPendingWorkList.setStageName(currentValue);

		else if (localName.equalsIgnoreCase("currentstageofwork")) {
			sitesListPendingWorkList.setCurrentStageOfWork(currentValue);
			currentValue = "";
		}	
		else if (localName.equalsIgnoreCase("beneficiaryname"))
			sitesListPendingWorkList.setBeneficiaryName(currentValue);
		
		else if (localName.equalsIgnoreCase("beneficiaryfhname"))
			sitesListPendingWorkList.setBeneficiaryFatherName(currentValue);
		
		else if (localName.equalsIgnoreCase("worktypname")){
			sitesListPendingWorkList.setWorkTypeName(currentValue);
			currentValue = "";
		}
		
		else if (localName.equalsIgnoreCase("upddate")){
			if(currentValue.equals("")){
				sitesListPendingWorkList.setUpdateDate("First Time");
			}else{
				sitesListPendingWorkList.setUpdateDate(currentValue);
			}
		}else if (localName.equalsIgnoreCase("beneficiarygender")){
			sitesListPendingWorkList.setBeneficiarygender(currentValue);
			currentValue = "";
		}else if (localName.equalsIgnoreCase("beneficiarycommunity")){
			sitesListPendingWorkList.setBeneficiarycommunity(currentValue);
			currentValue = "";
		}else if (localName.equalsIgnoreCase("initalamount")){
			sitesListPendingWorkList.setInitialAmount(currentValue);
			currentValue = "";
		}else if (localName.equalsIgnoreCase("amountspentsofar")){
			sitesListPendingWorkList.setAmountSpentSoFar(currentValue);
			currentValue = "";
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
	private void insertDataValue3(String workgroupcode, String workcode,
			String workstageorder, String workstagecode, String workstagename) {
		LoginScreen.db = LoginScreen.placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();

		 values.put("workgroupcode", workgroupcode);
		 values.put("workcode", workcode);
		 values.put("workstageorder", workstageorder);
		 values.put("workstagecode", workstagecode);
		 values.put("workstagename", workstagename);
		 
		 LoginScreen.db.insert("upcomingstage", null, values);
	}

}
