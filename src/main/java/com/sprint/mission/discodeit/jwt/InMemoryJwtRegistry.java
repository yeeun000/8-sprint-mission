package com.sprint.mission.discodeit.jwt;

import java.util.Map;
import java.util.Queue;
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

  private final int maxActiveJwtCount = 1;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void registerJwtInformation(JwtInformation jwtInformation) {
    UUID userId = jwtInformation.getUserDto().id();

    Queue<JwtInformation> token = origin.computeIfAbsent(userId,
        k -> new ConcurrentLinkedQueue<>());
    token.offer(jwtInformation);
    while (token.size() > maxActiveJwtCount) {
      token.poll();
    }
  }

  @Override
  public void invalidateJwtInformationByUserId(UUID userId) {
    origin.remove(userId);
  }

  @Override
  public boolean hasActiveJwtInformationByUserId(UUID userId) {
    Queue<JwtInformation> token = origin.get(userId);
    return token != null && !token.isEmpty();
  }

  @Override
  public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
    return origin.values().stream()
        .flatMap(Queue::stream)
        .anyMatch(jwtInformation -> jwtInformation.getAccessToken().equals(accessToken));
  }

  @Override
  public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
    return origin.values().stream()
        .flatMap(Queue::stream)
        .anyMatch(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken));
  }

  @Override
  public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
    UUID userId = newJwtInformation.getUserDto().id();
    Queue<JwtInformation> token = origin.get(userId);

    if(token != null) {
      token.removeIf(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken));
      token.offer(newJwtInformation);
      while (token.size() > maxActiveJwtCount) {
        token.poll();
      }
    }
    else{
      registerJwtInformation(newJwtInformation);
    }
  }

  @Scheduled(fixedDelay = 1000 * 60 * 5)
  @Override
  public void clearExpiredJwtInformation() {
    for (Queue<JwtInformation> token : origin.values()) {
      token.removeIf(jwtInformation -> !jwtTokenProvider.validateRefreshToken(jwtInformation.getRefreshToken()));
    }
    origin.entrySet().removeIf(entry -> entry.getValue().isEmpty());

  }

}
