package elonmeter.csv.action;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jorphan.gui.JLabeledTextField;

public class AddColumnAction
        implements ActionListener {

    private JTable grid;
    private PowerTableModel tableModel;
    private JButton addColumnButton;
    private JComponent sender;
	protected  JDialog addColumnDialog;
	protected  int w;
	protected  int h;
	protected JLabeledTextField varNameField;
	
    public AddColumnAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton addColumnButton) {
        this.grid = grid;
        this.tableModel = tableModel;
        this.addColumnButton = addColumnButton;
        this.sender = aSender;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		w=(int)toolkit.getScreenSize().getWidth()-100;
		h=(int)toolkit.getScreenSize().getHeight()-100;
    }
	private JDialog AddColumnDialog(){
		addColumnDialog = new JDialog(GuiPackage.getInstance().getMainFrame());
		// 定义窗体的宽高
		int windowsWide = 500;
		int windowsHeight = 80;
		addColumnDialog.setTitle("Add Variable");

		VerticalPanel mainPanel = new VerticalPanel();
		HorizontalPanel filterPanel = new HorizontalPanel();
		varNameField= new JLabeledTextField("Variable name:");
		JButton sureButton=new JButton("Add");
		filterPanel.add(varNameField);
		filterPanel.add(sureButton);
		sureButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				  if (grid.isEditing()) {
			            TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			            cellEditor.stopCellEditing();
			        }
			        String columnName=varNameField.getText();
			        tableModel.addNewColumn(columnName,String.class);
			        tableModel.fireTableDataChanged();
			        grid.updateUI();
			        addColumnButton.setEnabled(true);
			        sender.updateUI();
			        addColumnDialog.dispose();
			}
		});
		
		mainPanel.add(filterPanel);

		addColumnDialog.setSize(w/3, h/3);
		addColumnDialog.setBounds((w - windowsWide) / 2,(h - windowsHeight) / 2, windowsWide, windowsHeight);
		addColumnDialog.getContentPane().add(mainPanel,BorderLayout.CENTER);
		addColumnDialog.setResizable(true);
		addColumnDialog.setVisible(true);
		return addColumnDialog;
	}
    public void actionPerformed(ActionEvent e) {
    	 if (grid.isEditing()) {
             TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
             cellEditor.stopCellEditing();
         }
    	AddColumnDialog();
    }
}
