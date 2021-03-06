package com.kmsystem.config;

import com.kmsystem.common.security.CustomAccessDeniedHandler;
import com.kmsystem.common.security.CustomUserDetailsService;
import com.kmsystem.common.security.RestAuthenticationEntryPoint;
import com.kmsystem.common.security.jwt.filter.JwtAuthenticationFilter;
import com.kmsystem.common.security.jwt.filter.JwtRequestFilter;
import com.kmsystem.common.security.jwt.filter.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config ...");

        //??? ????????? ????????? ????????? ?????? ?????????
        http.formLogin().disable()
                .httpBasic().disable();

        http.cors();

        //CSRF?????? ?????? ?????? ?????????
        http.csrf().disable();

        //JWT?????? ?????? ?????? ??????????????? ??????
        http
        		.addFilterAt(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //????????? ?????? ??????
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/**").permitAll() //????????? ???????????? 
                .antMatchers("/register/**").access("permitAll")
                .antMatchers("/users/**").authenticated()
                .anyRequest().authenticated();

        http.logout()
                .logoutUrl("/logout") //302 ???????????? 
                .invalidateHttpSession(true)	// ?????? ?????????
                .deleteCookies("JSESSIONID","COOKIES")
                .logoutSuccessUrl("/logoutSuccess")
                .permitAll();
        
        //??????????????????
        http.exceptionHandling()
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler());

    }
    
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
    
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
    	return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    //swagger??????
    @Override public void configure(WebSecurity web) {
        web .ignoring()
                .mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs","/webjars/**", "/webjars/springfox-swagger-ui/*.{js,css}");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.setExposedHeaders(Arrays.asList("Authorization","Content-Disposition"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
