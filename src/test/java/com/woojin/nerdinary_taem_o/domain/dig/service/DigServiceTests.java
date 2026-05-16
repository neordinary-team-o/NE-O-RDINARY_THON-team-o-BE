package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private DigRepository digRepository;

    @InjectMocks
    private DigService digService;

    @Test
    void createSavesNewArtistSongAndDig() {
        User user = user(1L);
        Artist savedArtist = artist(2L, "BLACKPINK");
        Song savedSong = song(3L, "마지막처럼", savedArtist, 123456789L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findByName("BLACKPINK")).thenReturn(Optional.empty());
        when(artistRepository.save(any(Artist.class))).thenReturn(savedArtist);
        when(songRepository.findByTitleAndArtistId("마지막처럼", 2L)).thenReturn(Optional.empty());
        when(songRepository.save(any(Song.class))).thenReturn(savedSong);
        when(digRepository.existsByUserIdAndSongId(1L, 3L)).thenReturn(false);
        when(digRepository.save(any(Dig.class))).thenAnswer(invocation -> savedDig(invocation.getArgument(0)));

        DigCreateResponse response = digService.create(defaultRequest());

        assertThat(response.digId()).isEqualTo(10L);
        assertThat(response.songId()).isEqualTo(3L);
        assertThat(response.title()).isEqualTo("마지막처럼");
        assertThat(response.artist()).isEqualTo("BLACKPINK");
        assertThat(response.snapshotViewCount()).isEqualTo(123456789L);
        assertThat(response.currentViewCount()).isEqualTo(123456789L);
        assertThat(response.growthRate()).isEqualTo(0.0);
        assertThat(response.achievementBadge()).isNull();
        assertThat(response.dugAt()).isEqualTo(LocalDateTime.of(2026, 5, 17, 3, 20));
    }

    @Test
    void createReusesExistingArtistAndSong() {
        User user = user(1L);
        Artist artist = artist(2L, "BLACKPINK");
        Song song = song(3L, "마지막처럼", artist, 123456789L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findByName("BLACKPINK")).thenReturn(Optional.of(artist));
        when(songRepository.findByTitleAndArtistId("마지막처럼", 2L)).thenReturn(Optional.of(song));
        when(digRepository.existsByUserIdAndSongId(1L, 3L)).thenReturn(false);
        when(digRepository.save(any(Dig.class))).thenAnswer(invocation -> savedDig(invocation.getArgument(0)));

        DigCreateResponse response = digService.create(defaultRequest());

        assertThat(response.songId()).isEqualTo(3L);
        verify(artistRepository, never()).save(any(Artist.class));
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    void createRejectsUnknownUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> digService.create(defaultRequest()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);

        verifyNoInteractions(artistRepository, songRepository, digRepository);
    }

    @Test
    void createRejectsDuplicateDig() {
        User user = user(1L);
        Artist artist = artist(2L, "BLACKPINK");
        Song song = song(3L, "마지막처럼", artist, 123456789L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findByName("BLACKPINK")).thenReturn(Optional.of(artist));
        when(songRepository.findByTitleAndArtistId("마지막처럼", 2L)).thenReturn(Optional.of(song));
        when(digRepository.existsByUserIdAndSongId(1L, 3L)).thenReturn(true);

        assertThatThrownBy(() -> digService.create(defaultRequest()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_DIG);

        verify(digRepository, never()).save(any(Dig.class));
    }

    @Test
    void createRejectsNegativeViewCount() {
        DigCreateRequest request = new DigCreateRequest(
                1L,
                "Amq-qlqbjYA",
                "마지막처럼",
                "BLACKPINK",
                -1L,
                LocalDate.of(2017, 6, 22),
                "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                "초기에 발견한 곡"
        );

        assertThatThrownBy(() -> digService.create(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);

        verifyNoInteractions(userRepository, artistRepository, songRepository, digRepository);
    }

    @Test
    void createRejectsTooLongComment() {
        DigCreateRequest request = new DigCreateRequest(
                1L,
                "Amq-qlqbjYA",
                "마지막처럼",
                "BLACKPINK",
                123456789L,
                LocalDate.of(2017, 6, 22),
                "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                "a".repeat(101)
        );

        assertThatThrownBy(() -> digService.create(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);

        verifyNoInteractions(userRepository, artistRepository, songRepository, digRepository);
    }

    private DigCreateRequest defaultRequest() {
        return new DigCreateRequest(
                1L,
                "Amq-qlqbjYA",
                "마지막처럼",
                "BLACKPINK",
                123456789L,
                LocalDate.of(2017, 6, 22),
                "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                "초기에 발견한 곡"
        );
    }

    private User user(Long id) {
        User user = User.create("hongdae", "1234");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Artist artist(Long id, String name) {
        Artist artist = Artist.create(name, null);
        ReflectionTestUtils.setField(artist, "id", id);
        return artist;
    }

    private Song song(Long id, String title, Artist artist, Long viewCount) {
        Song song = Song.create(
                title,
                "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                artist,
                LocalDate.of(2017, 6, 22),
                viewCount
        );
        ReflectionTestUtils.setField(song, "id", id);
        return song;
    }

    private Dig savedDig(Dig dig) {
        ReflectionTestUtils.setField(dig, "id", 10L);
        ReflectionTestUtils.setField(dig, "dugAt", LocalDateTime.of(2026, 5, 17, 3, 20));
        return dig;
    }
}
