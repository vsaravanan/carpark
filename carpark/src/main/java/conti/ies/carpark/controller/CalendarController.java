package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

import conti.ies.carpark.dao.ICalendarDao;
import conti.ies.carpark.model.AdhocDao;
import conti.ies.carpark.model.Calendar;
import conti.ies.carpark.model.GenSearch;
import conti.ies.comp.Cons;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoRead;



@Controller
@RequestMapping("calendar")
public class CalendarController   {

	private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

	private Validator validator;


	@Autowired
	private ICalendarDao calendarDao;


	@Autowired
	private GenSearch search;

	@Autowired
	private AdhocDao adhocDao;


	public CalendarController() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
	}

	public GenSearch getSearch() {
		return search;
	}

	public void setSearch(GenSearch search) {
		this.search = search;
	}



    @RequestMapping(value = "read", method = { RequestMethod.POST})
    public @ResponseBody Object  read(@RequestBody Object filterParams) {

		CustLog.printJson("genSearch", filterParams);

		KendoRead kr = calendarDao.qryPaging(filterParams);

		CustLog.printJson("grid calendars", kr);

	    return kr;
    }


    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET, RequestMethod.POST })
    public String  search(@ModelAttribute GenSearch genSearch, Model  model) {

    	Date dFromDate = genSearch.getFromDate();
    	Date dToDate = genSearch.getToDate();
    	GenSearch newGenSearch = new GenSearch();
		newGenSearch.setFormMsg("");


    	if (dFromDate != null && dToDate != null)
    	{


        	String strFromDate = Cons.ddMMMyyyy.format(dFromDate);
        	String strToDate = Cons.ddMMMyyyy.format(dToDate);

        	logger.info("fromDate " + strFromDate);
        	logger.info("toDate " + strToDate);

    		String Sql = "select GenerateCalendar('<fromDate>',  '<toDate>') ";
    		Sql = Sql.replace("<fromDate>", strFromDate);
    		Sql = Sql.replace("<toDate>", strToDate);

        	logger.info(Sql);

    		adhocDao.executeSql(Sql);

    		newGenSearch.setFormMsg("Successfully populated calendar records for " + strFromDate + "  " + strToDate);

    	}



    	model.addAttribute("search", newGenSearch);

        return "Calendars";
    }






	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	public   @ResponseBody Object  update(@RequestBody ArrayList<Calendar>  models) {

	    CustLog.printJson("CalendarController updating ... ArrayList of Calendar : ", models);

    	boolean errorFound = false;


	    Map<String, Object> onerowError = new HashMap<>();
	    for (Calendar l : models)
	    {
	    	Set<ConstraintViolation<Calendar>> violations = validator.validate(l);

	        for (ConstraintViolation<Calendar> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in calendar data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) break;
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);

		    CustLog.printJson("CalendarController errors while updating...  : ", rootError);

			return rootError;
		}
		else
		{
			logger.info("saving calendars after validation. no errors found");
		    calendarDao.saveOrUpdate(models);
		    return models;
		}

	}

	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	public @ResponseBody List<Calendar> destroy(@RequestBody ArrayList<Calendar> models, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," Calendar id ");
    	request.setAttribute("childPages","Slots Used");

	    CustLog.printJson("CalendarController errors while deleting...  : ", models);


	    calendarDao.delete(models);
	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	public @ResponseBody void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," Calendar id ");
    	request.setAttribute("childPages","Slots Used");

    	logger.info("calendars deleting single: " + id);
	    calendarDao.delete(id);

	}



}

