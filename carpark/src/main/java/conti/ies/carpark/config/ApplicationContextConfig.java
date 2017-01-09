package conti.ies.carpark.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;




@Configuration // this will let spring know this contains bean definitions
@ComponentScan({"conti.ies.carpark"}) // this is the same as <context:component-scan>
@EnableWebMvc // mvc:annotation-driven It enables support for @Controller-annotated classes that use @RequestMapping to map incoming requests
@EnableTransactionManagement // to enable transactional support
@PropertySource({ "classpath:db.properties" })
public class ApplicationContextConfig  extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

/*
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }	
*/
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// if you want to keep it in resources
//    	registry.addResourceHandler("/resources/**")
//        .addResourceLocations("classpath:/");


    	registry.addResourceHandler("/js/**")
    	.addResourceLocations("/js/");

    	registry.addResourceHandler("/styles/**")
    	.addResourceLocations("/styles/");

    	registry.addResourceHandler("/css/**")
    	.addResourceLocations("/css/");

    	registry.addResourceHandler("/img/**")
    	.addResourceLocations("/img/");

    	registry.addResourceHandler("/png/**")
    	.addResourceLocations("/png/");

    	registry.addResourceHandler("/fonts/**")
    	.addResourceLocations("/fonts/");

    	registry.addResourceHandler("/file/**")
    	.addResourceLocations("/file/");

//MSS    	
//    	registry.addResourceHandler("/views/**")
//    	.addResourceLocations("/WEB-INF/views/");    	

    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean
	public LocaleResolver localeResolver() {

		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString("en"));
		return cookieLocaleResolver;
	}

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

      //MSS  
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

    @Autowired
    @Bean
    public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
//		dataSource.setUrl("jdbc:log4jdbc:postgresql://localhost:5432/postgres");
		
//		dataSource.setMinIdle(5);
//		dataSource.setMaxIdle(20);
//		dataSource.setMaxOpenPreparedStatements(180);

		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));  //10.221.52.139
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}





    @SuppressWarnings("serial")
	private Properties hibernateProperties() {

    	return new Properties() {
            {

               put("hibernate.dialect", env.getProperty("hibernate.dialect"));
               put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
               put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
               put("hibernate.order_inserts", env.getProperty("hibernate.order_inserts"));
               put("hibernate.order_updates", env.getProperty("hibernate.order_updates"));
               put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
               put("hibernate.jdbc.batch_versioned_data", env.getProperty("hibernate.jdbc.batch_versioned_data"));
			   //properties.put("hibernate.use_sql_comments", "true");
            }
         };

         }

    @Autowired
    @Bean
    public SessionFactory sessionFactory() {


    	LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource());
    	sessionBuilder.addProperties(hibernateProperties());
    	sessionBuilder.scanPackages("conti.ies.carpark.model");
    	return sessionBuilder.buildSessionFactory();

    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        //resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSizePerFile(20971520); // 20 MB
        resolver.setMaxInMemorySize(1048576); // 1 MB

        return resolver;
    }

/*    @Autowired
    @Bean
    public AnnotationSessionFactoryBean sessionFactory() {

       AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
       sessionFactory.setDataSource(dataSource());
       sessionFactory.setPackagesToScan(new String[] { "conti.ies.carpark.model" });
       sessionFactory.setHibernateProperties(hibernateProperties());

       return sessionFactory;
    }*/

/*    @Autowired
    @Bean
    public LocalSessionFactoryBean sessionFactory() {

    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
       sessionFactory.setDataSource(dataSource());
       sessionFactory.setPackagesToScan(new String[] { "conti.ies.carpark.model" });
       sessionFactory.setHibernateProperties(hibernateProperties());

       return sessionFactory;
    }
*/

	@Autowired
	@Bean
	public HibernateTransactionManager transactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);

		return transactionManager;
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

// the idea behind @EnableTransactionManagement is to enable the following
//@Bean
//public PlatformTransactionManager transactionManager(){
//   JpaTransactionManager transactionManager = new JpaTransactionManager();
//   transactionManager.setEntityManagerFactory(
//    entityManagerFactoryBean().getObject() );
//   return transactionManager;
//}

//  this is the same as <mvc:default-servlet-handler/>
//@Override
//public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//    configurer.enable();
//}


//    @Autowired
//    @Bean(name = "userDaoController")
//    public IUserDao getUserDao(SessionFactory sessionFactory) {
//    	return new UserDao(sessionFactory);
//
//    }

//    @Autowired
//    @Bean(name = "vehicleDaoController")
//    public IVehicleDao getVehicleDao(SessionFactory sessionFactory) {
//    	return new VehicleDao(sessionFactory);
//
//    }



//    @Bean
//    public static PropertySourcesPlaceholderConfigurer properties(){
//    	PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
//      Resource[] resources = new ClassPathResource[ ]
//        { new ClassPathResource( "log4j.properties" )};
//      ppc.setLocations( resources );
//      ppc.setIgnoreUnresolvablePlaceholders( true );
//      return ppc;
//    }


//@Bean(name = "dataSourceSpied")
//public DataSource getDataSourceSpied() {

//	BasicDataSource dataSource = new BasicDataSource();
	 // com.p6spy.engine.spy.P6SpyDriver
	 // net.sf.log4jdbc.sql.jdbcapi.DriverSpy
	//dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
	//dataSource.setUrl("jdbc:log4jdbc:postgresql://localhost:5432/postgres");

//@Bean(name = "dataSource")
//public DataSourceSpy getDataSource(DataSource dataSourceSpied) {
//return new DataSourceSpy(dataSourceSpied);
//}



//	StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
//			.applySettings(getHibernateProperties())
//			.build();
//
//	Metadata metadata = new MetadataSources( standardRegistry )
//			.addAnnotatedClass( User.class )
////			.addAnnotatedClassName( "org.hibernate.example.Customer" )
////			.addResource( "org/hibernate/example/Order.hbm.xml" )
//			.getMetadataBuilder()
//			.build();
//
//	SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
//			.build();
//
//	return sessionFactory;



// http://stackoverflow.com/questions/11257598/how-to-scan-packages-for-hibernate-entities-instead-of-using-hbm-xml
//<property name="hibernateProperties">
//<props>
//      <prop key="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</prop>
//      <prop key="hibernate.show_sql">${hibernate.show_sql}</prop>
//  <prop key="hibernate.format_sql">${hibernate.format_sql}</prop>
//  <prop key="hibernate.use_sql_comments">${hibernate.use_sql_comments}</prop>
//  <prop key="hbm2ddl.auto">validate</prop>
//  <prop key="hibernate.cache.use_query_cache">true</prop>
//  <prop key="hibernate.connection.release_mode">after_statement</prop>
//  <prop key="hibernate.cache.provider_class">net.sf.ehcache.hibernate.EhCacheProvider</prop>
//  <prop key="hibernate.cache.use_second_level_cache">${hibernate.cache.use_second_level_cache}</prop>
//  <prop key="hibernate.cache.use_structured_entries">${hibernate.cache.use_structured_entries}</prop>
//  <prop key="hibernate.jdbc.fetch_size">${hibernate.jdbc.fetch_size}</prop>