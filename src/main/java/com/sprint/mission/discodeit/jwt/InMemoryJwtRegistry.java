package com.sprint.mission.discodeit.jwt;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry {

  private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
  private final Set<String> accessTokenIndex = ConcurrentHashMap.newKeySet();
  private final Set<String> refreshTokenIndex = ConcurrentHashMap.newKeySet();

  private final int maxActiveJwtCount = 1;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void registerJwtInformation(JwtInformation jwtInformation) {
    UUID userId = jwtInformation.userDto().id();

    origin.compute(userId, (key, queue) -> {
      if (queue == null) {
        queue = new ConcurrentLinkedQueue<>();
      }
      if (queue.size() >= maxActiveJwtCount) {
        JwtInformation deprecatedJwtInformation = queue.poll();
        if (deprecatedJwtInformation != null) {
          removeTokenIndex(
              deprecatedJwtInformation.accessToken(),
              deprecatedJwtInformation.refreshToken()
          );
        }
      }
      queue.add(jwtInformation);
      addTokenIndex(
          jwtInformation.accessToken(),
          jwtInformation.refreshToken()
      );
      return queue;
    });
  }

  @Override
  public void invalidateJwtInformationByUserId(UUID userId) {
    origin.computeIfPresent(userId, (key, queue) -> {
      queue.forEach(jwtInformation -> {
        removeTokenIndex(
            jwtInformation.accessToken(),
            jwtInformation.refreshToken()
        );
      });
      queue.clear();
      return null;
    });
  }

  @Override
  public boolean hasActiveJwtInformationByUserId(UUID userId) {
    Queue<JwtInformation> queue = origin.get(userId);
    if (queue == null) return false;
    return queue.stream()
        .anyMatch(info -> !info.isRefreshTokenExpired());
  }

  @Override
  public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
    return accessTokenIndex.contains(accessToken);
  }

  @Override
  public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
    return refreshTokenIndex.contains(refreshToken);
  }

  @Override
  public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
    UUID userId = newJwtInformation.userDto().id();
    origin.computeIfPresent(userId, (key, queue) -> {
      queue.stream().filter(jwtInformation -> jwtInformation.refreshToken().equals(refreshToken))
          .findFirst()
          .ifPresent(jwtInformation -> {
            removeTokenIndex(jwtInformation.accessToken(), jwtInformation.refreshToken());
            queue.remove(jwtInformation);
            JwtInformation newJwt = jwtInformation.rotate(
                newJwtInformation.accessToken(),
                newJwtInformation.refreshToken()
            );
            queue.add(newJwt);
            addTokenIndex(
                newJwt.accessToken(),
                newJwt.refreshToken()
            );
          });
      return queue;
    });
  }

  @Scheduled(fixedDelay = 1000 * 60 * 5)
  @Override
  public void clearExpiredJwtInformation() {
    origin.entrySet().removeIf(entry -> {
      Queue<JwtInformation> queue = entry.getValue();
      queue.removeIf(jwtInformation -> {
        boolean isExpired =
            jwtInformation.isAccessTokenExpired()
                || jwtInformation.isRefreshTokenExpired();
        if (isExpired) {
          removeTokenIndex(
              jwtInformation.accessToken(),
              jwtInformation.refreshToken()
          );
        }
        return isExpired;
      });
      return queue.isEmpty();
    });

  }

  private void addTokenIndex(String accessToken, String refreshToken) {
    accessTokenIndex.add(accessToken);
    refreshTokenIndex.add(refreshToken);
  }

  private void removeTokenIndex(String accessToken, String refreshToken) {
    accessTokenIndex.remove(accessToken);
    refreshTokenIndex.remove(refreshToken);
  }


}
