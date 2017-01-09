package conti.ies.carpark.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import conti.ies.carpark.dao.IUserDao;
import conti.ies.carpark.model.User;
import conti.ies.carpark.service.CommonService;
import conti.ies.carpark.statics.StaticFuncs;


@Controller

public class UserController  {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserDao userDao;



//	@Autowired
//	UserValidator validator;
//	@Autowired
//	public UserController() {
//		//this.userDao = new UserDao(sessionFactory);
//	}
	@Autowired
	private CommonService commonService;

	@ModelAttribute("vehicleLkp")
	public Map<String, String> vehicleList() {
		return commonService.vehicleList();
	}

	@ModelAttribute("userType")
	public List<String> userType() {
		logger.info("userType....");
		List<String> lstUserType = new ArrayList<String>();
		if (StaticFuncs.hasRole("Admin"))
		{
			lstUserType.add("Admin");
			lstUserType.add("Internal");
			lstUserType.add("External");
		}
		else
		{
			if (StaticFuncs.hasRole("Internal"))
				lstUserType.add("Internal");
			else
				lstUserType.add("External");
		}
		return lstUserType;
	}



    @RequestMapping("admin/list")
    public String list(Model model){
        model.addAttribute("users", userDao.list());
        return "Users";
    }

    @RequestMapping(value = { "admin/add", "guest/signup"})
    public String add(Map<String, Object> model, HttpServletRequest request) {

    	logger.info("request.getRequestURI() " + request.getRequestURI());
		User user = new User();

    	if (request.getRequestURI().contains("guest"))
    		user.setGuest(true);
    	else
    		user.setGuest(false);


    	user.setUserType("External");

		model.put("user", user);

		return "UserForm";
    }



    @RequestMapping(value = {"admin/edit/{userId}", "allusers/view/{userId}", "allusers/edit/{userId}"})
    public String edit(@PathVariable String userId, Map<String, Object> model, HttpServletRequest request) {
    	User user = null;

    	logger.info("request.getRequestURI() " + request.getRequestURI());



    	if (request.getRequestURI().contains("allusers/edit"))
    		user = commonService.getUserFromUserName(userId);
    	else
    		user = userDao.get(Integer.parseInt(userId));

    	if (request.getRequestURI().contains("allusers/view"))
    		user.setView(true);;


    	model.remove("message");
    	model.put("user", user);

        return "UserForm";
    }



    @RequestMapping("admin/delete/{userId}")
    public String delete(@PathVariable Integer userId){
		userDao.delete(userId);
        return "redirect:/admin/list";
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

	@RequestMapping(value = {"allusers/save","guest/save"}, method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute User user, Errors errors, HttpServletRequest request) { // , , Model model HttpServletRequest request
		logger.info("saving user ..." );
		logger.info("request.getRequestURI() " + request.getRequestURI());


		if (request.getRequestURI().contains("guest"))
			user.setGuest(true);

		boolean isNew = user.getUserId() == null;

		if(! errors.hasErrors()) {

			boolean encryptPwd = true;
			if (! isNew)
			{
/*				if (StringUtils.isEmpty(user.getPwd()))
				{
					encryptPwd = false;
					errors.reject("pwd", "Password is required for new User");
				}
			}
			else
			{*/
				if (StringUtils.isEmpty(user.getPwd()))
				{
					encryptPwd = false;
					User userOrig = userDao.get(user.getUserId());
					user.setPwd(userOrig.getPwd());
				}
			}
			if (encryptPwd)
			{
			  String encryptedPassword = passwordEncoder.encode(user.getPwd());
			  user.setPwd(encryptedPassword);
			}

		}

		if(! errors.hasErrors())
		{

	    	request.setAttribute("errorSource","IdAlreadyExists");
	    	request.setAttribute("entityId","User");
	    	request.setAttribute("keyId",user.getUserName());

			userDao.saveOrUpdate(user);

			request.setAttribute("message","saved record");
		}
		else
		{
			logger.info("error in user data update" );
			//throw new ValidationException();
		}


		if (isNew && user.isGuest() && (!errors.hasErrors()))
			return "redirect:/guest/welcome?userId=" +user.getUserName();
		else
			return "UserForm";
	}





}

