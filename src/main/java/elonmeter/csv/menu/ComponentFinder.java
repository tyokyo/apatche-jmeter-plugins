package elonmeter.csv.menu;
import java.awt.Component;
import java.awt.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentFinder<T extends Component>
{
  private static final Logger log = LoggerFactory.getLogger(ComponentFinder.class);
  private final Class<T> search;

  public ComponentFinder(Class<T> cls)
  {
    this.search = cls;
  }

  public T findComponentIn(Container container) {
    log.debug("Searching in " + container);
    for (Component a : container.getComponents()) {
      if (this.search.isAssignableFrom(a.getClass())) {
        log.debug("Found " + a);
        return (T) a;
      }
      if ((a instanceof Container)) {
        Component res = findComponentIn((Container)a);
        if (res != null) {
          return (T) res;
        }
      }
    }
    return null;
  }
}