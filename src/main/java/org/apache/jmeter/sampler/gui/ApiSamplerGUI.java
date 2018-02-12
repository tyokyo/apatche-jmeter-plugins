package org.apache.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.sampler.ApiSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import elonmeter.csv.jmeter.HelpPanel;
import sioeye.spider.entities.ApiDetails;
import sioeye.spider.entities.ApiParameters;
import sioeye.spider.entities.Apis;
import sioeye.spider.entities.Scenes;
import sioeye.spider.helpers.PropertyHelpers;
import sioeye.spider.helpers.UrlHelper;
import sioeye.spider.impl.DefaultApiDetailSpider;
import sioeye.spider.impl.DefaultApiParameterSpider;
import sioeye.spider.impl.DefaultApiSpider;
import sioeye.spider.impl.DefaultSceneSpider;
import sioeye.spider.interfaces.IApiDetails;
import sioeye.spider.interfaces.IApiParametersSpider;
import sioeye.spider.interfaces.IApiSpider;

public class ApiSamplerGUI extends AbstractSamplerGui implements ActionListener{
	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		super.updateUI();
		if (this.tableModel != null) {
			ApiSampler utgForPreview = new ApiSampler();
			utgForPreview.setData(JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(this.tableModel, "threads_file_upload"));
		}
	}
	@Override
	protected void configureTestElement(TestElement mc) {
		// TODO Auto-generated method stub
		super.configureTestElement(mc);
	}
	private static final String CLIPBOARD_LINE_DELIMITERS = "\n"; //$NON-NLS-1$
	/** When pasting from the clipboard, split parameters on tab */
	private static final String CLIPBOARD_ARG_DELIMITERS = "\t"; //$NON-NLS-1$

	public static  String[] columnIdentifiers = {"文件名称","参数名称","MIME类型"};
	@SuppressWarnings("rawtypes")
	public static  Class[] columnClasses = {String.class,String.class,String.class};
	protected PowerTableModel tableModel;
	protected JTable grid;
	private JButton add;
	private JButton browse;
	private JButton delete;
	private JButton addFromClipboard;
	private static final Logger log = LoggerFactory.getLogger(ApiSamplerGUI.class);
	private static final long serialVersionUID = 1L;
	private static JRadioButton sessionTokenRadioButton;
	private static JLabeledTextField varsTextField;
	private static JLabeledTextField methodTextField;
	private static JLabeledTextField serverUrlTextField;
	private static HeaderTableModel headerModel;
	private static JTabbedPane jTabbedPane;
	public static JTable headerTable;
	public static JTable parameterTable;
	public static JDialog spiderDialog;
	public static JRadioButton propsRadioButton;
	public static JRadioButton varsRadioButton;
	public static ArgumentsPanel2 parameterArgumentsPanel;
	public static ArgumentsPanel headerArgumentsPanel;
	public static  JButton borwserButton;
	public static int w;
	public static int h;
	public ApiSamplerGUI(){
		serverUrlTextField = new JLabeledTextField(ApiSampler.SERVER);
		methodTextField = new JLabeledTextField(ApiSampler.METHOD);
		varsTextField = new JLabeledTextField(ApiSampler.ARGS);
		jTabbedPane = new JTabbedPane();
		parameterArgumentsPanel=new ArgumentsPanel2(null, Color.WHITE, true, true);
		headerArgumentsPanel=new ArgumentsPanel(null);
		borwserButton = new JButton("browser");
		init(); 
	}
	private void createTableModel() {
		this.tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
		this.grid .getTableHeader().setReorderingAllowed(false);
		this.grid.setModel(this.tableModel);
		this.grid.getTableHeader().setToolTipText("upload file");
	}
	private JTable createGrid() {
		this.grid = new JTable();
		createTableModel();
		this.grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		this.grid.setMinimumSize(new Dimension(200, 100));
		return this.grid;
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

		this.addFromClipboard = new JButton("addFromClipboard(enter-\\t)");		
		this.addFromClipboard.setActionCommand("AddfromClipboard");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.add.addActionListener(this);
		this.browse.addActionListener(this);
		this.delete.addActionListener(this);
		this.addFromClipboard.addActionListener(this);

		buttonPanel.add(this.add);
		buttonPanel.add(this.browse);
		buttonPanel.add(this.delete);
		buttonPanel.add(this.addFromClipboard);
		return buttonPanel;
	}
	private JPanel getFileUploadPanel() {
		JScrollPane scroll = new JScrollPane(createGrid());
		scroll.setPreferredSize(scroll.getMinimumSize());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(scroll, "Center");
		panel.add(Box.createVerticalStrut(70), "West");
		panel.add(makeButtonPanel(), "South");
		this.grid.revalidate();

		return panel;
	}
	public static JTable getHeaderTable(){
		headerModel = new HeaderTableModel(20);
		headerTable = new JTable(headerModel){
			private static final long serialVersionUID = 1L;
		};
		headerTable.getTableHeader().setReorderingAllowed(false);

		TableColumnModel tcm = headerTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(10);
		tcm.getColumn(1).setPreferredWidth(30);
		headerTable.setRowHeight(20);  	
		//push header data
		HashMap<String, String> map = PropertyHelpers.getHeaderMap();
		HeaderTableModel.construct(map);
		JTableHeader tableHeader = headerTable.getTableHeader();
		tableHeader.setReorderingAllowed(false);   //设置表格列不可重排
		DefaultTableCellRenderer hr =(DefaultTableCellRenderer)tableHeader.getDefaultRenderer();  //获得表格头的单元格对象
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);  //列名居中

		headerTable.getTableHeader().setResizingAllowed(true);
		headerTable.setRowSelectionAllowed(true);
		headerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		return headerTable;
	}
	public static JScrollPane getHeaderTableScrollPane(){
		JScrollPane tableJScrollPane = new JScrollPane();
		tableJScrollPane.setViewportView(new ArgumentsPanel(null));
		//tableJScrollPane.setViewportView(getHeaderTable());
		return tableJScrollPane;
	}
	public static  JDialog spiderDialog(){
		spiderDialog = new JDialog(GuiPackage.getInstance().getMainFrame());
		// 定义窗体的宽高
		int windowsWedth = 500;
		int windowsHeight = 350;
		spiderDialog.setTitle("Api spider");

		VerticalPanel mainPanel = new VerticalPanel();

		HorizontalPanel filterPanel = new HorizontalPanel();
		JLabel uLabel = new JLabel("search :");
		JTextField pathTF = new JTextField(40);
		filterPanel.add(uLabel);
		filterPanel.add(pathTF);
		mainPanel.add(filterPanel);

		HorizontalPanel treePanel = new HorizontalPanel();
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setPreferredSize(new Dimension(w/6,h/3));

		DefaultMutableTreeNode rootTree =new DefaultMutableTreeNode("document");
		DefaultTreeModel treeModel=new DefaultTreeModel(rootTree);
		final JTree tree = new JTree(treeModel);
		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount()==2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); 
					if(node.isLeaf()){
						DefaultSceneSpider spider = new DefaultSceneSpider();
						PropertyHelpers ph = new PropertyHelpers();
						List<Scenes> scenes= spider.getScene(ph.getApiDocUrl());
						for (Scenes scenes2 : scenes) {
							String url = scenes2.getSceneUrl();
							String scenseName = node.getParent().toString();
							if (scenseName.equals(scenes2.getSceneName())) {
								String method = node.toString();
								log.info("url="+url);
								log.info("method="+method);
								IApiParametersSpider parameterSpider = new DefaultApiParameterSpider();
								List<ApiParameters> parameterList = parameterSpider.getApiParameters(url, method);

								parameterArgumentsPanel.addArgument(parameterList);

								for (ApiParameters parameter:parameterList){
									log.info(parameter.toString());
								}

								IApiDetails detailsspider = new DefaultApiDetailSpider();
								List<ApiDetails> details=detailsspider.getApiDetails(url, method);
								for(ApiDetails detail : details){
									log.info(detail.toString());
									String desc = detail.getApiDesc();
									methodTextField.setText("/functions/"+method);
									//serverUrlTextField.setText(new PropertyHelpers().getServerUrl());
									serverUrlTextField.setText("${server}");
									spiderDialog.dispose();
								}
							}
						}
					}
				}
			}
		});
		scrollPane.setViewportView(tree);  
		insertSpiderData(rootTree);
		treePanel.add(scrollPane);
		mainPanel.add(treePanel);

		spiderDialog.setSize(w/3, h/3);
		spiderDialog.setBounds((w - windowsWedth) / 2,
				(h - windowsHeight) / 2, windowsWedth, windowsHeight);
		spiderDialog.getContentPane().add(mainPanel,BorderLayout.CENTER);
		spiderDialog.setResizable(true);
		spiderDialog.setVisible(true);
		return spiderDialog;
	}
	public static void insertSpiderData(DefaultMutableTreeNode rootTree){
		DefaultSceneSpider spider = new DefaultSceneSpider();
		PropertyHelpers ph = new PropertyHelpers();
		List<Scenes> scenes= spider.getScene(ph.getApiDocUrl());
		for(Scenes scene:scenes){
			DefaultMutableTreeNode scenschild = new DefaultMutableTreeNode(scene.getSceneName());
			IApiSpider spiderApi = new DefaultApiSpider();
			List<Apis> apis=spiderApi.getApi(scene.getSceneUrl());
			for (Apis api:apis){
				DefaultMutableTreeNode apichild = new DefaultMutableTreeNode(api.getApiName());
				scenschild.add(apichild);
			}
			rootTree.add(scenschild);
		}
	}
	private void init() {
		setLayout(new BorderLayout(10, 10));
		setBorder(makeBorder());
		//add(makeTitlePanel(), BorderLayout.NORTH);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		w=(int)toolkit.getScreenSize().getWidth()-100;
		h=(int)toolkit.getScreenSize().getHeight()-100;

		Container topPanel = makeTitlePanel();
		add(HelpPanel.addHelpLinkToPanel(topPanel, UrlHelper.api_sampler_sioeye), BorderLayout.NORTH);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(topPanel);

		HorizontalPanel serverPanel = new HorizontalPanel();
		//serverUrlTextField.setText("https://api.siocloud.sioeye.cn/functions/");
		serverPanel.add(serverUrlTextField);
		mainPanel.add(serverPanel);

		HorizontalPanel methodPanel = new HorizontalPanel();
		methodPanel.add(methodTextField);
		borwserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				spiderDialog();
			}
		});
		methodPanel.add(borwserButton);
		mainPanel.add(methodPanel);

		HorizontalPanel sessionPanel = new HorizontalPanel();
		sessionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"自动添加header")); 
		sessionTokenRadioButton=new JRadioButton("sessiontoken");
		sessionPanel.add(sessionTokenRadioButton);
		mainPanel.add(sessionPanel);

		VerticalPanel settingPanel = new VerticalPanel();
		settingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"请求结果保存为")); 

		ButtonGroup buttonGroup = new ButtonGroup();
		propsRadioButton = new JRadioButton("props");
		varsRadioButton = new JRadioButton("vars");
		varsRadioButton.setSelected(true);
		buttonGroup.add(propsRadioButton);
		buttonGroup.add(varsRadioButton);

		HorizontalPanel groupPanel = new HorizontalPanel();
		groupPanel.add(propsRadioButton);
		groupPanel.add(varsRadioButton);
		settingPanel.add(groupPanel);

		HorizontalPanel varsPanel = new HorizontalPanel();
		varsPanel.add(varsTextField);
		settingPanel.add(varsPanel);
		mainPanel.add(settingPanel);
		add(mainPanel, BorderLayout.NORTH);

		jTabbedPane.add("Header",headerArgumentsPanel);
		jTabbedPane.add("Parameter",parameterArgumentsPanel);
		jTabbedPane.add("Files Upload",getFileUploadPanel());

		add(jTabbedPane, BorderLayout.CENTER);
	}

	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}
	//设置显示名称
	@Override
	public String getStaticLabel() {
		// TODO Auto-generated method stub
		//return JMeterPluginsUtils.prefixLabel("Api Sampler");
		return "@HTTP请求-Api Sampler";
	}
	private void initFields(){
		sessionTokenRadioButton.setSelected(false);
		methodTextField.setText("");
		serverUrlTextField.setText("");
		varsTextField.setText("");
		parameterArgumentsPanel.clear();
		headerArgumentsPanel.clear();
	}
	@Override
	public void clearGui() {
		super.clearGui();
		initFields();
	}
	@Override
	public void modifyTestElement(TestElement sampler) {
		// TODO Auto-generated method stub
		super.configureTestElement(sampler);
		if (sampler instanceof ApiSampler) {
			ApiSampler testSmpler = (ApiSampler) sampler;
			testSmpler.setSessionToken(sessionTokenRadioButton.isSelected());
			testSmpler.setServer(serverUrlTextField.getText());
			testSmpler.setMethod(methodTextField.getText());
			testSmpler.setUserDefinedVariables((Arguments) parameterArgumentsPanel.createTestElement());
			testSmpler.setUserDefinedHeaders((Arguments) headerArgumentsPanel.createTestElement());
			testSmpler.setVars(varsRadioButton.isSelected());
			testSmpler.setProps(propsRadioButton.isSelected());
			testSmpler.setStoredVariables(varsTextField.getText());

			CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(this.tableModel, "threads_file_upload");
			testSmpler.setData(rows);
		}
	}
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		TestElement sampler = new ApiSampler();
		modifyTestElement(sampler);
		return sampler;
	}
	@Override
	public void configure(TestElement element) {
		super.configure(element);
		if(element instanceof ApiSampler){
			ApiSampler sampler = (ApiSampler) element;
			sessionTokenRadioButton.setSelected(sampler.getSessionToken());
			serverUrlTextField.setText(sampler.getServer());
			methodTextField.setText(sampler.getMethod());
			varsRadioButton.setSelected(sampler.getVars());
			propsRadioButton.setSelected(sampler.getProps());
			varsTextField.setText(sampler.getStoredVariables());
			JMeterProperty udv = sampler.getUserDefinedVariablesAsProperty();
			JMeterProperty hdv = sampler.getUserDefinedHeadersAsProperty();
			if (udv != null) {
				parameterArgumentsPanel.configure((Arguments) udv.getObjectValue());
			}
			if (hdv != null) {
				headerArgumentsPanel.configure((Arguments) hdv.getObjectValue());
			}

			JMeterProperty threadValues = sampler.getData();
			if (!(threadValues instanceof NullProperty)) {
				CollectionProperty columns = (CollectionProperty)threadValues;
				this.grid.updateUI();
				JMeterPluginsUtils.collectionPropertyToTableModelRows(columns, this.tableModel);
				updateUI();
			} else {
				log.warn("Received null property instead of collection");
			}

		}		
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
	/**
	 * Add values from the clipboard
	 * @param lineDelimiter Delimiter string to split clipboard into lines
	 * @param argDelimiter Delimiter string to split line into key-value pair
	 */
	protected void addFromClipboard(String lineDelimiter, String argDelimiter) {
		GuiUtils.stopTableEditing(grid);
		int rowCount = grid.getRowCount();
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
					log.info(clipboardCols.toString());
					tableModel.fireTableDataChanged();
				}
			}
			if (grid.getRowCount() > rowCount) {
				// Highlight (select) and scroll to the appropriate rows.
				int rowToSelect = tableModel.getRowCount() - 1;
				grid.setRowSelectionInterval(rowCount, rowToSelect);
				grid.scrollRectToVisible(grid.getCellRect(rowCount, 0, true));
			}
			grid.updateUI();
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String action = e.getActionCommand();
		if (action.equals("add")) {
			tableModel.addNewRow();
			grid.updateUI();
		}
		if (action.equals("AddfromClipboard")) {
			if (grid.isEditing()) {
				TableCellEditor cellEditor = grid.getCellEditor(grid.getEditingRow(), grid.getEditingColumn());
				cellEditor.cancelCellEditing();
			}
			addFromClipboard();
		}
		int rowSelected = this.grid.getSelectedRow();
		if (rowSelected>=0) {
			if (action.equals("delete")) {
				this.tableModel.removeRow(rowSelected);
				grid.updateUI();
			}
			if (action.equals("browse")) {
				String path = browseAndGetFilePath();
				if (StringUtils.isNotBlank(path)){
					this.tableModel.setValueAt(path, rowSelected, 0);
					grid.updateUI();
				}
			}
		}
	}
}