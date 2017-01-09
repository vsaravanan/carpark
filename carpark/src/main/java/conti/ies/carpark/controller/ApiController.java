package conti.ies.carpark.controller;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import conti.ies.carpark.dao.IParkingBillDao;
import conti.ies.carpark.dao.IParkingDao;
import conti.ies.carpark.dao.IParkingSlotDao;
import conti.ies.carpark.dao.Util;
import conti.ies.carpark.model.IPrepareAndExecute;
import conti.ies.carpark.model.Parking;
import conti.ies.carpark.model.ParkingBill;
import conti.ies.carpark.model.ParkingSlot;
import conti.ies.comp.ApiParams;
import conti.ies.comp.Cons;
import conti.ies.comp.CustLog;
import conti.ies.comp.HStore;
import conti.ies.comp.ReturnStatus;

class UserPwd   {
	String userName;
	String pwd;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}

@Controller
@RequestMapping("api")
@RestController
public class ApiController   {

	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

	private class ApiParking   {
		String entryOrExit;
		Integer parkingId;
		Integer slotId;
		Integer billId;
		String fromTime;
		String toTime;
		String userName;
		String parkingName;
	}




	@Autowired
	private IPrepareAndExecute pae;


	@Autowired
	private IParkingBillDao parkingBillDao;

	@Autowired
	private IParkingDao parkingDao;

	@Autowired
	private IParkingSlotDao parkingSlotDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


	@RequestMapping(value = "validateUser",  method = RequestMethod.POST)
	public @ResponseBody ReturnStatus validateUser(@RequestBody UserPwd userPwd) {

		  String password = userPwd.getPwd();
		  String encryptedPassword = passwordEncoder.encode(password);


	    CustLog.printJson("ApiController validateUser ", userPwd);
	    String Sql = "select u.pwd  from Users u where u.userName = '<userName>'";
	    Sql = Sql.replace("<userName>", userPwd.getUserName() );

		String retStatus = pae.executeQuery(Sql, "validateUser");

		boolean isSuccess = false;
		if (retStatus != null)
		{
			if (passwordEncoder.matches(userPwd.getPwd(), retStatus))
			{
				isSuccess = true;
			}

		}

		ReturnStatus newRetStatus = new ReturnStatus();

		newRetStatus.setStatus(! isSuccess ? Cons.ERROR : Cons.SUCCESS);
		newRetStatus.setErrorMsg(! isSuccess ? "Invalid User Id and Password for " + userPwd.getUserName()  : "");

		retStatus = null;

		return newRetStatus;

	}


	@RequestMapping(value = "reserve",  method = RequestMethod.POST)
	public @ResponseBody ReturnStatus reserve(@RequestBody ApiParams apiParams) {


	    CustLog.printJson("ApiController reserving ", apiParams);




		String Sql = "select apiReserveParking('<parkingName>', '<userName>', '<fromTime>', '<toTime>') as retStatus ";
		Sql = Sql.replace("<parkingName>", apiParams.getParkingName() );
		Sql = Sql.replace("<userName>", apiParams.getUserName() );
		Sql = Sql.replace("<fromTime>", apiParams.getTimeStart() );
		Sql = Sql.replace("<toTime>", apiParams.getTimeEnd() );

		String retStatus = pae.executeQuery(Sql, "Reserving... ");

		ReturnStatus newRetStatus = Util.parseReturnStatus(retStatus);

		return newRetStatus;
	}



	private Object validatePark(ApiParams apiParams, boolean Validate)
	{
		ParkingBill parkingBill;
		Parking parking;
		Integer parkingId;
		Integer billId = null;
		Integer slotId = null;
	    Date timeStart = null;
	    Date timeEnd = null;
	    Date fromTime = null;
	    Calendar cal = Calendar.getInstance();
	    boolean entry = false;
	    boolean exit = false;
	    Date timeStartInReserve = null;
	    Date exitTimeInReserve = null;
	    String errMsg = "";



	    if (Validate)
	    {
		    if (StringUtils.isBlank(apiParams.getParkingName()))
		    	return "Invalid Parking Name : " + apiParams.getParkingName();
		    if (StringUtils.isBlank(apiParams.getUserName()))
	    		return "Invalid User Name : " + apiParams.getUserName();
		    if (StringUtils.isBlank(apiParams.getTagId()))
	    		return "Invalid Tag Id : " + apiParams.getTagId();
		    if (StringUtils.isBlank(apiParams.getTimeStart()) && StringUtils.isBlank(apiParams.getTimeEnd()))
		    	return "Invalid both Starting Time and Ending Time cannot be Blank. Expecting at least one value.";
		    if (! StringUtils.isBlank(apiParams.getTimeStart()) && ! StringUtils.isBlank(apiParams.getTimeEnd()))
		    	return "Invalid input on Starting Time : " + apiParams.getTimeStart() + ".\n"
		    			+ "Ending time : " + apiParams.getTimeEnd() + ".\n"
		    			+ "Please do not set both values (Start Time and End Time)" + ".\n"
		    			+ "Expected value in only one of them";
	    }



	    if (! StringUtils.isBlank(apiParams.getTimeStart()))
	    {
	    	timeStart = CustLog.validateDate(apiParams.getTimeStart(), Cons.ddMMMyyyyHHmm.get());
		    if (Validate && timeStart == null)
		    	return "Invalid date format in Starting Time : " +  apiParams.getTimeStart() + ".\n"
		    			+ "Expecting format : " + Cons.ddMMMyyyyHHmm.get().toPattern();
		    else
		    {

	/*		    cal.setTime(timeStart);
			    cal.add(Calendar.MINUTE, -30);
			    if (timeStart.before(cal.getTime()))
			    	return "Invalid Starting Time : " + apiParams.getTimeStart() + ".\n"
	    					+ "Expecting the start time should be within the last 30 minutes : " + Cons.ddMMMyyyyHHmm.format(cal.getTime());
			    if (timeStart.after(new Date()))
			    	return "Invalid Starting Time : " + apiParams.getTimeStart() + ".\n"
					+ "Starting Time cannot be more than current time.";*/
		    	entry = true;
		    }

	    }
	    if (! StringUtils.isBlank(apiParams.getTimeEnd()))
	    {
	    	timeEnd = CustLog.validateDate(apiParams.getTimeEnd(), Cons.ddMMMyyyyHHmm.get());
		    if (Validate && timeEnd == null)
		    	return "Invalid date format in Starting Time : " +  apiParams.getTimeEnd() + ".\n"
		    			+ "Expecting format : " + Cons.ddMMMyyyyHHmm.get().toPattern();
		    else
		    {

/*		    cal.setTime(timeStart);
		    cal.add(Calendar.MINUTE, -30);
		    if (timeEnd.before(cal.getTime()))
		    	return "Invalid Ending Time : " + apiParams.getTimeEnd() + ".\n"
    					+ "Expecting the ending time should be within the last 30 minutes : " + Cons.ddMMMyyyyHHmm.format(cal.getTime());
		    if (timeEnd.after(new Date()))
		    	return "Invalid Ending Time : " + apiParams.getTimeEnd() + ".\n"
				+ "Ending Time cannot be more than current time.";*/
		    exit = true;
		    }
	    }

	    if (! entry && ! exit)
	    	return "Both entry time and exit time not found";

	    //query slotUsed, parkingSlot, Calendar for parkingName, userName and status = 'Parked', exitTime;
	    if (StringUtils.isBlank(apiParams.getParkingId()))
	    {
	    	if (entry)
	    	{
			    cal.setTime(timeStart);
			    cal.add(Calendar.HOUR , 1);
			    ApiParams apiParamsToReserve = new ApiParams();
			    	apiParamsToReserve.setParkingId(apiParams.getParkingId());
			    	apiParamsToReserve.setParkingName(apiParams.getParkingName());
			    	apiParamsToReserve.setUserName(apiParams.getUserName());
			    	apiParamsToReserve.setTimeStart(apiParams.getTimeStart());
			    	apiParamsToReserve.setTimeEnd(Cons.ddMMMyyyyHHmm.get().format(cal.getTime()));

			    ReturnStatus newRetStatus = reserve(apiParamsToReserve);

			    if (StringUtils.isBlank(newRetStatus.getParkingId()))
			    	errMsg = "Reservation failed for parking while entry parking.\n"
			    			+ newRetStatus.getErrorMsg();
			    if (Validate)
			    	return errMsg;

			    apiParams.setParkingId(newRetStatus.getParkingId());
	    	}
	    	if (exit)
	    	{
	    		String Sql = " select p.parkingId\r\n" +
	    				"from parking p \r\n" +
	    				"inner join users u\r\n" +
	    				"on 	p.userId =  u.userId\r\n" +
	    				"and 	u.userName = '<userName>'\r\n" +
	    				"inner join parkingSlot l\r\n" +
	    				"on	p.slotId = l.slotId\r\n" +
	    				"and 	l.location = '<parkingName>'\r\n" +
	    				"inner join parkingBill b\r\n" +
	    				"on	p.billId = b.billId\r\n" +
	    				"and 	b.status = 'Parked'\r\n" +
	    				" order by b.billId desc\r\n" +
	    				" limit 1 ";

	    		Sql = Sql.replace("<parkingName>", apiParams.getParkingName() );
	    		Sql = Sql.replace("<userName>", apiParams.getUserName() );
	    		Sql = Sql.replace("<timeEnd>", Cons.ddMMMyyyyHHmm.get().format(timeEnd) );

	    		logger.info(Sql);

	    		String retStatus = pae.executeQuery(Sql,"Get parking ids");

			    if (retStatus == null)
			    	errMsg = "Unable trace parking Id for the "
		    				+ " Parking name " + apiParams.getParkingName()
		    				+ " User name "+ apiParams.getUserName();
			    if (Validate)
			    	return errMsg;


	    		apiParams.setParkingId(retStatus);


	    	}


	    }

	    if (! StringUtils.isBlank(apiParams.getParkingId()))
		{
			parkingId = Integer.valueOf(apiParams.getParkingId());
			fromTime = timeStart;
			parking = parkingDao.get(parkingId);
			if (Validate && parking == null)
				return "Reservation Id : " + parkingId + " not found";
			else
				slotId = parking.getSlotId();
	    	if (Validate)
	    	{
				billId = Integer.valueOf(parking.getBillId());
				parkingBill = parkingBillDao.get(billId);
				if (parkingBill == null)
					return "Could not find bill Id " + billId + " for the Reservation Id : " + parkingId;
				timeStartInReserve = parkingBill.getEntryTimeInReserve();
				if (timeStartInReserve == null)
					return "Starting time is unknown for billId " + billId + " and for the Reservation Id : "
							+ parkingId;
				exitTimeInReserve = parkingBill.getExitTimeInReserve();
				if (exitTimeInReserve == null)
					return "Ending time is unknown for billId " + billId + " and for the Reservation Id : " + parkingId;
				if (entry) {
					if ("Parked".equals(parkingBill.getStatus()))
						return "Parking bill Status shows Reservation Id : " + parkingId + " is already parked.";

					if ((timeStart.equals(timeStartInReserve) || timeStart.after(timeStartInReserve))
							&& timeStart.before(exitTimeInReserve)) {

					} else
						return "Parking Starting time " + Cons.ddMMMyyyyHHmm.get().format(timeStart)
								+ " is not within reservation time.\n" + Cons.ddMMMyyyyHHmm.get().format(timeStartInReserve)
								+ " and " + Cons.ddMMMyyyyHHmm.get().format(exitTimeInReserve);

				}
				if (exit) {
					if (!"Parked".equals(parkingBill.getStatus()))
						return "Parking bill Status shows Reservation Id : " + parkingId
								+ " is not parked. So cannot exit";

					if (timeEnd.after(timeStartInReserve)
							&& (timeEnd.equals(exitTimeInReserve) || timeEnd.before(exitTimeInReserve))) {

					} else
						return "Parking Exit time " + Cons.ddMMMyyyyHHmm.get().format(timeEnd)
								+ " is not within the reservation time.\n"
								+ Cons.ddMMMyyyyHHmm.get().format(timeStartInReserve) + " and "
								+ Cons.ddMMMyyyyHHmm.get().format(exitTimeInReserve);

					fromTime = parkingBill.getEntryTime();
				}



			}


    		ApiParking apiParking = new ApiParking();
    		apiParking.entryOrExit = entry ? "entry" : "exit";
    		apiParking.parkingId = parkingId;
    		apiParking.slotId = slotId;
    		apiParking.billId = billId;
    		apiParking.fromTime = fromTime != null ? Cons.ddMMMyyyyHHmm.get().format(fromTime) : "";
    		apiParking.toTime = exit ? Cons.ddMMMyyyyHHmm.get().format(timeEnd) : "";
    		apiParking.userName = apiParams.getUserName();
    		apiParking.parkingName = apiParams.getParkingName();

//    		GsonBuilder builder = new GsonBuilder();
//    		Gson gson = builder.create();
//
//    		String apiParkingJson = gson.toJson(apiParking);

    		HStore hs = new HStore(apiParking);


    		logger.info(hs.toString());



    		String Sql;
    		if (Validate)
    			Sql = "select apiParking('<apiParkingJson>') as retStatus ";
    		else
    			Sql = "select apiParkingByForce('<apiParkingJson>') as retStatus ";

    		Sql = Sql.replace("<apiParkingJson>", hs.toString() );


    		logger.info(Sql);

    		String retStatus = pae.executeQuery(Sql, "apiParking");

    		ReturnStatus newRetStatus = Util.parseReturnStatus(retStatus);
    		newRetStatus.setParkingId(parkingId.toString());

    		if ( parking != null)
    		{
    			parking = parkingDao.get(parkingId);
    			if ( parking != null)
    				slotId = parking.getSlotId();
    			if (slotId != null)
    			{
	        		ParkingSlot parkingSlot = parkingSlotDao.get(slotId);
	        		newRetStatus.setSlotNo(parkingSlot.getSlotNo().toString());
	        		newRetStatus.setLevel(parkingSlot.getLevel().toString());
	        		newRetStatus.setLocation(parkingSlot.getLocation());
	        		newRetStatus.setTagId(parkingSlot.getTagId());
    			}

    		}



    		return newRetStatus;

		}


	    	return errMsg;

	}

	@RequestMapping(value = "park",  method = RequestMethod.POST)
	public @ResponseBody ReturnStatus park(@RequestBody ApiParams apiParams) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();


	    CustLog.printJson("ApiController parking ", apiParams);
	    ReturnStatus newRetStatus = null;
		Object retStatus = validatePark(apiParams, true);
		String lastErrorMsg = null;

		boolean lastRunSuccess = true;

		if (retStatus instanceof String)
		{
			lastErrorMsg =  retStatus.toString();
			lastRunSuccess = false;
		}
		else if  (retStatus instanceof ReturnStatus)
		{
			newRetStatus = (ReturnStatus) retStatus;
			if (! newRetStatus.getStatus().equals(Cons.SUCCESS))
			{
				lastErrorMsg = newRetStatus.getErrorMsg();
				lastRunSuccess = false;
			}
		}
		if (! lastRunSuccess)
		{
			logger.info(lastErrorMsg);
			retStatus = validatePark(apiParams, false);
		}


		if (retStatus instanceof String)
		{
			newRetStatus = new ReturnStatus();
			newRetStatus.setStatus(Cons.ERROR);
			newRetStatus.setErrorMsg((String) retStatus);
		}
		else if  (retStatus instanceof ReturnStatus)
		{
			newRetStatus = (ReturnStatus) retStatus;
		}
		if ( newRetStatus.getParkingId() == null)
			newRetStatus.setParkingId(apiParams.getParkingId());
		if ( newRetStatus.getLocation() == null)
			newRetStatus.setLocation(apiParams.getParkingName());
		if ( newRetStatus.getTagId() == null)
			newRetStatus.setTagId(apiParams.getTagId());
		if ( StringUtils.isBlank(newRetStatus.getErrorMsg()))
			newRetStatus.setErrorMsg(lastErrorMsg);

		return newRetStatus;
	}

}








// blank check on parkingName, userName, tagId
// either fromTime and toTime should have a value
// invalidate if both are null
// invalidate if both contains values
// fromTime should be within last 30 mins
// toTime should be within last 30 mins
// if toTime is given, check it is more than fromTime
// 	fromTime is entryTime get from the parkingBill using parkingId
// 	--- difficult do at the end if parkingId is not given then
//	---	difficult do at the end query slotUsed, parkingSlot, Calendar for parkingName, userName and status = 'Parked', exitTime;
// parkingId - join parking and parkingBill
// if fromTime is given and parkingId is given
// 	get entryTime from parkingBill
// 	validate entryTime within the reserved time
// if toTime is given and parkingId is given
// 	get exitTime from parkingBill
// 	validate exitTime within the reserved time
// update slotUsed with status = 'Parked' for the parkingId given
// update parkingSlot with status = 'Parked'
// add this validation to both Reserved and Parked
//	if parkingSlot.status = 'Parked' then return failure
// if parkingId is not given
//  update slotUsed with status = 'Parked' for 30 mins block
//	based on either fromTime or toTime whichever is nonblank provided.
//  and create the parkingId to return
//  	i.e insert into parkingBill
//			insert into parking