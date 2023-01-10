package com.oasis.cac.vas.auth.config;

import com.oasis.cac.vas.auth.config.filter.basic_login.JWTAuthenticationFilter;
import com.oasis.cac.vas.auth.config.filter.basic_login.JWTAuthorizationFilter;
import com.oasis.cac.vas.auth.config.filter.basic_login.LoginFailureHandler;
import com.oasis.cac.vas.auth.config.filter.social_login.facebook.FacebookOAuth2AuthenticationFilter;
import com.oasis.cac.vas.auth.config.filter.social_login.google.GoogleOAuth2AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${trusted.origins:http://localhost,http://127.0.0.1}")
    private String[] whitelist;

    @Qualifier("userService")
    @Autowired
    private UserDetailsService userDetailsService;

    private static final RequestMatcher RequestMatcher_PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/v1/api/public/**")
    );

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(RequestMatcher_PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.addFilterAt(getGoogleOAuth2AuthenticationFilter(), BasicAuthenticationFilter.class);

        http.addFilterAt(getFacebookOAuth2AuthenticationFilter(), BasicAuthenticationFilter.class);

        http.addFilterAt(getJWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(corsFilter(), SessionManagementFilter.class); //adds your custom CorsFilter

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/v1/api/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getJWTAuthorizationFilter()).logout()
                .logoutUrl("/v1/api/protected/auth/logout")
                .permitAll()
                .logoutSuccessHandler(myLogoutHandler()).and()
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public GoogleOAuth2AuthenticationFilter getGoogleOAuth2AuthenticationFilter() throws Exception {
        String url = "/v1/api/public/auth/connect/google/login";
        return new GoogleOAuth2AuthenticationFilter(url);
    }

    @Bean
    public FacebookOAuth2AuthenticationFilter getFacebookOAuth2AuthenticationFilter() throws Exception {
        return new FacebookOAuth2AuthenticationFilter("/api/public/auth/connect/facebook/login");
    }

    @Bean
    public JWTAuthenticationFilter getJWTAuthenticationFilter() throws Exception {
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter();
        filter.setFilterProcessesUrl("/v1/api/public/auth/login");
        return filter;
    }

    @Bean
    public JWTAuthorizationFilter getJWTAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager());
    }


    @Bean
    public LoginFailureHandler getLoginFailureHandler() throws Exception {
        return new LoginFailureHandler();
    }

    @Bean
    public LogoutHandler myLogoutHandler(){
        return new LogoutHandler();
    }

    @Bean
    public ServletListenerRegistrationBean requestContextListener() {
        return new ServletListenerRegistrationBean(new RequestContextListener());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(whitelist));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}