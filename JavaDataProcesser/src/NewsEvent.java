import java.util.List;
import java.util.Date;

public class NewsEvent {

	String company;
	String sourceName;
	List<Integer> titleSymbols;
	Date date;
	
	public NewsEvent(String company, String sourceName, List<Integer> titleSymbols) {
		this.company = company;
		this.sourceName = sourceName;
		this.titleSymbols = titleSymbols;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public List<Integer> getTitleSymbols() {
		return titleSymbols;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return company + ": " + titleSymbols + " on " + date + " by " + sourceName;
	}
}
