package com.oasis.cac.vas.utils.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EncryptInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(
                @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

            return true;
        }
        @Override
        public void postHandle(
                @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler,
                ModelAndView modelAndView) throws Exception {

//            System.out.println("came");


//            final CopyPrintWriter writer = new CopyPrintWriter(response.getWriter());
//            System.out.println(writer.getCopy());
//            HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);
//            String content = capturingResponseWrapper.getCaptureAsString();
        }

        @Override
        public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull Object handler, Exception exception) throws Exception {}
}

