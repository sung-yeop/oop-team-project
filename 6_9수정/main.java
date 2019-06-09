import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class main extends JSplitPane{
	protected JTree tree;
	protected JTextArea display_code;
	protected JTextArea display_use;
	
	
	public main() {
		super(HORIZONTAL_SPLIT);
		
		JTable c_table; //1)�߰�
		JTable d_table; //1)�߱�
		
		//table display
		
		ClassTableModel c_table_model = new ClassTableModel(); //1)�߰�
		DataTableModel d_table_model = new DataTableModel(); //1)�߰�
		
		c_table = new JTable(c_table_model); //1)�߰�
		d_table = new JTable(d_table_model); //1)�߰�
	
		//
		//�ֻ��� ����
		d_class d_c = new d_class("<�ֻ��� �̸�>");
		//���� ��� (method ����)
		d_method d_m1 = d_c.get_method("Stack()");
		d_method d_m2 = d_c.get_method("~Stack()");
		d_method d_m3 = d_c.get_method("push()");
		d_method d_m4 = d_c.get_method("stackPtr");
		//���� ��� (data ����)
		/*
		d_data d_d1 = d_c.get_data("size");
		d_data d_d2 = d_c.get_data("stackPtr");
		d_data d_d3 = d_c.get_data("ccc");
		d_data d_d4 = d_c.get_data("ddd");
		*/
		//�ֻ��� Table ������ �߰�
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
					display_code.append(((d_method)o).d_code_display());
					display_use.append(((d_method)o).d_use_display());
				}
				else if(o instanceof d_class) {
					getRootPane().add(new JScrollPane(c_table), "East"); //1)�߰�
					//getContentPane()
					setSize(100,50); //1)�߰�
					setVisible(true); //1)�߰�
				}
				else if(o instanceof d_data) {
					getRootPane().add(new JScrollPane(d_table), "East"); //1)�߰�
					//getContentPane()
					setSize(100,50); //1)�߰�
					setVisible(true); //1)�߰�
				}
			}
		});
		display_code = new JTextArea(5, 20);
		display_use = new JTextArea(5, 5);
		add(new JScrollPane(tree));
		add(new JScrollPane(display_code));
		add(new JScrollPane(display_use));
	}
	
	
	public static void main(String args[]) {
		JFrame f = new JFrame("Tree Model");
		main tree = new main();
		f.getContentPane().add("Center", tree);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500,400);
		f.setVisible(true);
		
	}
}
