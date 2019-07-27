package ru.com.m74.cubes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "security")
    private UserDetailsService userDetailsService;


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationEntryPoint entryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/", "/**/*.html").permitAll()
                .antMatchers("/favicon.ico", "/**/*.js", "/**/*.json", "/**/*.gif", "/**/*.png", "/**/*.css", "/resources/**").permitAll()
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler((request, response, exception) -> {
                    encoder().encode("admin");
                    response.sendRedirect(request.getHeader("referer"));
                })
                .successHandler((request, response, authentication) -> {
//                    auditService.login(request.getSession());

                    response.setContentType("text/html");
                    response.sendRedirect(request.getHeader("referer"));
                    response.getWriter().print("<html><head><script>history.back();</script></head></html>");
                    response.getWriter().close();
                })
                .permitAll();

        http
                .logout()
//                .addLogoutHandler((request, response, authentication) -> request.getSession().setAttribute("logout", true))
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                });

        http.headers().frameOptions().sameOrigin();
        http.exceptionHandling().authenticationEntryPoint(entryPoint());
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user")
//                .password(encoder().encode("password"))
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password(encoder().encode("admin"))
//                .roles("USER", "ADMIN");
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }
}
