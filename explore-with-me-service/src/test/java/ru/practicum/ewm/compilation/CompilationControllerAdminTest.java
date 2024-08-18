package ru.practicum.ewm.compilation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CompilationControllerAdminTest {

    @InjectMocks
    CompilationControllerAdmin compilationControllerAdmin;

    @Mock
    private CompilationService compilationService;

    private MockMvc mockMvc;
    private NewCompilationDto newCompilationDto;
    private CompilationDto compilationDto;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(compilationControllerAdmin).build();
        newCompilationDto = NewCompilationDto.builder()
                .title("test title")
                .pinned(true)
                .build();
        compilationDto = CompilationDto.builder()
                .id(1L)
                .title("test title")
                .events(Collections.emptyList())
                .pinned(true)
                .build();
    }

    @Test
    void createCompilation() throws Exception {
        when(compilationService.createCompilation(any()))
                .thenReturn(compilationDto);
        mockMvc.perform(
                        post("/admin/compilations")
                                .content(objectMapper.writeValueAsString(newCompilationDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(compilationDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(compilationDto.getTitle()), String.class))
                .andExpect(jsonPath("$.events", is(compilationDto.getEvents()), List.class)
                );
        verify(compilationService, times(1)).createCompilation(newCompilationDto);
    }

    @Test
    void deleteCompilation() throws Exception {
        mockMvc.perform(delete("/admin/compilations/{compId}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
        verify(compilationService, times(1)).delete(anyLong());
    }

    @Test
    void updateCompilation() throws Exception {
        UpdateCompilationRequest updateCompilationRequest = UpdateCompilationRequest.builder()
                .title("updated test title")
                .pinned(false)
                .build();
        when(compilationService.update(anyLong(), any(UpdateCompilationRequest.class)))
                .thenReturn(compilationDto);

        mockMvc.perform(patch("/admin/compilations/{compId}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(updateCompilationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(compilationDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(compilationDto.getTitle()), String.class))
                .andExpect(jsonPath("$.events", is(compilationDto.getEvents()), List.class))
                .andExpect(jsonPath("$.pinned", is(compilationDto.isPinned())));

        verify(compilationService, times(1)).update(1L, updateCompilationRequest);
    }
}