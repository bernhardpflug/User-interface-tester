package model;

import java.util.Iterator;
import java.util.Map.Entry;

import model.Button.KlickDuration;

import org.w3c.dom.Element;

public class Scenario extends ScreenCollection{

	public enum Device {
		MOBILE,
		DESKTOP
	}
	private static final String DEVICE = "device";
	private Device device;
	
	public Scenario() {
		super(null);
	}
	
	@Override
	public void applyXmlValues(Element screenElem) {
		super.applyXmlValues(screenElem);
		
		setDevice(Device.valueOf(screenElem.getAttribute(DEVICE).toUpperCase()));
	}
	
	public Window getStartElement() {
		return collectionItems.get(0);
	}
	
	public Screen getStartScreen() {
		return ModelUtils.getActiveScreen(getStartElement());
	}
	
	public Screen getScreen(String targetName) {
		return ModelUtils.getActiveScreen(getTarget(targetName));
	}
	
	private void validateTargets(Window element) {
		
		if (element instanceof ScreenCollection) {
			
			for (Window window : ((ScreenCollection) element).getWindowItems()) {
				validateTargets(window);
			}
		}
		else {
			
			Screen screen = (Screen)element;
			
			for (Button button : screen.getButtons()) {
				
				Iterator<Entry<KlickDuration, String>> klicks = button.getKlicks();
				
				while (klicks.hasNext()) {
					Entry<KlickDuration, String> klick = klicks.next();
					
					Window target = this.getTarget(klick.getValue());
					
					if (target==null) {
						throw new IllegalStateException("Bad target "+klick.getValue()+" in button (type "+klick.getKey()+") of screen "+screen.getFullId());
					}
//					else if (target instanceof ScreenCollection) {
//						throw new IllegalStateException("target "+klick.getValue()+" in button (type "+klick.getKey()+") of screen "+screen.getFullId()+" is not allowed to be a screencollection");
//					}
				}
			}
		}
	}
	
	public void validateTargets() {
		validateTargets(getRoot());
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}
}
