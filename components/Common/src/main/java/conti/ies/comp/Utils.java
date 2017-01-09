package conti.ies.comp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static List<String> removeBlanks(List<String> lst)
	{
		ArrayList<String> arr = new ArrayList<String>();
		for (String s : lst)
		    if (StringUtils.isNotEmpty(s))
		    	arr.add(s);
		return arr;
	}

	public static boolean IsNumber(String numberString)
	{
		try
		{
			 Double.parseDouble(numberString);
		     return true;
		}
		catch(Exception e)
		{
		    return false;
		}
	}


	public static String format(String message, String... arguments) {
	    for (String argument : arguments) {
	        String[] keyValue = argument.split("=");
	        if (keyValue.length != 2)
	            throw new IllegalArgumentException("Incorrect argument: " + argument);
	        String placeholder = "${" + keyValue[0] + "}";
	        if (!message.contains(placeholder))
	            throw new IllegalArgumentException(keyValue[0] + " does not exists.");
	        while (message.contains(placeholder))
	            message = message.replace(placeholder, keyValue[1]);
	    }

	    return message;
	}
}
