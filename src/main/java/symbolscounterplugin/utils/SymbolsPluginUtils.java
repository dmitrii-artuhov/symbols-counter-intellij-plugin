package symbolscounterplugin.utils;

import javax.swing.*;
import java.awt.*;

public class SymbolsPluginUtils {

    @SuppressWarnings("unchecked")
    public static <T extends JComponent> T findComponentByProperty(Container container, String name, String property) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent jComponent) {
                Object id = jComponent.getClientProperty(property);
                if (id != null && id.equals(name)) {
                    // Found a component with the specified ID
                    return (T) jComponent;
                }
            }
            if (component instanceof Container) {
                // Recursively search within containers
                T found = findComponentByProperty((Container) component, name, property);
                if (found != null) {
                    return found;
                }
            }
        }
        return null; // Component with the specified ID not found
    }
}
