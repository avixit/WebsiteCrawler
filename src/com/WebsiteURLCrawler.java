package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class WebsiteURLCrawler {
	
	private static String crawlWebsiteURL = "https://www.google.com";
	private static int urlProcessingCounter = 1;
	
	private static String filePath = "WebsiteCrawler_";
	private static boolean isCrawlingStarted = false;
	private static boolean isCrawlingEnded = false;
	
	private static HashMap<String, String> urlMap = null;
	private static HashMap<String, String> filterUrlMap = null;
	private static HashMap<String, String> urlBlackListParams = null;
	private static String filterDataType[] = {"External URL's", "JPEG", "PNG", "PDF","UNKNOWN_FILE_TYPE", "JS", "GIF"};
	
	private static int dataTypeDisplayLabelLength = 150;
	
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
		 
		 Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() { 
			    	if(isCrawlingStarted && !isCrawlingEnded){
			    		try {
					    	System.out.println("\n\nOops, processing is interrupted. Hang on !");
					    	System.out.println("We are saving crawling results to : " + new File(filePath).getAbsolutePath());
							saveFilteredDataToFile();
							System.out.println("Crawling results have been saved successfully in following file : " + new File(filePath).getAbsolutePath());
						} catch (Exception e) {
							System.out.println("Error while saving crawling results. Please run the crawler again.");
							e.printStackTrace();
						}
			    	}else{
			    		System.out.println("\nThank you for using the crawler. If you have any suggestions or improvements or want to contribute, please shoot me an email (avixit.aparnathi@gmail.com) with all the details.");
			    	}
				    
			    }
			 });
		 
	    if(args.length == 0){
	    	System.out.println("ERROR : Invalid usage. Please use like following : \"java -jar XX.jar http://www.test.com\"");
	    }
	    else if(args.length == 1){
	    	if("-help".equalsIgnoreCase(args[0])){
	    		System.out.println("USAGE : java -jar XX.jar http://www.test.com [-args]");
	    		System.out.println("where args include:");
	    		System.out.println("   -save : Save crawling results to a file. Currently only HTML is supported. i.e java -jar XX.jar http://www.test.com -save");
	    		System.out.println("\nSee https://github.com/avixit/WebsiteCrawler for more details.");
	    	}
	    	else if(!args[0].startsWith("http://") && !args[0].startsWith("https://")){
	    		System.out.println("ERROR : URL must start with http. i.e http://www.test.com or https://www.securetest.com");
	    		System.out.println("For help, please run following command : \"java -jar XX.jar -help\"");
	    	}
	    	else{
	    		crawlWebsiteURL = args[0];
	    		filePath = filePath + crawlWebsiteURL.substring(crawlWebsiteURL.indexOf(':')+3, crawlWebsiteURL.length()) + ".html";
	    		System.out.println("INFO : You have not passed any arguments for the url : " + args[0] + ", hence default profile will run.");
	    		crawlWebsiteWrapper();
	    		displayFilteredData(); //Displaying the results to the console
	    	}
	    }
	    else if(args.length == 2){
	    	 if("-save".equalsIgnoreCase(args[1])){
	    		crawlWebsiteURL = args[0];
	    		filePath = filePath + crawlWebsiteURL.substring(crawlWebsiteURL.indexOf(':')+3, crawlWebsiteURL.length()) + ".html";
	    		System.out.println("Crawling results will be stored in following file : " + new File(filePath).getAbsolutePath());
	    		crawlWebsiteWrapper();
	    		saveFilteredDataToFile(); //Saving the results to a file
	    	 }
	    	else{
	    		System.out.println("ERROR : Invalid argument. For help, please run following command : \"java -jar XX.jar -help\"");
	    	}
	    }
	    else{
	    	System.out.println("Invalid no of arguments. For help, please run following command : ");
	    	System.out.println("\"java -jar XX.jar -help");
	    }
	    
	    if(isCrawlingStarted)
	    	isCrawlingEnded = true;
	    	
	    }
	 
	 private static void displayDataTypeLabel(String dataType){
		 System.out.println();
		 String data = "[ " + dataType + " ]";
		 if(data.length() % 2 != 0)
			 data += "-";
		 for(int j=0 ; j<(dataTypeDisplayLabelLength-data.length())/2 ; j++){
			 System.out.print("-");
		 }
		 System.out.print(data);
		 for(int j=0 ; j<(dataTypeDisplayLabelLength-data.length())/2 ; j++){
			 System.out.print("-");
		 }
		 System.out.println();
	 }
	 
	 private static void displayFilteredData(){
		 
		 int localUrlCounter = 0;
		 for(int i=0 ; i<filterDataType.length ; i++){
			 localUrlCounter = 1;
			 displayDataTypeLabel(filterDataType[i]);
			 for(Map.Entry<String, String> entryClass : filterUrlMap.entrySet()){
		    		if(filterDataType[i].equalsIgnoreCase(entryClass.getValue()))
		    			System.out.println(localUrlCounter++ + " - "+entryClass.getKey());
			 }
		 }
		 
	 }
	 
 private static void saveFilteredDataToFile() throws Exception{
	 
	 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
	 	File f = new File(filePath);
	    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	    bw.write("<html>");
	    bw.write("<style>body{font-family: \"Helvetica Neue\",Helvetica,Arial,sans-serif; font-size: 14px; line-height: 1.42857143;} table{border-collapse: collapse; width: 100%; border: 1px solid #ddd;} table th{background-color: #f5f5f5;}</style>");
	    bw.write("<body>");
	    bw.write("<h1>Website crawling results for "+crawlWebsiteURL+"</h1>");
	    bw.write("<br/>");
	    bw.write("<b>Report generated on : </b>" + sdf.format(new Date()));
	    bw.write("<br/>");
		 
		 int localUrlCounter = 0;
		 for(int i=0 ; i<filterDataType.length ; i++){
			 localUrlCounter = 1;
			 bw.write("<br /><table border=\"1\"><tr><th colspan=\"2\">File Type : " + filterDataType[i] + "</tr></th>");
			 boolean isDataAvailable = false;
			 for(Map.Entry<String, String> entryClass : filterUrlMap.entrySet()){
		    		if(filterDataType[i].equalsIgnoreCase(entryClass.getValue()))
		    		{	bw.write("<tr><td width=\"5%\">"+localUrlCounter++ + "</td><td width=\"95%\">"+entryClass.getKey() + "</td></tr>");
		    			isDataAvailable=true;
		    		}
			 }
			 if(isDataAvailable)
				 bw.write("</table>");
			 else
				 bw.write("<tr><td>Oops, we couldn't find any "+filterDataType[i]+" files.</td></tr></table>");
		 }
		 

		bw.write("<br/><b><center>Thank you for using the crawler. If you have any suggestions or improvements or want to contribute, please shoot me an email (avixit.aparnathi@gmail.com) with all the details. </center></b>");
        bw.write("</body>");
        bw.write("</html>");

        bw.close();        
        
	 }
	    
	 private static void crawlWebsiteWrapper() throws Exception{
		 isCrawlingStarted = true;
		 crawlWebsite(crawlWebsiteURL, "");
	 }
	
	private static void crawlWebsite(String webSiteUrl, String params) throws Exception{
		
		if(!urlMap.containsKey(webSiteUrl+params)){
			
			if(params.endsWith(".pdf")){
			
				if(params.startsWith("/http"))
					filterUrlMap.put("[External PDF Link] - "+params.substring(1), filterDataType[3]);
				else
					filterUrlMap.put(webSiteUrl+params, filterDataType[3]);
			}
			
			if(params.startsWith("/http")){
				filterUrlMap.put(params.substring(1), filterDataType[0]);
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
				        			else if(internalParams.endsWith(".gif"))
				        				filterUrlMap.put(internalParams, filterDataType[6]);
				        			else
				        				filterUrlMap.put(internalParams, filterDataType[4]);
				                	
			            		}else{
			            			if(internalParams.endsWith(".jpg") || internalParams.endsWith(".jpeg"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[1]);
				        			else if(internalParams.endsWith(".png"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[2]);
				        			else if(internalParams.endsWith(".gif"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[6]);
				        			else
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[4]);
				                		
			            		}
			                	
			            		
			            	}catch(Exception e){
			            		e.printStackTrace();
			            	}finally{
			            		
			            	}
			            	
			            	return true;
			            }});
				 
				 doc.$("script").each(new JerryFunction() {
			            public Boolean onNode(Jerry $this, int index) {

			            	try{
			            		String internalParams = $this.attr("src")==null?"":$this.attr("src");
			            		
			            		if(internalParams.startsWith("http")){
			            			if(internalParams.endsWith(".js"))
				        				filterUrlMap.put(internalParams, filterDataType[5]);
				                	
			            		}else{
			            			if(internalParams.endsWith(".js"))
				        				filterUrlMap.put(webSiteUrl+internalParams, filterDataType[5]);
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
		            		String internalParams = $this.attr("href")==null || $this.attr("href")==""?"/":$this.attr("href");
		                	
		            		if(internalParams.charAt(0)!='/')
		            			internalParams = "/" + internalParams;
		            		
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
 		int responseCode = -1;
 		try{
 			System.out.print(urlProcessingCounter++ +  " - Processing : " + url);
 			URL obj = new URL(url);
 	 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

 	 		con.setRequestMethod("GET");

 	 		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
 	 		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
 	 		con.setRequestProperty("Cookie", cookieString);

 	 		responseCode = con.getResponseCode();
 	 		
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
 		}finally{
 			System.out.print(", Response Code : " + responseCode + "\n");
 		}
 		
 		return response.toString();

 	}


}

