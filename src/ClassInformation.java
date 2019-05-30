import java.util.*;

public class ClassInformation {
	private String name;
	private ArrayList<ClassVariable> variables;
	private ArrayList<ClassMethod> methods;
	
	public ClassInformation() {
		variables = new ArrayList<ClassVariable>();
		methods = new ArrayList<ClassMethod>();
	}
}
