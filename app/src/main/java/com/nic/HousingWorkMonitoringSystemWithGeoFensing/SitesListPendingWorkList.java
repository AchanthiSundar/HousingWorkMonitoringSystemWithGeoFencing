package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.util.ArrayList;

/** Contains getter and setter method for varialbles */
public class SitesListPendingWorkList {


	private static ArrayList<String> id = new ArrayList<String>();
	private static ArrayList<String> workid = new ArrayList<String>();
	private static ArrayList<String> schemeid = new ArrayList<String>();
	private static ArrayList<String> schemegrouppname = new ArrayList<String>();
	private static ArrayList<String> scheme = new ArrayList<String>();
	private static ArrayList<String> financialyear = new ArrayList<String>();
	private static ArrayList<String> agencyname = new ArrayList<String>();
	private static ArrayList<String> workgroupname = new ArrayList<String>();
	private static ArrayList<String> workgroupid = new ArrayList<String>();
	private static ArrayList<String> workname = new ArrayList<String>();
	private static ArrayList<String> worktypeid = new ArrayList<String>();
	private static ArrayList<String> block = new ArrayList<String>();
	private static ArrayList<String> village = new ArrayList<String>();
	private static ArrayList<String> villagecode = new ArrayList<String>();
	private static ArrayList<String> stagename = new ArrayList<String>();
	private static ArrayList<String> currentstageofwork = new ArrayList<String>();
	private static ArrayList<String> beneficiaryFatherName = new ArrayList<String>();
	private static ArrayList<String> beneficiaryName = new ArrayList<String>();
	private static ArrayList<String> workTypeName = new ArrayList<String>();
	private static ArrayList<String> upddate = new ArrayList<String>();
	
	private static ArrayList<String> beneficiarygender = new ArrayList<String>();
	private static ArrayList<String> beneficiarycommunity = new ArrayList<String>();
	private static ArrayList<String> initalamount = new ArrayList<String>();
	private static ArrayList<String> amountspentsofar = new ArrayList<String>();
	
	
	/**
	 * In Setter method default it will return arraylist change that to add
	 */
	public ArrayList<String> getId() {
		return id;
	}

	public void setId(String Id) {
		id.add(Id);
	}

	public ArrayList<String> getWorkId() {
		return workid;
	}

	public void setWorkId(String work_id) {
		workid.add(work_id);
	}

	public ArrayList<String> getSchemeId() {
		return schemeid;
	}

	public void setSchemeId(String schemeId) {
		schemeid.add(schemeId);
	}
	
	public ArrayList<String> getSchemeGroupName() {
		return schemegrouppname;
	}

	public void setSchemeGroupName(String schemegroupname) {
		schemegrouppname.add(schemegroupname);
	}

	public ArrayList<String> getScheme() {
		return scheme;
	}

	public void setScheme(String schemee) {
		scheme.add(schemee);
	}

	public ArrayList<String> getFinancialYear() {
		return financialyear;
	}

	public void setFinancialYear(String finyear) {
		financialyear.add(finyear);
	}

	public ArrayList<String> getAgencyName() {
		return agencyname;
	}

	public void setAgencyName(String name) {
		agencyname.add(name);
	}

	public ArrayList<String> getWorkGroupName() {
		return workgroupname;
	}

	public void setWorkGroupName(String name) {
		workgroupname.add(name);
	}

	public ArrayList<String> getWorkGroupId() {
		return workgroupid;
	}

	public void setWorkGroupId(String id) {
		workgroupid.add(id);
	}

	public ArrayList<String> getWorkName() {
		return workname;
	}

	public void setWorkName(String name) {
		workname.add(name);
	}

	public ArrayList<String> getWorkTypeId() {
		return worktypeid;
	}

	public void setWorkTypeId(String id) {
		worktypeid.add(id);
	}

	public ArrayList<String> getBlock() {
		return block;
	}

	public void setBlock(String name) {
		block.add(name);
	}

	public ArrayList<String> getVillage() {
		return village;
	}

	public void setVillage(String name) {
		village.add(name);
	}

	public ArrayList<String> getVillageCode() {
		return villagecode;
	}

	public void setVillageCode(String code) {
		villagecode.add(code);
	}

	public ArrayList<String> getStageName() {
		return stagename;
	}

	public void setStageName(String name) {
		stagename.add(name);
	}

	public ArrayList<String> getBeneficiarygender() {
		return beneficiarygender;
	}

	public void setBeneficiarygender(String gender) {
		beneficiarygender.add(gender);
	}
	
	public ArrayList<String> getBeneficiarycommunity() {
		return beneficiarycommunity;
	}

	public void setBeneficiarycommunity(String community) {
		beneficiarycommunity.add(community);
	}
	
	public ArrayList<String> getInitialAmount() {
		return initalamount;
	}

	public void setInitialAmount(String amount) {
		initalamount.add(amount);
	}
	
	
	public ArrayList<String> getAmountSpentSoFar() {
		return amountspentsofar;
	}

	public void setAmountSpentSoFar(String amount) {
		amountspentsofar.add(amount);
	}
	
	public ArrayList<String> getCurrentStageOfWork() {
		return currentstageofwork;
	}

	public void setCurrentStageOfWork(String work) {
		currentstageofwork.add(work);
	}

	public ArrayList<String> getBeneficiaryFatherName() {
		return beneficiaryFatherName;
	}

	public void setBeneficiaryFatherName(String name) {
		beneficiaryFatherName.add(name);
	}
	
	public ArrayList<String> getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String name) {
		beneficiaryName.add(name);
	}
	
	public ArrayList<String> getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String name) {
		workTypeName.add(name);
	}
	public ArrayList<String> getUpdateDate() {
		return upddate;
	}

	public void setUpdateDate(String UpdateDate) {
		upddate.add(UpdateDate);
	}
	public static void Clear() {

		id.clear();
		workid.clear();
		schemeid.clear();
		schemegrouppname.clear();
		scheme.clear();
		financialyear.clear();
		agencyname.clear();
		workgroupname.clear();
		workgroupid.clear();
		workname.clear();
		worktypeid.clear();
		block.clear();
		village.clear();
		villagecode.clear();
		stagename.clear();
		currentstageofwork.clear();
		beneficiaryName.clear();
		beneficiaryFatherName.clear();
		workTypeName.clear();
		upddate.clear();
		
		beneficiarygender.clear();
		beneficiarycommunity.clear();
		initalamount.clear();
		amountspentsofar.clear();
		
	}

}
