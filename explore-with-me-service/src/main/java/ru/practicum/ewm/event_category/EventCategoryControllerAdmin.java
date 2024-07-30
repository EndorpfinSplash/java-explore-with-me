package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.CategoryOutDto;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryControllerAdmin {

    private final EventCategoryService eventCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryOutDto create(@RequestBody @Valid final CategoryDto categoryDto) {
        return eventCategoryService.createEventCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("catId") final Integer eventCategoryId) {
        eventCategoryService.deleteEventCategoryById(eventCategoryId);
    }

    @PatchMapping("/{catId}")
    public CategoryOutDto patchById(@PathVariable("catId") Integer eventCategoryId,
                                    @RequestBody @Valid CategoryDto categoryDto
    ) {
        log.info("Patch request to update event_category_id={} received.", eventCategoryId);
        return eventCategoryService.updateEventCategory(eventCategoryId, categoryDto);
    }

}
