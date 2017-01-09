package conti.ies.carpark.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalDefaultExceptionHandler   {
	public static final String DEFAULT_ERROR_VIEW = "Error";

	private final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);


//	@ExceptionHandler(value = DataIntegrityViolationException.class)
//	public ModelAndView DataIntegrityViolationExceptionHandler(HttpServletRequest req, DataIntegrityViolationException e) throws DataIntegrityViolationException {
//
//		logger.error("[URL] : {}", req.getRequestURL(), e);
//
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("datetime", new Date());
//		mav.addObject("type", "DataIntegrity");
//	    mav.addObject("url",  req.getRequestURL());
//		mav.addObject("exception", e);
//		mav.setViewName(DEFAULT_ERROR_VIEW);
//		return mav;
//	}



	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

		logger.error("[URL] : {}", req.getRequestURL(), e);


		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;


		ModelAndView mav = new ModelAndView();
	    mav.addObject("url", req.getRequestURL());
		mav.addObject("exception", e);
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}

}