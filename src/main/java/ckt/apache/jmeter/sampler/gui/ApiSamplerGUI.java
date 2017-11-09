package ckt.apache.jmeter.sampler.gui;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.gui.layout.VerticalLayout;

import ckt.apache.jmeter.sampler.ApiSampler;

public class ApiSamplerGUI extends AbstractSamplerGui{
	private static final long serialVersionUID = 1L;
	private JLabeledTextField methodTextField;
	public ApiSamplerGUI(){
		init(); 
	}
	@Override
	public void configure(TestElement element) {
		super.configure(element);
	}
	public static JTable getHeaderTable(){
		HeaderTableModel headerModel = new HeaderTableModel(20);
		JTable spTable = new JTable(headerModel){
			private static final long serialVersionUID = 1L;
		};
		spTable.getTableHeader().setReorderingAllowed(false);
		TableColumnModel tcm = spTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(10);
		tcm.getColumn(1).setPreferredWidth(30);
		spTable.setRowHeight(20);  	
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("X_Sioeye_App_Id", "usYhGBBKDMiypaKFV8fc3kE4");
		map.put("X_Sioeye_App_Sign_Key", "5f3773d461775804ca2c942f8589f1d6,1476178217671");
		map.put("X_Sioeye_App_Production", "1");
		headerModel.construct(map);
		JTableHeader tableHeader = spTable.getTableHeader();
		tableHeader.setReorderingAllowed(false);   //设置表格列不可重排
		DefaultTableCellRenderer hr =(DefaultTableCellRenderer)tableHeader.getDefaultRenderer();  //获得表格头的单元格对象
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);  //列名居中

		spTable.getTableHeader().setResizingAllowed(true);
		spTable.setRowSelectionAllowed(true);
		spTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		return spTable;
	}
	public static JTable getParameterTable(){
		ParameterTableModel parameterModel = new ParameterTableModel(20);
		JTable spTable = new JTable(parameterModel){
			private static final long serialVersionUID = 1L;
		};
		spTable.getTableHeader().setReorderingAllowed(false);
		TableColumnModel tcm = spTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(10);
		tcm.getColumn(1).setPreferredWidth(30);
		spTable.setRowHeight(20);  	
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", "application/json");
		map.put("username", "tyokyo@126.com");
		map.put("password", "1234567890");
		parameterModel.construct(map);
		
		JTableHeader tableHeader = spTable.getTableHeader();
		tableHeader.setReorderingAllowed(false);   //设置表格列不可重排
		DefaultTableCellRenderer hr =(DefaultTableCellRenderer)tableHeader.getDefaultRenderer();  //获得表格头的单元格对象
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);  //列名居中

		spTable.getTableHeader().setResizingAllowed(true);
		spTable.setRowSelectionAllowed(true);
		spTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		return spTable;
	}
	public static JScrollPane getHeaderTableScrollPane(){
		JScrollPane tableJScrollPane = new JScrollPane();
		tableJScrollPane.setViewportView(getHeaderTable());
		return tableJScrollPane;
	}
	public static JScrollPane getParameterTableScrollPane(){
		JScrollPane tableJScrollPane = new JScrollPane();
		tableJScrollPane.setViewportView(getParameterTable());
		return tableJScrollPane;
	}
	@SuppressWarnings("deprecation")
	private void init() {
		setLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));
		setBorder(makeBorder());

		add(makeTitlePanel());
		VerticalPanel mainPanel = new VerticalPanel();
		
		HorizontalPanel methodPanel = new HorizontalPanel();
		methodTextField = new JLabeledTextField(ApiSampler.METHOD);
		methodPanel.add(methodTextField);
		JButton borwserButton = new JButton("browser");
		methodPanel.add(borwserButton);
		mainPanel.add(methodPanel);
		
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.add(getHeaderTableScrollPane());
		//headerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Header")); 
		
		HorizontalPanel parameterPanel = new HorizontalPanel();
		parameterPanel.add(getParameterTableScrollPane());
		
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.add("Header",headerPanel);
		jTabbedPane.add("Parameter",parameterPanel);
		
		mainPanel.add(jTabbedPane);
		
		HorizontalPanel actionPanel = new HorizontalPanel();
		JButton addbtn = new JButton("Add");
		JButton delbtn = new JButton("Del");
		actionPanel.add(addbtn);
		actionPanel.add(delbtn);
		mainPanel.add(actionPanel);
		
		add(mainPanel, "Center");
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
		return JMeterPluginsUtils.prefixLabel("Api Sampler");
	}

	private void initFields(){
		
	}

	@Override
	public void clearGui() {
		super.clearGui();
		initFields();
	}

	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		TestElement sampler = new ApiSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public void modifyTestElement(TestElement sampler) {
		// TODO Auto-generated method stub
		super.configureTestElement(sampler);
		if (sampler instanceof ApiSampler) {
			ApiSampler testSmpler = (ApiSampler) sampler;
			//testSmpler.setProperty(TestSampler.FUNCTION, functionTextField.getText());      
		}
	}
}