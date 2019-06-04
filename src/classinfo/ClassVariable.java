package classinfo;
import java.util.*;

public class ClassVariable extends PropertyData {
	private ArrayList<ClassMethod> methods;

	public ClassVariable() {
		methods = new ArrayList<ClassMethod>();
	}
	
	public ClassVariable(PropertyData property, ArrayList<ClassMethod> methods) {
		this.methods = methods;
	}
	
	public ArrayList<ClassMethod> getMethods() {
		return methods;
	}
}
