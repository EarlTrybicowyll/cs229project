import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SAHeadlinesProcesser {
	private List<NewsEvent> newsEvents = new ArrayList<NewsEvent>();
	private SymbolDictionary dict = new SymbolDictionary("test");

	public SAHeadlinesProcesser(String filename) {
		readFile(filename, "IBM");
	}

	public List<NewsEvent> getNewsEvents() {
		return newsEvents;
	}
	
	public void readFile(String filename, String company) {
		// Assumes the file is formatted as:
		// Headline
		// Author - Date - garbage
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			NewsEvent currentEvent;
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				line = line.trim();
				if(line.length()==0) {
					// skip blank lines and headlines that had no valid symbols in them
					continue;
				}

				// set the headline
				List<Integer> headline = parseHeadline(line, dict);
				if(headline.size() > 0) {
					currentEvent = new NewsEvent("IBM", "Seeking Alpha", headline);
					String line2 = br.readLine();
					currentEvent.setDate(parseDate(line2));
					if(currentEvent.getDate() != null) 
						newsEvents.add(currentEvent);
					//System.out.println(currentEvent);
				} else {
					br.readLine(); // skip next line
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Date parseDate(String line) {
		String[] symbols = line.split("•");
		String dateline = symbols[1];
		dateline.trim();
		dateline = dateline.substring(6);
		dateline = dateline.toLowerCase();
		Date date = null;
		try {
			date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(dateline);
		} catch (ParseException e) {
			try {
				date = new SimpleDateFormat("MMM dd ", Locale.ENGLISH).parse(dateline);
				date.setYear(113);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		return date;
	}

	public List<Integer> parseHeadline(String line, SymbolDictionary dict) {
		List<Integer> headline = new ArrayList<Integer>();
		String[] symbols = line.split(" ");
		for(int i=0; i<symbols.length; i++) {
			String symbol = symbols[i];
			symbol = symbol.trim();
			symbol = symbol.toLowerCase();
			char lastchar = symbol.charAt(symbol.length()-1);
			if(lastchar == 's' ) {
				symbol = symbol.substring(0,symbol.length()-1);
			}
			if(dict.getSymbol(symbol) != 0) {
				headline.add(dict.getSymbol(symbol));
			}
		}

		return headline;
	}
}
