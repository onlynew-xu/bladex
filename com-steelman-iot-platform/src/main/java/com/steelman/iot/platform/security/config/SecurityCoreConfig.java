package com.steelman.iot.platform.security.config;

import com.steelman.iot.platform.security.filter.JwtFilter;
import com.steelman.iot.platform.security.filter.LoginFilter;
import com.steelman.iot.platform.security.handler.SysSteelAuthenticationEntryPoint;
import com.steelman.iot.platform.security.handler.SysUserAccessDeniedHandler;
import com.steelman.iot.platform.security.handler.SysUserLogoutHandler;
import com.steelman.iot.platform.security.service.SysUserService;
import com.steelman.iot.platform.service.LogLoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author tang
 * date 2020-11-20
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityCoreConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private SysUserAccessDeniedHandler sysUserAccessDeniedHandler;
    @Resource
    private SysSteelAuthenticationEntryPoint sysSteelAuthenticationEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService).passwordEncoder(passwordEncoder());
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(redisTemplate, logLoginService);
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/api/login");
        return loginFilter;
    }

    @Bean
    JwtFilter jwtFilter() {
        return new JwtFilter(redisTemplate);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().logoutUrl("/api/logout").addLogoutHandler(new SysUserLogoutHandler(redisTemplate));
        http.formLogin().loginPage("/api/login");
        http.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/doc/*").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/*").permitAll()
                .antMatchers("/csrf").permitAll()
                .antMatchers("/export/*").permitAll()
                .antMatchers("/down/**").permitAll()
                .antMatchers("/api/monitor/**").permitAll()
                .antMatchers("/notify/**").permitAll()
//                .antMatchers( "/api/apiLogin/**").permitAll()
//                .antMatchers( "/api/company/**").permitAll()
//                .antMatchers( "/api/region/**").permitAll()
//                .antMatchers( "/api/pment/**").permitAll()
//                .antMatchers( "/api/deviceData/**").permitAll()
//                .antMatchers("/api/power/**").permitAll()
                .antMatchers("/electricity/**").permitAll()
                .anyRequest().authenticated();
        http.cors().and().csrf().disable();
        http.exceptionHandling().accessDeniedHandler(sysUserAccessDeniedHandler);
        http.exceptionHandling().authenticationEntryPoint(sysSteelAuthenticationEntryPoint);
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtFilter(), LoginFilter.class);
    }
}
