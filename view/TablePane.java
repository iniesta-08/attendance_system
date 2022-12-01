package view;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JScrollPane;
import java.util.List;
import java.util.ArrayList;

/**
 * This class builds a model of the backend table which 
 * contains all the input data read from the input files.
 * The table model can be updated dynamically during runtime
 * to accomodate more data.
 * 
 *
 * @author Shrinkhala Kayastha
 * @author Mukul Mahadik
 * @version 1.0
 */

public class TablePane {
    private static volatile TablePane INSTANCE;

    private JScrollPane jsp;
    private DefaultTableModel model;
    private JTable dataTable;
    private TableColumnModel colModel;

    private List<String> colNames = new ArrayList<String>();

    private TablePane() {
        model = new DefaultTableModel();
        dataTable = new JTable(model);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        colNames.add("ID");
        colNames.add("First Name");
        colNames.add("Last Name");
        colNames.add("ASURITE");

        for (String col : colNames)
            model.addColumn(col);

        jsp = new JScrollPane(this.dataTable,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
    }

    
    /** 
     * Returns instantiated object of this class
     */
    public static TablePane getInstance() {
        if (INSTANCE == null) {
            synchronized (TablePane.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TablePane();
                }
            }
        }
        return INSTANCE;
    }

    
    /** 
     * Returns GUI table component
     */
    public JTable getJTable() {
        return dataTable;
    }

    
    /** 
     * Returns GUI table model
     */
    public DefaultTableModel getTableModel() {
        return model;
    }

    
    /** 
     * Returns GUI scroll pane
     */
    public JScrollPane getJSP() {
        return jsp;
    }


    /** 
     * Sets the GUI table columns and their widths
     */
    public void setJTableColumnsWidth() {
        colModel = dataTable.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            TableColumn column = colModel.getColumn(i);
            column.setPreferredWidth(300);
        }
    }

    /** 
     * Returns GUI table columns
     */
    public void getColumns() {
        for (int i = 0; i < colModel.getColumnCount(); i++)
            System.out.println(colModel.getColumn(i).getHeaderValue());
    }

    
    /** 
     * Checks whether a column exists in the table
     * @param colName Column name to be checked
     */
    public boolean hasColumn(String colName) {
        for (int i = 0; i < colModel.getColumnCount(); i++)
            if (colName.equals(colModel.getColumn(i).getHeaderValue()))
                return true;
        return false;
    }
}
