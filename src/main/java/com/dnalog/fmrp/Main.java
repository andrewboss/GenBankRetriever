package com.dnalog.fmrp;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dnalog.fmrp.biodb.BioDBnetImpl;
import com.dnalog.fmrp.genbank.GenBankParser;
import com.dnalog.fmrp.google.GoogleDriveParser;
import com.dnalog.fmrp.google.GoogleSpreadsheetClient;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/* Initialise main variables */
		
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(new File("").getAbsolutePath() + "/src/com/dnalog/fmrp/config.properties"));
		} catch(Exception e) {
			prop.load(new FileInputStream(new File("").getAbsolutePath() + "/config.properties"));
		}
		
		/* Initialise main objects */
		
		HTTPBrowser httpClient = new HTTPBrowser();
		GenBankParser genBank = new GenBankParser();
		GoogleSpreadsheetClient gSpreadsheets = new GoogleSpreadsheetClient(prop);
		GoogleDriveParser gDrive = new GoogleDriveParser();
		String action = "insert";
		
		/* Prepare new row to be inserted into google spreadsheet (data comes from the command line) */
		
		ListEntry newRow = gSpreadsheets.getNewEmptyRow();
		
		for(String arg: args) {
			
			String[] setting = arg.split("=");
			
			if(!setting[0].equalsIgnoreCase("action")) {
				gSpreadsheets.setValue(newRow, setting[0], setting[1]);
			} else {
				action = setting[1].trim().toLowerCase();
			}
		}
			
		if(action.equals("insert")) {
			
			/* Read accession ID and try to find sequence in GenBank */
			
			String accessionID = newRow.getCustomElements().getValue("accessionid");
			String genBankURL = prop.getProperty("GENBANK_SEARCH_URL") + accessionID;
			System.out.println("Connecting to " + genBankURL);
			genBank.connect(genBankURL);
			String uID = genBank.extractUID();
			String tempFilePath = prop.getProperty("TEMP_FILE_PATH") + accessionID + ".fasta";
			
			if(uID.length() > 0) {
				
				System.out.println("Sequence found! GenBank UID: " + uID + "\nDownloading FASTA sequence file...");
				
				httpClient.saveAsFile(
					prop.getProperty("GENBANK_FASTA_DOWNLOAD_URL") + uID, 
					tempFilePath
				);
				
				System.out.println("FASTA File successfully downloaded: " + tempFilePath);
			
				/* Get last sequence ID */
				
				System.out.println("Please wait, getting latest information from Google Spreadsheets...");
				
				ListEntry row = gSpreadsheets.getLastRow(prop.getProperty("GOOGLE_MASTER_SPREADSHEET"), 0, "ID");
				int lastRowId = Integer.parseInt(gSpreadsheets.getValue(row, "ID"));
				int newRowId = lastRowId + 1;
				
				System.out.println("Last sequence ID: " + lastRowId);
				
				System.out.println("Copying file to Google Drive...");
				CopyFile.copyFile(tempFilePath, prop.getProperty("GOOGLE_DRIVE_MRNA_SEQUENCES_FOLDER_LOCAL_PATH") + Integer.toString(newRowId) + ".fasta");
				
				System.out.println("Wait when file will be synchronised with Google Drive...");
				
				/* Connect to google drive to get its URL */
				
				gDrive.connect(prop.getProperty("GOOGLE_DRIVE_MRNA_SEQUENCES_FOLDER_URL"));
				int retries = 0;
				
				while(!gDrive.fileExists(Integer.toString(newRowId) + ".fasta") && retries <= 100) {
					retries++;
					gDrive.connect(prop.getProperty("GOOGLE_DRIVE_MRNA_SEQUENCES_FOLDER_URL"));
					System.out.println("Checking if file was successfully synchronised... " + retries);
					Thread.sleep(5000);
				}
				
				String fileUrl = gDrive.getFileURL(Integer.toString(newRowId) + ".fasta");
				
				if(fileUrl.length() > 0) {
					
					System.out.println("File was successfully synchronised with Google Drive");
					
					/* Add new row to spreadsheet */
					
					System.out.println("Updating Google Spreadsheet with new entry...");
					
					SpreadsheetEntry spreadsheet = gSpreadsheets.getSpreadsheet(prop.getProperty("GOOGLE_MASTER_SPREADSHEET"));
					WorksheetEntry worksheet = gSpreadsheets.getWorksheet(spreadsheet, 0);
					
					gSpreadsheets.setValue(newRow, "ID", Integer.toString(newRowId));
					gSpreadsheets.setValue(newRow, "mRNASequenceFileURL", fileUrl);
					gSpreadsheets.setValue(newRow, "mRNASequenceFileFormat", "Fasta");
					gSpreadsheets.setValue(newRow, "GenBankURL", prop.getProperty("GENBANK_SEARCH_URL") + uID);
					
					newRow = gSpreadsheets.insertRow(worksheet, newRow, "ID");
					
					System.out.println("Google Spreadsheet was successfully updated");
					
					
				} else {
					System.out.println("Something went wrong, check if Google Drive is properly mounted");
				}
				
			} else {
				System.out.println("Unfortunately sequence could not be found in GenBank");
			}
			
		} else if(action.equals("updatecds")) {
			
			SpreadsheetEntry spreadsheet = gSpreadsheets.getSpreadsheet(prop.getProperty("GOOGLE_MASTER_SPREADSHEET"));
			WorksheetEntry worksheet = gSpreadsheets.getWorksheet(spreadsheet, 0);
			List<ListEntry> rows = (List<ListEntry>)gSpreadsheets.getRows(worksheet, "ID");
			
			for (ListEntry row : rows) {
				
				int rowID = Integer.parseInt(gSpreadsheets.getValue(row, "ID"));
				String accessionID = (String)gSpreadsheets.getValue(row, "AccessionID");
				System.out.println("Processing sequence with ID: " + rowID);
				
				String genBankURL = prop.getProperty("GENBANK_SEARCH_URL") + accessionID;
				System.out.println("Retrieving data from: " + genBankURL);
				genBank.connect(genBankURL);
				String uID = genBank.extractUID();
				System.out.println("Got UID: " + uID);
				
				String genBankSummaryURL = prop.getProperty("GENBANK_SUMMARY_URL") + uID;
				System.out.println("Retrieving GENBANK summary data from: " + genBankSummaryURL);
				genBank.connect(genBankSummaryURL);
				
				int[] cds = genBank.extractCDSCoordinates();
				
				System.out.println("Updating CDS to: " + cds[0] + ".." + cds[1]);
				
				
				gSpreadsheets.setValue(row, "CDS", cds[0] + ".." + cds[1]);
				
				row.update();
			}
        } else if(action.equals("updateest")) {

            SpreadsheetEntry spreadsheet = gSpreadsheets.getSpreadsheet(prop.getProperty("GOOGLE_MASTER_SPREADSHEET"));
            WorksheetEntry worksheet = gSpreadsheets.getWorksheet(spreadsheet, 0);
            List<ListEntry> rows = (List<ListEntry>)gSpreadsheets.getRows(worksheet, "ID");

            for (ListEntry row : rows) {

                int rowID = Integer.parseInt(gSpreadsheets.getValue(row, "ID"));
                String accessionID = (String)gSpreadsheets.getValue(row, "AccessionID");
                System.out.println("Processing sequence with ID: " + rowID);

                BioDBnetImpl bioDBClient = new BioDBnetImpl();

                String db2dbResult = bioDBClient.db2db("GenBank Nucleotide Accession", accessionID, "Ensembl Transcript ID", "");

                Pattern p = Pattern.compile(".*\\s+(ENST.*)\\s+");
                Matcher m = p.matcher(db2dbResult);

                if (m.find()) {

                    String est = m.group(1);
                    String[] ests = m.group(1).split(";");
                    int estNum = (ests.length > 0) ? ests.length : 1;

                    System.out.println("Found EST: " + m.group(1));
                    System.out.println("EST num: " + estNum);
                    System.out.println("Updating EST...");

                    gSpreadsheets.setValue(row, "EST", m.group(1));
                    row.update();
                    gSpreadsheets.setValue(row, "ESTNum", estNum + "");
                    row.update();

                } else {
                    gSpreadsheets.setValue(row, "EST", "");
                    gSpreadsheets.setValue(row, "ESTNum", "0");
                }


            }
        } else if(action.equals("findest")) {

            SpreadsheetEntry spreadsheet = gSpreadsheets.getSpreadsheet(prop.getProperty("GOOGLE_MASTER_SPREADSHEET"));
            WorksheetEntry worksheet = gSpreadsheets.getWorksheet(spreadsheet, 0);
            List<ListEntry> rows = (List<ListEntry>)gSpreadsheets.getRows(worksheet, "ID");

            for (ListEntry row : rows) {

                int rowID = Integer.parseInt(gSpreadsheets.getValue(row, "ID"));
                String accessionID = (String)gSpreadsheets.getValue(row, "AccessionID");
                String est = (String)gSpreadsheets.getValue(row, "EST");
                System.out.println("Processing sequence with ID: " + rowID);

                BioDBnetImpl bioDBClient = new BioDBnetImpl();

                String db2dbResult = bioDBClient.db2db("GenBank Nucleotide Accession", accessionID, "Ensembl Transcript ID", "");

                Pattern p = Pattern.compile(".*\\s+(ENST.*)\\s+");
                Matcher m = p.matcher(db2dbResult);

                if (m.find()) {
                    System.out.println("Found EST: " + m.group(1));
                    System.out.println("Updating EST...");
                    gSpreadsheets.setValue(row, "EST", m.group(1));
                    row.update();
                }


            }
        }
	}
}
