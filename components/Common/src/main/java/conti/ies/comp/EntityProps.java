package conti.ies.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class EntityProps {

	private Map<String, String> fieldMap;
	private Map<String, String> classProps;
	private Map<String, String> dataTypes;
	private List<String> listColumns;
	private Multimap<String, FieldFilter> fieldFilters= ArrayListMultimap.create();



	public EntityProps(Class kls) {
		dataTypes = getTypes(kls);
	}


	private static Map<String, String> getTypes(Class kls) {
		Field[] fields = kls.getDeclaredFields();
		final Map<String, String> dataTypes = new HashMap<>();
		for (Field field : fields) {
			dataTypes.put(field.getName(), field.getType().getSimpleName().toLowerCase());
		}
		return dataTypes;
	}


}


