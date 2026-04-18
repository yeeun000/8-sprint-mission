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
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

  private final JwtProperties jwtProperties;
  private final JWSSigner accessTokenSigner;
  private final JWSVerifier accessTokenVerifier;
  private final JWSSigner refreshTokenSigner;
  private final JWSVerifier refreshTokenVerifier;

  public JwtTokenProvider(JwtProperties jwtProperties) throws JOSEException {
    this.jwtProperties = jwtProperties;
    byte[] accessSecretBytes = jwtProperties.accessTokenSecret().getBytes(StandardCharsets.UTF_8);
    this.accessTokenSigner = new MACSigner(accessSecretBytes);
    this.accessTokenVerifier = new MACVerifier(accessSecretBytes);
    byte[] refreshSecretBytes = jwtProperties.refreshTokenSecret().getBytes(StandardCharsets.UTF_8);
    this.refreshTokenSigner = new MACSigner(refreshSecretBytes);
    this.refreshTokenVerifier = new MACVerifier(refreshSecretBytes);
  }

  public String generateAccessToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, jwtProperties.accessTokenExp(), accessTokenSigner,
        "access");
  }

  public String generateRefreshToken(DiscodeitUserDetails discodeitUserDetails)
      throws JOSEException {
    return generateToken(discodeitUserDetails, jwtProperties.refreshTokenExp(), refreshTokenSigner,
        "refresh");
  }

  private String generateToken(DiscodeitUserDetails discodeitUserDetails, int expirationMs,
      JWSSigner signer, String type)
      throws JOSEException {
    String tokenId = UUID.randomUUID().toString();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(discodeitUserDetails.getUser().id().toString())
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

  public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
    return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("Lax")
        .maxAge(jwtProperties.refreshTokenExp() / 1000)
        .build();
  }

  public ResponseCookie generateRefreshTokenExpirationCookie() {
    return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("Lax")
        .maxAge(0)
        .build();
  }

  public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie = generateRefreshTokenCookie(refreshToken);
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void expireRefreshCookie(HttpServletResponse response) {
    ResponseCookie cookie = generateRefreshTokenExpirationCookie();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
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

  public String getUserIdFromToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      return signedJWT.getJWTClaimsSet().getSubject();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }

  public Instant getAccessTokenExpiry(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      if (!signedJWT.verify(accessTokenVerifier)) {
        throw new IllegalArgumentException("Invalid access token");
      }
      Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
      return expiration.toInstant();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }

  public Instant getRefreshTokenExpiry(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      if (!signedJWT.verify(refreshTokenVerifier)) {
        throw new IllegalArgumentException("Invalid refresh token");
      }
      Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
      return expiration.toInstant();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid JWT token", e);
    }
  }

}

