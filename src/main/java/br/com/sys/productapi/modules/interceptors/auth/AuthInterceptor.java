package br.com.sys.productapi.modules.interceptors.auth;

import br.com.sys.productapi.config.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.sys.productapi.modules.jwt.service.AuthJwtService;

import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    @Autowired
    private AuthJwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        log.info("1 {}", this.isOptions(request));

        if (this.isOptions(request)) {
            return true;
        }

        log.info("2 {}", request.getHeader(TRANSACTION_ID));

        if (isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidationException("The transactionId header is required");
        }

        log.info("3 {}",  request.getHeader(AUTHORIZATION));

        var authorization = request.getHeader(AUTHORIZATION);
        this.jwtService.validateAuthorization(authorization);

        request.setAttribute("serviceid", UUID.randomUUID().toString());

        return true;
        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private Boolean isOptions(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        return false;
    }

}
