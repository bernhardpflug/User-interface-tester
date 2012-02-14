package at.mkw.android.event;

public class ButtonEvent {

private String target;
	
	public ButtonEvent(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}
}
