package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_category.dto.CategoryDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryControllerPublic {

    private final EventCategoryService eventCategoryService;


    @GetMapping
    public Collection<CategoryDto> getAll(
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to fetch event_categories from={} with size={} received.", from,size);
        Collection<CategoryDto> eventCategories = eventCategoryService.getEventCategories(from, size);
        log.info("{} events were fetched.", eventCategories.size());
        return eventCategories;
    }

    @GetMapping("/{id}")
    public CategoryDto getById(
            @PathVariable("id") Integer id
    ) {
        log.info("GET request to fetch event_category_id={} received.", id);
        CategoryDto categoryById = eventCategoryService.findById(id);
        log.info("{} event_category was found.", categoryById);
        return categoryById;
    }

}
