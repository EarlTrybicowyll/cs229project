import java.util.Date;
import java.util.List;


public class DataProcessingRunner {
	
	public static void main(String[] args) {
		SymbolDictionary symbolDictionary = new SymbolDictionary("english.dat");
		SAHeadlinesProcesser processer = new SAHeadlinesProcesser("SA_IBMHeadlines.txt");
		List<NewsEvent> events = processer.getNewsEvents();
		FinancialDataProcesser fdp = new FinancialDataProcesser("IBM", "IBMHistoricalQuotes.csv");
		for(NewsEvent event : events) {
			Date date = event.getDate();
		}
		
	}

}
