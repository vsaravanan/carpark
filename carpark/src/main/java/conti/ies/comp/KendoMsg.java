package conti.ies.comp;

import java.io.Serializable;

public class KendoMsg implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Msg;

	public KendoMsg(String msg) {
		super();
		Msg = msg;
	}

	public KendoMsg(String msg, String err) {
		super();
		Msg = msg;
		Err = err;
	}

	private String Err;

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getErr() {
		return Err;
	}

	public void setErr(String err) {
		Err = err;
	}


}
