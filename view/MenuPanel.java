package view;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import java.util.LinkedHashMap;

/**
 * This class generates the menu bar for the application.
 * It adds the following primary menu options with their respective sub menus.
 * Additionally, it contains the metadata containing the project team information.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */

public class MenuPanel {

    JMenuBar menuBar;
    JMenuItem[] options = new JMenuItem[5];
    JMenu[] choices = new JMenu[2];
    
    LinkedHashMap<String, String[]> menuTitles = new LinkedHashMap<String, String[]>() {{
        put("File", new String[] {"Load Roster", "Add Attendance", "Save Data", "Plot Data"});
        put("About", new String[] {"View Team Details"});
    }};

    String[] teamInfo = {   "Product Owner: Mukul C. Mahadik (mmahadik@asu.edu)", 
                            "Backend Developer: Aniket Agrawal (aagraw82@asu.edu)", 
                            "Backend Developer: Krithish Goli (kgoli1@asu.edu)", 
                            "Frontend Developer: Sarvesh Kapse (skapse@asu.edu)", 
                            "Design Architect: Shrinkhala Kayastha (skayast1@asu.edu)"
                        };
    
    public MenuPanel() {

        menuBar = new JMenuBar();
        int i = 0, j = 0;
        for (String key : menuTitles.keySet()) {
            choices[j] = new JMenu(key);
            for (String value : menuTitles.get(key)) {
                options[i] = new JMenuItem(value);
                choices[j].add(options[i++]);
            }
            menuBar.add(choices[j++]);
        }
    }


    
    /** 
     * Returns array of sub menu items 
     */
    public JMenuItem[] getMenuOptions() {
        return options;
    }

    
    /** 
     * Returns GUI MenuBar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    
    /** 
     * Returns metadata containing team information
     */
    public String[] getTeamInfo() {
        return teamInfo;
    }
}
