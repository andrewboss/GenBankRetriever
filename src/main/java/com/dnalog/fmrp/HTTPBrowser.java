package com.dnalog.fmrp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

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
	
	public String getHTML(String url) throws Exception {

		this.setURL(url);
		
		return this.getHTML();
	}

	public String getHTML() throws Exception {

        String result = "";

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HTTPRedirectHandler handler = new HTTPRedirectHandler();
        httpclient.setRedirectHandler(handler);

        HttpGet get = new HttpGet(url.toURI());

        HttpResponse response = httpclient.execute(get);
        HttpEntity responseEntity = response.getEntity();

        if(handler.lastRedirectedUri != null) {
            System.out.println("Redirected to: " + handler.lastRedirectedUri.toString());
        }

        if(responseEntity!=null) {
            result = EntityUtils.toString(responseEntity);
        }

        return result;
	}
	
	public void saveAsFile(String url, String fullFilePath) throws Exception {
		
		this.setURL(url);
		
		this.saveAsFile(fullFilePath);
	}

	public void saveAsFile(String fullFilePath) throws Exception {

		File output = new File(fullFilePath);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HTTPRedirectHandler handler = new HTTPRedirectHandler();
        httpclient.setRedirectHandler(handler);

        HttpGet get = new HttpGet(url.toURI());

        HttpResponse response = httpclient.execute(get);

        if(handler.lastRedirectedUri != null) {
            System.out.println("Redirected to: " + handler.lastRedirectedUri.toString());
        }
		
		//InputStream in = this.url.openStream();

		try {
			OutputStream out = new FileOutputStream(output);
			try {
				copy(response.getEntity().getContent(), out);
			} finally {
				out.close();
			}
		} finally {
			//in.close();
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
