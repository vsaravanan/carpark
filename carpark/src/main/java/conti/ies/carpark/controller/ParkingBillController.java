package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import conti.ies.carpark.dao.IParkingBillDao;
import conti.ies.carpark.model.GenSearch;
import conti.ies.carpark.model.ParkingBill;
import conti.ies.carpark.service.CommonService;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoRead;



@Controller
@RequestMapping("parkingBill")
public class ParkingBillController   {

	private static final Logger logger = LoggerFactory.getLogger(ParkingBillController.class);


	@Autowired
	private IParkingBillDao parkingBillDao;





	@Autowired
	private CommonService commonService;

	@ModelAttribute("status")
	public List<String> status() {
		return commonService.status();
	}



    @RequestMapping(value = "read", method = { RequestMethod.POST})
    public @ResponseBody Object  read(@RequestBody Object filterParams) {

    	CustLog.printJson("filterParams :", filterParams);

	    KendoRead kr = parkingBillDao.qryPaging(filterParams);

	    CustLog.printJson("grid parkingBills", kr);

	    return kr;
    }

    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET })
    public String  searchGet(Model model) {
    	model.addAttribute("search", new GenSearch() );
    	return "ParkingBills";
    }


    @RequestMapping("edit/{billId}")
    public String edit(@PathVariable Integer billId, Map<String, Object> model) {
    	ParkingBill parkingBill = parkingBillDao.get(billId);
    	model.remove("message");
    	model.put("parkingBill", parkingBill);

        return "ParkingBillForm";
    }

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute ParkingBill parkingBill, Errors errors, HttpServletRequest request) {

		logger.info("saving parkingBill ..." );
		if(! errors.hasErrors()) {
			parkingBillDao.saveOrUpdate(parkingBill);
			request.setAttribute("message","saved record");
		}
		else
			logger.info("error when updating parking Bill " );

		return "ParkingBillForm";
	}



/*

	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	public   @ResponseBody Object  update(@RequestBody ArrayList<ParkingBill>  models) {

	    //GsonBuilder builder = new GsonBuilder();
	    //Gson gson = builder.create();

	    //logger.info("parkingBills updating... : " + gson.toJson(models));

    	boolean errorFound = false;


	    Map<String, Object> onerowError = new HashMap<>();
	    for (ParkingBill l : models)
	    {
	    	Set<ConstraintViolation<ParkingBill>> violations = validator.validate(l);

	        for (ConstraintViolation<ParkingBill> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in parkingBill data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) break;
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);
		    //logger.info("parkingBills errors... : " + gson.toJson(rootError));
			return rootError;
		}
		else
		{
			logger.info("saving parkingBills after validation. no errors found");
		    parkingBillDao.saveOrUpdate(models);
		    return models;
		}

	}*/

	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	public @ResponseBody List<ParkingBill> destroy(@RequestBody ArrayList<ParkingBill> models, HttpServletRequest request) { //, @ModelAttribute GenSearch search, Model model) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," ParkingBill id ");
    	request.setAttribute("childPages","Parking");

	    CustLog.printJson("ParkingBillsController errors while deleting...  : ", models);

	    parkingBillDao.delete(models);

	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	public @ResponseBody void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," ParkingBill id ");
    	request.setAttribute("childPages","Parking");

    	logger.info("parkingBills deleting single: " + id);
	    parkingBillDao.delete(id);

	}



}

