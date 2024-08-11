package ru.practicum.ewm.compilation;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestStatus;
import ru.practicum.ewm.request.RequestsCountByEvent;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public CompilationDto createCompilation(final @Valid NewCompilationDto newCompilationDto) {
        Set<Event> eventList = newCompilationDto.getEvents().stream()
                .map(eventId -> eventRepository.findById(eventId).orElseThrow(
                                () -> new EventNotFoundException(MessageFormat.format("Event with id={0} was not found", eventId))
                        )
                )
                .collect(Collectors.toSet());

        Compilation savedCompilation = compilationRepository.save(
                CompilationMapper.newCompilationToCompilation(
                        newCompilationDto,
                        eventList
                )
        );
        Map<Long, Long> confirmedRequestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestsCountByEvent::getEventId, RequestsCountByEvent::getCount));

        return CompilationMapper.compilationToCompilationDto(savedCompilation, confirmedRequestsByEvent);
    }

    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EventNotFoundException(MessageFormat.format("Compilation with id={0} was not found", compId)
                )
        );
        compilationRepository.delete(compilation);
    }

    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilationForUpdate = compilationRepository.findById(compId).orElseThrow(
                () -> new EventNotFoundException(MessageFormat.format("Compilation with id={0} was not found", compId)
                )
        );
        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> eventSet = updateCompilationRequest.getEvents().stream()
                    .map(eventId -> eventRepository.findById(eventId).orElseThrow(
                                    () -> new EventNotFoundException(
                                            MessageFormat.format("Compilation with id={0} was not found", eventId)
                                    )
                            )
                    )
                    .collect(Collectors.toSet());
            compilationForUpdate.setEvents(eventSet);
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilationForUpdate.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilationForUpdate.setPinned(updateCompilationRequest.getPinned());
        }

        Compilation compilationSaved = compilationRepository.save(compilationForUpdate);
        Map<Long, Long> confirmedRequestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestsCountByEvent::getEventId, RequestsCountByEvent::getCount));
        return CompilationMapper.compilationToCompilationDto(compilationSaved, confirmedRequestsByEvent);
    }

    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EventNotFoundException(MessageFormat.format("Compilation with id={0} was not found", compId)
                )
        );
        Map<Long, Long> confirmedRequestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestsCountByEvent::getEventId, RequestsCountByEvent::getCount));
        return CompilationMapper.compilationToCompilationDto(compilation, confirmedRequestsByEvent);
    }

    public List<CompilationDto> getCompilations(final Boolean pinned,
                                                final Integer from,
                                                final Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Compilation> compilations = compilationRepository.findAll(page).toList();

        Map<Long, Long> confirmedRequestsByEvent = requestRepository.countRequestByEventId(RequestStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(RequestsCountByEvent::getEventId, RequestsCountByEvent::getCount));
        return compilations.stream().
                map(compilation -> CompilationMapper.compilationToCompilationDto(compilation, confirmedRequestsByEvent))
                .collect(Collectors.toList());
    }
}
