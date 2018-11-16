package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.util.ArrayList;

/** Contains getter and setter method for varialbles */
public class SitesList {

	/** Variables */
	public static ArrayList<String> photo = new ArrayList<String>();
	public static ArrayList<String> emp_name = new ArrayList<String>();
	public static ArrayList<String> emp_designation = new ArrayList<String>();
	public static ArrayList<String> districtcode = new ArrayList<String>();
	public static ArrayList<String> blockcode = new ArrayList<String>();
	public static ArrayList<String> emp_lastVisitDate = new ArrayList<String>();
	public static ArrayList<String> ServiceProvider = new ArrayList<String>();
	public static ArrayList<String> version = new ArrayList<String>();

	/**
	 * In Setter method default it will return arraylist change that to add
	 */

	public ArrayList<String> getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo.add(photo);
	}

	public ArrayList<String> getEmpName() {
		return emp_name;
	}

	public void setEmpName(String emp_name) {
		this.emp_name.add(emp_name);
	}

	public ArrayList<String> getDistrictCode() {
		return districtcode;
	}

	public void setDistrictCode(String districtcode) {
		this.districtcode.add(districtcode);
	}

	public ArrayList<String> getBlockCode() {
		return blockcode;
	}

	public void setBlockCode(String blockcode) {
		this.blockcode.add(blockcode);
	}

	public ArrayList<String> getEmpDesignation() {
		return emp_designation;
	}

	public void setEmpDesignation(String emp_designation) {
		this.emp_designation.add(emp_designation);
	}

	public ArrayList<String> getLastVisitDate() {
		return emp_lastVisitDate;
	}

	public void setLastVisitDate(String emp_lastVisitDate) {
		this.emp_lastVisitDate.add(emp_lastVisitDate);
	}

	public ArrayList<String> getServiceProvider() {
		return ServiceProvider;
	}

	public void setServiceProvider(String ServiceProvider) {
		this.ServiceProvider.add(ServiceProvider);
	}
	
	public ArrayList<String> getVersion() {
		return version;
	}

	public void setgetVersion(String version) {
		this.version.add(version);
	}
	
	public static void Clear() {
		photo.clear();
		emp_name.clear();
		emp_designation.clear();
		districtcode.clear();
		blockcode.clear();
		emp_lastVisitDate.clear();
		ServiceProvider.clear();
		version.clear();
	}

}
