import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BayesianClassifier {

	SymbolDictionary dict;
	int[] positiveSymbolCounts;
	int[] negativeSymbolCounts;
	int totalPositiveSymbols;
	int totalNegativeSymbols;
	int totalSymbols;
	int numberPositiveResults = 0;
	int numberNegativeResults = 0;
	
	public BayesianClassifier(SymbolDictionary dict) {
		this.dict = dict;
	}
	
	
	/** 
	 * Using the current model, makes predictions for each news event and returns them in a list.
	 * @param events
	 * @return
	 */
	public List<Boolean> test(List<NewsEvent> events) {
		List<Boolean> results = new ArrayList<Boolean>();
		for(NewsEvent event : events) {
			results.add(classifyEvent(event));
		}
		return results;
	}
	
	/**
	 * Makes a classification based on the current model for a single news event
	 * @return
	 */
	public Boolean classifyEvent(NewsEvent event) {
		double probabilityTrue = 0;
		double probabilityFalse = 0;
		
		for(Integer symbol : event.getTitleSymbols()) {
			probabilityTrue = probabilityTrue + Math.log(positiveSymbolCounts[symbol]) - Math.log(totalPositiveSymbols);
			probabilityFalse = probabilityFalse + Math.log(negativeSymbolCounts[symbol]) - Math.log(totalNegativeSymbols);
		}
		probabilityTrue = probabilityTrue + Math.log(numberPositiveResults / (0.0+numberPositiveResults + numberNegativeResults));
		probabilityFalse = probabilityFalse + Math.log(numberNegativeResults / (0.0+numberPositiveResults + numberNegativeResults));		
		return probabilityTrue > probabilityFalse;
	}
	
	
	/**
	 * Trains the model on the list of news events, assuming that the results are a boolean indicating whether 
	 * buying was a good idea or not.  Overwrites old data.
	 * @param events
	 * @param results
	 */
	public void train(List<NewsEvent> events, List<Boolean> results) {
		positiveSymbolCounts = new int[dict.size()+1];
		negativeSymbolCounts = new int[dict.size()+1];
		System.out.println("sizze: " + dict.size());
		for(int i=0; i<negativeSymbolCounts.length; i++) {
			positiveSymbolCounts[i] = 1;
			negativeSymbolCounts[i] = 1;
		}
		totalPositiveSymbols = negativeSymbolCounts.length;
		totalNegativeSymbols = negativeSymbolCounts.length;
		totalSymbols = totalPositiveSymbols + totalNegativeSymbols;
		numberPositiveResults = 0;
		numberNegativeResults = 0;
		
		for(int i=0; i< events.size(); i++) {
			NewsEvent event = events.get(i);
			Boolean result = results.get(i);
			if(result) {
				totalPositiveSymbols += addSymbols(positiveSymbolCounts, event.getTitleSymbols());
				numberPositiveResults++;
			} else {
				totalNegativeSymbols += addSymbols(negativeSymbolCounts, event.getTitleSymbols());
				numberNegativeResults++;
			}
		}
		totalSymbols = totalPositiveSymbols + totalNegativeSymbols;
		List<Integer> posList = new ArrayList<Integer>();
		List<Integer> negList = new ArrayList<Integer>();
		for(int i=0; i<positiveSymbolCounts.length; i++) {
			posList.add(positiveSymbolCounts[i]);
			negList.add(negativeSymbolCounts[i]);
		}
		System.out.println("Positive Frequencis:  " + posList);
		System.out.println("Negative Frequencis:  " + negList);
	}
	
	public int addSymbols(int[] symbolCounts, List<Integer> symbols) {
		int count = 0;
		for(Integer i : symbols) {
			count++;
			symbolCounts[i]++;
		}
		return count;
	}
}
