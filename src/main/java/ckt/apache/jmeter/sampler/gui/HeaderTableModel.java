package ckt.apache.jmeter.sampler.gui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class HeaderTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7495940408592595397L;

	@SuppressWarnings("rawtypes")
	private static Vector content = null;

	private  String[] title_name = {
		"Key",
		"Value"
	};

	@SuppressWarnings("rawtypes")
	public HeaderTableModel() {
		content = new Vector();
	}
	public static void construct(HashMap<String, String> maps){
		content.removeAllElements();
		//Vector vectors = new Vector();
		int i = 0;
		Iterator iter = maps.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			String value = maps.get(key).toString();
			Vector v = new Vector(2);
			v.add(0, key);
			v.add(1, value);
			content.add(v);
		}
		i = i +1;
	}
	public HeaderTableModel(int count) {
		content = new Vector(count);
	}

	public void serialize(){
		int count = getRowCount();
		for (int i = 0; i < count; i++) {
			setValueAt(i, i, 0);
		}
	}
	public void addRow(
			String key,
			String value
			)
	{
		Vector v = new Vector(2);
		v.add(0, key);
		v.add(1, value);
		content.add(v);
		this.fireTableDataChanged();
	}
	public void setValueAt(Object value, int row, int col) {
		((Vector) content.get(row)).remove(col);
		((Vector) content.get(row)).add(col, value);
		this.fireTableCellUpdated(row, col);
	}

	public String getColumnName(int col) {
		return title_name[col];
	}

	public int getColumnCount() {
		return title_name.length;
	}

	public int getRowCount() {
		return content.size();
	}

	public Object getValueAt(int row, int col) {
		return ((Vector) content.get(row)).get(col);
	}

	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
	public ArrayList<Integer> getSelectRows(){
		ArrayList<Integer> sList = new ArrayList<Integer>();
		int count = getRowCount();
		for (int i = 0; i < count; i++) {
			boolean select = Boolean.parseBoolean(String.valueOf(getValueAt(i, 1)));
			if (select) {
				sList.add(i);
			}
		}
		return sList;
	}
}






