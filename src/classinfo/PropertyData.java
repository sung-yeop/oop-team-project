package classinfo;

public class PropertyData {
	private String name;
	private String type;
	private String access;
	
	public PropertyData(String name, String type, String access) {
		this.name = name;
		this.type = type;
		this.access = access;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
}
