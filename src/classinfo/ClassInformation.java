package classinfo;
import java.util.*;

public class ClassInformation {
	private String name;
	private ArrayList<ClassVariable> variables;
	private ArrayList<ClassMethod> methods;
	
	public ClassInformation() {
		setVariables(new ArrayList<ClassVariable>());
		setMethods(new ArrayList<ClassMethod>());
	}
	
	public ClassInformation(String name) {
		super();
		this.name = name;
	}

	public ArrayList<ClassVariable> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<ClassVariable> variables) {
		this.variables = variables;
	}

	public ArrayList<ClassMethod> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<ClassMethod> methods) {
		this.methods = methods;
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<PropertyData> getAllProperty() {
		ArrayList<PropertyData> allProps = new ArrayList<PropertyData>();
		for(ClassVariable variable: variables) {
			allProps.add(variable.getProperty());
		}
		for(ClassMethod method: methods) {
			allProps.add(method.getProperty());
		}
		return allProps;
	}
}
