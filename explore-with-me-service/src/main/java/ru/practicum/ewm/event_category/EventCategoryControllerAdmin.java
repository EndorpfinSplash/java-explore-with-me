package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.NewCategoryDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryControllerAdmin {

    private final EventCategoryService eventCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid final NewCategoryDto newCategoryDto) {
        log.info("POST request to create new category: {} was received", newCategoryDto);
        CategoryDto eventCategory = eventCategoryService.createEventCategory(newCategoryDto);
        log.info("{} was created", eventCategory);
        return eventCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("catId") final Integer eventCategoryId) {
        log.info("DELETE request to remove category_id = {} was received", eventCategoryId);
        eventCategoryService.deleteEventCategoryById(eventCategoryId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchById(@PathVariable("catId") Integer eventCategoryId,
                                    @RequestBody @Valid NewCategoryDto newCategoryDto
    ) {
        log.info("Patch request to update event_category_id={} received.", eventCategoryId);
        CategoryDto categoryDto = eventCategoryService.updateEventCategory(eventCategoryId, newCategoryDto);
        log.info("{} was patched", categoryDto);
        return categoryDto;
    }

}
