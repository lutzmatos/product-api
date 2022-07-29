package br.com.sys.productapi.modules.interceptors.auth;

import br.com.sys.productapi.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static br.com.sys.productapi.utils.RequestUtil.getCurrentRequest;

@Component
public class FeignAuthClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    @Override
    public void apply(RequestTemplate template) {

        var currentRequest = getCurrentRequest();
        template
                .header(AUTHORIZATION, getCurrentRequest().getHeader(AUTHORIZATION))
                .header(TRANSACTION_ID, getCurrentRequest().getHeader(TRANSACTION_ID));
    }


}
