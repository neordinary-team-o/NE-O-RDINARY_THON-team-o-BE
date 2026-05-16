package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigRefreshService;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DigControllerTests {

    @Mock
    private DigService digService;

    @Mock
    private DigRefreshService digRefreshService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DigController(digService, digRefreshService)).build();
    }

    @Test
    void createReturnsCreatedWithApiResponse() throws Exception {
        when(digService.create(any())).thenReturn(new DigCreateResponse(
                1L,
                2L,
                "마지막처럼",
                "BLACKPINK",
                "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                123456789L,
                123456789L,
                0.0,
                null,
                LocalDateTime.of(2026, 5, 17, 3, 20)
        ));

        mockMvc.perform(post("/api/digs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "videoId": "Amq-qlqbjYA",
                                  "title": "마지막처럼",
                                  "artist": "BLACKPINK",
                                  "viewCount": 123456789,
                                  "uploadDate": "2017-06-22",
                                  "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                                  "comment": "초기에 발견한 곡"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.digId").value(1))
                .andExpect(jsonPath("$.data.songId").value(2))
                .andExpect(jsonPath("$.data.title").value("마지막처럼"))
                .andExpect(jsonPath("$.data.artist").value("BLACKPINK"))
                .andExpect(jsonPath("$.data.snapshotViewCount").value(123456789))
                .andExpect(jsonPath("$.data.currentViewCount").value(123456789))
                .andExpect(jsonPath("$.data.growthRate").value(0.0))
                .andExpect(jsonPath("$.data.achievementBadge").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());
    }
}
