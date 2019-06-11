package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.*;

import classinfo.ClassMethod;
import classinfo.ClassVariable;

public class DataTableModel extends AbstractTableModel{
	
	private List<List<String>> tableData;

	String[] columnName = {"Name", "Methods"};
	
	public DataTableModel(ClassVariable variable) {
		tableData = new ArrayList<List<String>>();
		List<String> stringArr = new ArrayList<String>();
		stringArr.add(variable.getName());
		List<String> methodNames = variable.getMethods().stream().map(x -> x.getName()).collect(Collectors.toList());
		stringArr.add(String.join("\n", methodNames));
		tableData.add(stringArr);
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