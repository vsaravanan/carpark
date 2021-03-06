package conti.ies.carpark.config;


import conti.ies.carpark.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
@EnableWebSecurity // EnableWebSecurity will provide configuration via HttpSecurity from <http> jsp pages
//@EnableGlobalMethodSecurity // EnableGlobalMethodSecurity provides AOP security on methods, some of annotation it will enable are PreAuthorize PostAuthorize also it has support for JSR-250
	//(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
// auth.inMemoryAuthentication()
// auth.jdbcAuthentication()
// auth.ldapAuthentication()

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);


	@Autowired
	@Qualifier("dataSource")
	DataSource dataSource;

	@Autowired
	CommonService commonService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

	  auth.jdbcAuthentication().dataSource(dataSource)

		.usersByUsernameQuery(
			"select username, pwd as password, state='Active' as enabled from users where username=?")
		.authoritiesByUsernameQuery(
			"select username, 'ROLE_' || userType as authority  from users where username=?")
		.passwordEncoder(bCryptPasswordEncoder())
		;


//	    Authentication auth2 = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth2);

	}

	 @Bean
	 public BCryptPasswordEncoder bCryptPasswordEncoder(){
		 return new BCryptPasswordEncoder(10);
	 }

	@Override
	public void configure(WebSecurity web) throws Exception
	{
//			Ignore any request that starts with "/resources/".
//			web.debug(false).ignoring().antMatchers("/js/**", "/css/**", "/img/**", "/png/**", "/styles/**", "/layout/**", "/decorators/**",
//			"/api/**","/user/add","/user/save" );

		boolean isRole = false;


		Map<String, String> mapRole = commonService.mapRole(isRole);

		List<String> list =new ArrayList<String>();


		for (Map.Entry<String, String> entry : mapRole.entrySet())
		{

			String mAccessTo = entry.getKey();
			list.add(mAccessTo);
		}
		web.debug(false).ignoring().antMatchers(list.toArray(new String[list.size()]));
//		web.debug(false).ignoring().antMatchers("/**");


	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		boolean isRole = true;

		Map<String, String> mapRole = commonService.mapRole(isRole);
		for (Map.Entry<String, String> entry : mapRole.entrySet())
		{
			String mRole = entry.getValue();
			String mAccessTo = entry.getKey();

			mAccessTo = mAccessTo.substring(mRole.length());
			logger.info("role / accessTo : " + mRole + "/" + mAccessTo);

			String[] strsplitted = mRole.split("/");


			http.authorizeRequests()
					.antMatchers("/" + mAccessTo + "/**").hasAnyRole(strsplitted);

		}

		http
				.authorizeRequests().anyRequest().denyAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/allusers/welcome", true)
				.failureUrl("/login?error")
				.usernameParameter("userName")
				.passwordParameter("pwd")
				.permitAll()
//			.and()
//		  		.exceptionHandling().accessDeniedPage("/Access_Denied")
				.and()
				.logout()
				.logoutSuccessUrl("/login?logout")
				.deleteCookies( "JSESSIONID" )
				.invalidateHttpSession(true)
				.permitAll()
				.and()
				.csrf()
				//.disable()
				.and()
				.sessionManagement()
				.invalidSessionUrl( "/login" )
				.maximumSessions(1)
		;

//		http.csrf().disable();
//		http.authorizeRequests().anyRequest().permitAll();
//		http.authorizeRequests().antMatchers("/*").permitAll();


	}

}
