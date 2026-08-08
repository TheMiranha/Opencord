package com.miranda.opencord.core.infrastructure.controller;

import com.miranda.opencord.core.infrastructure.controller.dto.ErrorResponse;
import com.miranda.opencord.core.infrastructure.controller.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;

@RestControllerAdvice(basePackages = "com.miranda.opencord")
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> type = returnType.getParameterType();

        return !SuccessResponse.class.isAssignableFrom(type) && !ErrorResponse.class.isAssignableFrom(type);
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String requestId = (String) servletRequest.getAttribute("requestId");

        return new SuccessResponse<>(
                Instant.now(),
                requestId,
                body
        );
    }
}
