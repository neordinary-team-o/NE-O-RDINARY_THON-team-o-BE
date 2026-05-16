package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigRefreshService;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
        mockMvc = MockMvcTestSupport.mockMvc(new DigController(digService, digRefreshService));
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

    @Test
    void createRejectsMissingRequiredFields() throws Exception {
        mockMvc.perform(post("/api/digs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "artist": "",
                                  "viewCount": -1,
                                  "comment": "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void createRejectsInvalidUploadDateFormat() throws Exception {
        mockMvc.perform(post("/api/digs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "videoId": "Amq-qlqbjYA",
                                  "title": "마지막처럼",
                                  "artist": "BLACKPINK",
                                  "viewCount": 123456789,
                                  "uploadDate": "2017/06/22"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void refreshGrowthRateReturnsNotFoundWhenDigDoesNotExist() throws Exception {
        doThrow(new EntityNotFoundException(ErrorCode.DIG_NOT_FOUND))
                .when(digRefreshService).refreshGrowthRate(99L);

        mockMvc.perform(patch("/api/digs/{digId}/growth-rate", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("DIG_NOT_FOUND"));
    }
}
