package conti.ies.carpark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import conti.ies.carpark.dao.IParkingSlotDao;
import conti.ies.carpark.dao.IUserDao;
import conti.ies.carpark.model.ParkingSlot;
import conti.ies.carpark.model.User;
import conti.ies.comp.CustLog;


@Controller
@RequestMapping("lookup")
public class LookupController {

	@Autowired
	private IParkingSlotDao parkingSlotDao;

	@RequestMapping("listParkingSlot")
	public @ResponseBody List<ParkingSlot> listParkingSlot() {

		List<ParkingSlot> lst = parkingSlotDao.list();

		CustLog.printJson("ParkingSlot", lst);

		return lst;

	}


	@Autowired
	private IUserDao userDao;

	@RequestMapping("listUser")
	public @ResponseBody List<User> listUser() {

		List<User> lst = userDao.list();

		CustLog.printJson("listUser", lst);

		return lst;

	}



}
