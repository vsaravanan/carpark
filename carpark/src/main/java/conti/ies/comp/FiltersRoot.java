package conti.ies.comp;

import java.util.List;

public class FiltersRoot {

	private int skip;

	private int pageSize;

	private List<gSort> sort;

	private gFilters filter;

	public gFilters getFilter() {
		return filter;
	}

	public void setFilter(gFilters filter) {
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
}
