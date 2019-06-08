package classinfo;
import java.util.*;

public class ClassMethod extends PropertyData {
	private String content;
	private ArrayList<ClassVariable> variables;
	private ArrayList<ClassVariable> parameters;
	
	public ClassMethod() {
		variables = new ArrayList<ClassVariable>();
		parameters = new ArrayList<ClassVariable>();
	}
	
	public ClassMethod(PropertyData data) {
		this();
		this.setAccess(data.getAccess());
		this.setName(data.getName());
		this.setType(data.getType());
	}
	
	public ClassMethod(String content, ArrayList<ClassVariable> variables) {
		this.content = content;
		this.variables = variables;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public ArrayList<ClassVariable> getVariables() {
		return variables;
	}
	
	public ArrayList<ClassVariable> getParameters() {
		return parameters;
	}
	
	public void setParameters(ArrayList<ClassVariable> parameters) {
		this.parameters = parameters;
	}
}
