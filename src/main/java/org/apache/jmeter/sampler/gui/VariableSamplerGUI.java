package org.apache.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JButton;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.sampler.VariableSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sioeye.spider.helpers.UrlHelper;
import elonmeter.csv.jmeter.HelpPanel;

public class VariableSamplerGUI extends AbstractSamplerGui{
	@Override
	protected void configureTestElement(TestElement mc) {
		// TODO Auto-generated method stub
		super.configureTestElement(mc);
	}
	
	private static final Logger log = LoggerFactory.getLogger(VariableSamplerGUI.class);
	private static final long serialVersionUID = 1L;
	private static JLabeledTextField variablesTextField;
	private static JLabeledTextField writeToLocationTextField;
	public static  JButton borwserFfmpegButton;
	public static int w;
	public static int h;
	public VariableSamplerGUI(){
		writeToLocationTextField = new JLabeledTextField("write to file:");
		borwserFfmpegButton = new JButton("browser");
		variablesTextField = new JLabeledTextField("variable names(,)");
		init(); 
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

		HorizontalPanel ffmpegPanel = new HorizontalPanel();
		//serverUrlTextField.setText("https://api.siocloud.sioeye.cn/functions/");
		ffmpegPanel.add(writeToLocationTextField);
		ffmpegPanel.add(borwserFfmpegButton);
		mainPanel.add(ffmpegPanel);
		
		HorizontalPanel urlPanel = new HorizontalPanel();
		urlPanel.add(variablesTextField);
		mainPanel.add(urlPanel);

		add(mainPanel, BorderLayout.CENTER);
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
		return JMeterPluginsUtils.prefixLabel("Write To File Sampler");
	}
	private void initFields(){
		writeToLocationTextField.setText("");
		variablesTextField.setText("");
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
		if (sampler instanceof VariableSampler) {
			VariableSampler variableSampler = (VariableSampler) sampler;
			variableSampler.setWriteToPath(writeToLocationTextField.getText());
			variableSampler.setVariableNames(variablesTextField.getText());
		}
	}
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		TestElement sampler = new VariableSampler();
		modifyTestElement(sampler);
		return sampler;
	}
	@Override
	public void configure(TestElement element) {
		super.configure(element);
		if(element instanceof VariableSampler){
			VariableSampler variableSampler = (VariableSampler) element;
			writeToLocationTextField.setText(variableSampler.getWriteToPath());
			variablesTextField.setText(variableSampler.getVariableNames());
		}		
	}
}