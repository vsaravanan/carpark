package conti.ies.carpark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import conti.ies.carpark.statics.StaticFuncs;


@Controller
public class HomeController {

	@RequestMapping("/")
	public String home() {
	        return "redirect:/login";
	        //return "redirect:/calendar";
	}


	@RequestMapping("/allusers")
	public String user() {
		if (StaticFuncs.hasRole("Admin"))
		{
	        return "redirect:/admin/list";
		}
		else
		{
			String userName = StaticFuncs.getUserName();
			return "redirect:/allusers/edit/" + userName;
		}

	}

	@RequestMapping("/vehicle")
	public String vehicle() {
	        return "Vehicles";
	}

	@RequestMapping("/calendar")
	public String calendar() {
	        return "Calendars";
	}

	@RequestMapping("/parkingSlot")
	public String parkingSlot() {
	        return "parkingSlots";
	}

	@RequestMapping("/slotUsed")
	public String slotUsed() {
	        return "SlotUseds";
	}

	@RequestMapping("/parking")
	public String parking() {
	        return "Parkings";
	}

	@RequestMapping("/parkingBill")
	public String parkingBill() {
	        return "ParkingBills";
	}




	@RequestMapping("/vehicle1")
	public String vehicle1() {
	        return "Vehicles1";
	}

	@RequestMapping("/testing")
	public ModelAndView anyThing()  {
		ModelAndView model = new ModelAndView("Testing");
		return model;
	}
	
	@RequestMapping("/guest/jstest")
	public String jstest()  {
		return "jstest";
	}

	@RequestMapping("/download/showVideo/{videoName}")
	public String showVideo(@PathVariable String videoName, Model model)  {
		model.addAttribute("videoName", videoName);
		return "ShowVideo";
	}	

}
