import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import classinfo.ClassInformation;
import classinfo.ClassMethod;
import classinfo.ClassVariable;
import classinfo.PropertyData;
import ui.*;

public class Main extends JFrame {
	protected JTree tree;
	protected JTextArea display_code;
	protected JTextArea display_use;
	
	public Main(ClassInformation classInfo) {
		
		setLayout(null);
		
		JTable c_table; //1)추가
		JTable d_table; //1)추기
		
		JPanel Panel_Tree = new JPanel();
		JPanel Panel_Use = new JPanel();
		JPanel Panel_Code = new JPanel();
		JPanel Panel_C_Table = new JPanel();
		JPanel Panel_D_Table = new JPanel();
		
		display_code = new JTextArea(60, 30);
		display_use = new JTextArea(10, 5);
		
		Panel_Use.setBorder(new TitledBorder("<Use>"));
		Panel_Code.add(display_code);
		Panel_Use.add(display_use);
		
		//table display
		
		ClassTableModel c_table_model = new ClassTableModel(classInfo.getAllProperty()); 
		
		c_table = new JTable(c_table_model); //1)추가
		d_table = new JTable(d_table_model); //1)추가
		
		//6_12 Table Column 길이 변경
		c_table.getColumnModel().getColumn(0).setPreferredWidth(85);
		c_table.getColumnModel().getColumn(1).setPreferredWidth(85);
		c_table.getColumnModel().getColumn(2).setPreferredWidth(85);
		
		d_table.getColumnModel().getColumn(0).setPreferredWidth(85);
		d_table.getColumnModel().getColumn(1).setPreferredWidth(85);
		
		
		Panel_C_Table.add(c_table);
		Panel_D_Table.add(d_table);
		
		ClassTreeModel tree_model = new ClassTreeModel(classInfo);
		
		tree = new JTree(tree_model);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				Object o = e.getPath().getLastPathComponent();
				if(o instanceof ClassMethod) {
					display_code.setText(((ClassMethod)o).getContent());
					Panel_Code.setVisible(true);
					List<String> variableNames = ((ClassMethod)o).getVariables().stream().map(x -> x.getName()).collect(Collectors.toList());;
					display_use.setText(String.join("\n", variableNames));
					Panel_Use.setVisible(true);
				}
				else if(o instanceof ClassInformation) {

					Panel_C_Table.setVisible(true);
					Panel_D_Table.setVisible(false);
					Panel_Code.setVisible(false);
					Panel_Use.setVisible(false);
				}
				else if(o instanceof ClassVariable) {
					DataTableModel d_table_model = new DataTableModel((ClassVariable)o);
					d_table.setModel(d_table_model);
					Panel_D_Table.setVisible(true);
					Panel_C_Table.setVisible(false);
					Panel_Code.setVisible(false);
					Panel_Use.setVisible(false);
				}
			}
		});
		
		//##
		Panel_Code.setBounds(215, 20, 300, 450);
		getContentPane().add(Panel_Code);
		
		Panel_Use.setBounds(5, 330, 200, 120);
		getContentPane().add(Panel_Use);
		
		Panel_Tree.setBounds(5, 20, 200, 300);
		getContentPane().add(Panel_Tree);
		Panel_Tree.setVisible(true);
		


		Panel_C_Table.setBounds(215, 20, 300, 450);
		getContentPane().add(Panel_C_Table);
		
		Panel_D_Table.setBounds(215, 20, 300, 450);
		getContentPane().add(Panel_D_Table);
		
		Panel_Tree.add(tree);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(535,490);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer buffer = ReadFile.read("examples/Stack.h");
		if(buffer != null) {
			Parsing p = new Parsing();
			ClassInformation classInfo = p.parse(buffer);
			// UI 뿌리기
			classInfo.getName(); // 클래스의 이름
			classInfo.getMethods(); // 클래스에서 사용중인 모든 메소드를 가져옴
			classInfo.getVariables(); // 클래스에서 사용중인 모든 변수를 가져옴
			ArrayList<PropertyData> aa = classInfo.getAllProperty(); // 클래스가 사용하는 모든 메소드와 변수의 이름, 타입, 접근제어자를 가져옴
			
			new Main(classInfo);
		}
		
	}

}
