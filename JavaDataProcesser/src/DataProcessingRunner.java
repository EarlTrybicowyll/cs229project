import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DataProcessingRunner {
	
	public static void main(String[] args) {
		SymbolDictionary symbolDictionary = new SymbolDictionary("english.dat");
		SAHeadlinesProcesser processer = new SAHeadlinesProcesser("SA_NFLXHeadlines.txt");
		List<NewsEvent> events = processer.getNewsEvents();
		Collections.shuffle(events);
		FinancialDataProcesser fdp = new FinancialDataProcesser("GOOG", "NFLXHistoricalQuotes.csv");
		List<NewsEvent> trainData = new ArrayList<NewsEvent>();
		List<NewsEvent> testData = new ArrayList<NewsEvent>();
		// remove any data that is not recent enough to evaluate (i.e. within the past month)
		List<NewsEvent> prunedEvents = new ArrayList<NewsEvent>();
		Date lastDate = null;
		Calendar cal = Calendar.getInstance();
		try {
			lastDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse("10/15/2013");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(lastDate);
		Calendar extraCal = Calendar.getInstance();
		System.out.println("last date: " + lastDate);
		for(NewsEvent event : events) {
			//System.out.println("event:  " + event.getDate());
			//System.out.println("cal event: " + cal.getTime());
			extraCal.setTime(event.getDate());
			if(cal.after(extraCal))
				prunedEvents.add(event);
		}
		System.out.println("Pruned size is " + prunedEvents.size() + " of " + events.size());
		
		events = prunedEvents;
		
		for(int i=0; i<events.size(); i++) {
			if(i<0.7*events.size()) {
				trainData.add(events.get(i));
			} else {
				testData.add(events.get(i));
			}
		}
		
		List<Boolean> trainResults = new ArrayList<Boolean>();
		List<Boolean> testResults = new ArrayList<Boolean>();
		//System.out.println(fdp.quotes.keySet());
		
		for(NewsEvent event : trainData) {
			// check to see if the price increased in 1 month or not
			boolean result = getCorrectResult(event, cal, fdp);
			//System.out.println("train data: " + event + " is " + result);
			trainResults.add(result);
		}
		for(NewsEvent event : testData) {
			boolean result = getCorrectResult(event, cal, fdp);
			testResults.add(result);
		}
		
		BayesianClassifier bc = new BayesianClassifier(symbolDictionary);
		System.out.println("Training on a set of size " + trainData.size());
		bc.train(trainData, trainResults);
		
		List<Boolean> results = bc.test(testData);
		compareResults(testResults, results);
		
	}
	
	public static void compareResults(List<Boolean> realResults, List<Boolean> results) {
		double error = 0;
		for(int i=0; i< realResults.size(); i++) {
			if(realResults.get(i) != results.get(i)) {
				error++;
			}
		}
		System.out.println("Testing error of " + error / realResults.size() + " on a test set of size " + realResults.size());
	}
	
	public static boolean getCorrectResult(NewsEvent event, Calendar cal, FinancialDataProcesser fdp) {
		Date date = event.getDate();
		cal.setTime(date);
		StockQuote startQuote = fdp.getQuote(date);
		cal.add(Calendar.MONTH, 1);
		Date endDate = cal.getTime();
		//System.out.println("end date: " + endDate);
		StockQuote endQuote = fdp.getQuote(endDate);
		
		double priceDifference = endQuote.getClose() - startQuote.getClose();
		boolean result = priceDifference > 0;
		return result;
	}
	
	

}
