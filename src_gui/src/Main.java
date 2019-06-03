import javax.swing.*;
import javax.swing.event.*;

public class Main extends JSplitPane {
	protected JTree tree;
	protected JTextArea display;
	
	public Main() {
	
	super(HORIZONTAL_SPLIT);
	// 구상
	data_class dc = new data_class("Stack<class T>");
	dc.add(new dm("Stack()", "void", "public"));
	dc.add(new dm("~Stack()", "void", "public"));
	dc.add(new dm("push()", "bool", "public"));
	dc.add(new dm("stackPtr", "T*", "private"));
	
	data_method dm1 = dc.getmethod("Stack()");
	dm1.du_add(new du("size"));   
	dm1.du_add(new du("top"));   
	dm1.du_add(new du("stackPtr"));
	
	//dc를 display하기 위한 경로 설정이 필요함
	//ClassTreeModel에서 getChild 부분 수정 필요 
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
				
				//Display 공간을 나눠줘야함
				//큰 Display공간에는 코드, 작은 Display공간에는 usㄷ를 나타내도록
				
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
