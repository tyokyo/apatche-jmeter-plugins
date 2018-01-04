package elonmeter.csv.action;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class ImportFileAction implements ActionListener {
	private static final Logger log = LoggingManager.getLoggerForClass();
	private JTable grid;
	private PowerTableModel tableModel;
	private JButton importFromFileButton;
	private JComponent sender;
	private JDialog importFromFileDialog;
	protected  int w;
	protected  int h;

	public ImportFileAction(JComponent aSender, JTable grid, PowerTableModel tableModel, JButton importFromFileButton) {
		this.grid = grid;
		this.tableModel = tableModel;
		this.importFromFileButton = importFromFileButton;
		this.sender = aSender;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		w=(int)toolkit.getScreenSize().getWidth()-100;
		h=(int)toolkit.getScreenSize().getHeight()-100;
	}
	private JDialog importColumnFromFileDialog(){
		importFromFileDialog = new JDialog(GuiPackage.getInstance().getMainFrame());
		// 定义窗体的宽高
		int windowsWide = 500;
		int windowsHeight = 80;
		importFromFileDialog.setTitle("Import from file:");
		int columnSize=tableModel.getColumnCount();
		String[] header=new String[columnSize];
		for (int i = 0; i < columnSize; i++) {
			String columnName=tableModel.getColumnName(i).toString();
			header[i]=columnName;
		}
		VerticalPanel mainPanel = new VerticalPanel();
		HorizontalPanel filterPanel = new HorizontalPanel();
		JLabel label=new JLabel("File name:");
		final JTextField fileField=new JTextField("");
		JButton browseButton=new JButton("Browse..");
		browseButton.addActionListener(new BrowseAction(fileField));
		JButton sumbitButton=new JButton("Confirm");

		filterPanel.add(label);
		filterPanel.add(fileField);
		filterPanel.add(browseButton);
		filterPanel.add(sumbitButton);

		sumbitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GuiUtils.stopTableEditing(grid);
				int rowCount = grid.getRowCount();
				if (grid.isEditing()) {
					TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
					cellEditor.stopCellEditing();
				}
				File f = new File(fileField.getText());
				if (f.exists()) {
					try {
						FileReader fr =  new FileReader (fileField.getText());
						BufferedReader br = new BufferedReader (fr);
						String line;
						while ((line = br.readLine() )!=null) {
							log.info(line);
							String[] clipboardCols = columnParser(line.split(","));
							if (clipboardCols.length > 0) {
								tableModel.addRow(clipboardCols);
								tableModel.fireTableDataChanged();
							}
						}
						fr.close();
						if (grid.getRowCount() > rowCount) {
							// Highlight (select) and scroll to the appropriate rows.
							int rowToSelect = tableModel.getRowCount() - 1;
							grid.setRowSelectionInterval(rowCount, rowToSelect);
							grid.scrollRectToVisible(grid.getCellRect(rowCount, 0, true));
						}
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}finally{
						importFromFileDialog.dispose();
					}
				}else{
					
				}
			}
		});
		mainPanel.add(filterPanel);
		importFromFileDialog.setSize(w/3, h/3);
		importFromFileDialog.setBounds((w - windowsWide) / 2,(h - windowsHeight) / 2, windowsWide, windowsHeight);
		importFromFileDialog.getContentPane().add(mainPanel,BorderLayout.CENTER);
		importFromFileDialog.setResizable(true);
		importFromFileDialog.setVisible(true);
		return importFromFileDialog;
	}
	protected String[] columnParser(String[] clipboardLine){
		int columnCount = grid.getColumnCount();
		String[] column=new String[columnCount];
		for (int i = 0; i < column.length; i++) {
			column[i]="";
		}
		if (clipboardLine.length==columnCount) {
			column= clipboardLine;
		}else if (clipboardLine.length>columnCount) {
			for (int i = 0; i < columnCount; i++) {
				column[i]=clipboardLine[i];
			}
		}else {
			for (int i = 0; i < clipboardLine.length; i++) {
				column[i]=clipboardLine[i];
			}
		}
		return column;
	}
	public void actionPerformed(ActionEvent e) {
		if (grid.isEditing()) {
			TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
			cellEditor.stopCellEditing();
		}
		importColumnFromFileDialog();
	}
}
