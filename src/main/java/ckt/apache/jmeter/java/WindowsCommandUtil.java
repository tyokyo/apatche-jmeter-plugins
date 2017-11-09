package ckt.apache.jmeter.java;

import java.io.IOException;
import java.util.Scanner;

public class WindowsCommandUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// startTask("E://Fetion//Fetion.exe");
		killTask("javaw");
	}

	/**
	 * 杀死一个进程
	 *
	 * @param task
	 */
	public static void killTask(String task) {
		try {
			Process process = Runtime.getRuntime().exec("taskList");
			Scanner in = new Scanner(process.getInputStream());
			int count = 0;
			while (in.hasNextLine()) {
				count++;
				String temp = in.nextLine();
				if (temp.contains(task)) {
					String[] t = temp.split(" ");
					// 判断该进程所占内存是否大于20M
					if (Integer.parseInt(t[t.length - 2].replace(",", "")) > 20000) {
						temp = temp.replaceAll(" ", "");
						// 获得pid
						String pid = temp.substring(9, temp.indexOf("Console"));
						Runtime.getRuntime().exec("tskill " + pid);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 显示当前机器的所有进程
	 */
	public static void showTaskList() {
		try {
			Process process = Runtime.getRuntime().exec("taskList");
			Scanner in = new Scanner(process.getInputStream());
			int count = 0;
			while (in.hasNextLine()) {
				count++;
				System.out.println(count + ":" + in.nextLine());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 启动一个进程
	 *
	 * @param task
	 */
	public static void startTask(String task) {
		try {
			Runtime.getRuntime().exec(task);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 