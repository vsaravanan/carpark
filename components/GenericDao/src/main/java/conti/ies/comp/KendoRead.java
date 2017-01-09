package conti.ies.comp;

import java.util.List;

public final class KendoRead {
	private List<?> data;
	private Integer total;

	public KendoRead(List<?> data, Integer total) {
		super();
		this.data = data;
		this.total =  total;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

}
