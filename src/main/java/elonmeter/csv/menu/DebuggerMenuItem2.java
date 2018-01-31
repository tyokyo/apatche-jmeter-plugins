package elonmeter.csv.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebuggerMenuItem2 extends JMenuItem
implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DebuggerMenuItem2.class);
	private static final String TITLE ="extend-by-ckt";
	public DebuggerMenuItem2()
	{
		super(TITLE, getBugIcon(false));
		addActionListener(this);
		addToolbarIcon();
	}

	private void addToolbarIcon() {
		GuiPackage instance = GuiPackage.getInstance();
		if (instance != null) {
			final MainFrame mf = instance.getMainFrame();
			final ComponentFinder finder = new ComponentFinder(JMeterToolBar.class);
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
					JMeterToolBar toolbar = null;
					while (toolbar == null) {
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException e) {
							DebuggerMenuItem2.log.debug("Did not add btn to toolbar", e);
						}
						DebuggerMenuItem2.log.debug("Searching for toolbar");
						toolbar = (JMeterToolBar)finder.findComponentIn(mf);
					}

					int pos = 21;
					Component toolbarButton = DebuggerMenuItem2.this.getToolbarButton();
					toolbarButton.setSize(toolbar.getComponent(pos).getSize());
					toolbar.add(toolbarButton, pos);
				}
			});
		}
	}

	private Component getToolbarButton() {
		JButton button = new JButton(getBugIcon(true));
		button.setToolTipText(TITLE);
		button.addActionListener(this);
		return button;
	}

	public void actionPerformed(ActionEvent e)
	{

	}

	public static ImageIcon getBugIcon(boolean large)
	{
		/* if (large) {
      return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/bug22.png"));
    }*/
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/properties/stop.png"));
	}

	public static ImageIcon getStartIcon()
	{
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/start.png"));
	}

	public static ImageIcon getStopIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/stop.png"));
	}

	public static ImageIcon getStepIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/step.png"));
	}

	public static ImageIcon getLogoIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/logo.png"));
	}

	public static ImageIcon getBPIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/breakpoint.png"));
	}

	public static ImageIcon getContinueIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/continue.png"));
	}

	public static ImageIcon getPauseIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/pause.png"));
	}

	public static Icon getHelpIcon() {
		return new ImageIcon(DebuggerMenuItem2.class.getResource("/com/blazemeter/jmeter/debugger/help.png"));
	}
}
