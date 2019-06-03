import javax.swing.tree.*;
import javax.swing.event.*;

public class ClassTreeModel implements TreeModel {
	protected data_class data_class;
	
	public ClassTreeModel(data_class c) {
		data_class = c;
	}
	
	public Object getChild(Object parent, int index) {
		if(parent instanceof data_class) {
			data_class d = (data_class) parent;
			return d.getmethod(index);
		}
		else if(parent instanceof data_method) {
			data_method m = (data_method) parent;
			return m.du_get(index); // return m.getdata(index)로 수정해줘야함
		}
		return null;
	}
	
	public int getChildCount(Object parent) {
		if(parent instanceof data_class) {
			return 2;
		}
		else if(parent instanceof data_method) {
			data_method m = (data_method)parent;
			return m.du_size(); //du_size와 dc_size는 동일함 따라서 상관없음
		}
		return 0;
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		if(parent instanceof data_class) {
			data_class c = (data_class) parent;
			return c.getIndexOf((dm)child);
		} else if(parent instanceof data_method) {
			data_method m = (data_method) parent;
			return m.du_getIndexOf((du) child);
		}
		return -1;
	}
	
	public Object getRoot() {
		return data_class;
	}
	
	public boolean isLeaf(Object node) {
		if(node instanceof data_class) {
			return false;
		}
		else if(node instanceof data_method) {
			return false;
		}
		return true;
	}
	
	public void addTreeModelListener(TreeModelListener l) {}
	
	public void removeTreeModelListener(TreeModelListener l) {}
	
	public void valueForPathChanged(TreePath path, Object newValue) {}
	
	
}
