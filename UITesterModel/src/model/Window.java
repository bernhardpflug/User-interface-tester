package model;


public abstract class Window extends ModelElement {

	private Window parent;

	public Window(Window parent) {
		super();

		this.parent = parent;
	}

	public Window getRoot() {

		if (this.parent != null) {
			return parent.getRoot();
		}

		return this;
	}

	private Window getTarget(String target, Window searchin, boolean activate) {

		if (target.equals(searchin.getFullId())) {
			return searchin;
		}

		if (searchin instanceof ScreenCollection) {

			if (target.startsWith(searchin.getFullId())) {

				ScreenCollection collection = (ScreenCollection) searchin;
				
				int index = 0;
				for (Window window : collection.getWindowItems()) {

					Window destination = getTarget(target, window, activate);

					if (destination != null) {
						if (activate) {
							collection.setCurrentPosition(index);
						}

						return destination;
					}

					index++;
				}

				return null;
			}
		} else if (searchin instanceof Screen) {
			return null;
		} else {
			throw new RuntimeException("unkown class");
		}

		return null;
	}

	public String getFullId() {
		if (parent != null) {
			return parent.getFullId() + "." + this.getId();
		} else {
			return this.getId();
		}
	}

	public Window getTarget(String fullidtarget) {

		Window target = getTarget(fullidtarget, getRoot(), false);

		if (target != null) {
			return target;
		}

		throw new RuntimeException("Could not find target for " + fullidtarget);
	}

	public Window activateTarget(String fullidtarget) {

		Window target = getTarget(fullidtarget, getRoot(), true);

		if (target != null) {
			return target;
		}

		throw new RuntimeException("Could not find target for " + fullidtarget);
	}

	public void setParent(Window parent) {
		this.parent = parent;
	}

	public Window getParent() {
		return parent;
	}
}
