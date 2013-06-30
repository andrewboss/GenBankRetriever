package com.dnalog.fmrp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPBrowser {

	private URL url;

	public HTTPBrowser(String url) throws MalformedURLException {
		this.setURL(url);
	}
	
	public HTTPBrowser() throws MalformedURLException {
		
	}
	
	public void setURL(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	private String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}
	
	public String getHTML(String url) throws IOException {

		this.setURL(url);
		
		return this.getHTML();
	}

	public String getHTML() throws IOException {

		InputStream in = this.url.openStream();
		String result = this.convertStreamToString(in);
		in.close();
		
		return result;
	}
	
	public void saveAsFile(String url, String fullFilePath) throws Exception {
		
		this.setURL(url);
		
		this.saveAsFile(fullFilePath);
	}

	public void saveAsFile(String fullFilePath) throws Exception {

		File output = new File(fullFilePath);
		
		
		InputStream in = this.url.openStream();

		try {
			OutputStream out = new FileOutputStream(output);
			try {
				copy(in, out);
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	private void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount == -1) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

}
