package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

  public static <E, D> PageResponse<D> fromPage(
      Page<E> page,
      Function<E, D> mapper
  ) {
    List<D> content = page.getContent()
        .stream()
        .map(mapper)
        .toList();

    return new PageResponse<>(
        content,
        page.getNumber(),
        page.getSize(),
        page.getTotalElements()
    );
  }

  public static <E, D> PageResponse<D> fromSlice(
      Slice<E> slice,
      Function<E, D> mapper
  ) {
    List<D> content = slice.getContent()
        .stream()
        .map(mapper)
        .toList();

    return new PageResponse<>(
        content,
        slice.getNumber(),
        slice.getSize(),
        null
    );
  }
}
