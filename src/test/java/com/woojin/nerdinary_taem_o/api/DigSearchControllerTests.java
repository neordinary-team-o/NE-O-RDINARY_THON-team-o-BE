package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DigSearchControllerTests {

    @Mock
    private DigSearchService digSearchService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcTestSupport.mockMvc(new DigSearchController(digSearchService));
    }

    @Test
    void getDigDetailReturnsNotFoundWhenDigDoesNotExist() throws Exception {
        when(digSearchService.getDigDetail(99L))
                .thenThrow(new EntityNotFoundException(ErrorCode.DIG_NOT_FOUND));

        mockMvc.perform(get("/api/digs/{digId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("DIG_NOT_FOUND"));
    }

    @Test
    void getMyDigsRejectsMissingUserId() throws Exception {
        mockMvc.perform(get("/api/digs"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void getDigDetailRejectsInvalidDigIdType() throws Exception {
        mockMvc.perform(get("/api/digs/not-number"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }
}
