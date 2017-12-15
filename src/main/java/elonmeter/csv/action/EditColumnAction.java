package elonmeter.csv.action;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.log.Logger;

import elonmeter.csv.jmeter.GridDataSetConfig;
import elonmeter.csv.jmeter.GridDataSetConfigGui;

public class EditColumnAction implements ActionListener {

	private JTable grid;
	private PowerTableModel tableModel;
	private JButton editColumnButton;
	private Object[] defaultValues;
	private JComponent sender;
	private JDialog editColumnDialog;
	protected  int w;
	protected  int h;
	private JComboBox<String> jCbox;
	public EditColumnAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton editColumnButton) {
		this.grid = grid;
		this.tableModel = tableModel;
		this.editColumnButton = editColumnButton;
		this.sender = aSender;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		w=(int)toolkit.getScreenSize().getWidth()-100;
		h=(int)toolkit.getScreenSize().getHeight()-100;
	}
	private JDialog editColumnDialog(){
		editColumnDialog = new JDialog(GuiPackage.getInstance().getMainFrame());
		// 定义窗体的宽高
		int windowsWide = 500;
		int windowsHeight = 80;
		editColumnDialog.setTitle("edit variable name");
		int columnSize=tableModel.getColumnCount();
		String[] header=new String[columnSize];
		for (int i = 0; i < columnSize; i++) {
			String columnName=tableModel.getColumnName(i).toString();
			header[i]=columnName;
		}
		jCbox=new JComboBox<String>(header);
		VerticalPanel mainPanel = new VerticalPanel();

		HorizontalPanel fromPanel = new HorizontalPanel();
		fromPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"select ariable")); 
		JLabel label=new JLabel("Variable name:");
		fromPanel.add(label);
		fromPanel.add(jCbox);
		mainPanel.add(fromPanel);

		HorizontalPanel toPanel = new HorizontalPanel();
		toPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"change ariable to")); 
		final JLabeledTextField valueField=new JLabeledTextField("Variable value:");
		JButton submitButton=new JButton("modify");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.stopCellEditing();
				}
				int index =jCbox.getSelectedIndex();
				String column=jCbox.getSelectedItem().toString();
				String changeValue=valueField.getText();
				grid.getColumnModel().getColumn(index).setHeaderValue(changeValue);
				GridDataSetConfigGui.varChangedNewValue=changeValue;
				GridDataSetConfigGui.varChangedOldValue=column;
				
				grid.getTableHeader().firePropertyChange(column, 1, 2);
				
				tableModel.fireTableDataChanged();
				grid.updateUI();
				editColumnDialog.setEnabled(true);
				sender.updateUI();
				editColumnDialog.dispose();
			}
		});
		toPanel.add(valueField);
		toPanel.add(submitButton);
		mainPanel.add(toPanel);

		editColumnDialog.setSize(w/3, h/3);
		editColumnDialog.setBounds((w - windowsWide) / 2,(h - windowsHeight) / 2, windowsWide, windowsHeight);
		editColumnDialog.getContentPane().add(mainPanel,BorderLayout.CENTER);
		editColumnDialog.setResizable(true);
		editColumnDialog.setVisible(true);
		return editColumnDialog;
	}
	public void actionPerformed(ActionEvent e) {
		if (grid.isEditing()) {
			TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			cellEditor.stopCellEditing();
		}

		editColumnDialog();

		// Highlight (select) the appropriate row.
		int rowToSelect = tableModel.getRowCount() - 1;
		if (rowToSelect < grid.getRowCount()) {
			grid.setRowSelectionInterval(rowToSelect, rowToSelect);
		}
		sender.updateUI();
	}
}
