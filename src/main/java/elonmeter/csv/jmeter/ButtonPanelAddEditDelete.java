package elonmeter.csv.jmeter;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.jmeter.gui.util.PowerTableModel;

import elonmeter.csv.action.AddColumnAction;
import elonmeter.csv.action.DeleteColumnAction;
import elonmeter.csv.action.EditColumnAction;

public class ButtonPanelAddEditDelete extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JButton deleteColumnButton;
	private final JButton addColumnButton;
	private final JButton editColumnButton;
	
	private final PowerTableModel tableModel;

	public ButtonPanelAddEditDelete(JTable grid, PowerTableModel tableModel) {
		setLayout(new GridLayout(1, 2));
		addColumnButton = new JButton("Add Column");
		deleteColumnButton = new JButton("Delete Column");
		editColumnButton= new JButton("Edit Column");
		
		addColumnButton.addActionListener(new AddColumnAction(this, grid, tableModel, addColumnButton));
		editColumnButton.addActionListener(new EditColumnAction(this, grid, tableModel, editColumnButton));
		deleteColumnButton.addActionListener(new DeleteColumnAction(this, grid, tableModel, deleteColumnButton));
		
		
		add(addColumnButton);
		add(deleteColumnButton);
		add(editColumnButton);
		
		this.tableModel = tableModel;
	}

	public void checkDeleteButtonStatus() {
		deleteColumnButton.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
	}
}