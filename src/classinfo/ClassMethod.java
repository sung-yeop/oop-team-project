package classinfo;
import java.util.*;

public class ClassMethod {
	private PropertyData property;
	private String content;
	private ArrayList<ClassVariable> variables;
	
	public ClassMethod() {
		variables = new ArrayList<ClassVariable>();
	}
	
	public ClassMethod(PropertyData property, String content, ArrayList<ClassVariable> variables) {
		this.property = property;
		this.content = content;
		this.variables = variables;
	}

	public PropertyData getProperty() {
		return property;
	}
	
	public String getContent() {
		return content;
	}
	
	public ArrayList<ClassVariable> getVariables() {
		return variables;
	}
}
