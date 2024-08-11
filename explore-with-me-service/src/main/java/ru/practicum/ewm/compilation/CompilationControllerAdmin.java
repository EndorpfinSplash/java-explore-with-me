package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("POST request {} to create compilations received.", newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("DELETE request  to delete compilation_id = {} received.", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                  @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("UPDATE request for compilation_id = {} received.", compId);
        return compilationService.update(compId, updateCompilationRequest);
    }
}
