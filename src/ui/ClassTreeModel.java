package ui;

import javax.swing.tree.*;

import classinfo.ClassInformation;

import javax.swing.event.*;

public class ClassTreeModel implements TreeModel{
	protected ClassInformation d_class;
	
	public ClassTreeModel(ClassInformation d_class) {
		this.d_class = d_class;
	}
	
	public Object getChild(Object parent, int index) {
		if(parent instanceof ClassInformation) {  //최상위 바로 아래 노드에서 method와 data를 구분 짓는 코드 <- 안될 수도 있음 
			ClassInformation c = (ClassInformation) parent;
			return c.getAllProperty().get(index);
		}
		else 
			return null;
	}
	
	public int getChildCount(Object parent) {
		if(parent instanceof ClassInformation) {
			return ((ClassInformation) parent).getAllProperty().size();
		}
		else 
			return 0;
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof ClassInformation) {
			ClassInformation d = (ClassInformation) parent;
			return d.getAllProperty().indexOf(child);
		}
		else 
			return -1;
	}
	
	public Object getRoot() {
		return d_class;
	}
	
	public boolean isLeaf(Object node) {
		if(node instanceof ClassInformation) {
			return false;
		}
		else
			return true;
	}
	

	public void addTreeModelListener(TreeModelListener l) {}
	
	public void removeTreeModelListener(TreeModelListener l) {}
	
	public void valueForPathChanged(TreePath path, Object newValue) {}
	
}