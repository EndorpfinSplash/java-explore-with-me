package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_category.dto.CategoryOutDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryControllerPublic {

    private final EventCategoryService eventCategoryService;


    @GetMapping
    public Collection<CategoryOutDto> getAll(
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to fetch event_categories from={} with size={} received.", from,size);
        return eventCategoryService.getEventCategories(from, size);
    }

    @GetMapping("/{id}")
    public CategoryOutDto getById(
            @PathVariable("id") Integer id
    ) {
        log.info("GET request to fetch event_category_id={} received.", id);
        return eventCategoryService.findById(id);
    }

}
