package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class WebsiteURLCrawler {
	
	private final static String crawlWebsiteURL = "https://www.hackerrank.com";
	private static int urlProcessingCounter = 1;
	
	private static HashMap<String, String> urlMap = null;
	private static HashMap<String, String> filterUrlMap = null;
	private static HashMap<String, String> urlBlackListParams = null;
	private static String filterDataType[] = {"URL", "JPEG", "PNG", "PDF","UNKNOWN_FILE_TYPE", "GIF"};
	
	static{
		urlMap = new HashMap<String, String>();
		filterUrlMap = new HashMap<String, String>();
		urlBlackListParams = new HashMap<String, String>();
		
		urlBlackListParams.put("", "");
		urlBlackListParams.put(null, "");
		urlBlackListParams.put("null", "");
		urlBlackListParams.put("#", "");
		urlBlackListParams.put("/", "");
		urlBlackListParams.put("/#", "");
	}
	
	 public static void main(String[] args) throws Exception {
	    	
	    	crawlWebsiteWrapper();
	    	
	    	System.out.println("--------------------Completed-----------------");
	    	
	    	displayFilteredData();
	    	
	    	
	    }
	 
	 private static void displayFilteredData(){
		 
		 int localUrlCounter = 0;
		 for(int i=0 ; i<filterDataType.length ; i++){
			 localUrlCounter = 1;
			 System.out.println("\n----------------------------------------------------[ " + filterDataType[i] + " ]----------------------------------------------------");
			 for(Map.Entry<String, String> entryClass : filterUrlMap.entrySet()){
		    		if(filterDataType[i].equalsIgnoreCase(entryClass.getValue()))
		    			System.out.println(localUrlCounter++ + " - "+entryClass.getKey());
			 }
		 }
		 
		 /*for(Map.Entry<String, String> entryClass : urlMap.entrySet()){
 		
			 if("SUCCESS".equalsIgnoreCase(entryClass.getValue()))
				 System.out.println("Ok : " + entryClass.getKey());
		 }*/
		 
	 }
	    
	 private static void crawlWebsiteWrapper() throws Exception{
		 crawlWebsite(crawlWebsiteURL, "");
	 }
	
	private static void crawlWebsite(String webSiteUrl, String params) throws Exception{
		
		if(!urlMap.containsKey(webSiteUrl+params)){
			
			System.out.println(urlProcessingCounter++ +  " - Processing : " + webSiteUrl+params);	 
			
			if(params.endsWith(".pdf")){
			
				if(params.startsWith("http"))
					filterUrlMap.put("[External PDF Link] - "+params, filterDataType[3]);
				else
					filterUrlMap.put(webSiteUrl+params, filterDataType[3]);
			}
			
			if(params.startsWith("http")){
				filterUrlMap.put(params, filterDataType[0]);
				urlMap.put(webSiteUrl+params, "SUCCESS");
			}
			else{
			
				Jerry doc = Jerry.jerry(sendGet(webSiteUrl+params, ""));
		        
				 doc.$("img").each(new JerryFunction() {
			            public Boolean onNode(Jerry $this, int index) {

			            	try{
			            		String internalParams = $this.attr("src");
			            		
			            		if(internalParams.startsWith("http")){
			            			if(internalParams.endsWith(".jpg") || internalParams.endsWith(".jpeg"))
				        				filterUrlMap.put(internalParams, filterDataType[1]);
				        			else if(internalParams.endsWith(".png"))
				        				filterUrlMap.put(internalParams, filterDataType[2]);
				        			else
				        				filterUrlMap.put(internalParams, filterDataType[4]);
				                	
			            		}else{
			            			if(internalParams.endsWith(".jpg") || internalParams.endsWith(".jpeg"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[1]);
				        			else if(internalParams.endsWith(".png"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[2]);
				        			else
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[4]);
				                		
			            		}
			                	
			            		
			            	}catch(Exception e){
			            		e.printStackTrace();
			            	}finally{
			            		
			            	}
			            	
			            	return true;
			            }});
				
			    doc.$("a").each(new JerryFunction() {
		            public Boolean onNode(Jerry $this, int index) {

		            	try{
		            		String internalParams = $this.attr("href");
		                	
		                	if(!urlBlackListParams.containsKey(internalParams) && !internalParams.startsWith("#"))
		                		crawlWebsite(webSiteUrl, internalParams);
		                	
		            	}catch(Exception e){
		            		e.printStackTrace();
		            	}finally{
		            		
		            	}
		            	
		            	return true;
		            }});
			}
			
		}

    }

	private static String sendGet(String url, String cookieString) throws Exception {

 		StringBuffer response = new StringBuffer();
 		try{
 			URL obj = new URL(url);
 	 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

 	 		con.setRequestMethod("GET");

 	 		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
 	 		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
 	 		con.setRequestProperty("Cookie", cookieString);

 	 		int responseCode = con.getResponseCode();

 	 		BufferedReader in = new BufferedReader(
 	 		        new InputStreamReader(con.getInputStream()));
 	 		String inputLine;

 	 		while ((inputLine = in.readLine()) != null) {
 	 			response.append(inputLine);
 	 		}
 	 		in.close();
	    	urlMap.put(url, "SUCCESS");
 		}catch(Exception e){
 			urlMap.put(url, "ERROR"); 
 		}
 		
 		return response.toString();

 	}


}

