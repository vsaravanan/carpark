package conti.ies.carpark.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import conti.ies.carpark.dao.IVehicleDao;
import conti.ies.carpark.dao.IVwVehicleDao;
import conti.ies.carpark.dao.Util;
import conti.ies.carpark.model.Vehicle;
import conti.ies.carpark.model.VwVehicle;
import conti.ies.comp.CustLog;



@Controller
@RequestMapping("vehicle")
@RestController
public class VehicleController  {

	private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	private Validator validator;

	@Autowired
	private IVehicleDao vehicleDao;

	@Autowired
	private IVwVehicleDao vwVehicleDao;

	public VehicleController() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
	}


    @RequestMapping(value = "read", method = RequestMethod.GET)
    @ResponseBody public  List<VwVehicle>  read() {

    	List<VwVehicle> vwVehicles = vwVehicleDao.list();

    	CustLog.printJson("VehicleController reading List of Vehicles...  : ", vwVehicles);


        return vwVehicles;
    }



	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	@ResponseBody public Object  update(@RequestBody List<VwVehicle>  models) {


	    List<Vehicle> lObj = Util.Cast(models);
	    CustLog.printJson("VehicleController updating ... ArrayList of VwVehicle : ", lObj);

    	boolean errorFound = false;


	    Map<String, Object> onerowError = new HashMap<>();
	    for (Vehicle l : lObj)
	    {
	    	Set<ConstraintViolation<Vehicle>> violations = validator.validate(l);

	        for (ConstraintViolation<Vehicle> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in vehicle data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) 
	        	break;
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);
	    	CustLog.printJson("VehicleController errors while updating...  : ", rootError);
			return rootError;
		}
		else
		{
			logger.info("saving vehicles after validation. no errors found");
		    vehicleDao.saveOrUpdate(lObj);
		    return models;
		}

	}

	// it will not work for RequestMethod.DELETE for multiple records.
	// DELETE is catered for single record only
	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	@ResponseBody public List<VwVehicle> destroy(@RequestBody List<VwVehicle> models, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," VwVehicle id ");
    	request.setAttribute("childPages","Users or Parking");

	    List<Vehicle> lObj = Util.Cast(models);

    	CustLog.printJson("VehicleController deleting List of Vehicles...  : ", lObj);


	    vehicleDao.delete(lObj);
	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	@ResponseBody public void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," VwVehicle id ");
    	request.setAttribute("childPages","Users or Parking");

    	logger.info("vehicles deleting single: " + id);
	    vehicleDao.delete(id);

	}
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    @ResponseBody public void save(String fileName, String base64, String contentType,
            HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename="
                + fileName);
        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
}

