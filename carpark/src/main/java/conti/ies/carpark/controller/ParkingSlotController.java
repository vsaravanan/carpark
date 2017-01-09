package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

import conti.ies.carpark.dao.IParkingSlotDao;
import conti.ies.carpark.model.GenSearch;
import conti.ies.carpark.model.ParkingSlot;
import conti.ies.carpark.service.CommonService;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoRead;



@Controller
@RequestMapping("parkingSlot")
public class ParkingSlotController   {

	private static final Logger logger = LoggerFactory.getLogger(ParkingSlotController.class);

	private Validator validator;


	@Autowired
	private IParkingSlotDao parkingSlotDao;

	@Autowired
	private CommonService commonService;

	public ParkingSlotController() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
	}
	
	@ModelAttribute("status")
	public List<String> status() {
		return commonService.status();
	}

    @RequestMapping(value = "read", method = { RequestMethod.POST})
    @ResponseBody public Object  read(@RequestBody Object filterParams) {

		CustLog.printJson("genSearch", filterParams);

		KendoRead kr = parkingSlotDao.qryPaging(filterParams);

		CustLog.printJson("grid parkingSlots", kr);

	    return kr;
    }

    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET, RequestMethod.POST })
    public String  searchGet(Model model) {
    	model.addAttribute("search", new GenSearch() );
    	return "ParkingSlots";
    }



    @RequestMapping("edit/{slotId}")
    public String edit(@PathVariable Integer slotId, Map<String, Object> model) {
    	ParkingSlot parkingSlot = parkingSlotDao.get(slotId);
    	model.remove("message");
    	model.put("parkingSlot", parkingSlot);

        return "ParkingSlotForm";
    }

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute ParkingSlot parkingSlot, Errors errors, HttpServletRequest request) {

		logger.info("saving parkingSlot ..." );
		if(! errors.hasErrors()) {
			parkingSlotDao.saveOrUpdate(parkingSlot);
			request.setAttribute("message","saved record");
		}
		else
			logger.info("error when updating parking Slot " );

		return "ParkingSlotForm";
	}


	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	@ResponseBody public Object  update(@RequestBody List<ParkingSlot>  models) {

	    CustLog.printJson("ParkingSlotController updating ... ArrayList of ParkingSlot : ", models);

    	boolean errorFound = false;


	    Map<String, Object> onerowError = new HashMap<>();
	    for (ParkingSlot l : models)
	    {
	    	Set<ConstraintViolation<ParkingSlot>> violations = validator.validate(l);

	        for (ConstraintViolation<ParkingSlot> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in parkingSlot data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) 
	        	break;
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);

		    CustLog.printJson("ParkingSlotController errors while updating...  : ", rootError);

			return rootError;
		}
		else
		{
			logger.info("saving parkingSlots after validation. no errors found");
		    parkingSlotDao.saveOrUpdate(models);
		    return models;
		}

	}

	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	@ResponseBody public List<ParkingSlot> destroy(@RequestBody List<ParkingSlot> models, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," slot id ");
    	request.setAttribute("childPages","Slots Used and Parking");

	    CustLog.printJson("ParkingSlotController errors while deleting...  : ", models);


	    parkingSlotDao.delete(models);
	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	@ResponseBody public void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," slot id ");
    	request.setAttribute("childPages","Slots Used and Parking");

    	logger.info("parkingSlots deleting single: " + id);
	    parkingSlotDao.delete(id);

	}


}

