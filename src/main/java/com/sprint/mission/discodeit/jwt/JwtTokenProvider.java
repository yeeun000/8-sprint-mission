package com.sprint.mission.discodeit.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH-TOKEN";
  private final int accessTokenExpirationMs;
  private final int refreshTokenExpirationMs;

  private final JWSSigner accessTokenSigner;
  private final JWSVerifier accessTokenVerifier;
  private final JWSSigner refreshTokenSigner;
  private final JWSVerifier refreshTokenVerifier;

  public JwtTokenProvider(
      @Value("${discodeit.jwt.access-token-secret}") String accessTokenSecret,
      @Value("${discodeit.jwt.access-token-exp}") int accessTokenExpirationMs,
      @Value("${discodeit.jwt.refresh-token-secret}") String refreshTokenSecret,
      @Value("${discodeit.jwt.refresh-token-exp}") int refreshTokenExpirationMs
  ) throws JOSEException {
    this.accessTokenExpirationMs = accessTokenExpirationMs;
    this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    byte[] accessSecretBytes = accessTokenSecret.getBytes(StandardCharsets.UTF_8);
    this.accessTokenSigner = new MACSigner(accessSecretBytes);
    this.accessTokenVerifier = new MACVerifier(accessSecretBytes);
    byte[] refreshSecretBytes = refreshTokenSecret.getBytes(StandardCharsets.UTF_8);
    this.refreshTokenSigner = new MACSigner(refreshSecretBytes);
    this.refreshTokenVerifier = new MACVerifier(refreshSecretBytes);
  }

  public String generateAccessToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, accessTokenExpirationMs, accessTokenSigner,
        "access");
  }

  public String generateRefreshToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, refreshTokenExpirationMs, refreshTokenSigner,
        "refresh");
  }

  private String generateToken(DiscodeitUserDetails discodeitUserDetails, int expirationMs,
      JWSSigner signer, String type)
      throws JOSEException {
    String tokenId = UUID.randomUUID().toString();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(discodeitUserDetails.getUsername())
        .jwtID(tokenId)
        .claim("userId", discodeitUserDetails.getUser().id())
        .claim("type", type)
        .claim("roles", discodeitUserDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList())
        .issueTime(now)
        .expirationTime(expiryDate)
        .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(signer);
    String compactJWT = signedJWT.serialize();
    return compactJWT;
  }

  public Cookie generateRefreshTokenCookie(String refreshToken) {
    Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(refreshTokenExpirationMs / 1000);
    return cookie;
  }

  public Cookie generateRefreshTokenExpirationCookie() {
    Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }

  public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = generateRefreshTokenCookie(refreshToken);
    response.addCookie(cookie);
  }

  public void expireRefreshCookie(HttpServletResponse response) {
    Cookie cookie = generateRefreshTokenExpirationCookie();
    response.addCookie(cookie);
  }

  public boolean validateAccessToken(String token) {
    boolean result = verifyToken(token, accessTokenVerifier, "access");
    return result;
  }

  public boolean validateRefreshToken(String token) {
    boolean result = verifyToken(token, refreshTokenVerifier, "refresh");
    return result;
  }

  private boolean verifyToken(String token, JWSVerifier verifier, String expectedType) {

    try {
      SignedJWT signedJWT = SignedJWT.parse(token);

      if (!signedJWT.verify(verifier)) {
        return false;
      }

      String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("type");
      if (!expectedType.equals(tokenType)) {
        return false;
      }

      Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
      boolean valid = exp != null && exp.after(new Date());
      return valid;
    } catch (Exception e) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      String subject = signedJWT.getJWTClaimsSet().getSubject();
      return subject;
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }


}

