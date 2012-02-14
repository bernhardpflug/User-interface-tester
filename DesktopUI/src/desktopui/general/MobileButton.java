package desktopui.general;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.JXButton;

@SuppressWarnings("serial")
public class MobileButton extends JXButton {

	ImageIcon iconEnabled, iconDisabled;
	
	public MobileButton(String enabledIcon, String disabledIcon) {
		super();
		
		iconEnabled = new ImageIcon(getClass().getResource(enabledIcon));
		iconDisabled = new ImageIcon(getClass().getResource(disabledIcon));
	}
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		if (enabled) {
			setIcon(iconEnabled);
		}
		else {
			setIcon(iconDisabled);
		}
	}
}
