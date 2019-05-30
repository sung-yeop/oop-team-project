package classinfo;
import java.util.*;

public class ClassVariable {
	private PropertyData property;
	private ArrayList<ClassMethod> methods;

	public ClassVariable() {
		methods = new ArrayList<ClassMethod>();
	}
	
	public ClassVariable(PropertyData property, ArrayList<ClassMethod> methods) {
		this.property = property;
		this.methods = methods;
	}
	
	public PropertyData getProperty() {
		return property;
	}
	
	public ArrayList<ClassMethod> getMethods() {
		return methods;
	}
}
