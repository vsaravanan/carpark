package conti.ies.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilterPtrRoot {

	private int skip;

	private int pageSize;

	private List<gSort> sort;

	private gFilterPtr filter;

	private String orderBy;

	private String orderByRow;

	private String where;

	private String extraTables = "";

	private String extraSearch = "";

	private boolean btnSearch = false;

	private Set<String> joinTables;

	private Set<String> joinWheres;

	private List<gFieldFilter> fieldFilters = new ArrayList<>();

	public gFilterPtr getFilter() {
		return filter;
	}

	public void setFilter(gFilterPtr filter) {
		this.filter = filter;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<gSort> getSort() {
		return sort;
	}

	public void setSort(List<gSort> sort) {
		this.sort = sort;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}


	public String getExtraSearch() {
		return extraSearch;
	}

	public void setExtraSearch(String extraSearch) {
		this.extraSearch =  extraSearch;
	}

	public String getExtraTables() {
		return extraTables;
	}

	public void setExtraTables(String extraTables) {
		this.extraTables = extraTables;
	}

	public String getOrderByRow() {
		return orderByRow;
	}

	public void setOrderByRow(String orderByRow) {
		this.orderByRow = orderByRow;
	}

	public Set<String> getJoinTables() {
		return joinTables;
	}

	public void setJoinTables(Set<String> joinTables) {
		this.joinTables = joinTables;
	}

	public Set<String> getJoinWheres() {
		return joinWheres;
	}

	public void setJoinWheres(Set<String> joinWheres) {
		this.joinWheres = joinWheres;
	}

	public boolean isBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(boolean btnSearch) {
		this.btnSearch = btnSearch;
	}

	public List<gFieldFilter> getFieldFilters() {
		return fieldFilters;
	}

	public void setFieldFilters(List<gFieldFilter> fieldFilters) {
		this.fieldFilters = fieldFilters;
	}


}
