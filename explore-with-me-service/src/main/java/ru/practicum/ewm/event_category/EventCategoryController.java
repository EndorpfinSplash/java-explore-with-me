package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    @PostMapping
    public EventCategoryOutDto create(@RequestBody final EventCategoryCreationDto eventCategoryCreationDto) {
        return eventCategoryService.createEventCategory(eventCategoryCreationDto);
    }


}
