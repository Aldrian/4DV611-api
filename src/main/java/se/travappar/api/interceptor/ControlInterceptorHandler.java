package se.travappar.api.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControlInterceptorHandler extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(ControlInterceptorHandler.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("Handling request " + httpServletRequest.getMethod() + " with headers " + httpServletRequest.getHeaderNames());
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Request-Methods", "OPTIONS, PUT, GET, DELETE, POST");
        logger.info("Request " + httpServletRequest.getMethod() + " handled with resonse headers " + httpServletResponse.getHeaderNames());
    }

}
