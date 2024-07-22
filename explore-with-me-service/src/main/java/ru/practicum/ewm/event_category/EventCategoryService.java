package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event_category.dto.CategoryDto;
import ru.practicum.ewm.event_category.dto.CategoryOutDto;
import ru.practicum.ewm.exception.EventCategoryNotFoundException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public Collection<CategoryOutDto> getAllEventCategories() {
        return eventCategoryRepository.findAll().stream()
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .collect(Collectors.toList());
    }

    public CategoryOutDto createEventCategory(CategoryDto categoryDto) {
        EventCategory eventCategory = EventCategoryMapper.categoryDtoToEventCategory(categoryDto);
        EventCategory savedEventCategory = eventCategoryRepository.save(eventCategory);
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(savedEventCategory);
    }

    public CategoryOutDto updateEventCategory(Integer eventCategoryId, CategoryDto eventCategoryUpdateDto) {
        EventCategory eventCategoryForUpdate = eventCategoryRepository.findById(Long.valueOf(eventCategoryId)).orElseThrow(
                () -> new EventCategoryNotFoundException(MessageFormat.format("Category with id={0} was not found",
                        eventCategoryId)
                )
        );
        eventCategoryForUpdate.setName(eventCategoryUpdateDto.getName());
        EventCategory updatedEventCategory = eventCategoryRepository.save(eventCategoryForUpdate);
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(updatedEventCategory);
    }

    public CategoryOutDto getEventCategoryIdById(Long eventCategoryId) {
        EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryId)
                .orElseThrow(
                        () -> new EventCategoryNotFoundException(MessageFormat.format("Category with id={0} was not found",
                                eventCategoryId))
                );
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(eventCategory);
    }

    public void deleteEventCategoryById(Integer id) {
        Long eventCategoryId = Long.valueOf(id);
        eventCategoryRepository.findById(eventCategoryId).orElseThrow(
                () -> new EventCategoryNotFoundException(
                        MessageFormat.format("Category with id={0} was not found", eventCategoryId))
        );
        eventCategoryRepository.deleteById(eventCategoryId);
    }

    public Collection<CategoryOutDto> getEventCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return eventCategoryRepository.findAll(page).stream()
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .collect(Collectors.toList());
    }

    public CategoryOutDto findById(Integer eventCategoryId) {
        return eventCategoryRepository.findById(Long.valueOf(eventCategoryId))
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .orElseThrow(
                        () -> new EventCategoryNotFoundException(
                                MessageFormat.format("Category with id={0} was not found",
                                        eventCategoryId)
                        )
                );
    }
}
