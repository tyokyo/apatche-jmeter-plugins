package org.apache.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

public class HTTPFileArgsPanel extends JPanel
implements ActionListener
{
	private static final long serialVersionUID = 240L;

	private JLabel tableLabel;
	private transient JTable table;
	public static transient ObjectTableModel tableModel;
	private JButton add;
	private JButton browse;
	private JButton delete;
	private static final String ADD = "add";
	private static final String BROWSE = "browse";
	private static final String DELETE = "delete";
	private static final String FILEPATH = "send_file_filename_label";
	private static final String PARAMNAME = "send_file_param_name_label";
	private static final String MIMETYPE = "send_file_mime_label";

	public HTTPFileArgsPanel()
	{
		init();
	}
	public HTTPFileArgsPanel(String label)
	{
		this.tableLabel = new JLabel(label);
		init();
	}

	private void initializeTableModel()
	{
		this.tableModel = new ObjectTableModel(new String[] { "send_file_filename_label", "send_file_param_name_label", "send_file_mime_label" }, HTTPFileArg.class, new Functor[] { new Functor("getPath"), new Functor("getParamName"), new Functor("getMimeType") }, new Functor[] { new Functor("setPath"), new Functor("setParamName"), new Functor("setMimeType") }, new Class[] { String.class, String.class, String.class });
	}

	public static boolean testFunctors()
	{
		HTTPFileArgsPanel instance = new HTTPFileArgsPanel();
		instance.initializeTableModel();
		return instance.tableModel.checkFunctors(null, instance.getClass());
	}

	public void modifyTestElement(TestElement testElement)
	{
		GuiUtils.stopTableEditing(this.table);
		if ((testElement instanceof HTTPSamplerBase)) {
			HTTPSamplerBase base = (HTTPSamplerBase)testElement;
			int rows = this.tableModel.getRowCount();

			Iterator modelData = this.tableModel.iterator();
			HTTPFileArg[] files = new HTTPFileArg[rows];
			int row = 0;
			while (modelData.hasNext()) {
				HTTPFileArg file = (HTTPFileArg)modelData.next();
				files[(row++)] = file;
			}
			base.setHTTPFiles(files);
		}
	}

	public boolean hasData() {
		return this.tableModel.iterator().hasNext();
	}

	public void configure(TestElement testElement)
	{
		if ((testElement instanceof HTTPSamplerBase)) {
			HTTPSamplerBase base = (HTTPSamplerBase)testElement;
			this.tableModel.clearData();
			for (HTTPFileArg file : base.getHTTPFiles()) {
				this.tableModel.addRow(file);
			}
			checkDeleteAndBrowseStatus();
		}
	}

	private void checkDeleteAndBrowseStatus()
	{
		if (this.tableModel.getRowCount() == 0) {
			this.browse.setEnabled(false);
			this.delete.setEnabled(false);
		} else {
			this.browse.setEnabled(true);
			this.delete.setEnabled(true);
		}
	}

	public void clear()
	{
		GuiUtils.stopTableEditing(this.table);
		this.tableModel.clearData();
	}

	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action.equals("add")) {
			addFile("");
		}
		runCommandOnSelectedFile(action);
	}

	private void runCommandOnSelectedFile(String command)
	{
		GuiUtils.stopTableEditing(this.table);

		int rowSelected = this.table.getSelectedRow();
		if (rowSelected >= 0) {
			runCommandOnRow(command, rowSelected);
			this.tableModel.fireTableDataChanged();

			checkDeleteAndBrowseStatus();

			if (this.tableModel.getRowCount() != 0) {
				int rowToSelect = rowSelected;
				if (rowSelected >= this.tableModel.getRowCount()) {
					rowToSelect = rowSelected - 1;
				}
				this.table.setRowSelectionInterval(rowToSelect, rowToSelect);
			}
		}
	}

	private void runCommandOnRow(String command, int rowSelected)
	{
		if ("delete".equals(command)) {
			this.tableModel.removeRow(rowSelected);
		} else if ("browse".equals(command)) {
			String path = browseAndGetFilePath();
			if (StringUtils.isNotBlank(path))
				this.tableModel.setValueAt(path, rowSelected, 0);
		}
	}

	private void addFile(String path)
	{
		GuiUtils.stopTableEditing(this.table);

		this.tableModel.addRow(new HTTPFileArg(path));

		checkDeleteAndBrowseStatus();

		int rowToSelect = this.tableModel.getRowCount() - 1;
		this.table.setRowSelectionInterval(rowToSelect, rowToSelect);
	}

	private String browseAndGetFilePath()
	{
		String path = "";
		JFileChooser chooser = FileDialoger.promptToOpenFile();
		if (chooser != null) {
			File file = chooser.getSelectedFile();
			if (file != null) {
				path = file.getPath();
			}
		}
		return path;
	}

	protected void stopTableEditing()
	{
		GuiUtils.stopTableEditing(this.table);
	}

	private Component makeMainPanel()
	{
		initializeTableModel();
		this.table = new JTable(this.tableModel);
		JMeterUtils.applyHiDPI(this.table);
		this.table.getTableHeader().setDefaultRenderer(new HeaderAsPropertyRenderer());
		this.table.setSelectionMode(0);
		return makeScrollPane(this.table);
	}

	private Component makeLabelPanel()
	{
		JPanel labelPanel = new JPanel(new FlowLayout(0));
		labelPanel.add(this.tableLabel);
		return labelPanel;
	}

	private JPanel makeButtonPanel()
	{
		this.add = new JButton(JMeterUtils.getResString("add"));
		this.add.setActionCommand("add");
		this.add.setEnabled(true);

		this.browse = new JButton(JMeterUtils.getResString("browse"));
		this.browse.setActionCommand("browse");

		this.delete = new JButton(JMeterUtils.getResString("delete"));
		this.delete.setActionCommand("delete");

		checkDeleteAndBrowseStatus();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.add.addActionListener(this);
		this.browse.addActionListener(this);
		this.delete.addActionListener(this);
		buttonPanel.add(this.add);
		buttonPanel.add(this.browse);
		buttonPanel.add(this.delete);
		return buttonPanel;
	}

	private void init()
	{
		JPanel p = this;

		p.setLayout(new BorderLayout());

		if (this.tableLabel != null) {
			p.add(makeLabelPanel(), "North");
		}
		p.add(makeMainPanel(), "Center");

		p.add(Box.createVerticalStrut(70), "West");
		p.add(makeButtonPanel(), "South");

		this.table.revalidate();
	}

	private JScrollPane makeScrollPane(Component comp) {
		JScrollPane pane = new JScrollPane(comp);
		pane.setPreferredSize(pane.getMinimumSize());
		return pane;
	}
}