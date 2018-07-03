package org.apache.jmeter.sampler.util;

public class FileFilter extends javax.swing.filechooser.FileFilter{
	public boolean accept(java.io.File f) {
		if (f.isDirectory())return true;
		return f.getName().endsWith(".png")||f.getName().endsWith(".jpg")||f.getName().endsWith(".mp4"); 
	} 
	public String getDescription(){
		return ".png.jpg.mp4";
	}
}
