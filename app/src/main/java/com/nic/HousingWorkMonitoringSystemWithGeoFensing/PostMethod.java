package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class PostMethod {

	static String param[];
	static String paramValue[];
	String Url;

	Context context;
	static String responseText;

	public PostMethod(Context con, String Url) {
		this.context = con;
		this.Url = Url;
	}

	public String post() {

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Url);

			int size = param.length;

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					size);

			for (int i = 0; i < size; i++) {
				nameValuePairs.add(new BasicNameValuePair(param[i],
						paramValue[i]));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//HttpResponse response = httpclient.execute(httppost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseText = httpclient.execute(httppost, responseHandler);
			// System.out.println("Response is : " + responseText);

			return responseText;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String post1() {

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Url);

			int size = param.length;

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					size);

			for (int i = 0; i < size; i++) {
				nameValuePairs.add(new BasicNameValuePair(param[i],
						paramValue[i]));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			httppost.setEntity(formEntity);			
			HttpResponse res = httpclient.execute(httppost);
			
		    BufferedReader in = null; 
			StringBuffer sb = null;
		    try {
		    	
		    	 in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8")); 
			     sb = new StringBuffer(""); 
			     String line = ""; 
			     String NL = System.getProperty("line.separator"); 
			     while ((line = in.readLine()) != null) { 
			    	 sb.append(line + NL); 
			     }	 
			} catch (Exception e) {
				System.out.println("XML Pasing Excpetion = " + e);
			}
			
		    return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
