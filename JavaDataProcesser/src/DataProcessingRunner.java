import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DataProcessingRunner {
	
	public static void main(String[] args) {
		SymbolDictionary symbolDictionary = new SymbolDictionary("english.dat");
		List<String> companies = Arrays.asList("IBM", "GOOG", "NFLX", "SHLD");
		List<DataPoint> masterData = new ArrayList<DataPoint>();
		for(String company : companies) {
			SAHeadlinesProcesser processer = new SAHeadlinesProcesser("SA_" + company + "Headlines.txt");
			List<NewsEvent> events = processer.getNewsEvents();
			events = pruneEvents(events);
			FinancialDataProcesser fdp = new FinancialDataProcesser(company, company + "HistoricalQuotes.csv");
			for(NewsEvent event : events) {
				Boolean result = getCorrectResult(event, fdp);
				DataPoint newPoint = new DataPoint(event, result);
				masterData.add(newPoint);
			}
		}
		
		Collections.shuffle(masterData);
		List<DataPoint> trainData = new ArrayList<DataPoint>();
		List<DataPoint> testData = new ArrayList<DataPoint>();
		
		for(int i=0; i<masterData.size(); i++) {
			if(i<0.7*masterData.size()) {
				trainData.add(masterData.get(i));
			} else {
				testData.add(masterData.get(i));
			}
		}

		BayesianClassifier bc = new BayesianClassifier(symbolDictionary);
		System.out.println("Training on a set of size " + trainData.size());
		bc.train(trainData);
		
		List<Boolean> results = bc.test(testData);
		compareResults(testData, results);
		
	}
	
	/**
	 * Removes all events that did not occur within the last month
	 * @return
	 */
	public static List<NewsEvent> pruneEvents(List<NewsEvent> events) {
		List<NewsEvent> prunedEvents = new ArrayList<NewsEvent>();
		Date lastDate = null;
		Calendar cal = Calendar.getInstance();
		try {
			lastDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse("8/15/2013");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(lastDate);
		Calendar extraCal = Calendar.getInstance();
		for(NewsEvent event : events) {
			extraCal.setTime(event.getDate());
			if(cal.after(extraCal))
				prunedEvents.add(event);
		}
		System.out.println("Pruned size is " + prunedEvents.size() + " of " + events.size());
		return prunedEvents;
	}
	
	public static void compareResults(List<DataPoint> dataPoints, List<Boolean> results) {
		double error = 0;
		for(int i=0; i< dataPoints.size(); i++) {
			if(dataPoints.get(i).getResult() != results.get(i)) {
				error++;
			}
		}
		System.out.println("Testing error of " + error / dataPoints.size() + " on a test set of size " + dataPoints.size());
	}
	
	public static boolean getCorrectResult(NewsEvent event, FinancialDataProcesser fdp) {
		Calendar cal = Calendar.getInstance();
		Date date = event.getDate();
		cal.setTime(date);
		StockQuote startQuote = fdp.getQuote(date);
		cal.add(Calendar.MONTH, 3);
		Date endDate = cal.getTime();
		//System.out.println("end date: " + endDate);
		StockQuote endQuote = fdp.getQuote(endDate);
		
		double priceDifference = endQuote.getClose() - startQuote.getClose();
		boolean result = priceDifference > 0;
		return result;
	}
	
	

}
