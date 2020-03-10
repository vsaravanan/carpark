package conti.ies.comp;

import lombok.Data;

import java.util.List;

@Data
public final class KendoRead {
	private List<?> data;
	private Integer total;
	private String message;

	public KendoRead(List< ? > data, Integer total, String message) {
		this.data = data;
		this.total = total;
		this.message = message;
	}

	public KendoRead(List<?> data, Integer total) {
		super();
		this.data = data;
		this.total =  total;
	}

}
