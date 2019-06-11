package ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import classinfo.PropertyData;

public class ClassTableModel extends AbstractTableModel{
	
	private List<List<String>> tableData;

	String[] columnName = {"Name", "Type", "Access"};
	
	public ClassTableModel(ArrayList<PropertyData> properties) {
		tableData = new ArrayList<List<String>>();
		
		for(PropertyData prop: properties) {
			List<String> stringArr = new ArrayList<String>();
			stringArr.add(prop.getName());
			stringArr.add(prop.getType());
			stringArr.add(prop.getAccess());
			tableData.add(stringArr);
		}
	}
	
	public int getColumnCount() {
		return columnName.length;
	}
	
	public int getRowCount() {
		return tableData.size();
	}
	
	public String getColumnName(int col) {
		return columnName[col];
	}
	
	public Object getValueAt(int row, int col) {
		return tableData.get(row).get(col);
	}
	
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
}
