package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import conti.ies.carpark.dao.ISlotUsedDao;
import conti.ies.carpark.dao.IVwSlotUsedDao;
import conti.ies.carpark.dao.Util;
import conti.ies.carpark.model.GenSearch;
import conti.ies.carpark.model.IPrepareAndExecute;
import conti.ies.carpark.model.SlotUsed;
import conti.ies.comp.Cons;
import conti.ies.comp.Cons.ePark;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoRead;
import conti.ies.comp.ReturnStatus;



@Controller
@RequestMapping("slotUsed")
public class GenerateSlotController   {

	private static final Logger logger = LoggerFactory.getLogger(GenerateSlotController.class);


	@Autowired
	private ISlotUsedDao slotUsedDao;

	@Autowired
	private IVwSlotUsedDao vwSlotUsedDao;

	@Autowired
	private IPrepareAndExecute pae;



    @RequestMapping(value = "read", method = { RequestMethod.POST})
    public @ResponseBody Object  read(@RequestBody Object filterParams) {

    	CustLog.printJson("filterParams :", filterParams);

	    KendoRead kr = vwSlotUsedDao.qryPaging(filterParams);

	    //CustLog.printJson("grid slotUseds", kr);

	    return kr;
    }

    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET })
    public String  searchGet(Model model) {
    	model.addAttribute("search", new GenSearch() );
    	return "SlotUseds";
    }


    @RequestMapping(value = "search",  method = { RequestMethod.POST })
    public String  searchPost(@ModelAttribute GenSearch search, Model model) {

    	String newStatus = search.getNewStatus();

    	logger.info("btnClick " + newStatus);
		logger.info("slotId " + search.getSlotId() );
		logger.info("selectedRows " + search.getSelectedRows() );
		logger.info("page " + search.getPage() );
		logger.info("userId " + search.getUserId() + "  " + search.getUserName()  );

		Map<String, ePark> mapPark = new HashMap<String, ePark>();
		{
			mapPark.put("Open", ePark.OPEN);
			mapPark.put("Reserve", ePark.RESERVED);
			mapPark.put("Park", ePark.PARKED);
			mapPark.put("Cancel", ePark.OPEN);
			mapPark.put("Inactive", ePark.INACTIVE);
		}


    	GenSearch newSearch;
		newStatus = mapPark.get(newStatus).toString();
		search.setNewStatus(newStatus);

		if (newStatus.equals(ePark.RESERVED.toString()) || newStatus.equals(ePark.PARKED.toString()) )
			newSearch = ReserveParking(search);
		else if (search.getFromDate() != null && search.getToDate()  != null)
			newSearch = GenerateSlots(search);
		else
			newSearch = ReserveParking(search);

		newSearch.setPage(search.getPage());


		model.addAttribute("search", newSearch);


        return "SlotUseds";
    }

    public GenSearch ReserveParking(GenSearch search)
    {
    	GenSearch newSearch = new GenSearch();

    	if ( search.getNewStatus().equals(ePark.OPEN.toString()) || search.getNewStatus().equals(ePark.INACTIVE.toString()))
			search.setUserId("null");

		if (search.getSelectedRows().equals(""))
		{
			newSearch.setFormErr("Row need to be selected for " + search.getNewStatus());
			return newSearch;
		}
		else if ( search.getUserId().equals("") )
		{
			newSearch.setFormErr("User Id should be selected for " + search.getNewStatus());
			return newSearch;
		}

//slotUsedIds text, status text, userId int, parkingId int, slotId int, startTime timestamp

		String Sql = "select ReserveParking('<slotUsedIds>',  '<status>', <userId>, <parkingId>, <slotId>, <startTime>, <endTime>) as retStatus ";
		Sql = Sql.replace("<status>", search.getNewStatus() );
		Sql = Sql.replace("<userId>", search.getUserId() );
		Sql = Sql.replace("<slotUsedIds>", search.getSelectedRows() );
		Sql = Sql.replace("<parkingId>", "null" );
		Sql = Sql.replace("<slotId>", "null" );
		Sql = Sql.replace("<startTime>", "null" );
		Sql = Sql.replace("<endTime>", "null" );

		logger.info(Sql);

		String retStatus = pae.executeQuery(Sql, "ReserveParking...");

		ReturnStatus newRetStatus = Util.parseReturnStatus(retStatus);


		if (! newRetStatus.getStatus().equals(Cons.SUCCESS))
			newSearch.setFormErr( "Error : " + newRetStatus.getErrorMsg() +
					"\n You have attempted to change status to " + search.getNewStatus() +
					" ,\n userId " + search.getUserId()  + "  " + search.getUserName() +
					"\n for slotids " + search.getSelectedRows()
					);
		else
			newSearch.setFormMsg("Successfully updated " +
							" with status " + search.getNewStatus() +
							" ,\n userId " + search.getUserId()  + "  " + search.getUserName() +
							//" , parkingId " +
							"\n for slotids " + search.getSelectedRows()
			);
		return newSearch;


    }

    public GenSearch GenerateSlots(GenSearch search)
    {
    	String strFromDate = Cons.ddMMMyyyyHHmm.get().format(search.getFromDate());
    	String strToDate = Cons.ddMMMyyyyHHmm.get().format(search.getToDate());
    	String newStatus = search.getNewStatus();

    	logger.info("fromDate " + strFromDate);
    	logger.info("toDate " + strToDate);

//    	strFromDate = Cons.ddMMMyyyy.format(search.getFromDate());
//    	strToDate = Cons.ddMMMyyyy.format(search.getToDate());
//
//    	logger.info("fromDate " + strFromDate);
//    	logger.info("toDate " + strToDate);

		String Sql = "select GenerateSlotUsed('<fromDate>',  '<toDate>', '<slotId>', '<status>', null, null ) ";
		Sql = Sql.replace("<fromDate>", strFromDate);
		Sql = Sql.replace("<toDate>", strToDate);
		Sql = Sql.replace("<slotId>", search.getSlotId());
		Sql = Sql.replace("<status>", newStatus);

    	logger.info(Sql);
    	pae.executeQuery(Sql,"Generate Slots");

	   	GenSearch newSearch = new GenSearch();

		if (newStatus.equals(ePark.OPEN.toString()))
			newSearch.setFormMsg("Successfully populated slots records for " +
    						strFromDate + " and " + strToDate +
    						" with status " + newStatus);
		else
			newSearch.setFormMsg("Successfully updated " +
					" with status " + newStatus +
					" for " + strFromDate + " and " + strToDate
    				);

		return newSearch;
    }



/*

	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	public   @ResponseBody Object  update(@RequestBody ArrayList<SlotUsed>  models) {

	    //GsonBuilder builder = new GsonBuilder();
	    //Gson gson = builder.create();

	    //logger.info("slotUseds updating... : " + gson.toJson(models));

    	boolean errorFound = false;


	    Map<String, Object> onerowError = new HashMap<>();
	    for (SlotUsed l : models)
	    {
	    	Set<ConstraintViolation<SlotUsed>> violations = validator.validate(l);

	        for (ConstraintViolation<SlotUsed> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in slotUsed data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) break;
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);
		    //logger.info("slotUseds errors... : " + gson.toJson(rootError));
			return rootError;
		}
		else
		{
			logger.info("saving slotUseds after validation. no errors found");
		    slotUsedDao.saveOrUpdate(models);
		    return models;
		}

	}*/

	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	public @ResponseBody List<SlotUsed> destroy(@RequestBody ArrayList<SlotUsed> models, HttpServletRequest request) { //, @ModelAttribute GenSearch search, Model model) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," SlotUsed id ");
    	request.setAttribute("childPages","Parking");

	    CustLog.printJson("GenerateSlotController errors while deleting...  : ", models);

	    slotUsedDao.delete(models);

	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	public @ResponseBody void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," SlotUsed id ");
    	request.setAttribute("childPages","Parking");

    	logger.info("slotUseds deleting single: " + id);
	    slotUsedDao.delete(id);

	}



}

