import javax.swing.*;
import javax.swing.event.*;

public class Main extends JSplitPane {
	protected JTree tree;
	protected JTextArea display;
	
	public Main() {
	
	super(HORIZONTAL_SPLIT);
	// ����
	data_class dc = new data_class("Stack<class T>");
	dc.add(new dm("Stack()", "void", "public"));
	dc.add(new dm("~Stack()", "void", "public"));
	dc.add(new dm("push()", "bool", "public"));
	dc.add(new dm("stackPtr", "T*", "private"));
	
	data_method dm1 = dc.getmethod("Stack()");
	dm1.du_add(new du("size"));   
	dm1.du_add(new du("top"));   
	dm1.du_add(new du("stackPtr"));
	
	//dc�� display�ϱ� ���� ��� ������ �ʿ���
	//ClassTreeModel���� getChild �κ� ���� �ʿ� 
	dm1.dc_add(new dc("asdfjaosidjfpqijefpoiqjpefiojqpwioefjqpfoei"));  
	dm1.dc_add(new dc("Basdfasdfasdfadfi")); 
	dm1.dc_add(new dc("casdfasdgdfjaosidjfpqijefpoiqjpefiojqpwioefjqpfoei"));
	
	
	ClassTreeModel model = new ClassTreeModel(dc);
	
	tree = new JTree(model);
	tree.addTreeSelectionListener(new TreeSelectionListener() {
		public void valueChanged(TreeSelectionEvent e) {
			Object o = e.getPath().getLastPathComponent();
			if(o instanceof data_method) {
				display.append(((data_method)o).du_display()); 
				
				//Display ������ ���������
				//ū Display�������� �ڵ�, ���� Display�������� us���� ��Ÿ������
				
				if(o instanceof data_method) {
					display.append(((data_method)o).dc_display());
				}
			}
			else if (o instanceof data_data) {
				//display.append(((data_data)o.display());
			}
		}
	});
	
	display = new JTextArea(5, 20);
	add(new JScrollPane(tree));
	add(new JScrollPane(display));	
	}
	
	public static void main(String args[]) {
		JFrame f = new JFrame("Tree Model Example");
		Main tree = new Main();
		f.getContentPane().add("Center", tree);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 300);
		f.setVisible(true);
	}
}
