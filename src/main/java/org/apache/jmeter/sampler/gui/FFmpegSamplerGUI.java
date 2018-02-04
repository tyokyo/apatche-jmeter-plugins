package org.apache.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.JButton;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.sampler.ApiSampler;
import org.apache.jmeter.sampler.FFmpegSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sioeye.spider.helpers.UrlHelper;
import elonmeter.csv.jmeter.HelpPanel;

public class FFmpegSamplerGUI extends AbstractSamplerGui{
	@Override
	protected void configureTestElement(TestElement mc) {
		// TODO Auto-generated method stub
		super.configureTestElement(mc);
	}
	
	private static final Logger log = LoggerFactory.getLogger(FFmpegSamplerGUI.class);
	private static final long serialVersionUID = 1L;
	private static JLabeledTextField videoPathTextField;
	private static JLabeledTextField pushUrlTextField;
	private static JLabeledTextField ffpmegTextField;
	public static  JButton borwserFfmpegButton;
	public static  JButton borwserVideoButton;
	public static int w;
	public static int h;
	public FFmpegSamplerGUI(){
		ffpmegTextField = new JLabeledTextField("ffmpeg path");
		pushUrlTextField = new JLabeledTextField("push url");
		videoPathTextField = new JLabeledTextField("video path");
		borwserFfmpegButton = new JButton("browser");
		borwserVideoButton = new JButton("browser");
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
		ffmpegPanel.add(ffpmegTextField);
		ffmpegPanel.add(borwserFfmpegButton);
		mainPanel.add(ffmpegPanel);
		
		HorizontalPanel urlPanel = new HorizontalPanel();
		urlPanel.add(pushUrlTextField);
		mainPanel.add(urlPanel);
		
		HorizontalPanel videoPanel = new HorizontalPanel();
		videoPanel.add(videoPathTextField);
		videoPanel.add(borwserVideoButton);
		mainPanel.add(videoPanel);

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
		return JMeterPluginsUtils.prefixLabel("FFMpeg Sampler");
	}
	private void initFields(){
		ffpmegTextField.setText("");
		pushUrlTextField.setText("");
		videoPathTextField.setText("");
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
		if (sampler instanceof FFmpegSampler) {
			FFmpegSampler fFmpegSampler = (FFmpegSampler) sampler;
			fFmpegSampler.setffpmeg(ffpmegTextField.getText());
			fFmpegSampler.setVideoPushUrl(pushUrlTextField.getText());
			fFmpegSampler.setVideoPath(videoPathTextField.getText());
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
		if(element instanceof FFmpegSampler){
			FFmpegSampler fFmpegSampler = (FFmpegSampler) element;
			ffpmegTextField.setText(fFmpegSampler.getffpmeg());
			videoPathTextField.setText(fFmpegSampler.getVideoPath());
			pushUrlTextField.setText(fFmpegSampler.getVideoPushUrl());
		}		
	}
}