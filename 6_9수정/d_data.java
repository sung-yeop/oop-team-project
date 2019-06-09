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
		// table로 display할 수 있도록 데이터를 넘겨주던가 아니면 바로 display
	}
}
