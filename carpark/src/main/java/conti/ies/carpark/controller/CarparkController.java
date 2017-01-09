package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import conti.ies.carpark.dao.ICarparkDao;
import conti.ies.carpark.model.Carpark;
import conti.ies.carpark.model.GenSearch;
import conti.ies.carpark.service.CommonService;
import conti.ies.carpark.statics.StaticFuncs;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoMsg;
import conti.ies.comp.KendoRead;



@Controller
@RequestMapping("carpark")
@SessionAttributes("search")
public class CarparkController   {

	private static final Logger logger = LoggerFactory.getLogger(CarparkController.class);

	private Validator validator;

	@Autowired
	private ICarparkDao carparkDao;


	@Autowired
	private CommonService commonService;

	@Autowired
	private GenSearch search = new GenSearch();

	@ModelAttribute("search")
	public GenSearch search() {
		String userName = StaticFuncs.getUserName();
		this.search.setUserName(userName);
		this.search.setUserId(commonService.getUserIdFromUserName(userName).toString());


		return search;
	}



	public CarparkController() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();


	}


    @RequestMapping(value = "read", method = { RequestMethod.POST})
    public @ResponseBody Object  read(@RequestBody Object filterParams) {

		CustLog.printJson("genSearch", filterParams);

		KendoRead kr = carparkDao.qryPaging(filterParams);

		CustLog.printJson("grid carparks", kr);

	    return kr;
    }

    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET, RequestMethod.POST })
    public String  searchGet(Model model) {

    	return "Carparks";
    }



    @RequestMapping("edit/{slotId}")
    public String edit(@PathVariable Integer slotId, Map<String, Object> model) {
    	Carpark carpark = carparkDao.get(slotId);
    	model.remove("message");
    	model.put("carpark", carpark);

        return "CarparkForm";
    }

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute Carpark carpark, Errors errors, HttpServletRequest request) {

		logger.info("saving carpark ..." );
		if(! errors.hasErrors()) {
			carparkDao.saveOrUpdate(carpark);
			request.setAttribute("message","saved record");
		}
		else
			logger.info("error when updating parking Slot " );

		return "CarparkForm";
	}


	@RequestMapping(value = "update",  method = { RequestMethod.POST, RequestMethod.PUT })
	public   @ResponseBody Object  update(@RequestBody ArrayList<Carpark>  models) {

	    CustLog.printJson("CarparkController updating ... ArrayList of Carpark : ", models);

    	boolean errorFound = false;


    	StringBuilder sb = new StringBuilder();
	    Map<String, Object> onerowError = new HashMap<>();
	    for (Carpark l : models)
	    {
	    	Set<ConstraintViolation<Carpark>> violations = validator.validate(l);

	        for (ConstraintViolation<Carpark> violation : violations)
	        {
	        	errorFound = true;
	            String propertyPath = violation.getPropertyPath().toString();
	            String message = violation.getMessage();

	            Map<String, String> fieldError = new HashMap<>();
	            fieldError.put("errors",message);
	            onerowError.put(propertyPath, fieldError );

				logger.error("error in carpark data when updating..." + propertyPath + "  " + message);
	        }
	        if (errorFound) break;
	        sb.append("\n" + l.getCarpark());
	    }


		if(errorFound)
		{
		    Map<String, Object> rootError = new HashMap<>();
		    rootError.put("Errors", onerowError);

		    CustLog.printJson("CarparkController errors while updating...  : ", rootError);

			return rootError;
		}
		else
		{
			logger.info("saving carparks after validation. no errors found");
		    carparkDao.saveOrUpdate(models);

		    //return new Object[] {models, search};
			return new KendoMsg("Successfully updated " + sb);
		}

	}

	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	public @ResponseBody Object destroy(@RequestBody ArrayList<Carpark> models, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," carpark id ");
    	request.setAttribute("childPages","Parking");

	    CustLog.printJson("CarparkController errors while deleting...  : ", models);


	    carparkDao.delete(models);

	    //return models;

//	    search.setFormMsg("Successfully deleted");
//		return search;

		return new KendoMsg("Successfully deleted \n" + models.get(0).getCarpark() );
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	public @ResponseBody void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," carpark id ");
    	request.setAttribute("childPages","Parking");

    	logger.info("carparks deleting single: " + id);
	    carparkDao.delete(id);

	}

	public GenSearch getSearch() {
		return search;
	}

	public void setSearch(GenSearch search) {
		this.search = search;
	}


}

