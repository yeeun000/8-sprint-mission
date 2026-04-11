package com.sprint.mission.discodeit.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
  private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
  private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
    /*
     * 응답 바디에 CSRF 토큰을 넣을 때는 BREACH 공격을 막기 위해 항상 XOR 방식을 사용해라
     */
    this.xor.handle(request, response, csrfToken);
    /*
     * 지연 생성된 토큰을 실제로 만들어서 쿠키에 담기도록 한다
     */
    csrfToken.get();
  }

  @Override
  public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
    String headerValue = request.getHeader(csrfToken.getHeaderName());
    /*
     * 요청에 헤더가 있으면 plain 방식으로 처리해라
     * SPA에서 쿠키에 있는 토큰을 꺼내서 헤더에 넣은 경우에 해당한다
     *
     * 그 외 모든 경우에는 XOR 방식 사용
     * 서버 렌더링 폼에서 hidden input으로 _csrf를 보낼 때 해당한다
     */
    return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
  }
}
