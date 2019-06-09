import javax.swing.tree.*;
import javax.swing.event.*;

public class ClassTreeModel implements TreeModel{
	protected d_class d_class;
	
	public ClassTreeModel(d_class d_class) {
		this.d_class = d_class;
	}
	
	public Object getChild(Object parent, int index) {
		if(parent instanceof d_class) {  //최상위 바로 아래 노드에서 method와 data를 구분 짓는 코드 <- 안될 수도 있음 
			d_class c = (d_class) parent;
			return c.get_method(index);
		}
		else 
			return null;
	}
	
	public int getChildCount(Object parent) {
		if(parent instanceof d_class) {
			return 4;
		}
		else 
			return 0;
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof d_class) {
			d_class d = (d_class) parent;
			return d.get_methodIndexOf((d_method)child);
		}
		else 
			return -1;
	}
	
	public Object getRoot() {
		return d_class;
	}
	
	public boolean isLeaf(Object node) {
		if(node instanceof d_class) {
			return false;
		}
		else
			return true;
	}
	

	public void addTreeModelListener(TreeModelListener l) {}
	
	public void removeTreeModelListener(TreeModelListener l) {}
	
	public void valueForPathChanged(TreePath path, Object newValue) {}
	
}

