import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FinancialDataProcesser {
	String company;
	Map<Date, StockQuote> quotes = new HashMap<Date, StockQuote>();
	
	public FinancialDataProcesser(String company, String filename) {
		this.company = company;
		parseCSV(filename);
	}
	
	public StockQuote getQuote(Date date) {
		// gets the quote on the first day after the given date for which there is data
		// looks at most 5 days ahead
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		for(int i=0; i<10; i++) {
			if(quotes.containsKey(date)) {
				return quotes.get(date);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
			date = cal.getTime();
		}
		System.out.println("returning null for " + date);
		return null;
	}
	
	public void parseCSV(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			br.readLine();
			br.readLine();
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				String[] data = line.split(";");
				Date date;
				date = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(data[0]);
				StockQuote quote = new StockQuote();
				quote.setClose(Double.parseDouble(data[1]));
				quote.setVolume(Integer.parseInt(data[2]));
				quote.setOpen(Double.parseDouble(data[3]));
				quote.setLow(Double.parseDouble(data[4]));
				quote.setHigh(Double.parseDouble(data[5]));
				quotes.put(date, quote);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
