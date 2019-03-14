package us.luckylu.dev.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring security 配置
 * Created by Hello on 2018/5/31.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

	/**
	 * 配置用户认证
	 * 实例: hello - 123456
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("hello")
				.password("123456")
				.roles("ADMIN");
	}

	/**
	 * 请求授权
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().cors().disable().headers().disable() // 去掉跨域请求
			.authorizeRequests()
			.antMatchers("/","/static/**","/dev/**","/webSite/**","/webSiteInfo/**","/aboutMe").permitAll()// 配置不需要身份认证的请求地址
			.anyRequest().authenticated() // 其他所有访问路径需要身份认证
			.and()
			.formLogin()
			.loginPage("/login") // 指定登录请求地址
			.defaultSuccessUrl("/admin") // 登录成功后的默认跳转页面
			.permitAll()
			.and()
			.logout()
			.logoutSuccessUrl("/login")
			.permitAll();


	}
}
