package conti.ies.comp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import conti.ies.carpark.statics.StaticFuncs;


@Data
public class UtilPaging {

    private static final Logger logger = LoggerFactory.getLogger(UtilPaging.class);

    private static final Map< String, String > likes = new HashMap<>();
    private static final Map< String, String > dateRange = new HashMap<>();
    private static Map< String, EntityProps > eps = new HashMap<>();

    private static Map< String, Collection< FieldFilter > > fieldFilters = new HashMap<>();
    private static List< String > listColumns;
    private static Map< String, String > dataTypes;
    private static Map< String, String > fieldMaps;
    private static final String Q = "'";

    static {
        likes.put("contains", " like '%<value>%'");
        likes.put("doesnotcontain", " not like '%<value>%'");
        likes.put("startswith", " like '<value>%'");
        likes.put("endswith", " like '%<value>'");

        dateRange.put("startswith", " >= ");
        dateRange.put("endswith", " <= ");
    }


    public static EntityProps populateEntityProps(Class kls) {
        String className = kls.getSimpleName().toLowerCase();

        synchronized (UtilPaging.class) {
            if (!UtilPaging.eps.containsKey(className)) {
                EntityProps ep = UtilPaging.getProps(kls);
                String drivingTable = ep.getClassProps().get("drivingTable");
                List< String > listColumns = StaticFuncs.getColumns(drivingTable);
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
                    ep = (EntityProps) m.invoke(tmpKls);
                    break;
                }
            }

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e1) {
            e1.printStackTrace();
        }

        return ep;
    }

    public static PagingContainer getPaging(Object kendoFilter, Class kls) {

        String className = kls.getSimpleName().toLowerCase();

        if (!eps.containsKey(className))
            UtilPaging.populateEntityProps(kls);

        EntityProps ep = eps.get(className);

        Gson gson = Common.myGson();

        String mFilter = gson.toJson(kendoFilter);

        gListArrayPtr mainFilter = null;
        try {
            mainFilter = gson.fromJson(mFilter, gListArrayPtr.class);
        } catch (Exception e) {

        }

        FilterPtrRoot gfrPtr = gson.fromJson(mFilter, FilterPtrRoot.class);
        String strGfrPtr = gson.toJson(gfrPtr);

        dataTypes = ep.getDataTypes();
        listColumns = ep.getListColumns();

        Map< String, String > fieldWhere = new HashMap<>();
        Multimap< String, FieldFilter > multiFieldFilters = ep.getFieldFilters();
        fieldFilters = multiFieldFilters.asMap();

        // populate field and datatypes from entity class
        fieldMaps = dataTypes.entrySet().stream()
                .filter(fieldName -> listColumns.contains(fieldName.getKey().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // populate field and datatypes from FieldFilters class
        fieldFilters.forEach((fieldName, cff) -> {
            fieldMaps.put(fieldName, fieldFilters.get(fieldName).stream().findFirst().orElse(null).getType());
        });

        String strSort = "";
        String strOrderByRow = "";

        // Sorting
        if (gfrPtr.getSort() != null) {
            for (gSort sort : gfrPtr.getSort()) {

                strSort += !strSort.equals("") ? ", " : " order by ";

                String sortField = sort.getField();

                strSort += " o." + sortField + " " + sort.getDir();

                if (listColumns.contains(sortField.toLowerCase())) {
                    strOrderByRow += !strOrderByRow.equals("") ? ", " : " order by ";
                    strOrderByRow += " s." + sortField + " " + sort.getDir();

                }

            }
        }

        gfrPtr.setOrderBy(strSort);
        gfrPtr.setOrderByRow(strOrderByRow);

        // list of Field Filters
        List< gFilter > listmgFilter = new ArrayList();

        for (gFieldFilter ff : gfrPtr.getFieldFilters()) {
            gFilter tmpgFilter = new gFilter(ff.getField(), "", ff.getWhere());
            fieldWhere.put(ff.getField(), ff.getWhere());
            listmgFilter.add(tmpgFilter);
        }

//		Merge Field Filters into Main Filters
        if (listmgFilter.size() > 0) {
            if (mainFilter.getFilter() != null) {

                List cMainFilter = mainFilter.getFilter().getFilters();
                cMainFilter.addAll(listmgFilter);

            } else {
                gListArray mgListArray = new gListArray(listmgFilter);
                mgListArray.setLogic("and");
                mainFilter.setFilter(mgListArray);

            }

        }

        // send to process filters
        gWhere gwhere = new gWhere();
        if (mainFilter.getFilter() != null && mainFilter.getFilter().getFilters().size() > 0) {
            String strCarr = gson.toJson(mainFilter.getFilter());
            String logic = mainFilter.getFilter().getLogic();
            gwhere = processFilters(0, logic, strCarr);
            logger.info(gwhere.whereclause);
            logger.info("pause");

        }


        gfrPtr.setWhere(gwhere.getWhereclause());
        gfrPtr.setJoinTables(gwhere.getJoinTables());
        gfrPtr.setJoinWheres(gwhere.getJoinWheres());

        PagingContainer pc = new PagingContainer();
        pc.setFpr(gfrPtr);
        pc.setEp(ep);
        pc.setMessage(gwhere.getWhereclause());

//		return this values to GenericDao.qryPaging
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
                return operator;
        }
    }

    public static String whereClause(gFilter gf, String alias) {

        String myOprVal = null;
        String myField = gf.getField();
        String myOpr = getOperator(gf.getOperator());
        String myVal = gf.getValue();

        logger.info(myField);
        switch (fieldMaps.get(myField)) {
            case "string":
                myOprVal = likes.get(myOpr);
                if (myOprVal != null) {
                    myOprVal = myOprVal.replaceAll("<value>", myVal);
                }
                break;
            case "integer":
                break;
            case "date":
                break;
            case "int":
                break;
            case "bigdecimal":
                myOpr = dateRange.get(myOpr);
                if (myOpr == null) {
                    myOpr = getOperator(gf.getOperator());
                }
                break;

        }

        switch (fieldMaps.get(myField)) {
            case "string":
                if (myOprVal == null) {
                    myOprVal = myOpr + Q + myVal + Q;
                }
                break;
            case "integer":
            case "int":
            case "bigdecimal":
                myOprVal = myOpr + myVal;
                break;
            case "bool":
                if (myVal.equals("true")) {
                    myOprVal = myOpr + Q + "t" + Q;
                } else if (myVal.equals("false")) {
                    myOprVal = myOpr + Q + "f" + Q;
                }
                break;
            case "date":
                if (myVal.contains("GMT")) {
                    Date tmpDt = null;

                    try {
                        tmpDt = Cons.MMMddyyyyHHmmss.parse(myVal.substring(4, 25));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    myVal = tmpDt.toString();
                }
                myOprVal = myOpr + Q + myVal + Q;
                break;

        }

        String fieldOprVal;


        if ("".equals(myOpr)) {
            fieldOprVal = Common.ltrim(myVal);
        } else {
            if (myOprVal == null) {
                myOprVal = myOpr + myVal;
            }
            fieldOprVal = alias + "." + myField + myOprVal;

        }


        return fieldOprVal;
    }


    public static gWhere processFilters(int level, String logic, String str) {
        Gson gson = Common.myGson();
        String tab = StringUtils.repeat("\t", level);

        Set< String > joinTables = new HashSet<>();
        Set< String > joinWheres = new HashSet<>();
        Set< String > fields = new HashSet<>();

        gWhere gwhere = new gWhere();

        gFilter rgFilter = null;

        // parsing for gFilter
        try {
            rgFilter = gson.fromJson(str, gFilter.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }


        if (rgFilter.getField() != null) {
            fields.add(rgFilter.getField());


            String fieldName = rgFilter.getField();
            Collection< FieldFilter > ffs = fieldFilters.get(fieldName);
//			String myDataType = dataTypes.get(fieldName);
//			String myOpr = getOperator(mygFilter.getOperator());
//			String myVal = mygFilter.getValue();
//			logger.info("(1) " + fieldName + " " + myOpr + " " + myVal);

            String alias = "s";

            for (FieldFilter ff : Common.checkIsEmpty(ffs)) {

                if (!ff.getAlias().equals("")) {
                    joinTables.add(" " + ff.getJoinTable() + " " + ff.getAlias());
                    joinWheres.add(" " + ff.getJoinWhere() + " ");
                    alias = ff.getAlias();
                }

            }

            String fieldOprVal = whereClause(rgFilter, alias);
            fieldOprVal = fieldOprVal.replaceAll("<tab>", tab);

//			logger.info(" 1 " + sb.toString());
            gwhere.setWhereclause(fieldOprVal);
            gwhere.setJoinTables(joinTables);
            gwhere.setJoinWheres(joinWheres);
            gwhere.setFields(fields);
            return gwhere;


        }

        // parsing for gListArray
        gListArray rgListArray = null;

        try {
            rgListArray = gson.fromJson(str, gListArray.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        try {

            if (rgListArray.getFilters() != null) {

                String retVal = "";
                StringBuilder sb = new StringBuilder();

                for (Object obj : rgListArray.getFilters()) {

                    if (sb.length() > 0) {
                        sb.append("\n <tab> \t " + rgListArray.getLogic() + " ");
                    } else {
                        sb.append("\n <tab> \t \t ");
                    }

                    gWhere cgwhere = processFilters(level + 1, rgListArray.getLogic(), gson.toJson(obj));
                    retVal = cgwhere.whereclause;
                    retVal = retVal.replaceAll("<tab>", tab);
                    sb.append(retVal);
                    Common.replaceAll(sb, "<tab>", tab);

                    gwhere.joinTables.addAll(cgwhere.joinTables);
                    gwhere.joinWheres.addAll(cgwhere.joinWheres);
                    gwhere.fields.addAll(cgwhere.fields);

//						logger.info(" 2 " + sb.toString());

                }

                String newVal = "\n <tab> ( " +
                        sb.toString() +
                        "\n <tab> ) ";
                newVal = newVal.replaceAll("<tab>", tab);

//				logger.info(" 3 " + newVal);

                gwhere.setWhereclause(newVal);

                return gwhere;


            }

        } catch (JsonSyntaxException e) {
        }

        return null;


    }

}

@Data
class gSort {
    private String field;
    private String dir;

}

@Data
class gFilter {

    private String field;
    private String operator;
    private String value;

    public gFilter() {
    }

    public gFilter(String field, String operator, String value) {
        super();
        this.field = field;
        this.operator = operator;
        this.value = value;
    }


}

@Data
class gFieldFilter {

    private String field;
    private String where;

}

@Data
abstract class gLogic {

    private String logic;

}


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
class gFilters extends gLogic {

    private List< gFilter > filters;

}


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
class gFilterPtr extends gLogic {

    private List< gFilters > filters;

}

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
class gListArray extends gLogic {

    private List filters;

}


@Data
@EqualsAndHashCode(callSuper = true)
class gListArrayPtr extends gLogic {

    private gListArray filter;

}

@Data
class gWhere {
    String whereclause;
    Set< String > joinTables = new HashSet<>();
    Set< String > joinWheres = new HashSet<>();
    Set< String > fields = new HashSet<>();
}



