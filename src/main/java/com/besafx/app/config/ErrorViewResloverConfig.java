package com.besafx.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@PropertySource("classpath:httpErrorCodes.properties")
public class ErrorViewResloverConfig implements ErrorViewResolver {

    @Autowired
    private Environment env;

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest httpServletRequest, HttpStatus httpStatus, Map<String, Object> map) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", generateErrorMessage((int) httpServletRequest.getAttribute("javax.servlet.error.status_code")));
        modelAndView.addObject("errorCode", httpServletRequest.getAttribute("javax.servlet.error.status_code"));
        return modelAndView;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            // route all errors towards /error .
            final ErrorPage errorPage = new ErrorPage("/error");
            container.addErrorPages(errorPage);
        });
    }

    public String generateErrorMessage(final int error_code) {
        String message = "";
        switch (error_code) {
            case 400:
                message = env.getProperty("400");
                break;
            case 401:
                message = env.getProperty("401");
                break;
            case 403:
                message = env.getProperty("403");
                break;
            case 404:
                message = env.getProperty("404");
                break;
            case 500:
                message = env.getProperty("500");
                break;
        }
        return message;
    }

}
