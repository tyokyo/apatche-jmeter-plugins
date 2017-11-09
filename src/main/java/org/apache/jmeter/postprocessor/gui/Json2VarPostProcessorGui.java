package org.apache.jmeter.postprocessor.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jmeter.postprocessor.Json2VarPostProcessor;

public class Json2VarPostProcessorGui extends AbstractPostProcessorGui{
	private static final long serialVersionUID = 1L;
	public static JRadioButton propsRadioButton ;
	public static JRadioButton varsRadioButton;
	public Json2VarPostProcessorGui() {
		init();
	}
	private void init() {
		setBorder(makeBorder());
		setLayout(new BorderLayout());
		JPanel vertPanel = new VerticalPanel();
		vertPanel.add(makeTitlePanel());
		add(vertPanel, BorderLayout.NORTH);

		ButtonGroup buttonGroup = new ButtonGroup();
		propsRadioButton = new JRadioButton("props");
		varsRadioButton = new JRadioButton("vars");
		varsRadioButton.setSelected(true);
		HorizontalPanel domainPanel = new HorizontalPanel();
		buttonGroup.add(propsRadioButton);
		buttonGroup.add(varsRadioButton);
		domainPanel.add(propsRadioButton);
		domainPanel.add(varsRadioButton);
		domainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Variable")); 
		vertPanel.add(domainPanel);

		HorizontalPanel textPanel = new HorizontalPanel();
		JLabeledTextArea textArea = new JLabeledTextArea("User Guide");
		textArea.setEnabled(false);
		textArea.setText("select props,  use ${__props.get(sessiontoken)} to  invoke \nselect vars,use ${sessiontoken} to invoke");
		textPanel.add(textArea);
		//textPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"user guide")); 
		vertPanel.add(textPanel);


	}
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		Json2VarPostProcessor json2VarPostProcessor = new Json2VarPostProcessor();
		modifyTestElement(json2VarPostProcessor);
		return json2VarPostProcessor;
	}
	@Override
	public void clearGui() {
		// TODO Auto-generated method stub
		super.clearGui();
		varsRadioButton.setSelected(true);
		propsRadioButton.setSelected(false);
	}
	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return getClass().getSimpleName();
	}
	@Override
	public String getStaticLabel()
	{
		return JMeterPluginsUtils.prefixLabel("JSON2JMeterVariables Post Processor");
	}
	@Override
	public void modifyTestElement(TestElement element) {
		// TODO Auto-generated method stub
		super.configureTestElement(element);
		if(element instanceof Json2VarPostProcessor){
			Json2VarPostProcessor jpAssertion = (Json2VarPostProcessor) element;
			jpAssertion.setVars(varsRadioButton.isSelected());
			jpAssertion.setProps(propsRadioButton.isSelected());
		}		
	}
	@Override
	public void configure(TestElement element) {
		// TODO Auto-generated method stub
		super.configure(element);
		if(element instanceof Json2VarPostProcessor){
			Json2VarPostProcessor jvp = (Json2VarPostProcessor) element;
			varsRadioButton.setSelected(jvp.getVars());
			propsRadioButton.setSelected(jvp.getProps());
		}		
	}
}
