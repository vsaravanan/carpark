package conti.ies.comp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import conti.ies.carpark.statics.StaticFuncs;



public class UtilPaging {

	private static final Logger logger = LoggerFactory.getLogger(UtilPaging.class);

//	private static Map<String, Object> dataTypes = new HashMap<>();
//	private static Map<String, Object> classProps = new HashMap<>();
	private final static Map<String, String> likes = new HashMap<>();
	private final static Map<String, String> dateRange = new HashMap<>();

	private static Map<String, EntityProps> eps = new HashMap<>();



	static {
		likes.put("contains", " like '%<value>%'");
		likes.put("doesnotcontain", " not like '%<value>%'");
		likes.put("startswith", " like '<value>%'");
		likes.put("endswith", " like '%<value>'");

		dateRange.put("startswith", " >= ");
		dateRange.put("endswith", " <= ");
	}



	public static EntityProps populateEntityProps(Class kls)
	{


		String className = kls.getSimpleName().toLowerCase();

		synchronized(UtilPaging.class){
			if (! UtilPaging.eps.containsKey(className))
			{
				EntityProps ep =  UtilPaging.getProps(kls);
				String drivingTable = ep.getClassProps().get("drivingTable");
				List<String> listColumns = StaticFuncs.getColumns(drivingTable);
				ep.setListColumns(listColumns);
				UtilPaging.eps.put(className, ep);
			}

		}
		return UtilPaging.eps.get(className);
	}

	private static EntityProps getProps(Class kls) {
		//Field[] fields = kls.getDeclaredFields();
		//final Map<String, String> classProps = new HashMap<>();
		Object tmpKls = null;
		EntityProps ep = null;
		try {
			tmpKls = kls.newInstance();
			//String mySql = (String) kls.getMethod("retSql", null).invoke(tmpKls, null);
			for (Method m : kls.getMethods()) {
				if (m.getName().startsWith("getEntityProps") && m.getParameterTypes().length == 0) {
					ep =  (EntityProps) m.invoke(tmpKls);
					break;
				}
			}

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e1) {
			e1.printStackTrace();
		}

		return ep;
	}

	private enum Paths {Manual, Kendo}


	public static PagingContainer getPaging(Object kendoFilter, Class kls) {


		String className = kls.getSimpleName().toLowerCase();

		if (! eps.containsKey(className))
			UtilPaging.populateEntityProps(kls);

		EntityProps ep = eps.get(className);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		String mFilter = gson.toJson(kendoFilter);

		//mFilter = "{\"take\":5,\"skip\":0,\"page\":1,\"pageSize\":5,\"sort\":[{\"field\":\"slotUsedId\",\"dir\":\"asc\"}],\"btnSearch\":false,\"fieldFilters\":[{\"field\":\"userId\",\"where\":\" s.userId in (11 ) \"}]}";
		//mFilter = "{\"take\":5,\"skip\":0,\"page\":1,\"pageSize\":5,\"filter\":{\"logic\":\"and\",\"filters\":[{\"field\":\"slotId\",\"operator\":\"neq\",\"value\":2}]},\"btnSearch\":false,\"fieldFilters\":[{\"field\":\"slotId\",\"where\":\" s.slotId in (2 ) \"},{\"field\":\"userId\",\"where\":\" s.userId in (11 ) \"}]}";
		//logger.info(mFilter);
		FilterPtrRoot gfrPtr = gson.fromJson(mFilter, FilterPtrRoot.class);
		String strGfrPtr = gson.toJson(gfrPtr);

		FiltersRoot gfrFilters = gson.fromJson(mFilter, FiltersRoot.class);
		String strGfrs = gson.toJson(gfrFilters);



		@SuppressWarnings("unchecked")
		Map<String, String> dataTypes = ep.getDataTypes();
		List<String> listColumns = ep.getListColumns();
		boolean btnSearch = gfrPtr.isBtnSearch();
		Map<String, String> fieldWhere =  new HashMap<>();
		Map<String, FieldFilter> fieldFilters = ep.getFieldFilters();
		Set<String> joinTables = new HashSet<>();
		Set<String> joinWheres = new HashSet<>();

		for (gFieldFilter ff : gfrPtr.getFieldFilters()) {
			fieldWhere.put(ff.getField(), ff.getWhere());
		}


		String strSort = "";
		String strOrderByRow = "";

		if (gfrPtr.getSort() !=  null)
		{
			for (gSort sort : gfrPtr.getSort()) {

				strSort += ! strSort.equals("") ? ", " : " order by ";

				String sortField =sort.getField();

//				String sortFieldtype = dataTypes.get(sortField);
//				if (dataTypes.containsKey(sortFieldtype) )
//					sortField = sortFieldtype;

				strSort += " o." + sortField + " " + sort.getDir();

				if (listColumns.contains(sortField.toLowerCase()))
				{
					strOrderByRow += ! strOrderByRow.equals("")? ", " : " order by ";
					strOrderByRow += " s." + sortField + " " + sort.getDir();

				}


			}
		}
		gfrPtr.setOrderBy(strSort);

		gfrPtr.setOrderByRow(strOrderByRow);



		List<gFilters> parentFltrs = new ArrayList<>();
		String parentLogic = "and";

		String whereParent = "";
		if (gfrPtr.getFilter() != null ) {

			// remove empty elements from child filters
			Iterator<gFilter> iterFltrs = gfrFilters.getFilter().getFilters().iterator();
			while (iterFltrs.hasNext()) {
				if (iterFltrs.next().getField() == null) {
					iterFltrs.remove();
				}
			}

			// add child filters to parent filter
			if (gfrFilters.getFilter().getFilters().size() > 0)
			{
				List<gFilters> gfrs = gfrPtr.getFilter().getFilters();
				gfrs.add(gfrFilters.getFilter());
			}

			gFilterPtr fltrRt = gfrPtr.getFilter();


			// set parentFilters and parentLogic for outer loop
			parentFltrs = fltrRt.getFilters();
			parentLogic = fltrRt.getLogic();

			// remove empty elements from parentFilters
			Iterator<gFilters> iterFltrRt = parentFltrs.iterator();
			while (iterFltrRt.hasNext()) {
				if (iterFltrRt.next().getLogic() == null) {
					iterFltrRt.remove();
				}
			}

		}

		List<String> listFields =  new ArrayList<>();

		for (gFilters mnFltrs : parentFltrs) {
			for (gFilter chFltrs : mnFltrs.getFilters()) {
				listFields.add(chFltrs.getField());
			}
		}



		for (Map.Entry<String, String> entry : fieldWhere.entrySet()) {
			String tmpField = "{\"logic\":\"and\",\"filters\":[{\"field\":\"<fieldName>\",\"operator\":\"\",\"value\":\"\"}]}";

			if (!listFields.contains(entry.getKey()))
			{

				String tmpField2 = tmpField.replace("<fieldName>", entry.getKey());

				gFilters gltrs = gson.fromJson( tmpField2, gFilters.class);

				parentFltrs.add(gltrs);
			}


		}


		if (parentFltrs != null )
		{

			// parentFilters loop
			for (gFilters mnFltrs : parentFltrs) {
				String whereChild = "";
				String childLogic = mnFltrs.getLogic();

					// childFilters loop
					for (gFilter chFltrs : mnFltrs.getFilters()) {

							String fieldName = chFltrs.getField();
							String fieldMap = null;
							FieldFilter ff = null;
							String strCondition = "";
							Paths myPath = Paths.Kendo;

							if (btnSearch)
							{
								if (fieldMap == null)
								{
									ff = fieldFilters.get(fieldName);
									if (ff != null)
									{
										fieldMap = ff.getType();
										if (fieldWhere.size() > 0)
										{
											if (fieldWhere.get(fieldName) != null)
											{
												strCondition = " ( " + fieldWhere.get(fieldName) + " ) ";
												myPath = Paths.Manual;
											}
										}
									}
								}

							}


							if (strCondition.equals(""))
							{
								if (listColumns.contains(fieldName.toLowerCase()))
								{
									fieldMap = dataTypes.get(fieldName);
									myPath = Paths.Kendo;
								}
							}

							if (fieldMap == null)
							{
								ff = fieldFilters.get(fieldName);
								if (ff != null)
								{
									fieldMap = ff.getType();
									if (fieldWhere.size() > 0)
									{
										if (fieldWhere.get(fieldName) != null)
										{
											strCondition = " ( " + fieldWhere.get(fieldName) + " ) ";
											myPath = Paths.Manual;
										}
									}
								}
							}


							String myOpr = getOperator(chFltrs.getOperator());
							String myVal = chFltrs.getValue();
							String Q = "'";
							logger.info("myOpr : " + myOpr + ", myVal : " + myVal + ", parentLogic : " + parentLogic + ", myPath : " + myPath );

							if (myPath == Paths.Kendo)  // strCondition.equals("")
							{

								if (fieldMap == null)
								{
									if (dataTypes.get(fieldMap) !=  null)
									{ // this block can be removed after verifying getEntryTimeHHmm in Calendar
										fieldName = fieldMap;
										// get fieldMap using field alias
										fieldMap = dataTypes.get(fieldName);
									}
								}

								logger.info("myOpr : " + myOpr + ", myVal : " + myVal + ", fieldMap : " + fieldMap  );

								if (myOpr == null)
								{ // this block for starts with, contains, end with, not equal to, etc,
									if (fieldMap != null)
									{
										switch (fieldMap) {
										case "string":
											strCondition = likes.get(chFltrs.getOperator()).replace("<value>", myVal);
											break;
										case "integer":
										case "date":
										case "int":
										case "bigdecimal":
											myOpr = dateRange.get(chFltrs.getOperator());
											break;
										case "bool":
											if (myVal.equals("true"))
												strCondition = myOpr + "'t'";
											else if (myVal.equals("false"))
												strCondition = myOpr + "'f'";
											break;
										}
									}
								}


								logger.info("strCondition : " + strCondition  + ", myOpr : " + myOpr  + ", myVal : " + myVal);


								if (strCondition.equals("") && fieldMap != null && myOpr != null)
								{
									// it is also expecting myOpr != null and myVal != null

									switch (fieldMap) {
									case "string":
										//if (strCondition ! equals "")
										strCondition = myOpr + Q + myVal + Q;
										break;
									case "integer":
									case "int":
									case "bigdecimal":
										strCondition = myOpr + myVal;
										break;
									case "date":

									if (myVal.contains("GMT"))
										{
											//System.out.println(" timestamp " + myVal.substring(0, 25));
											Date tmpDt = null;

											try {
												tmpDt = Cons.MMMddyyyyHHmmss.parse(myVal.substring(4, 25));
											} catch (ParseException e) {
												e.printStackTrace();
											}

											myVal = tmpDt.toString();
										}
											strCondition = myOpr + Q + myVal + Q;
										break;

									} // switch

								}

								logger.info("strCondition : " + strCondition  + ", myVal : " + myVal  );

							} // Paths.Kendo


							if (! strCondition.equals("")) {

								String alias = "s";

								if (ff != null)
								{
									if (! ff.getAlias().equals(""))
									{ 	// it should be at the bottom of the block
										// bcoz it is depending to ! strCondition.equals("")
										// which cannot be determined early.

										joinTables.add(" " + ff.getJoinTable() + " " + ff.getAlias());
										joinWheres.add(" " + ff.getJoinWhere() + " ");
										alias = ff.getAlias();
									}

								}
								if (myPath == Paths.Kendo)
									strCondition = " ( " + alias + "." + fieldName + " " + strCondition + " ) ";



								if (whereChild.equals(""))
									whereChild = strCondition;
								else {
									if (childLogic.equals("or"))
										whereChild = " ( " + whereChild + " " + childLogic + "   " + strCondition
												+ "  ) ";
									else
										whereChild = "   " + whereChild + " " + childLogic + "   " + strCondition
												+ "    ";
								}

								logger.info("strCondition : " + strCondition  );

								logger.info("whereChild : " + whereChild);

							}



					} // childLogic != null


					if (parentLogic == null) {
						whereParent = whereChild;
						break;
					} else {
						if (! whereChild.equals("") )
						{
							if (whereParent.equals(""))
								whereParent = whereChild;
							else {
								if (parentLogic.equals("or"))
									whereParent = " ( " + whereParent + " " + parentLogic + "   " + whereChild + "   )";
								else
									whereParent = "   " + whereParent + " " + parentLogic + "   " + whereChild + "   ";
							}

						}
					}

					logger.info("parentLogic : " + parentLogic);

			} // mnFltrs loop

		}

		//if (whereParent ! quals "")
		//	whereParent = " where "  + whereParent;
		logger.info("whereParent  : " + whereParent);
		gfrPtr.setWhere(whereParent);
		gfrPtr.setJoinTables(joinTables);
		gfrPtr.setJoinWheres(joinWheres);


		PagingContainer pc = new PagingContainer();
		pc.setFpr(gfrPtr);
		pc.setEp(ep);

		return pc;

	}


	public static String getOperator(String operator) {
		switch (operator.toLowerCase()) {
		case "eq":
			return " = ";
		case "neq":
			return " != ";
		case "gte":
			return " >= ";
		case "gt":
			return " > ";
		case "lte":
			return " <= ";
		case "lt":
			return " < ";
		default:
			return null;
		}
	}


	public static Map<String, EntityProps> getEps() {
		return eps;
	}


	public static void setEps(Map<String, EntityProps> eps) {
		UtilPaging.eps = eps;
	}



}

class gSort {
	private String field;
	private String dir;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}

class gFilter {

	private String field;
	private String operator;
	private String value;

	public gFilter() {}

	public gFilter(String field, String operator, String value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}



class gFilters {

	public gFilters() {}

	public gFilters(String logic, List<gFilter> filters) {
		super();
		this.logic = logic;
		this.filters = filters;
	}

	private String logic;

	private List<gFilter> filters;

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public List<gFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<gFilter> filters) {
		this.filters = filters;
	}

}

class gFieldFilter {

	private String field;

	private String where;

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}

}

/*class gListFieldFilter {
	private List<gFieldFilter> listFF;
}
*/
class gFilterPtr {
	private String logic;

	private List<gFilters> filters;

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public List<gFilters> getFilters() {
		return filters;
	}

	public void setFilters(List<gFilters> filters) {
		this.filters = filters;
	}

}

/*
 *
 *
 * class gFilterPtrRoot {
 *
 * private int skip;
 *
 * private int pageSize;
 *
 * private List<gSort> sort;
 *
 * private gFilterPtr filter;
 *
 *
 * public gFilterPtr getFilter() { return filter; }
 *
 * public void setFilter(gFilterPtr filter) { this.filter = filter; }
 *
 * public int getSkip() { return skip; }
 *
 * public void setSkip(int skip) { this.skip = skip; }
 *
 * public int getPageSize() { return pageSize; }
 *
 * public void setPageSize(int pageSize) { this.pageSize = pageSize; }
 *
 * public List<gSort> getSort() { return sort; }
 *
 * public void setSort(List<gSort> sort) { this.sort = sort; }
 *
 *
 * }
 *
 * class gFiltersRoot {
 *
 * private int skip;
 *
 * private int pageSize;
 *
 * private List<gSort> sort;
 *
 * private gFilters filter;
 *
 * public gFilters getFilter() { return filter; }
 *
 * public void setFilter(gFilters filter) { this.filter = filter; }
 *
 * public int getSkip() { return skip; }
 *
 * public void setSkip(int skip) { this.skip = skip; }
 *
 * public int getPageSize() { return pageSize; }
 *
 * public void setPageSize(int pageSize) { this.pageSize = pageSize; }
 *
 * public List<gSort> getSort() { return sort; }
 *
 * public void setSort(List<gSort> sort) { this.sort = sort; }
 *
 * }
 *
 *
 */



// String mFilter =
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"}]}}
// ";
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"},{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"2\"}]}}
// " ;
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"},{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"2\"},{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-09-30T16:00:00.000Z\"}]}}
// " ;
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-09-30T16:00:00.000Z\"},{\"logic\":\"or\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"},{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"2\"}]}]}}
// ";
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"logic\":\"or\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"},{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"2\"}]},{\"logic\":\"or\",\"filters\":[{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-09-30T16:00:00.000Z\"},{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-10-14T16:00:00.000Z\"}]}]}}
// ";
// "
// {\"take\":15,\"skip\":0,\"page\":1,\"pageSize\":15,\"sort\":[{\"field\":\"entryTime\",\"dir\":\"desc\"}],\"filter\":{\"logic\":\"and\",\"filters\":[{\"logic\":\"or\",\"filters\":[{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"1\"},{\"field\":\"entryTimeHHmm\",\"operator\":\"eq\",\"value\":\"2\"}]},{\"logic\":\"or\",\"filters\":[{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-09-30T16:00:00.000Z\"},{\"field\":\"entryDate\",\"operator\":\"eq\",\"value\":\"2015-10-14T16:00:00.000Z\"}]},{\"logic\":\"and\",\"filters\":[{\"field\":\"id\",\"operator\":\"startswith\",\"value\":\"7580\"},{\"field\":\"id\",\"operator\":\"endswith\",\"value\":\"7583\"}]}]}}
// " ;