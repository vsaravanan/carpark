package conti.ies.carpark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import conti.ies.carpark.dao.IUserDao;
import conti.ies.carpark.model.AdhocDao;
import conti.ies.carpark.model.IPrepareAndExecute;
import conti.ies.carpark.model.User;

@Service
public class CommonService  {

	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);


	@Autowired
	private AdhocDao adhocDao;

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IPrepareAndExecute pae;

	public List<String> listRole() {
		return adhocDao.listLkp("SELECT distinct roleType FROM UserRole WHERE roleType IS NOT NULL");
	}

	public Map<String, String> mapRole(boolean isRole) {

		if (isRole)
			return adhocDao.mapLkp("SELECT roleType || accessTo as accessTo, roleType FROM UserRole WHERE roleType IS NOT NULL order by 1");
		else
			return adhocDao.mapLkp("SELECT accessTo, CAST('' AS varchar ) as roleType FROM UserRole WHERE roleType IS null");
	}


	public Map<String, String> vehicleList() {
		logger.info("vehicleList....");
		return adhocDao.mapLkp("SELECT vehicleId, vehicleNo FROM Vehicle");
	}


	public List<String> status() {
		logger.info("status....");
		List<String> lstStatus = new ArrayList<String>();
		lstStatus.add("Open");
		lstStatus.add("Reserved");
		lstStatus.add("Parked");
		lstStatus.add("Inactive");
		return lstStatus;
	}

	public User getUserFromUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	public Integer getUserIdFromUserName(String userName) {

		User user = userDao.findByUserName(userName);

		if (user != null)
			return user.getUserId();
		else
			return null;

	}


	public Integer getSessionId() {
		String retStatus = pae.executeQuery("SELECT nextval('session_seq') as sessionId ", "getSessionId");
		return Integer.valueOf(retStatus);
	}

}
