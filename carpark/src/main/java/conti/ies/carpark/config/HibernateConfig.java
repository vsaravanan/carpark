package conti.ies.carpark.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
//@EnableEncryptableProperties
@Log4j2
public class HibernateConfig {

	@Autowired
	Environment env;

    @Autowired
    PooledPBEStringEncryptor jasyptStringEncryptor;

    @Value("${spring.jpa.packages-to-scan}")
    private String springJpaPackagesToScan;


    @SuppressWarnings("serial")
	private Properties hibernateProperties() {


    	return new Properties() {
            {

                setProperty("driverClassName", env.getProperty("spring.datasource.driverClassName"));
                setProperty("url", env.getProperty("spring.datasource.url"));
                setProperty("username", env.getProperty("spring.datasource.username"));
                setProperty("password", env.getProperty("spring.datasource.password"));

                setProperty("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
                setProperty("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show-sql"));
                setProperty("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
//                setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");

            }
         };

    }

    @Bean
    public DataSource dataSource() {

        Properties prop = hibernateProperties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(prop.getProperty("driverClassName"));
        dataSource.setUrl(prop.getProperty("url"));
        dataSource.setUsername(prop.getProperty("username"));
        dataSource.setPassword(prop.getProperty("password"));
        return dataSource;
    }


    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(springJpaPackagesToScan);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    @Bean // important it is used in transaction
    public HibernateTransactionManager transactionManager(
            final SessionFactory sessionFactory,
            final DataSource dataSource
    )
    {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }

    @Bean(name="jasyptStringEncryptor")
    static public PooledPBEStringEncryptor stringEncryptor(@Autowired Environment env) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        String jasee = env.getProperty("jasypt.encryptor.password");
        if (StringUtils.isBlank(jasee)) {
            jasee = System.getenv("jasypt_encryptor_password");
        }
        jasee = StringUtils.stripEnd(jasee, "");
        config.setPassword(jasee);
        config.setAlgorithm(env.getProperty("security.password.algorithm"));
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }


}

