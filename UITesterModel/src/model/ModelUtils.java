package model;

import java.util.ArrayList;

public class ModelUtils {

//	public static Screen getActiveScreen(ScreenCollection col, ScreenCollection.Type typeRestriction) {
//		
//		if (col.getActiveChild() instanceof ScreenCollection) {
//			
//			if (typeRestriction == null || col.getType().equals(typeRestriction)) {
//				return getActiveScreen(((ScreenCollection)col.getActiveChild()),typeRestriction);
//			}
//			else {
//				throw new RuntimeException(col+" breaks type restriction rule "+typeRestriction);
//			}
//		}
//		else if (col.getActiveChild() instanceof Screen) {
//			return (Screen)col.getActiveChild();
//		}
//		return null;
//	}
	
	/**
	 * returns only real screens, goes through screen collections and sliders
	 * For collections and sliders always active child is considered
	 */
	public static Screen getActiveScreen(Window window) {
		
		if (window instanceof ScreenCollection) {
			return getActiveScreen(((ScreenCollection)window).getActiveChild());
		}
		else if (window instanceof Screen){
			return (Screen)window;
		}
		else {
			throw new RuntimeException("unkown window subclass "+window.getClass());
		}
	}
	
	/**
	 * Get slider instance this window is child of
	 * @param window to search for
	 * @return slider first found slider parent or null if non exists
	 */
	public static Slider getSlider(Window window) {
		
		if (window instanceof Slider) {
			
			if (window.getParent() != null && getSlider(window.getParent())!=null) {
				throw new RuntimeException("Found slider within slider");
			}
			return (Slider)window;
		}
		
		if (window.getParent() != null) {
			return getSlider(window.getParent());
		}
		
		return null;
	}
	
//	public static ArrayList<Screen> getSliderElements(Screen screen) {
//		
//		ArrayList<Screen> screens = new ArrayList<Screen>();
//		
//		Screen iterateScreen=screen;
//		
//		//go to the most left screen
//		while (iterateScreen.getSlider() != null) {
//			Slider slider = iterateScreen.getSlider();
//			
//			if (slider.getLeft()!= null) {
//				iterateScreen = ModelUtils.getActiveScreen(screen.getTarget(slider.getLeft()));
//			}
//			else {
//				break;
//			}
//		}
//		
//		//go as far right as possible collecting all screens
//		while (iterateScreen.getSlider() != null) {
//			
//			screens.add(iterateScreen);
//			
//			Slider slider = iterateScreen.getSlider();
//			
//			if (slider.getRight()!= null) {
//				iterateScreen = ModelUtils.getActiveScreen(screen.getTarget(slider.getRight()));
//			}
//			else {
//				break;
//			}
//		}
//		
//		return screens;
//	}
}
