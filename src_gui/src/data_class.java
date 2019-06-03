import java.util.*;

class data_class {
	protected String name;
	protected ArrayList<dm> member;
	protected data_method n1, n2, n3, n4; //수정 1
	
	public data_class(String name) {
		this.name = name;
		member = new ArrayList<dm>();
		n1 = new data_method("Stack()");
		n2 = new data_method("~Stack()");
		n3 = new data_method("push()");
		n4 = new data_method("stackPtr");
	}
	
	public void add(dm e) {
		member.add(e);
	}
	
	public dm get(int index) {
		return member.get(index);
	}
	
	public int size() {
		return member.size();
	}
	
	public int getIndexOf(dm e) {
		for(int i = 0; i < member.size(); i++) {
			dm dmm = member.get(i);
			if(dmm == e) {
				return i;
			}
		}
		return -1;
	}
	
	public String toString() {
		return name;
	}
	
	//아래는 많은 수정이 필요
	public data_method getmethod(String name) {
		if(name.equals("Stack()"))
			return n1;
		else if(name.equals("~Stack()"))
			return n2;
		else if(name.equals("push()"))
			return n3;
		else 
			return n4;
	}
	
	public data_method getmethod(int index) {
		if(index == 0)
			return n1;
		else if (index == 1)
			return n2;
		else if (index == 2)
			return n3;
		else 
			return n4;
	}

	
	//Display부분 코드 필요 : Table형식으로 display가 나올 수 있도록 클래스를 하나 더 추가
}
