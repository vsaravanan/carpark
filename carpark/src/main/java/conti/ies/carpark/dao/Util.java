package conti.ies.carpark.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import conti.ies.carpark.model.Vehicle;
import conti.ies.carpark.model.VwVehicle;
import conti.ies.comp.Cons;
import conti.ies.comp.CustLog;
import conti.ies.comp.HStore;
import conti.ies.comp.ReturnStatus;



public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static  List<Vehicle> Cast(List<VwVehicle>  vws) {
	    List<Vehicle> lv = new  ArrayList<>();
	    for (VwVehicle vw : vws)
	    {
	    	Vehicle vehicle = new Vehicle(vw);
	    	lv.add(vehicle);
	    }
	    return lv;
	}

	public static ReturnStatus parseReturnStatus(String retStatus)
	{


		logger.info("retStatus :  " + retStatus);

		ReturnStatus newRetStatus = new ReturnStatus();

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();


		if (retStatus.contains("=>"))
		{
			HStore hs = new HStore(retStatus);
			retStatus = gson.toJson(hs.asMap());

		}


		if (CustLog.isJSONValid(retStatus))
			newRetStatus = gson.fromJson(retStatus, ReturnStatus.class);

		if (newRetStatus.getStatus() == null)
		{
			newRetStatus.setStatus(retStatus.equals(Cons.SUCCESS) ? Cons.SUCCESS  : Cons.ERROR);
			newRetStatus.setErrorMsg(retStatus.equals(Cons.SUCCESS) ? "" : retStatus);

		}

		return newRetStatus;
	}

}
