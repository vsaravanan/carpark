package conti.ies.comp;

public class FieldFilter {

	private String type;
	private String alias;
	private String joinTable;
	private String joinWhere;



	public FieldFilter(String type, String alias, String joinTable, String joinWhere) {
		super();
		this.type = type;
		this.alias = alias;
		this.joinTable = joinTable;
		this.joinWhere = joinWhere;
	}
	public String getJoinTable() {
		return joinTable;
	}
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}
	public String getJoinWhere() {
		return joinWhere;
	}
	public void setJoinWhere(String joinWhere) {
		this.joinWhere = joinWhere;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}


}
