import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

public class main extends JFrame{
	protected JTree tree;
	protected JTextArea display_code;
	protected JTextArea display_use;
	
	public main() {
		
		setLayout(null);
		
		JTable c_table; //1)추가
		JTable d_table; //1)추기
		
		JPanel Panel_Tree = new JPanel();
		JPanel Panel_Use = new JPanel();
		JPanel Panel_Code = new JPanel();
		JPanel Panel_C_Table = new JPanel();
		JPanel Panel_D_Table = new JPanel();
		
		//table display
		
		ClassTableModel c_table_model = new ClassTableModel(); //1)추가
		DataTableModel d_table_model = new DataTableModel(); //1)추가
		
		c_table = new JTable(c_table_model); //1)추가
		d_table = new JTable(d_table_model); //1)추가
	
		//##
		//##
		
		//
		//최상위 생성
		d_class d_c = new d_class("<최상위 이름>");
		//하위 노드 (method 생성)
		d_method d_m1 = d_c.get_method("Stack()");
		d_method d_m2 = d_c.get_method("~Stack()");
		d_method d_m3 = d_c.get_method("push()");
		d_method d_m4 = d_c.get_method("stackPtr");
		
		d_m1.add(new d_use_code("use1", "code1"));
		d_m2.add(new d_use_code("use2", "code2"));
		d_m3.add(new d_use_code("use3", "code3"));
		d_m4.add(new d_use_code("use4", "code4"));
		//하위 노드 (data 생성)
		/*
		d_data d_d1 = d_c.get_data("size");
		d_data d_d2 = d_c.get_data("stackPtr");
		d_data d_d3 = d_c.get_data("ccc");
		d_data d_d4 = d_c.get_data("ddd");
		*/
		//최상위 Table 데이터 추가
		/*
		d_c.add(new d_name_type_access("Stack()", "void", "public"));
		d_c.add(new d_name_type_access("~Stack()", "void", "public"));
		d_c.add(new d_name_type_access("push()", "bool", "public"));
		d_c.add(new d_name_type_access("stackPtr", "T*", "private"));
		*/
		
		ClassTreeModel tree_model = new ClassTreeModel(d_c);
		
		
		tree = new JTree(tree_model);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				Object o = e.getPath().getLastPathComponent();
				if(o instanceof d_method) {
					Panel_Code.add(display_code);
					display_code.append(((d_method)o).d_code_display());
					Panel_Code.setVisible(true);
					
					Panel_Use.add(display_use);
					display_use.append(((d_method)o).d_use_display());
					Panel_Use.setBorder(new TitledBorder("<Use>"));
					Panel_Use.setVisible(true);
				}
				else if(o instanceof d_class) {
					Panel_C_Table.add(c_table);
					Panel_C_Table.setVisible(true);
				}
				else if(o instanceof d_data) {
					Panel_D_Table.add(d_table);
					Panel_D_Table.setVisible(true);
				}
			}
		});
		display_code = new JTextArea(60, 30);
		display_use = new JTextArea(10, 5);
		
		//##
		Panel_Code.setBounds(215, 20, 300, 450);
		getContentPane().add(Panel_Code);
		
		Panel_Use.setBounds(5, 330, 200, 120);
		getContentPane().add(Panel_Use);
		
		Panel_Tree.setBounds(5, 20, 200, 300);
		getContentPane().add(Panel_Tree);
		Panel_Tree.setVisible(true);
		


		Panel_C_Table.setBounds(215, 20, 300, 450);
		
		Panel_D_Table.setBounds(215, 20, 300, 450);
		
		Panel_Tree.add(tree);
	}
	
	
	public static void main(String args[]) {
		main main = new main();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(535,490);
		main.setVisible(true);
		
	}
}
