import javax.swing.*;
import javax.swing.table.*;

public class DataTableModel extends AbstractTableModel{
	
	Object[][] class_table_data = {
			{"asdF","asdf,asdf"},
			{"asdF","asdf,asdfadsf"},
			{"asdF","asdf,asdgasdg"},
			{"asdF","asdf,asdhsdgsg"}
	};

	String[] columnName = {"Name", "methods"};
	
	public int getColumnCount() {
		return columnName.length;
	}
	
	public int getRowCount() {
		return class_table_data.length;
	}
	
	public String getColumnName(int col) {
		return columnName[col];
	}
	
	public Object getValueAt(int row, int col) {
		return class_table_data[row][col];
	}
	
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
}
