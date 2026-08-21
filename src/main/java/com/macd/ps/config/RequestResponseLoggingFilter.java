package com.macd.ps.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final String REDACTED = "[REDACTED]";
    private static final Pattern SENSITIVE_KEY_PATTERN = Pattern.compile(
            "(?i)(\\\"(?:password|secret|token|authorization|api[-_]?key|cookie|set-cookie|cardNumber|cardHolderName|expiryDate|cvv|accessToken|refreshToken)\\\"\\s*:\\s*\\\")([^\\\"]*)(\\\")",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestAndResponse(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper request,
                                       ContentCachingResponseWrapper response,
                                       long duration) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        String requestHeaders = Collections.list(request.getHeaderNames())
                .stream()
                .map(name -> name + "=" + sanitizeValue(name, request.getHeader(name)))
                .collect(Collectors.joining(", ", "{", "}"));

        String requestBody = sanitizePayload(new String(request.getContentAsByteArray(), StandardCharsets.UTF_8));
        String responseBody = sanitizePayload(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        String logMessage = "HTTP {} {}{} | status={} | duration={}ms | headers={} | requestBody={} | responseBody={}";
        Object[] logArguments = {
            method,
            uri,
            queryString == null ? "" : "?" + queryString,
            response.getStatus(),
            duration,
            requestHeaders,
            requestBody.isBlank() ? "<empty>" : requestBody,
            responseBody.isBlank() ? "<empty>" : responseBody
        };

        if (response.getStatus() >= HttpServletResponse.SC_BAD_REQUEST) {
            log.error(
                logMessage,
                logArguments
            );
            return;
        }

        log.info(
            logMessage,
            logArguments
        );
    }

    private String sanitizePayload(String payload) {
        if (payload == null || payload.isBlank()) {
            return "";
        }

        String sanitized = SENSITIVE_KEY_PATTERN.matcher(payload).replaceAll(
                m -> m.group(1) + REDACTED + m.group(3)
        );

        sanitized = sanitized.replaceAll("(?i)(authorization\\s*:\\s*)([^,\\s]+)", "$1" + REDACTED);
        sanitized = sanitized.replaceAll("(?i)(cookie\\s*:\\s*)([^,\\s]+)", "$1" + REDACTED);
        sanitized = sanitized.replaceAll("(?i)(password\\s*[:=]\\s*)([^,\\s]+)", "$1" + REDACTED);
        sanitized = sanitized.replaceAll("(?i)(secret\\s*[:=]\\s*)([^,\\s]+)", "$1" + REDACTED);
        sanitized = sanitized.replaceAll("(?i)(token\\s*[:=]\\s*)([^,\\s]+)", "$1" + REDACTED);

        return sanitized;
    }

    private String sanitizeValue(String headerName, String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String lower = headerName.toLowerCase(Locale.ROOT);
        if (lower.contains("authorization")
                || lower.contains("cookie")
                || lower.contains("set-cookie")
                || lower.contains("token")
                || lower.contains("secret")
                || lower.contains("key")) {
            return REDACTED;
        }

        return value;
    }
}
