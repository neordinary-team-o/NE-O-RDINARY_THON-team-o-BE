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
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigSnapshotRepository;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.song.repository.SongRepository;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import com.woojin.nerdinary_taem_o.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    @Mock
    private DigSnapshotRepository digSnapshotRepository;

    @InjectMocks
    private DigService digService;

    @Test
    void createStoresSongDigAndInitialSnapshot() {
        User user = User.builder().nickname("user").password("1234").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findByNameAndPlatform("한로로", Platform.YOUTUBE)).thenReturn(Optional.empty());
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(songRepository.findByExternalUrlAndPlatform(
                "https://www.youtube.com/watch?v=video1",
                Platform.YOUTUBE
        )).thenReturn(Optional.empty());
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(digRepository.existsByUserAndSong(any(User.class), any(Song.class))).thenReturn(false);
        when(digRepository.save(any(Dig.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(digSnapshotRepository.save(any(DigSnapshot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateDigRequest request = new CreateDigRequest(
                1L,
                "video1",
                "0+0",
                "한로로",
                14205L,
                LocalDate.of(2024, 3, 15),
                "https://i.ytimg.com/vi/video1/hqdefault.jpg",
                "좋은 곡입니다"
        );

        CreateDigResponse response = digService.create(request);

        assertThat(response.title()).isEqualTo("0+0");
        assertThat(response.artist()).isEqualTo("한로로");
        assertThat(response.viewsAtDig()).isEqualTo(14205L);
        assertThat(response.uploadDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        assertThat(response.comment()).isEqualTo("좋은 곡입니다");

        ArgumentCaptor<DigSnapshot> snapshotCaptor = ArgumentCaptor.forClass(DigSnapshot.class);
        verify(digSnapshotRepository).save(snapshotCaptor.capture());
        assertThat(snapshotCaptor.getValue().getCurrentViews()).isEqualTo(14205L);

        ArgumentCaptor<Song> songCaptor = ArgumentCaptor.forClass(Song.class);
        verify(songRepository).save(songCaptor.capture());
        assertThat(songCaptor.getValue().getThumbnailUrl())
                .isEqualTo("https://i.ytimg.com/vi/video1/hqdefault.jpg");
    }

    @Test
    void createRejectsDuplicateDig() {
        User user = User.builder().nickname("user").password("1234").build();
        Artist artist = Artist.builder().name("한로로").platform(Platform.YOUTUBE).build();
        Song song = Song.builder()
                .artist(artist)
                .title("0+0")
                .externalUrl("https://www.youtube.com/watch?v=video1")
                .platform(Platform.YOUTUBE)
                .uploadDate(LocalDate.of(2024, 3, 15))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findByNameAndPlatform("한로로", Platform.YOUTUBE)).thenReturn(Optional.of(artist));
        when(songRepository.findByExternalUrlAndPlatform(
                "https://www.youtube.com/watch?v=video1",
                Platform.YOUTUBE
        )).thenReturn(Optional.of(song));
        when(digRepository.existsByUserAndSong(user, song)).thenReturn(true);

        CreateDigRequest request = new CreateDigRequest(
                1L,
                "video1",
                "0+0",
                "한로로",
                14205L,
                LocalDate.of(2024, 3, 15),
                "https://i.ytimg.com/vi/video1/hqdefault.jpg",
                "좋은 곡입니다"
        );

        assertThatThrownBy(() -> digService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_DIG);
        verify(digRepository, never()).save(any(Dig.class));
    }

    @Test
    void createRejectsUnknownUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        CreateDigRequest request = new CreateDigRequest(
                999L,
                "video1",
                "0+0",
                "한로로",
                14205L,
                LocalDate.of(2024, 3, 15),
                "https://i.ytimg.com/vi/video1/hqdefault.jpg",
                "좋은 곡입니다"
        );

        assertThatThrownBy(() -> digService.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    void createRejectsTooLongComment() {
        String comment = "a".repeat(101);
        CreateDigRequest request = new CreateDigRequest(
                1L,
                "video1",
                "0+0",
                "한로로",
                14205L,
                LocalDate.of(2024, 3, 15),
                "https://i.ytimg.com/vi/video1/hqdefault.jpg",
                comment
        );

        assertThatThrownBy(() -> digService.create(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }
}
