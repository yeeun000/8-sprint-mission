package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String Header = "Discodeit-Request-ID";
  private static final String REQUEST_ID = "request_id";
  private static final String REQUEST_METHOD = "request_method";
  private static final String REQUEST_URL = "request_url";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String requestId = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, requestId);
    MDC.put(REQUEST_METHOD, request.getMethod());
    MDC.put(REQUEST_URL, request.getRequestURI());
    response.setHeader(Header, requestId);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex)
      throws Exception {
    MDC.clear();
  }
}
