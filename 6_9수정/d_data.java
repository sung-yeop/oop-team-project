import java.util.*;

class d_data {
	protected String name;
	protected ArrayList<d_methods> d_methods;
	
	public d_data(String name) {
		this.name = name;
		d_methods = new ArrayList<d_methods>();
	}
	
	public String toString() {
		return name;
	}
	
	public void display() {
		// table�� display�� �� �ֵ��� �����͸� �Ѱ��ִ��� �ƴϸ� �ٷ� display
	}
}
