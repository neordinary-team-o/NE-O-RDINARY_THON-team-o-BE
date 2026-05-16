package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCardDto;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DigSearchService {

    private static final int HOME_PAGE_SIZE = 6;

    private final DigRepository digRepository;

    public PageResponse<DigCardDto> getMyDigs(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, HOME_PAGE_SIZE);
        Page<Dig> digPage = digRepository.findMyDigsWithSong(userId, pageable);

        List<DigCardDto> content = digPage.getContent()
                .stream()
                .map(DigCardDto::from)
                .toList();

        return PageResponse.of(digPage, content);
    }
}
