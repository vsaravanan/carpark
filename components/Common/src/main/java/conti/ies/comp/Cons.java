package conti.ies.comp;

import java.text.SimpleDateFormat;

public class Cons {
	public static final String SUCCESS = "success";
	public static final String ERROR =  "error";
	public static final String STRddMMMyyyy = "dd-MMM-yyyy";
	public static final String STRddMMMyyyyHHmm = "dd-MMM-yyyy HH:mm";
	public static final SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy");
	//public static final SimpleDateFormat ddMMMyyyyHHmm = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	public static final SimpleDateFormat MMMddyyyyHHmmss = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");

	public static final ThreadLocal<SimpleDateFormat> ddMMMyyyyHHmm = new ThreadLocal<SimpleDateFormat>() {
	    @Override
	    protected SimpleDateFormat initialValue() {
	        return new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	    }
	};	
	
	public static final SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");

	public enum eSlot {

		NEW("New"),
		OPEN("Open"),
		CLOSED("Closed");

        private String status;
        private eSlot(String status) {
            this.status = status;
        }

        @Override
        public String toString(){
        	return status;
        }

    }


	public enum ePark {

		OPEN("Open"),
		RESERVED("Reserved"),
		PARKED("Parked"),
		INACTIVE("Inactive");

        private String status;
        private ePark(String status) {
            this.status = status;
        }

        @Override
        public String toString(){
        	return status;
        }

    }

	public enum eExec {

		Exec("Exec"),
		Query("Query"),
		Columns("Columns");

        private String status;

        private eExec(String status) {
            this.status = status;
        }

        @Override
        public String toString(){
        	return status;
        }


    }
}
