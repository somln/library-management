package sub.librarymanagement.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import sub.librarymanagement.common.auth.jwt.JWTFilter;
import sub.librarymanagement.common.auth.jwt.JWTProperties;
import sub.librarymanagement.common.auth.jwt.JWTUtil;
import sub.librarymanagement.common.auth.jwt.LoginFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/metrics/**").permitAll()
                .requestMatchers("/api/actuator/**").permitAll()
                .requestMatchers("http://43.200.223.110:8080").permitAll()
                .requestMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
                        "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**",
                        "/webjars/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/books/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/books/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/tags").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tags/*").hasRole("ADMIN")
                .requestMatchers("/login", "/api/join").permitAll()
                .anyRequest().authenticated());
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정 추가
        http.cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.applyPermitDefaultValues();
                    configuration.addAllowedOriginPattern("*");
                    configuration.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"));

                    configuration.setAllowCredentials(true);
                    configuration.addExposedHeader(JWTProperties.HEADER_STRING_TOKEN);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    return configuration;
                }));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers(
                            "/join",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/actuator/**",
                            "/api/actuator/**",
                            "/metrics/**"
                    );
        };
    }


}