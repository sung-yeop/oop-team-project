import java.util.*;

public class data_method {
	public String name;
	public ArrayList<du> du_member;
	public ArrayList<dc> dc_member; 
	//dc.java에는 코드를 받아야하기 때문에 ArrayList<String>형식으로 데이터를 받음
	
	public data_method(String name) {
		this.name = name;
		du_member = new ArrayList<du>();
		dc_member = new ArrayList<dc>();
	}
	
	public void du_add(du e) {
		du_member.add(e);
	}
	
	public void dc_add(dc e) {
		dc_member.add(e);
	}
	
	public du du_get(int index) {
		return du_member.get(index);
	}
	
	public dc dc_get(int index) {
		return dc_member.get(index);
	}
	
	public int du_size() {
		return du_member.size();
	}
	
	public int dc_size() {
		return dc_member.size();
	}
	
	public int du_getIndexOf(du e) {
		for(int i = 0; i < du_member.size(); i++) {
			du duu = du_member.get(i);
			if(duu == e) {
				return i;
			}
		}
		return -1;
	}
	
	public int dc_getIndexOf(dc e) {
		for(int i = 0; i < dc_member.size(); i++) {
			dc dcc = dc_member.get(i);
			if(dcc == e) {
				return i;
			}
		}
		return -1;
	}

	public String toString() {
		return name;
	}
	
	public String du_display() {
		String info = "use>";
		for(int i = 0; i < du_member.size(); i++) {
			info += "\n" + du_member.get(i);
		}
		return info;
	}
	
	public String dc_display() {
		String info = "\n";
		for(int i = 0; i < dc_member.size(); i++) {
			info += "\n" + dc_member.get(i);
		}
		return info;
	}
	// 작은 display에는 use만 표시
	// 큰 display에는 코드를 표시
}
