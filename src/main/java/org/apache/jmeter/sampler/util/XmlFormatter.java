package org.apache.jmeter.sampler.util;

import org.apache.xml.serialize.OutputFormat;  
import org.apache.xml.serialize.OutputFormat;  
import org.apache.xml.serialize.XMLSerializer;  
import org.w3c.dom.Document;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import java.io.IOException;  
import java.io.StringReader;  
import java.io.StringWriter;  
import java.io.Writer;  
@SuppressWarnings("deprecation")
public class XmlFormatter {  
    @SuppressWarnings("deprecation")
	public String format(String unformattedXml) {  
        try {  
            final Document document = parseXmlFile(unformattedXml);  
            OutputFormat format = new OutputFormat(document);  
            format.setLineWidth(65);  
            format.setIndenting(true);  
            format.setIndent(2);  
            Writer out = new StringWriter();  
            XMLSerializer serializer = new XMLSerializer(out, format);  
            serializer.serialize(document);  
            return out.toString();  
        } catch (IOException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    private Document parseXmlFile(String in) {  
        try {  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            InputSource is = new InputSource(new StringReader(in));  
            return db.parse(is);  
        } catch (ParserConfigurationException e) {  
            throw new RuntimeException(e);  
        } catch (SAXException e) {  
            throw new RuntimeException(e);  
        } catch (IOException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    public static void main(String[] args) throws Exception{  
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PARAM><DBID>35</DBID><SEQUENCE>atgtca</SEQUENCE><MAXNS>10</MAXNS><MINIDENTITIES>90</MINIDENTITIES><MAXEVALUE>10</MAXEVALUE><USERNAME>admin</USERNAME><PASSWORD>111111</PASSWORD><TYPE>P</TYPE><RETURN_TYPE>2</RETURN_TYPE></PARAM>";//未格式化前的xml  
        System.out.println(new XmlFormatter().format(s));  
    }  
}  