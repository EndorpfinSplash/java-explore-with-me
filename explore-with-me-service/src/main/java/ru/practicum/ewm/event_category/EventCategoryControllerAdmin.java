package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_category.dto.EventCategoryCreationDto;
import ru.practicum.ewm.event_category.dto.EventCategoryOutDto;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryControllerAdmin {

    private final EventCategoryService eventCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventCategoryOutDto create(@RequestBody final EventCategoryCreationDto eventCategoryCreationDto) {
        return eventCategoryService.createEventCategory(eventCategoryCreationDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("catId") final Integer eventCategoryId) {
        eventCategoryService.deleteEventCategoryById(eventCategoryId);
    }

    @PatchMapping("/{catId}")
    public EventCategoryOutDto patchById(@PathVariable("catId") Integer eventCategoryId,
                                         @RequestBody EventCategoryCreationDto eventCategoryCreationDto
    ) {
        log.info("Patch request to update event_category_id={} received.", eventCategoryId);
        return eventCategoryService.updateEventCategory(eventCategoryId,eventCategoryCreationDto);
    }

}
