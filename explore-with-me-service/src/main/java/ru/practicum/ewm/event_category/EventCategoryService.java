package ru.practicum.ewm.event_category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public Collection<EventCategoryOutDto> getAllEventCategories() {
        return eventCategoryRepository.findAll().stream()
                .map(EventCategoryMapper::eventCategoryToEventCategoryOutDto)
                .collect(Collectors.toList());
    }

    public EventCategoryOutDto createEventCategory(EventCategoryCreationDto eventCategoryCreationDto) {
        EventCategory eventCategory = EventCategoryMapper.eventCategoryCreationDtoToEventCategory(eventCategoryCreationDto);
        EventCategory savedEventCategory = null;
        try {
            savedEventCategory = eventCategoryRepository.save(eventCategory);
        } catch (DataIntegrityViolationException e) {
//            throw new NonUniqueEmail(String.format("Email = %s address has already in use", user.getEmail())
            throw new RuntimeException();
        }
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(savedEventCategory);
    }

    public EventCategoryOutDto updateEventCategory(Long eventCategoryId, EventCategoryCreationDto eventCategoryUpdateDto) {
        EventCategory eventCategoryForUpdate = eventCategoryRepository.findById(eventCategoryId).orElseThrow(
//                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        eventCategoryForUpdate.setName(eventCategoryUpdateDto.getName());

        EventCategory updatedEventCategory  = null;
        try {
            updatedEventCategory = eventCategoryRepository.save(eventCategoryForUpdate);
        } catch (DataIntegrityViolationException e) {

//            throw new NonUniqueEmail(String.format("Email = %s address has already in use", updatedUser.getEmail()));
        }

        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(updatedEventCategory);
    }

    public EventCategoryOutDto getEventCategoryIdById(Long eventCategoryId) {
        EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryId)
                .orElseThrow(
//                        () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
                );
        return EventCategoryMapper.eventCategoryToEventCategoryOutDto(eventCategory);
    }

    public void deleteEventCategoryById(Long id) {
        eventCategoryRepository.deleteById(id);
    }
}
