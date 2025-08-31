package comimperialnet.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

    /** Convierte claims de Keycloak -> authorities de Spring:
     *  - scope -> SCOPE_*
     *  - realm_access.roles -> ROLE_*
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthConverter() {
        return jwt -> {
            // scopes (opcional)
            Set<SimpleGrantedAuthority> scopeAuths = Optional.ofNullable(jwt.getClaimAsString("scope"))
                    .map(scopes -> Arrays.stream(scopes.split(" "))
                            .map(s -> "SCOPE_" + s)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet()))
                    .orElseGet(Set::of);

            // roles de realm
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            Collection<GrantedAuthority> roleAuths = Optional.ofNullable(realmAccess)
                    .map(m -> (Collection<String>) m.get("roles"))
                    .orElseGet(List::of)
                    .stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toSet());

            Collection<GrantedAuthority> authorities =
                    Stream.concat(scopeAuths.stream(), roleAuths.stream()).collect(Collectors.toSet());

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // API stateless
                .authorizeExchange(reg -> reg
                        // Rutas públicas (Prometheus debe poder entrar sin token)
                        .pathMatchers("/actuator/health", "/actuator/info", "/actuator/prometheus").permitAll()
                        // Ejemplo de ruta solo ADMIN
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        // Todo lo demás requiere JWT válido
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                );

        return http.build();
    }

}
