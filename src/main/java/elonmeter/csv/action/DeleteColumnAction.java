package elonmeter.csv.action;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jorphan.gui.JLabeledTextField;

public class DeleteColumnAction
        implements ActionListener {

    private JTable grid;
    private PowerTableModel tableModel;
    private JButton deleteColumnButton;
    private JComponent sender;
    private JDialog deleteColumnDialog;
    private JComboBox<String> jCbox;
    protected  int w;
	protected  int h;
	
    public DeleteColumnAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton deleteColumnButton) {
        this.grid = grid;
        this.tableModel = tableModel;
        this.deleteColumnButton = deleteColumnButton;
        this.sender = aSender;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
		w=(int)toolkit.getScreenSize().getWidth()-100;
		h=(int)toolkit.getScreenSize().getHeight()-100;
    }
    private JDialog deleteColumnDialog(){
    	deleteColumnDialog = new JDialog(GuiPackage.getInstance().getMainFrame());
		// 定义窗体的宽高
		int windowsWide = 500;
		int windowsHeight = 80;
		deleteColumnDialog.setTitle("delete column");
		int columnSize=tableModel.getColumnCount();
		String[] header=new String[columnSize];
		for (int i = 0; i < columnSize; i++) {
			String columnName=tableModel.getColumnName(i).toString();
			header[i]=columnName;
		}
		jCbox=new JComboBox<String>(header);
		VerticalPanel mainPanel = new VerticalPanel();
		HorizontalPanel filterPanel = new HorizontalPanel();
		JLabel label=new JLabel("Variable name:");
		JButton sureButton=new JButton("Delete");
		filterPanel.add(label);
		filterPanel.add(jCbox);
		filterPanel.add(sureButton);
		sureButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				  if (grid.isEditing()) {
			            TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			            cellEditor.stopCellEditing();
			        }
				  	int index =jCbox.getSelectedIndex();
				  	tableModel.removeColumn(index);
			        tableModel.fireTableDataChanged();
			        grid.updateUI();
			        deleteColumnDialog.setEnabled(true);
			        sender.updateUI();
			        deleteColumnDialog.dispose();
			}
		});
		mainPanel.add(filterPanel);
		deleteColumnDialog.setSize(w/3, h/3);
		deleteColumnDialog.setBounds((w - windowsWide) / 2,(h - windowsHeight) / 2, windowsWide, windowsHeight);
		deleteColumnDialog.getContentPane().add(mainPanel,BorderLayout.CENTER);
		deleteColumnDialog.setResizable(true);
		deleteColumnDialog.setVisible(true);
		return deleteColumnDialog;
	}
    public void actionPerformed(ActionEvent e) {
        if (grid.isEditing()) {
            TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
            cellEditor.stopCellEditing();
        }
        deleteColumnDialog();
    }
}
