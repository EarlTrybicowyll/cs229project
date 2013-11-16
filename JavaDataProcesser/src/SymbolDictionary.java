import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SymbolDictionary {
	private Map<String, Integer> symbolMap = new HashMap<String, Integer>();
	
	public SymbolDictionary(String filename) {
		// do nothing with filename yet;
		defaultSymbolMap();
	}
	
	public Integer getSymbol(String word) {
		if(symbolMap.containsKey(word)) {
			return symbolMap.get(word);
		}
		return 0;
	}
	
	public void defaultSymbolMap() {
		List<String> symbolList = Arrays.asList("buy", "sell", "good", "growth", "beat", "new", "stop", "acquire", "disappointing");
		for(int i=0; i<symbolList.size(); i++) {
			symbolMap.put(symbolList.get(i), i+1);
		}
//		symbolMap.put("buy", 1);
//		symbolMap.put("sell", 2);
//		symbolMap.put("cheap", 3);
//		symbolMap.put("good", 4);
//		symbolMap.put("poor", 5);
//		symbolMap.put("bad",6);
//		symbolMap.put("IPO", 7);
//		symbolMap.put("overvalued", 8);
//		symbolMap.put("fiasco", 9);
//		symbolMap.put("undervalued", 10);
//		symbolMap.put("trail", 11);
//		symbolMap.put("loom", 13);
//		symbolMap.put("meet", 14);
//		symbolMap.put("exceed", 15);
//		symbolMap.put("beat", 16);
//		symbolMap.put("fail", 17);
//		symbolMap.put("fell", 18);
//		symbolMap.put("rose", 19);
//		symbolMap.put("growth", 12);
//		symbolMap.put("failure", 20);
//		symbolMap.put("great", 21);
//		symbolMap.put("jump", 22);
//		symbolMap.put("impres", 23);
//		symbolMap.put("threat", 24);
//		symbolMap.put("monopoly", 25);
//		symbolMap.put("concern", 26);
//		symbolMap.put("lead", 27);
//		symbolMap.put("mis", 28);
//		launch / ed
//		failing
//		former
	}
	
	public Collection<String> getSymbols() {
		return symbolMap.keySet();
	}
	
	public int size() {
		return getSymbols().size();
	}
}
