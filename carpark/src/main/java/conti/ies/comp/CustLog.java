package conti.ies.comp;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustLog {

	private static final Logger logger = LoggerFactory.getLogger(CustLog.class);
	private static GsonBuilder builder = new GsonBuilder();

	private static Gson gson = new Gson();

	public CustLog() {

	    gson = builder.disableHtmlEscaping().create();


	}

	public static void printJson(String msg, Object obj)
	{
		logger.info(msg + " : " + gson.toJson(obj));
	}

	public static boolean isJSONValid(String JSON_STRING) {
	    try {
	        new JSONObject(JSON_STRING);
	    } catch (Exception ex) {
	        try {
	            new JSONArray(JSON_STRING);
	        } catch (Exception ex1) {
	            return false;
	        }
	    }
	    return true;
	}

	public static Date validateDate(String timeInput, SimpleDateFormat dateFormat)
	{
	    Date newDate = null;

    	try {

    		newDate = dateFormat.parse(timeInput);

    	} catch (Exception e) {  //ParseException

    	}


		return newDate;
	}



}
