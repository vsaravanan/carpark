package conti.ies.carpark.config;

import conti.ies.comp.Cons;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

// This annotation is used to define @ExceptionHandler, @InitBinder, and @ModelAttribute methods that apply to all @RequestMapping methods
@ControllerAdvice
public class GlobalBindingInitializer implements WebBindingInitializer {

	@Override
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {

//        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, new DecimalFormat("#.##"),true));
        binder.registerCustomEditor(Date.class,  new CustomDateEditor(Cons.ddMMMyyyyHHmm.get() , true, 17));

    }

}