package elonmeter.csv.jmeter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class GridBufferReader {
	private static final Logger log = LoggingManager.getLoggerForClass();
	private BufferedReader input;
	private static final String SEPERATOR = ",";
	 public GridBufferReader(String csvFileName) {
	        try {
	            input = new BufferedReader(new FileReader(csvFileName));
	        } catch (FileNotFoundException ex) {
	            log.error("File not found: " + ex.getMessage());
	        }
	    }
	public GridBufferReader(BufferedReader input) {
		this.input = input;
	}
	public ArrayList<String[]> getColumnsData(){
		ArrayList<String[]> columns = new ArrayList<String[]>();
		if (input != null) {
			try {
				String line;
				while ((line = input.readLine()) != null) {
					if (line.startsWith("#")) {
						continue;
					}else {
						log.info(line);
						if ("".equals(line.trim())) {
							
						}else {
							String[] lineValues = JOrphanUtils.split(line, SEPERATOR, false);
							columns.add(lineValues);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return columns;
	}
}
