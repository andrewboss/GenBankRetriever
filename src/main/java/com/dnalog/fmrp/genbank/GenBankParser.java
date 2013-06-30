package com.dnalog.fmrp.genbank;

import com.dnalog.fmrp.HTTPBrowser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenBankParser {
	
	String html;
	
	public GenBankParser() {
	}
	
	public void connect(String url) throws IOException {
		HTTPBrowser httpClient = new HTTPBrowser();
		this.html = httpClient.getHTML(url);
	}
	
	public String extractUID() throws Exception {
		
		String result = "";
		Pattern UID_PATTERN = Pattern.compile("<input name=\"EntrezSystem2.PEntrez.Nuccore.Sequence_ResultsPanel.Sequence_DisplayBar.uid\" sid=\"1\" type=\"hidden\" value=\"(\\d+)\" />");
		
		Matcher m = UID_PATTERN.matcher(html);
		
		if (m.find()) {
			result = m.group(1);
		} else {
			result = "";
		}
		
		return result;
	}
	
	public int[] extractCDSCoordinates() throws Exception {
		
		Pattern CDS_PATTERN = Pattern.compile("from=(\\d+)&amp;to=(\\d+)\">CDS</a>");
		int[] cds = new int[2];
		
		Matcher m = CDS_PATTERN.matcher(html);
		
		if (m.find()) {
			cds = new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))};
		} else {
			cds = new int[]{0, 0};
		}
		
		return cds;
	}
}
