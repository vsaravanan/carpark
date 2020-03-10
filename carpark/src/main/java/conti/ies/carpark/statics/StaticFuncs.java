package conti.ies.carpark.statics;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User;

import conti.ies.carpark.model.IPrepareAndExecute;
import conti.ies.carpark.service.StaticContextAccessor;



public final class StaticFuncs {

	private static final Logger logger = LoggerFactory.getLogger(StaticFuncs.class);


	private static final IPrepareAndExecute pae = StaticContextAccessor.getBean(IPrepareAndExecute.class);;


	public static boolean isAuthenticated()
	{
		return SecurityContextHolder.getContext().getAuthentication() != null;
	}

	public static boolean hasRole(String role) {

	        SecurityContext context = SecurityContextHolder.getContext();
	        if (context == null)
	            return false;

	        Authentication authentication = context.getAuthentication();
	        if (authentication == null)
	            return false;

	        for (GrantedAuthority auth : authentication.getAuthorities()) {
	            if (("ROLE_" + role).equals(auth.getAuthority()))
	            {
	            	logger.info("found " + "ROLE_" + role);
	                return true;
	            }
	        }

	        return false;
	    }

	public static String getUserName() {

        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null)
            return null;

        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return null;

        String userName = ((User) authentication.getPrincipal()).getUsername();
        return userName;
	}

	public static BigDecimal stringToBigDecimal(String val) {
		try {

			return new BigDecimal(val.trim());

		} catch (Exception e) {

		}
		return null;
	}

	public static List<String> getColumns(String tableName)
	{

		String sql = "SELECT * FROM " + tableName + "  where 1 = 2";
		List<String> list = pae.columns(sql, tableName + " columns");
		return list;
	}

}
