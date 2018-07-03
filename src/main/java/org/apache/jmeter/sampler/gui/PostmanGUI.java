package org.apache.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.sampler.PostmanSampler;
import org.apache.jmeter.sampler.util.FileFilter;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sioeye.spider.helpers.UrlHelper;
import elonmeter.csv.jmeter.HelpPanel;

public class PostmanGUI extends AbstractSamplerGui{
	@Override
	protected void configureTestElement(TestElement mc) {
		// TODO Auto-generated method stub
		super.configureTestElement(mc);
	}
	private  JLabeledChoice choice;
	private  JLabeledTextField pathTextField; 
	private  JLabeledTextField fileUploadTextField;
	private   JButton borwserButton;
	private  final Logger log = LoggerFactory.getLogger(PostmanGUI.class);

	public  int w;
	public  int h;
	public PostmanGUI(){
		String [] method = {"PUT"};                //定义字符串
		choice = new JLabeledChoice("method",method);
		pathTextField = new JLabeledTextField("request path");

		fileUploadTextField=new JLabeledTextField("  upload file path");
		borwserButton= new JButton("browser");
		init(); 
	}
	private String getJmeterHome(){
		Map<String,String> map = System.getenv(); 
		Iterator<?> it = map.entrySet().iterator(); 
		while(it.hasNext()) 
		{ 
			Entry<?, ?> entry = (Entry<?, ?>)it.next(); 
			if ("JMETER_HOME".equals(entry.getKey())) {
				log.info(entry.getValue().toString());
				return entry.getValue().toString();
			}
		} 
		return "";
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

		HorizontalPanel urlPanel = new HorizontalPanel();
		//serverUrlTextField.setText("https://api.siocloud.sioeye.cn/functions/");
		urlPanel.add(choice);
		urlPanel.add(pathTextField);
		mainPanel.add(urlPanel);

		HorizontalPanel filePanel = new HorizontalPanel();
		filePanel.add(fileUploadTextField);
		borwserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();
				String dir =  getJmeterHome();
				if (new File(dir).isDirectory()) {
					jfc.setCurrentDirectory(new File(getJmeterHome()));  
				}
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
				FileFilter filter = new FileFilter();
				jfc.setFileFilter(filter);
				//jfc.showDialog(new JLabel(), "选择");  
				int intRetVal = jfc.showOpenDialog(new JFrame("选择"));  
				if (intRetVal == JFileChooser.APPROVE_OPTION) {
					String file = jfc.getSelectedFile().getPath();
					//获取操作系统
					Properties prop = System.getProperties();
					String os = prop.getProperty("os.name");
					//判断是不是windiws操作系统
					if(os.startsWith("win") || os.startsWith("Win")){ 
						//windows操作系统,斜杠做转换
						file = file.replaceAll("\\\\", "/");
					} 
					fileUploadTextField.setText(file);
					log.info(file);
				} 
			}
		});

		filePanel.add(borwserButton);
		mainPanel.add(filePanel);

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
		return JMeterPluginsUtils.prefixLabel("@FileUpload Sampler");
	}
	private void initFields(){
		choice.setSelectedIndex(0);
		pathTextField.setText("");
		fileUploadTextField.setText("");
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
		if (sampler instanceof PostmanSampler) {
			PostmanSampler postmanSampler = (PostmanSampler) sampler;
			String selectedMethod = choice.getText();
			postmanSampler.setMethod(selectedMethod);
			String path = pathTextField.getText();
			postmanSampler.setRequsetPath(path);
			String fileUploadLoc = fileUploadTextField.getText();
			postmanSampler.setUploadFile(fileUploadLoc);
		}
	}
	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		TestElement sampler = new PostmanSampler();
		modifyTestElement(sampler);
		return sampler;
	}
	@Override
	public void configure(TestElement element) {
		super.configure(element);
		if(element instanceof PostmanSampler){
			PostmanSampler postmanSampler = (PostmanSampler) element;
			String method = postmanSampler.getMethod();
			String[] items = choice.getItems();
			int index=0;
			for (int i = 0; i < items.length; i++) {
				String itemString=items[i];
				if (itemString.equals(method)) {
					index=i;
					break;
				}
			}
			choice.setSelectedIndex(index);
			pathTextField.setText(postmanSampler.getRequsetPath());
			fileUploadTextField.setText(postmanSampler.getUploadFile());
		}		
	}
}
