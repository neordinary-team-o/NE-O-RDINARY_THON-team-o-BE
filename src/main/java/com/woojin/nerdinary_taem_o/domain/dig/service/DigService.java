package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.enums.Platform;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.common.exception.model.DuplicateResourceException;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
import com.woojin.nerdinary_taem_o.domain.artist.entity.Artist;
import com.woojin.nerdinary_taem_o.domain.artist.repository.ArtistRepository;
import com.woojin.nerdinary_taem_o.domain.dig.dto.CreateDigRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.CreateDigResponse;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.entity.DigSnapshot;
import com.woojin.nerdinary_taem_o.domain.dig.enums.RarityBadge;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigSnapshotRepository;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.song.repository.SongRepository;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import com.woojin.nerdinary_taem_o.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DigService {

    private static final int COMMENT_MAX_LENGTH = 100;
    private static final String YOUTUBE_WATCH_URL = "https://www.youtube.com/watch?v=";

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final DigRepository digRepository;
    private final DigSnapshotRepository digSnapshotRepository;

    @Transactional
    public CreateDigResponse create(CreateDigRequest request) {
        validate(request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        Artist artist = findOrCreateArtist(request.artist().trim());
        Song song = findOrCreateSong(request, artist);

        if (digRepository.existsByUserAndSong(user, song)) {
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_DIG);
        }

        LocalDateTime now = LocalDateTime.now();
        Dig dig = Dig.builder()
                .user(user)
                .song(song)
                .digDate(now)
                .viewsAtDig(request.viewCount())
                .daysSinceUpload(daysSinceUpload(request.uploadDate()))
                .growthRateAtDig(0.0)
                .digScore(0)
                .rarityBadge(RarityBadge.SURFACE)
                .comment(request.comment().trim())
                .build();
        Dig savedDig = digRepository.save(dig);

        digSnapshotRepository.save(DigSnapshot.builder()
                .dig(savedDig)
                .snapshotDate(now)
                .currentViews(request.viewCount())
                .growthRate(0.0)
                .build());

        return new CreateDigResponse(
                savedDig.getId(),
                song.getId(),
                song.getTitle(),
                artist.getName(),
                savedDig.getViewsAtDig(),
                song.getUploadDate(),
                savedDig.getDigDate(),
                savedDig.getComment()
        );
    }

    private Artist findOrCreateArtist(String artistName) {
        return artistRepository.findByNameAndPlatform(artistName, Platform.YOUTUBE)
                .orElseGet(() -> artistRepository.save(Artist.builder()
                        .name(artistName)
                        .platform(Platform.YOUTUBE)
                        .build()));
    }

    private Song findOrCreateSong(CreateDigRequest request, Artist artist) {
        String externalUrl = youtubeUrl(request.videoId().trim());
        return songRepository.findByExternalUrlAndPlatform(externalUrl, Platform.YOUTUBE)
                .orElseGet(() -> songRepository.save(Song.builder()
                .artist(artist)
                .title(request.title().trim())
                .externalUrl(externalUrl)
                .thumbnailUrl(normalizeOptional(request.thumbnailUrl()))
                .platform(Platform.YOUTUBE)
                .uploadDate(request.uploadDate())
                .build()));
    }

    private int daysSinceUpload(LocalDate uploadDate) {
        long days = ChronoUnit.DAYS.between(uploadDate, LocalDate.now());
        return (int) Math.max(days, 0);
    }

    private String youtubeUrl(String videoId) {
        return YOUTUBE_WATCH_URL + videoId;
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void validate(CreateDigRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "요청 본문을 입력해주세요");
        }
        if (request.userId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "userId를 입력해주세요");
        }
        if (isBlank(request.videoId())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "videoId를 입력해주세요");
        }
        if (isBlank(request.title())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "곡 제목을 입력해주세요");
        }
        if (isBlank(request.artist())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "아티스트를 입력해주세요");
        }
        if (request.viewCount() == null || request.viewCount() < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "현재 조회수를 올바르게 입력해주세요");
        }
        if (request.uploadDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "업로드 날짜를 입력해주세요");
        }
        if (isBlank(request.comment())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "한줄평을 입력해주세요");
        }
        if (request.comment().trim().length() > COMMENT_MAX_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "한줄평은 100자 이하로 입력해주세요");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
