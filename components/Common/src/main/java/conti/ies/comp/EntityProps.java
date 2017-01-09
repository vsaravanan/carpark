package conti.ies.comp;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntityProps {

	private Map<String, String> fieldMap;
	private Map<String, String> classProps;
	private Map<String, String> dataTypes;
	private List<String> listColumns;
	private Map<String, FieldFilter> fieldFilters;


	public Map<String, String> getFieldMap() {
		return fieldMap;
	}
	public EntityProps(Class kls) {
		dataTypes = getTypes(kls);
	}
	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
		for (Map.Entry<String, String> e : fieldMap.entrySet()) {
			dataTypes.put(e.getKey(), e.getValue());
		}
	}
	public Map<String, String> getClassProps() {
		return classProps;
	}
	public void setClassProps(Map<String, String> classProps) {
		this.classProps = classProps;
	}



	private static Map<String, String> getTypes(Class kls) {
		Field[] fields = kls.getDeclaredFields();
		final Map<String, String> dataTypes = new HashMap<>();
		for (Field field : fields) {
			dataTypes.put(field.getName(), field.getType().getSimpleName().toLowerCase());
		}
		return dataTypes;
	}
	public Map<String, String> getDataTypes() {
		return dataTypes;
	}
	public void setDataTypes(Map<String, String> dataTypes) {
		this.dataTypes = dataTypes;
	}
	public List<String> getListColumns() {
		return listColumns;
	}
	public void setListColumns(List<String> listColumns) {
		this.listColumns = listColumns;
	}

	public Map<String, FieldFilter> getFieldFilters() {
		return fieldFilters;
	}
	public void setFieldFilters(Map<String, FieldFilter> fieldFilters) {
		this.fieldFilters = fieldFilters;
	}



}


