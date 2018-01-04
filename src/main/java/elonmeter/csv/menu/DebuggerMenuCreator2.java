package elonmeter.csv.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import org.apache.jmeter.gui.plugin.MenuCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebuggerMenuCreator2
  implements MenuCreator
{
  private static final Logger log = LoggerFactory.getLogger(DebuggerMenuCreator2.class);

  public JMenuItem[] getMenuItemsAtLocation(MenuCreator.MENU_LOCATION location)
  {
    if (location == MenuCreator.MENU_LOCATION.RUN) {
      try {
        return new JMenuItem[] { new DebuggerMenuItem2() };
      } catch (Throwable e) {
        log.error("Failed to load debugger", e);
        return new JMenuItem[0];
      }
    }
    return new JMenuItem[0];
  }

  public JMenu[] getTopLevelMenus()
  {
    return new JMenu[0];
  }

  public boolean localeChanged(MenuElement menu)
  {
    return false;
  }

  public void localeChanged()
  {
  }
}