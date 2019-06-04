package classinfo;
import java.util.*;

public class ClassMethod extends PropertyData {
	private String content;
	private ArrayList<ClassVariable> variables;
	
	public ClassMethod() {
		variables = new ArrayList<ClassVariable>();
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
}
