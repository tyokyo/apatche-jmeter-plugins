package elonmeter.csv.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.jmeter.gui.util.PowerTableModel;

public class AddRowAction
        implements ActionListener {

    private JTable grid;
    private PowerTableModel tableModel;
    private JButton addRowButton;
    private JComponent sender;

    public AddRowAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton addRowButton) {
        this.grid = grid;
        this.tableModel = tableModel;
        this.addRowButton = addRowButton;
        this.sender = aSender;
    }

    public void actionPerformed(ActionEvent e) {
        if (grid.isEditing()) {
            TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
            cellEditor.stopCellEditing();
        }

        tableModel.addNewRow();
        tableModel.fireTableDataChanged();

        // Enable DELETE (which may already be enabled, but it won't hurt)
        addRowButton.setEnabled(true);

        // Highlight (select) the appropriate row.
        int rowToSelect = tableModel.getRowCount() - 1;
        if (rowToSelect < grid.getRowCount()) {
            grid.setRowSelectionInterval(rowToSelect, rowToSelect);
        }
        sender.updateUI();
    }
}
