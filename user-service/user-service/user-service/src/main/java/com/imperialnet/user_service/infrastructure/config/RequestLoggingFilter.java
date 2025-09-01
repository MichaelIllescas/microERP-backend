package com.imperialnet.user_service.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString();

        // Guardamos traceId en el MDC
        MDC.put(TRACE_ID, traceId);

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // Obtener usuario autenticado
        String user = "anonymous";
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            user = jwt.getClaimAsString("preferred_username");
        }

        log.info("➡️ Request: method={} uri={} user={} traceId={}", method, uri, user, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            log.info("⬅️ Response: status={} duration={}ms uri={} user={} traceId={}",
                    status, duration, uri, user, traceId);

            // Muy importante limpiar el MDC
            MDC.clear();
        }
    }
}
