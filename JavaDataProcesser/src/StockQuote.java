
public class StockQuote {
	double open;
	double close;
	double high;
	double low;
	int volume;
	
	public StockQuote() {
	}
	
	public void setOpen(double open) {
		this.open = open;
	}
	public double getOpen() {
		return open;
	}

	public void setClose(double close) {
		this.close = close;
	}
	public double getClose() {
		return close;
	}
	
	public void setHigh(double high) {
		this.high = high;
	}
	public double getHigh() {
		return high;
	}
	
	public void setLow(double low) {
		this.low = low;
	}
	public double getLow() {
		return low;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getVolume() {
		return volume;
	}
	
	public double getDifference() {
		return close - open;
	}
}

