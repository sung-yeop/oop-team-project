import java.util.*;

class d_method {
	protected String name;
	protected ArrayList<d_use_code> d_use_code_member;
	
	public d_method(String name) {
		this.name = name;
		
		d_use_code_member = new ArrayList<d_use_code>();
	}
	
	public void add(d_use_code e) {
		d_use_code_member.add(e);
	}
	
	public d_use_code use_code_get(int index) {
		return d_use_code_member.get(index);
	}
	
	public int size() {
		return d_use_code_member.size(); 
	} //어차피 코드와 use의 갯수는 동일하므로 대표적으로 d_use의 size를 return함
	
	public int getIndexOf(d_use_code e) {
		for(int i = 0; i < d_use_code_member.size(); i++) {
			d_use_code du = d_use_code_member.get(i);
			if(du == e) {
				return i;
			}
		}
		return -1;
	}//위와 같은 논리
	
	public String toString() {
		return name;
	}
	
	public String d_use_display() {
		String info = "<use>";
		for(int i = 0; i < d_use_code_member.size(); i++) {
			info += "\n" + d_use_code_member.get(i).use;
		}
		return info;
	}
	
	public String d_code_display() {
		String info = "\n";
		for(int i = 0; i < d_use_code_member.size(); i++) {
			info += "\n" + d_use_code_member.get(i).code;
		}
		return info;
	}
}
