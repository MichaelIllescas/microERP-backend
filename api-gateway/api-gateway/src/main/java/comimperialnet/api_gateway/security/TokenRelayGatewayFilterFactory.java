package comimperialnet.api_gateway.security;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // toma el header Authorization del request entrante
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null) {
                // lo copia al request que sigue al microservicio
                return chain.filter(
                        exchange.mutate()
                                .request(r -> r.headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, token)))
                                .build()
                );
            }

            return chain.filter(exchange);
        };
    }
}
