package com.unicast.unicast_backend.configuration;

import javax.sql.DataSource;

import com.unicast.unicast_backend.filters.JwtAuthenticationFilter;
import com.unicast.unicast_backend.filters.JwtAuthorizationFilter;
import com.unicast.unicast_backend.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityConstants securityConstants;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/public/*").permitAll()
            .antMatchers(HttpMethod.GET, "/api/subjects").permitAll()
            .antMatchers(HttpMethod.GET, "/api/subjects/search/nameStartsWith").permitAll()
            .antMatchers(HttpMethod.GET, "/api/subjects/search/nameContaining").permitAll()
            .antMatchers(HttpMethod.GET, "/api/subjects/search/name").permitAll()
            .antMatchers(HttpMethod.GET, "/api/degrees").permitAll()
            .antMatchers(HttpMethod.GET, "/api/degrees/search/nameStartsWith").permitAll()
            .antMatchers(HttpMethod.GET, "/api/degrees/search/nameContaining").permitAll()
            .antMatchers(HttpMethod.GET, "/api/degrees/search/name").permitAll()
            .antMatchers(HttpMethod.GET, "/api/universities").permitAll()
            .antMatchers(HttpMethod.GET, "/api/universities/search/nameStartsWith").permitAll()
            .antMatchers(HttpMethod.GET, "/api/universities/search/nameContaining").permitAll()
            .antMatchers(HttpMethod.GET, "/api/degrees").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), securityConstants, userDetailsService))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void initialize(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }
}