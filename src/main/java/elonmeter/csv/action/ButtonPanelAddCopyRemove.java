package elonmeter.csv.action;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.apache.jmeter.gui.util.PowerTableModel;

public class ButtonPanelAddCopyRemove extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JButton deleteRowButton;

    private final PowerTableModel tableModel;

    public ButtonPanelAddCopyRemove(JTable grid, PowerTableModel tableModel) {
        setLayout(new GridLayout(1, 2));

        JButton addRowButton = new JButton("Add Row");
        JButton copyRowButton = new JButton("Copy Row");
        deleteRowButton = new JButton("Delete Row");

        addRowButton.addActionListener(new AddRowAction(this, grid, tableModel, deleteRowButton));
        copyRowButton.addActionListener(new CopyRowAction(this, grid, tableModel, deleteRowButton));
        deleteRowButton.addActionListener(new DeleteRowAction(this, grid, tableModel, deleteRowButton));

        add(addRowButton);
        add(copyRowButton);
        add(deleteRowButton);
        this.tableModel = tableModel;
    }

    public void checkDeleteButtonStatus() {
        deleteRowButton.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
    }
}
