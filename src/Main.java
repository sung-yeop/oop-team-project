import classinfo.ClassInformation;

public class Main {
	
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
			classInfo.getAllProperty(); // 클래스가 사용하는 모든 메소드와 변수의 이름, 타입, 접근제어자를 가져옴
		}
	}

}
