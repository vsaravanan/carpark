package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import conti.ies.carpark.dao.IParkingDao;
import conti.ies.carpark.dao.IVwParkingDao;
import conti.ies.carpark.model.GenSearch;
import conti.ies.carpark.model.Parking;
import conti.ies.comp.CustLog;
import conti.ies.comp.KendoRead;



@Controller
@RequestMapping("parking")
public class ParkingController   {

	private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);


	@Autowired
	private IVwParkingDao vwParkingDao;

	@Autowired
	private IParkingDao parkingDao;


    @RequestMapping(value = "read", method = { RequestMethod.POST})
    public @ResponseBody Object  read(@RequestBody Object filterParams) {

    	CustLog.printJson("filterParams :", filterParams);

	    KendoRead kr = vwParkingDao.qryPaging(filterParams);

	    CustLog.printJson("grid parking", kr);

	    return kr;
    }

    @RequestMapping(value = {"", "search"},  method = { RequestMethod.GET })
    public String  searchGet(Model model) {
    	model.addAttribute("search", new GenSearch() );
    	return "Parkings";
    }


	@RequestMapping(value = "destroy",  method = RequestMethod.POST)
	public @ResponseBody List<Parking> destroy(@RequestBody ArrayList<Parking> models, HttpServletRequest request) { //, @ModelAttribute GenSearch search, Model model) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," Parking id ");
    	request.setAttribute("childPages","SlotUsed");

	    CustLog.printJson("ParkingController while deleting...  : ", models);

	    parkingDao.delete(models);

	    return models;
	}

	@RequestMapping(value = "destroySingle/{id}",  method = { RequestMethod.POST, RequestMethod.DELETE })
	public @ResponseBody void destroySingle(@PathVariable("id") Integer id, HttpServletRequest request) {
    	request.setAttribute("errorSource","childRecordExists");
    	request.setAttribute("keyIdAndValue"," Parking id ");
    	request.setAttribute("childPages","SlotUsed");

    	logger.info("parking deleting single: " + id);
    	parkingDao.delete(id);

	}



}

