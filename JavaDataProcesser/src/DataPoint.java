
public class DataPoint {

	private NewsEvent event;
	private Boolean result;
	
	public DataPoint(NewsEvent event, Boolean result) { 
		this.event = event;
		this.result = result;
	}
	
	public NewsEvent getEvent() {
		return event;
	}
	public void setEvent(NewsEvent event) {
		this.event = event;
	}
	
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
}
