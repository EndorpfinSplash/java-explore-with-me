package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.NewCategoryDto;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public Collection<CategoryDto> getAllEventCategories() {
        return eventCategoryRepository.findAll().stream()
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .collect(Collectors.toList());
    }

    public CategoryDto createEventCategory(NewCategoryDto newCategoryDto) {
        EventCategory eventCategory = EventCategoryMapper.categoryDtoToEventCategory(newCategoryDto);
        EventCategory savedEventCategory = eventCategoryRepository.save(eventCategory);
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(savedEventCategory);
    }

    public CategoryDto updateEventCategory(Integer eventCategoryId, NewCategoryDto eventCategoryUpdateDto) {
        EventCategory eventCategoryForUpdate = findEventCategory(Long.valueOf(eventCategoryId));
        eventCategoryForUpdate.setName(eventCategoryUpdateDto.getName());
        EventCategory updatedEventCategory = eventCategoryRepository.save(eventCategoryForUpdate);
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(updatedEventCategory);
    }

    public void deleteEventCategoryById(Integer id) {
        Long eventCategoryId = Long.valueOf(id);
        findEventCategory(eventCategoryId);
        eventCategoryRepository.deleteById(eventCategoryId);
    }

    public Collection<CategoryDto> getEventCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventCategoryRepository.findAll(page).stream()
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Integer eventCategoryId) {
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(
                findEventCategory(Long.valueOf(eventCategoryId))
        );
    }

    public EventCategory findEventCategory(Long eventCategoryId) {
        return eventCategoryRepository.findById(eventCategoryId)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Category with id={0} was not found",
                                eventCategoryId))
                );
    }
}
