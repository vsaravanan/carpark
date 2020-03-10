package conti.ies.comp;

import lombok.Data;

@Data
public class PagingContainer {
	private FilterPtrRoot fpr;
	private EntityProps ep;
	private String message;

}
