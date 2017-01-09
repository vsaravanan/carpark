package conti.ies.carpark.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import conti.ies.carpark.statics.StaticFuncs;

@Controller
@SessionAttributes("userId")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/allusers/welcome", method = RequestMethod.GET)
	public String  allusersWelcome(ModelMap model) {
		return Welcome(model, null);
	}

	@RequestMapping(value = "/guest/welcome", method = RequestMethod.GET)
	public String guestWelcome(ModelMap model, @RequestParam("userId") String userId) {
		return Welcome(model, userId);
	}


	private String Welcome(ModelMap model, String userId) {
		logger.info("Welcome userId " + userId);
		if (userId == null)
			userId = StaticFuncs.getUserName();
		model.addAttribute("userId", userId);
		return "welcome";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		model.addAttribute("userId", StaticFuncs.getUserName());
		return "admin";
	}


	@RequestMapping(value = "/Access_Denied", method = { RequestMethod.GET, RequestMethod.POST })
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("userId", StaticFuncs.getUserName());
		return "accessDenied";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	@RequestMapping(value="/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

}