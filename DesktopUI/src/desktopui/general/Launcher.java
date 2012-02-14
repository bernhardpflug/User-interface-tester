package desktopui.general;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import model.ModelUtils;
import model.Scenario;
import model.Screen;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.error.ErrorInfo;

import xml.XmlParser;
import desktopui.event.ButtonEvent;
import desktopui.event.ButtonListener;

@SuppressWarnings("serial")
public class Launcher extends JXFrame implements ButtonListener {

	public static boolean DEBUG_LINKS = false;
	public static boolean MODIFY_LINKS = false;
	public static boolean LINK_TEXT = false;

	private static Launcher launcher;

	Scenario scenario;
	private static File workingDir;
	private File xmlFile, xsdFile;

	JComponent activeComponent;
	ImagePanel activeImage;
	MobileButtonBar mobileButtonBar;

	final static SplashScreen splash = SplashScreen.getSplashScreen();;

	public Launcher() {
		setLayout(new BorderLayout());
	}

	private void displayScreen(Screen screen) {

		if (activeComponent != null) {
			this.getContentPane().remove(activeComponent);
			activeImage.destroy();
		}

		activeImage = null;
		activeImage = new EffectImagePanel(screen);

		if (mobileButtonBar != null) {
			mobileButtonBar.applyBehaviour(screen);
			mobileButtonBar.setButtonListener(this);
		}
		activeImage.setButtonListener(this);

		if (screen.isScrollable()) {

			// if prev screen had no scrollpane add width diff
			if (!(activeComponent instanceof JScrollPane)) {
				setAllSizes(getWidth() + 15, getHeight());
			}

			JScrollPane scrollPane = new JScrollPane(activeImage);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			activeComponent = null;
			activeComponent = scrollPane;
			this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		} else {

			// if prev screen had scrollpane reduce width diff
			if ((activeComponent instanceof JScrollPane)) {
				setAllSizes(getWidth() - 15, getHeight());
			}

			activeComponent = null;
			activeComponent = activeImage;
			this.getContentPane().add(activeImage, BorderLayout.CENTER);
		}

		this.repaint();
		this.validate();
		activeImage.onDisplay();

		// request focus to get keyevents
		// this.requestFocus();
	}

	private void setAllSizes(int x, int y) {
		Dimension dim = new Dimension(x, y);
		setSize(dim);
		setPreferredSize(dim);
	}

	private void registerKeyListener() {

		KeyListener keyListener = new KeyListener() {

			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'd') {
					DEBUG_LINKS = !DEBUG_LINKS;
					repaint();

					if (mobileButtonBar != null) {
						mobileButtonBar.setDebugMode(DEBUG_LINKS);
					}
				}

				if (e.getKeyChar() == 'm') {
					MODIFY_LINKS = !MODIFY_LINKS;
					repaint();
				}

				if (e.getKeyChar() == 't') {
					LINK_TEXT = !LINK_TEXT;
					repaint();
				}

				if (e.getKeyChar() == 'r') {
					startApplication();
				}

			}
		};
		addKeyListener(keyListener);

		if (mobileButtonBar != null) {
			mobileButtonBar.addKeyListener(keyListener);
		}
	}

	public void requestRepaint() {

		repaint();
	}

	public void actionPerformed(ButtonEvent event) {

		displayScreen(ModelUtils.getActiveScreen(scenario.activateTarget(event.getTarget())));
	}

	public boolean showSelectionDialog() {

		JFileChooser fc = new JFileChooser();

		// TODO remove
		File dir = new File("/Users/Bernhard/Dropbox/Masterarbeit/UITesting");

		if (workingDir != null && workingDir.getParentFile() != null) {
			fc.setCurrentDirectory(workingDir.getParentFile());
		} else {
			if (dir.exists()) {
				fc.setCurrentDirectory(dir);
			}
		}

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			workingDir = fc.getSelectedFile();
			xmlFile = new File(workingDir.getAbsolutePath() + "/" + workingDir.getName() + ".xml");

			if (!xmlFile.exists()) {
				JOptionPane.showMessageDialog(this, "Couldn't find equally named xml file inside folder", "Error", JOptionPane.ERROR_MESSAGE);
			}

			File[] xsdFiles = workingDir.listFiles(new FilenameFilter() {

				public boolean accept(File file, String name) {
					if (name.endsWith(".xsd")) {
						return true;
					}
					return false;
				}

			});

			if (xsdFiles.length != 1) {
				if (xsdFiles.length == 0) {
					JOptionPane.showMessageDialog(this, "Couldn't find schema file inside folder", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Only one xsd file allowed inside folder", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				xsdFile = xsdFiles[0];
			}

			if (xmlFile.exists() && xsdFile != null) {
				return true;
			}
		}

		return false;
	}

	public boolean loadScenario() {

		try {
			scenario = XmlParser.parseFile(xsdFile, xmlFile);
			// get screen before validating, otherwise startelement is changed
			Screen screen = scenario.getStartScreen();
			scenario.validateTargets();
			ImagePool.setWorkingDir(workingDir.getAbsolutePath() + "/");
			// ImagePool.loadImages(scenario);

			if (scenario.getDevice().equals(Scenario.Device.MOBILE)) {
				mobileButtonBar = new MobileButtonBar();
				this.getContentPane().add(mobileButtonBar, BorderLayout.SOUTH);
			}

			registerKeyListener();

			displayScreen(screen);

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			JXErrorPane.showDialog(null, new ErrorInfo("Unable to load szenario", ex.getMessage(), ex.toString(), "Error", ex, Level.ALL, null));
		}

		return false;
	}

	public static void startApplication() {

		if (launcher != null) {
			launcher.setVisible(false);
			ImagePool.reset();
			launcher = null;
		}

//		 initUltimateSplashScreen();

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

//				JFrame.setDefaultLookAndFeelDecorated(true);
				launcher = new Launcher();

				if (launcher.showSelectionDialog() && launcher.loadScenario()) {

					launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					launcher.pack();
					launcher.setLocationRelativeTo(null);
					launcher.setResizable(false);
					launcher.setVisible(true);
				} else {
					System.exit(1);
				}
			}
		});
	}

	public static void main(String[] args) {

		startApplication();
	}

	private static void initUltimateSplashScreen() {
		if (splash != null) {

			final Graphics2D g = splash.createGraphics();

			if (g != null) {

				g.dispose();

				SwingUtilities.invokeLater(new Runnable() {

					public void run() {

						try {
							System.out.println(getClass().getResource("../../../imgs/nightvision.png"));
							System.out.println(ClassLoader.getSystemResource("src/imgs/nightvision.png"));
							final BufferedImage nightvision = ImageIO.read(getClass().getResource("imgs/nightvision.png"));
							final BufferedImage nightvision_fade = ImageIO.read(getClass().getResource("imgs/nightvision_fade.png"));
							
							Animator animator = new Animator(3000, 1, Animator.RepeatBehavior.REVERSE, new TimingTarget() {
								public void begin() {
								}

								public void end() {
								}

								public void repeat() {
								}

								public void timingEvent(float fraction) {
									if (splash.isVisible()) {

										Graphics2D g = (Graphics2D) splash.createGraphics();
										g.setComposite(AlphaComposite.Clear);
										g.fillRect(0, 0, splash.getSize().width, splash.getSize().height);
										g.setPaintMode();
										// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
										// RenderingHints.VALUE_ANTIALIAS_ON);
										g.drawImage(nightvision, 0, 0, null);
										g.setComposite(AlphaComposite.SrcOver.derive(fraction));
										g.drawImage(nightvision_fade, 0, 0, null);
										g.dispose();
										splash.update();
									}
								}

							});
							animator.setAcceleration(1f);
							animator.setDeceleration(0.0f);
							animator.start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				});

			} else {
				System.out.println("splash graphics is null");
			}
		} else {
			System.out.println("splash is null");
		}
	}
}
