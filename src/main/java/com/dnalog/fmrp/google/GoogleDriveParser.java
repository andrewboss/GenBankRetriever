package com.dnalog.fmrp.google;

import com.dnalog.fmrp.HTTPBrowser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleDriveParser {
	
	String html;
	
	public GoogleDriveParser() {
	}
	
	public void connect(String url) throws IOException {
		try {
			HTTPBrowser httpClient = new HTTPBrowser();
			this.html = httpClient.getHTML(url);
		} catch(Exception e){
			this.html = "";
		}
	}
	
	public boolean fileExists(String fileName) throws Exception {
		
		boolean result = false;
		Pattern LINK_PATTERN = Pattern.compile("\"name\":\""+fileName+"\",\"objectId\":\"[0-9a-zA-Z]+\",\"openUrl\":\"https:[\\/]?[^\" ]+\\/edit");
		
		Matcher m = LINK_PATTERN.matcher(html);
		if (m.find()) {
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
	
	public String getFileURL(String fileName) {
		
		String result = "";
		Pattern LINK_PATTERN = Pattern.compile("\"name\":\""+fileName+"\",\"objectId\":\"[0-9a-zA-Z]+\",\"openUrl\":\"(https:[\\/]?[^\" ]+\\/edit)");
		
		Matcher m = LINK_PATTERN.matcher(html);
		if (m.find()) {
			result = m.group(1);
		} else {
			result =  "";
		}
		
		return result.replaceAll("\\\\", "");
	}
}
