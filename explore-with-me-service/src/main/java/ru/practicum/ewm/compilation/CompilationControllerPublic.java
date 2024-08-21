package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @RequestParam(value = "from", defaultValue = "0") final Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") final Integer size) {
        log.info("GET request to fetch compilations received.");
        List<CompilationDto> compilations = compilationService.getCompilations(pinned, from, size);
        log.info("{} compilations fetched.", compilations.size());
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable(name = "compId") Long compId) {
        log.info("GET request to fetch compilationId={} received.", compId);
        CompilationDto compilation = compilationService.getCompilation(compId);
        log.info("{} compilation fetched.", compilation);
        return compilation;
    }
}
