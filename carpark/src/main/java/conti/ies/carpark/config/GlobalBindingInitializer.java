package conti.ies.carpark.config;

import conti.ies.comp.Cons;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration
public class GlobalBindingInitializer {

    @Bean
    public ConfigurableWebBindingInitializer configurableWebBindingInitializer() {

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        // add our custom converters and formatters
        //conversionService.addConverter(...);
        //conversionService.addFormatter(...);
        initializer.setConversionService(conversionService);
        //we can set our custom validator
        //initializer.setValidator(....);

        // Assembly custom property editor
        initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
            //PropertyEditors are not thread-safe. For each request, we need a new PropertyEditor object.
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            propertyEditorRegistry.registerCustomEditor(Date.class,
                    new CustomDateEditor(Cons.ddMMMyyyyHHmm.get(), true, 17));


        });
        return initializer;

    }
}