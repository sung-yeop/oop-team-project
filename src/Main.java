
public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer buffer = ReadFile.Read("examples/Stack.h");
		if(buffer != null) {
			System.out.println(buffer);
		}
	}

}
