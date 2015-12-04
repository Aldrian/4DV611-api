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
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Request-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        httpServletResponse.addHeader("Access-Control-Request-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        httpServletResponse.addHeader("Access-Control-Max-Age", "3600");
        logger.info("Request " + httpServletRequest.getMethod() + " handled with resonse headers " + httpServletResponse.getHeaderNames());
    }

}
