package elonmeter.csv.action;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CopyLoadButtonPanel extends JPanel{
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final long serialVersionUID = 1L;
	private static final String ADD_FROM_CLIPBOARD = "addFromClipboard"; // $NON-NLS-1$
	private static final String ADD_FROM_FILE = "addFromFile"; // $NON-NLS-1$
	/** When pasting from the clipboard, split lines on linebreak */
	private static final String CLIPBOARD_LINE_DELIMITERS = "\n"; //$NON-NLS-1$
	/** When pasting from the clipboard, split parameters on tab */
	private static final String CLIPBOARD_ARG_DELIMITERS = "\t"; //$NON-NLS-1$
	private final PowerTableModel tableModel;
	private final JTable table;

	public CopyLoadButtonPanel(final JTable table, PowerTableModel tableModel) {
		this.table=table;
		setLayout(new GridLayout(1, 2));
		JButton addFromClipboardButton = new JButton("addFromClipboard(enter-\\t)");		
		addFromClipboardButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (table.isEditing()) {
					TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
					cellEditor.cancelCellEditing();
				}
				addFromClipboard();
			}
		});
		addFromClipboardButton.setEnabled(true);
		
		JButton addFromFileButton = new JButton("addFromFile(enter-,)");		
		addFromFileButton.addActionListener(new ImportFileAction(this, table, tableModel, addFromFileButton));
		addFromFileButton.setEnabled(true);

		add(addFromClipboardButton);
		add(addFromFileButton);
		this.tableModel = tableModel;
	}
	protected String[] columnParser(String[] clipboardLine){
		int columnCount = table.getColumnCount();
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
	/**
	 * Add values from the clipboard
	 * @param lineDelimiter Delimiter string to split clipboard into lines
	 * @param argDelimiter Delimiter string to split line into key-value pair
	 */
	protected void addFromClipboard(String lineDelimiter, String argDelimiter) {
		GuiUtils.stopTableEditing(table);
		int rowCount = table.getRowCount();
		try {
			String clipboardContent = GuiUtils.getPastedText();
			if(clipboardContent == null) {
				return;
			}
			String[] clipboardLines = clipboardContent.split(lineDelimiter);
			for (String clipboardLine : clipboardLines) {
				log.info(clipboardLine);
				String[] clipboardCols = columnParser(clipboardLine.split(argDelimiter));
				if (clipboardCols.length > 0) {
					tableModel.addRow(clipboardCols);
					tableModel.fireTableDataChanged();
				}
			}
			if (table.getRowCount() > rowCount) {
				// Highlight (select) and scroll to the appropriate rows.
				int rowToSelect = tableModel.getRowCount() - 1;
				table.setRowSelectionInterval(rowCount, rowToSelect);
				table.scrollRectToVisible(table.getCellRect(rowCount, 0, true));
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this,
					"Could not add read arguments from clipboard:\n" + ioe.getLocalizedMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (UnsupportedFlavorException ufe) {
			JOptionPane.showMessageDialog(this,
					"Could not add retrieve " + DataFlavor.stringFlavor.getHumanPresentableName()
					+ " from clipboard" + ufe.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	protected void addFromClipboard() {
		addFromClipboard(CLIPBOARD_LINE_DELIMITERS, CLIPBOARD_ARG_DELIMITERS);
	}
	
	public void checkDeleteButtonStatus() {
		//deleteRowButton.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
	}
}
