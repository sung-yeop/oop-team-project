import java.util.*;

class d_class {
	protected String name;
	protected ArrayList<d_name_type_access> d_name_type_access_member;
	
	//~
	protected d_method method1, method2, method3, method4;
	protected d_data data1, data2, data3, data4;
	
	//~
	public static Object[][] class_table_data;
	
	public d_class(String name) {
		this.name = name;
		d_name_type_access_member = new ArrayList<d_name_type_access>();
		
		//데이터 타입에 맞춰서 node 매치
		method1 = new d_method("Stack()");
		method2 = new d_method("~Stack()");
		method3 = new d_method("push()");
		method4 = new d_method("stackPtr");
		
		
		/*
		data1 = new d_data("size");
		data2 = new d_data("stackPtr");
		data3 = new d_data("ddd");
		data4 = new d_data("ccc");
		*/
	}
	
	//node 정렬
	public d_method get_method(String name) {
		if(name.equals("Stack()")) {
			return method1;
		}
		else if(name.equals("~Stack()")) {
			return method2;
		}
		else if(name.equals("push()")) {
			return method3;
		}
		else
			return method4;
	}
	/*
	public d_data get_data(String name) {
		if(name.equals("size")) {
			return data1;
		}
		else if(name.equals("stackPtr")) {
			return data2;
		}
		else if(name.equals("ccc")) {
			return data3;
		}
		else 
			return data4;
	}
	*/
	
	public d_method get_method(int index) {
		if(index == 0)
			return method1;
		else if(index == 1)
			return method2;
		else if (index == 2)
			return method3;
		else if (index == 3)
			return method4;
		else 
			return null;
	}
	
	/*
	public d_data get_data(int index) {
		if(index == 4)
			return data1;
		else if (index == 5)
			return data2;
		else if (index == 6)
			return data3;
		else if (index == 7)
			return data4;
		else 
			return null;
	}
	*/
	
	public int get_methodIndexOf(d_method d) {
		if(d == method1)
			return 0;
		else if (d == method2)
			return 1;
		else if (d == method3)
			return 2;
		else if (d == method4)
			return 3;
		else 
			return 10;
	}
	
	/*
	public int get_dataIndexOf(d_data d) {
		if(d == data1)
			return 4;
		else if (d == data2)
			return 5;
		else if (d == data3)
			return 6;
		else if (d == data4)
			return 7;
		else
			return 11;
	}
	*/
	
	//d_name_type_access에 데이터 추가
	public void add(d_name_type_access e) {
		d_name_type_access_member.add(e);
	}
	
	public d_name_type_access get(int index) {
		return d_name_type_access_member.get(index);
	}
	
	public int size() {
		return d_name_type_access_member.size();
	}
	
	public int getIndexOf(d_name_type_access e) {
		for(int i = 0; i < d_name_type_access_member.size(); i++) {
			d_name_type_access dnta = d_name_type_access_member.get(i);
			if(dnta == e) {
				return i;
			}
		}
		return -1;
	}
	//
	
	public String toString() {
		return name;
	}
	
	
	
}

