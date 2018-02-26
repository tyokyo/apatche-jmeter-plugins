package org.apache.jmeter.asserations.jsonpathassertion.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.asserations.jsonpathassertion.JSONAssertion;
import org.apache.jmeter.assertions.gui.AbstractAssertionGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import sioeye.spider.helpers.UrlHelper;
import elonmeter.csv.jmeter.HelpPanel;

public class JSONAssertionGui extends AbstractAssertionGui
implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	private JLabeledTextField jsonPath = null;
	private JComboBox<String> operator = null;
	private JLabeledTextField ifJsonField= null;
	private JTextField expectField= null;
	private JLabeledTextArea jsonValue = null;
	private JCheckBox jsonValidation = null;
	private JCheckBox expectNull = null;
	private JCheckBox invert = null;
	private static final String WIKIPAGE = "JSONAssertion";
	private JCheckBox isRegex;

	public JSONAssertionGui()
	{
		init();
	}

	public void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());
		//add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), "JSONAssertion"), "North");
		Container container = makeTitlePanel();
		add(HelpPanel.addHelpLinkToPanel(container, UrlHelper.api_assertion_sioeye), BorderLayout.NORTH);

		HorizontalPanel assertPanel = new HorizontalPanel();
		this.ifJsonField = new JLabeledTextField("JSON Path:");
		this.expectField = new JTextField();
		this.operator = new JComboBox<String>();
		this.operator.addItem("=");
		this.operator.addItem(">");
		this.operator.addItem("<");
		this.operator.addItem("!=");
		
		assertPanel.add(this.ifJsonField);
		assertPanel.add(this.operator);
		assertPanel.add(this.expectField);
		assertPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"If ")); 

		VerticalPanel panel = new VerticalPanel();
		//panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		VerticalPanel thenPanel = new VerticalPanel();
		thenPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Then")); 
		this.jsonPath = new JLabeledTextField("JSON Path: ");
		this.jsonValidation = new JCheckBox("Validate against expected value");
		this.isRegex = new JCheckBox("Match as regular expression");
		this.jsonValue = new JLabeledTextArea("Expected Value: ");
		this.expectNull = new JCheckBox("Expect null");
		this.invert = new JCheckBox("Invert assertion (will fail if above conditions met)");

		this.jsonValidation.addChangeListener(this);
		this.expectNull.addChangeListener(this);
		thenPanel.add(this.jsonPath);
		thenPanel.add(this.jsonValidation);
		thenPanel.add(this.isRegex);
		thenPanel.add(this.jsonValue);
		thenPanel.add(this.expectNull);
		thenPanel.add(this.invert);

		panel.add(assertPanel);
		panel.add(thenPanel);

		add(panel, "Center");
	}

	public void clearGui()
	{
		super.clearGui();
		this.operator.setSelectedIndex(0);
		this.ifJsonField.setText("$.");
		this.expectField.setText("");
		this.jsonPath.setText("$.");
		this.jsonValue.setText("");
		this.jsonValidation.setSelected(false);
		this.expectNull.setSelected(false);
		this.invert.setSelected(false);
		this.isRegex.setSelected(true);
	}

	public TestElement createTestElement()
	{
		JSONAssertion jpAssertion = new JSONAssertion();
		modifyTestElement(jpAssertion);
		jpAssertion.setComment(JMeterPluginsUtils.getWikiLinkText("JSONAssertion"));
		return jpAssertion;
	}

	public String getLabelResource()
	{
		return getClass().getSimpleName();
	}

	public String getStaticLabel()
	{
		return "@JSON Assertion";
	}

	public void modifyTestElement(TestElement element)
	{
		super.configureTestElement(element);
		if ((element instanceof JSONAssertion)) {
			JSONAssertion jpAssertion = (JSONAssertion)element;
			jpAssertion.setOperator(this.operator.getSelectedItem().toString());
			jpAssertion.setJsonIf(this.ifJsonField.getText());
			jpAssertion.setJsonExpect(this.expectField.getText());
			jpAssertion.setJsonPath(this.jsonPath.getText());
			jpAssertion.setExpectedValue(this.jsonValue.getText());
			jpAssertion.setJsonValidationBool(this.jsonValidation.isSelected());
			jpAssertion.setExpectNull(this.expectNull.isSelected());
			jpAssertion.setInvert(this.invert.isSelected());
			jpAssertion.setIsRegex(this.isRegex.isSelected());
		}
	}

	public void configure(TestElement element)
	{
		super.configure(element);
		JSONAssertion jpAssertion = (JSONAssertion)element;
		this.operator.setSelectedItem(jpAssertion.getOperator());
		this.ifJsonField.setText(jpAssertion.getJsonIf());
		this.expectField.setText(jpAssertion.getJsonExpect());
		this.jsonPath.setText(jpAssertion.getJsonPath());
		this.jsonValue.setText(jpAssertion.getExpectedValue());
		this.jsonValidation.setSelected(jpAssertion.isJsonValidationBool());
		this.expectNull.setSelected(jpAssertion.isExpectNull());
		this.invert.setSelected(jpAssertion.isInvert());
		this.isRegex.setSelected(jpAssertion.isUseRegex());
	}

	public void stateChanged(ChangeEvent e)
	{
		this.jsonValue.setEnabled((this.jsonValidation.isSelected()) && (!this.expectNull.isSelected()));
		this.isRegex.setEnabled((this.jsonValidation.isSelected()) && (!this.expectNull.isSelected()));
	}
}