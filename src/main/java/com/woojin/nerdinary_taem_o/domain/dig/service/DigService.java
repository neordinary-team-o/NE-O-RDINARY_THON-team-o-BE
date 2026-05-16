package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.common.exception.model.DuplicateResourceException;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.song.entity.Artist;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.song.repository.ArtistRepository;
import com.woojin.nerdinary_taem_o.domain.song.repository.SongRepository;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import com.woojin.nerdinary_taem_o.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DigService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final DigRepository digRepository;

    @Transactional
    public DigCreateResponse create(DigCreateRequest request) {
        validate(request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        Artist artist = artistRepository.findByName(request.artist().trim())
                .orElseGet(() -> artistRepository.save(Artist.create(request.artist().trim(), null)));
        Song song = songRepository.findByTitleAndArtistId(request.title().trim(), artist.getId())
                .orElseGet(() -> songRepository.save(Song.create(
                        request.title().trim(),
                        request.thumbnailUrl(),
                        artist,
                        request.uploadDate(),
                        request.viewCount()
                )));

        if (digRepository.existsByUserIdAndSongId(user.getId(), song.getId())) {
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_DIG);
        }

        Dig dig = Dig.create(
                user,
                song,
                request.viewCount(),
                request.uploadDate(),
                0.0,
                0,
                request.comment(),
                request.videoId()
        );
        Dig saved = digRepository.save(dig);

        return DigCreateResponse.from(saved);
    }

    private void validate(DigCreateRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "요청 값은 필수입니다.");
        }
        if (request.userId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "사용자 ID는 필수입니다.");
        }
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "곡 제목은 필수입니다.");
        }
        if (request.artist() == null || request.artist().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "아티스트 이름은 필수입니다.");
        }
        if (request.viewCount() == null || request.viewCount() < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "조회수는 0 이상이어야 합니다.");
        }
        if (request.comment() != null && request.comment().length() > 100) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "한줄 평가는 100자 이하여야 합니다.");
        }
    }
}
