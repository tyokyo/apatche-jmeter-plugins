package elonmeter.csv.jmeter;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import elonmeter.csv.action.ButtonPanelAddCopyRemove;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GridDataSetConfigGui extends AbstractConfigGui implements TableModelListener, CellEditorListener{
	private static final long serialVersionUID = -7191034273955150216L;
	public static final String WIKIPAGE = "GridDataSetConfig";
	private static final Logger log = LoggingManager.getLoggerForClass();
	private JLabeledTextField variableTextField;
	public static  String[] columnIdentifiers = {  };
	@SuppressWarnings("rawtypes")
	public static  Class[] columnClasses = {  };
	protected PowerTableModel tableModel;
	protected JTable grid;
	protected ButtonPanelAddCopyRemove buttons;
	protected ButtonPanelAddEditDelete columnButtons;
	public static String varChangedOldValue="";
	public static String varChangedNewValue="";
	public GridDataSetConfigGui() {
		initGui();
		initGuiValues();
	}
	private void createTableModel() {
		this.tableModel = new PowerTableModel(columnIdentifiers, columnClasses);
		this.tableModel.addTableModelListener(this);
		grid.getTableHeader().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				System.out.println("propertyChange-Listener");
				String header=getTableHeader();
				variableTextField.setText(header);
			}
		});
		//表头不可拖动
		this.grid .getTableHeader().setReorderingAllowed(false);
		
		this.grid.setModel(this.tableModel);
		this.grid.getTableHeader().setToolTipText("variable name");
	}
	private JTable createGrid() {
		this.grid = new JTable();
		this.grid.getDefaultEditor(String.class).addCellEditorListener(this);
		createTableModel();
		this.grid.setSelectionMode(0);
		this.grid.setMinimumSize(new Dimension(200, 100));

		return this.grid;
	}
	private JPanel createParamsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Variables values"));
		panel.setPreferredSize(new Dimension(200, 200));

		JScrollPane scroll = new JScrollPane(createGrid());
		scroll.setPreferredSize(scroll.getMinimumSize());
		panel.add(scroll, "Center");

		this.buttons = new ButtonPanelAddCopyRemove(this.grid, this.tableModel);

		this.columnButtons=new ButtonPanelAddEditDelete(grid, tableModel);
		VerticalPanel btnPanel = new VerticalPanel();
		btnPanel.add(this.buttons);
		btnPanel.add(this.columnButtons);

		panel.add(btnPanel, "South");

		return panel;
	}
	private void initGui() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		HorizontalPanel varPanel = new HorizontalPanel();
		variableTextField=new JLabeledTextField("Variable names(comma-delimited)");
		variableTextField.setEnabled(false);
		varPanel.add(variableTextField);
		varPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Variable Config")); 

		Container topPanel = makeTitlePanel();
		add(JMeterPluginsUtils.addHelpLinkToPanel(topPanel, WIKIPAGE), BorderLayout.NORTH);
		topPanel.add(varPanel,BorderLayout.SOUTH);
		add(topPanel, BorderLayout.NORTH);
		add(createParamsPanel(), BorderLayout.CENTER);
	}

	private void initGuiValues() {
		variableTextField.setText("");
		tableModel.clearData();
	}
	@Override
	public String getLabelResource() {
		return "table_data_set_config";
	}

	@Override
	public String getStaticLabel() {
		return "Grid Data Set Config";
	}

	@Override
	public TestElement createTestElement() {
		GridDataSetConfig element = new GridDataSetConfig();
		modifyTestElement(element);
		return element;
	}

	@Override
	public void modifyTestElement(TestElement element) {
		configureTestElement(element);
		if (element instanceof GridDataSetConfig) {
			GridDataSetConfig randomCSV = (GridDataSetConfig) element;
			if (varChangedOldValue.equals("")&&varChangedNewValue.equals("")) {
				randomCSV.setVariableNames(this.variableTextField.getText());
			}else {
				randomCSV.setVariableNames(this.variableTextField.getText().replace(varChangedOldValue, varChangedNewValue));
			}
			varChangedOldValue="";
			varChangedNewValue="";
			
			if (this.grid.isEditing()) {
				this.grid.getCellEditor().stopCellEditing();
			}
			if ((element instanceof GridDataSetConfig)) {
				GridDataSetConfig utg = (GridDataSetConfig)element;
				CollectionProperty rows = JMeterPluginsUtils.tableModelRowsToCollectionProperty(this.tableModel, "threadgriddataset");
				utg.setData(rows);
			}
		}
	}
	@Override
	public void configure(TestElement element) {
		super.configure(element);
		if (element instanceof GridDataSetConfig) {
			GridDataSetConfig randomCSV = (GridDataSetConfig) element;
			variableTextField.setText(randomCSV.getVariableNames());
			GridDataSetConfig utg = (GridDataSetConfig)element;

			JMeterProperty threadValues = utg.getData();
			if (!(threadValues instanceof NullProperty)) {
				int len =tableModel.getColumnCount();
				for (int i = 0; i < len; i++) {
					tableModel.removeColumn(0);
				}
				CollectionProperty columns = (CollectionProperty)threadValues;
				String[] newIdentifiers=utg.getVariableNames().split(",");
				System.out.println(newIdentifiers.length);
				for (String Identifier : newIdentifiers) {
					if (Identifier.length()==0) {
						
					}else {
						tableModel.addNewColumn(Identifier,String.class);
					}
				}
				this.grid.updateUI();
				JMeterPluginsUtils.collectionPropertyToTableModelRows(columns, this.tableModel);
				updateUI();
			} else {
				log.warn("Received null property instead of collection");
			}
		}
	}

	@Override
	public void clearGui() {
		super.clearGui();
		initGuiValues();
	}
	@Override
	public void editingCanceled(ChangeEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void editingStopped(ChangeEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("tableChanged-Listener");
		String header=getTableHeader();
		System.out.println(header);
		variableTextField.setText(header);
	}
	public String getTableHeader(){
		String header="";
		int columnCount=grid.getTableHeader().getColumnModel().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String columnString=grid.getColumnName(i);
			if (i==0) {
				header=header+columnString;
			}else {
				header=header+","+columnString;
			}
		}
		return header;
	}
	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		super.updateUI();
		if (this.tableModel != null) {
			GridDataSetConfig utgForPreview = new GridDataSetConfig();
			utgForPreview.setData(JMeterPluginsUtils.tableModelRowsToCollectionPropertyEval(this.tableModel, "threadgriddataset"));
		}
	}
}
