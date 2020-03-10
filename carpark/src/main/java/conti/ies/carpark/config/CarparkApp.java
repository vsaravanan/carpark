package conti.ies.carpark.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;


//@Configuration // this will let spring know this contains bean definitions
//@ComponentScan({"conti.ies.carpark"}) // this is the same as <context:component-scan>
//@EnableWebMvc // mvc:annotation-driven It enables support for @Controller-annotated classes that use @RequestMapping to map incoming requests
//@EnableTransactionManagement // to enable transactional support
//@PropertySource({ "classpath:db.properties" })
// exclude = HibernateJpaAutoConfiguration.class is required to avoid Exception: class org.springframework.orm.jpa.EntityManagerHolder cannot be cast to class org.springframework.orm.hibernate5.SessionHolder
@SpringBootApplication (exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan({"conti.ies.carpark"})

@Log4j2
public class CarparkApp {

    @Autowired
    Environment env;

    public static void main(String[] args) throws Exception {
        for(String arg:args) {
            if(arg.contains("jasypt.encryptor.password")) {
                String[] values = arg.split("jasypt.encryptor.password=");
                Integer pos = 0;
                if (values.length > 1) {
                    String somevalue = values[1];
                    System.setProperty("jasypt.encryptor.password", somevalue);
                }
                break;

            } else if(arg.contains("jasypt_encryptor_password")) {
                String[] values = arg.split("jasypt_encryptor_password=");
                Integer pos = 0;
                if (values.length > 1) {
                    String somevalue = values[1];
                    System.setProperty("jasypt.encryptor.password", somevalue);
                }
                break;

            }
        }

        SpringApplication.run(CarparkApp.class, args);
    }


    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/messages", "classpath:messages/validation");
        // if true, the key of the message will be displayed if the key is not
        // found, instead of throwing a NoSuchMessageException
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        // # -1 : never reload, 0 always reload
        messageSource.setCacheSeconds(0);
        return messageSource;
    }


}
/*    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(20971520); // 20 MB
        resolver.setMaxInMemorySize(1048576); // 1 MB

        return resolver;
    }	*/
