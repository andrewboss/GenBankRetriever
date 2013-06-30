package com.dnalog.fmrp.google;

import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class GoogleSpreadsheetClient {

	private Properties prop;
	private SpreadsheetService service;
	private URL SPREADSHEET_FEED_URL;
	SpreadsheetFeed feed;

	public GoogleSpreadsheetClient(Properties prop) throws IOException, ServiceException {
		
		this.prop = prop;
		
		this.service = new SpreadsheetService("MySpreadsheetIntegration-v1"); 
		this.service.setUserCredentials(this.prop.getProperty("GOOGLE_API_USER"), this.prop.getProperty("GOOGLE_API_PASSWORD"));
		this.SPREADSHEET_FEED_URL = new URL(this.prop.getProperty("GOOGLE_SPREADSHEET_FEED_URL"));
		this.feed = this.service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
	}
	
	public SpreadsheetEntry getSpreadsheet(String spreadsheetName) {
		
		// Make a request to the API and get all spreadsheets.
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		
		for (SpreadsheetEntry spreadsheet : spreadsheets) {

			if( spreadsheet.getTitle().getPlainText().equals(spreadsheetName)) {
				return spreadsheet;
			}
		}
		
		return null;
	}
	
	public WorksheetEntry getWorksheet(SpreadsheetEntry spreadsheet, int index) throws IOException, ServiceException {
		
		List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
		return worksheets.get(index);
	}
	
	public List<ListEntry> getRows(WorksheetEntry worksheet, String orderBy) throws URISyntaxException, IOException, ServiceException {
		
	    URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString() + "?orderby=" + orderBy).toURL();
	    ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		
		return listFeed.getEntries();
	}
	
	public ListEntry getLastRow(String spreadsheetName, int worksheetNumber, String orderBy) throws IOException, ServiceException, URISyntaxException {
		
		SpreadsheetEntry spreadsheet = this.getSpreadsheet(spreadsheetName);
		WorksheetEntry worksheet = this.getWorksheet(spreadsheet, worksheetNumber);
		List<ListEntry> rows = this.getRows(worksheet, orderBy);
		
		return rows.get(rows.size() - 1);
	}
	
	public String getValue(ListEntry row, String columnName) {
		for (String tag : row.getCustomElements().getTags()) {
			if(tag.toLowerCase().equals(columnName.toLowerCase())) {
				return row.getCustomElements().getValue(tag);
			}
	     }
		return "";
	}
	
	public ListEntry getNewEmptyRow() {
		 
		ListEntry row = new ListEntry();
		 
		 return row;
	}
	
	public void setValue(ListEntry row, String columnName, String columnValue) {
		
		row.getCustomElements().setValueLocal(columnName.toLowerCase(), columnValue);
	}
	
	public ListEntry insertRow(WorksheetEntry worksheet, ListEntry row, String orderBy) throws IOException, ServiceException, URISyntaxException {
		URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString() + "?orderby=" + orderBy).toURL();
		row = service.insert(listFeedUrl, row);
		
		return row;
	}

	public void printSpreadsheet(String spreadsheetName) throws AuthenticationException, MalformedURLException, IOException, ServiceException, URISyntaxException{
		
		// Make a request to the API and get all spreadsheets.
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		// Iterate through all of the spreadsheets returned
		for (SpreadsheetEntry spreadsheet : spreadsheets) {

			if( spreadsheet.getTitle().getPlainText().equals(spreadsheetName)) {
				System.out.println("Found: " + spreadsheet.getTitle().getPlainText());

				// Make a request to the API to fetch information about all
				// worksheets in the spreadsheet.
				List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();
				
				WorksheetEntry worksheet = worksheets.get(0);
				String title = worksheet.getTitle().getPlainText();
				int rowCount = worksheet.getRowCount();
				int colCount = worksheet.getColCount();

				// Print the fetched information to the screen for this worksheet.
				System.out.println("Spreadsheet rows:" + rowCount + "; cols: " + colCount);
				
			    //URL listFeedUrl = worksheet.getListFeedUrl();
			    URL listFeedUrl = new URI(worksheet.getListFeedUrl().toString() + "?orderby=ID").toURL();
			    ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

			    // Iterate through each row, printing its cell values.
			    for (ListEntry row : listFeed.getEntries()) {
			      // Print the first column's cell value
			      System.out.print(row.getTitle().getPlainText() + "\t");
			      // Iterate over the remaining columns, and print each cell value
			      for (String tag : row.getCustomElements().getTags()) {
			        System.out.print(row.getCustomElements().getValue(tag) + "\t");
			      }
			      System.out.println();
			    }
			}
		}
	}
}
