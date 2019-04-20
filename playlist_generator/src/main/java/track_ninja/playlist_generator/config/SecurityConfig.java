package track_ninja.playlist_generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import track_ninja.playlist_generator.security.FirstLoginInterceptor;
import track_ninja.playlist_generator.security.models.JwtAuthenticationFilter;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Resource(name = "UserServiceImpl")
    private UserDetailsService userDetailsService;

    // Config security datasource from AppConfig
    @Autowired
    private DataSource securityDataSource;

    // Config password encoder (bcrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FirstLoginInterceptor firstLoginInterceptor2(){
        return new FirstLoginInterceptor();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Config how to check username, password, whether user is enabled (1) and username's role (in db role should be kept as "ROLE_USER", "ROLE_ADMIN", etc. for this to work properly)
    // If enabled = 0 => can't pass login
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(securityDataSource)
                .usersByUsernameQuery(
                        "select username,password, enabled  from users where username=?")
                .authoritiesByUsernameQuery(
                        "select\n" +
                                "\n" +
                                "a.username,\n" +
                                "b.role_type as role\n" +
                                "\n" +
                                "from users as a\n" +
                                "join authorities as b\n" +
                                "on a.authority_id = b.authority_id where a.username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/api").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/filter/**").permitAll()
                .antMatchers("/api/user/**").hasRole("USER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                /*.loginPage("/login")
                .loginProcessingUrl("/authenticateUser")
                *//*.defaultSuccessUrl("/home")*/
                .and()
                .logout()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied").and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        http.cors().and().csrf().disable().httpBasic(); // added only for the purposes of testing with Postman (remove afterwards); stops more complicated authentication processes, which use tokens

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("Access-Control-Allow-Origin");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-type");
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(firstLoginInterceptor2()).addPathPatterns("/api/admin/**");

    }
}
