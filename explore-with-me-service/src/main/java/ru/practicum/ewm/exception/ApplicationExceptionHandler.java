package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice("ru.practicum.ewm")
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler({
            NonUniqueEmail.class,
            EventNotValidArgumentException.class,
            DataIntegrityViolationException.class,
            NotValidRequestException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError catchUniqueConstraint(final Exception e) {
        return new ApiError("CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler({
            ParticipantsLimitationException.class,
            NotApplicableEvent.class,
            IncorrectStatusException.class

    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError catchParticipantsChecks(final Exception e) {
        return new ApiError("CONFLICT",
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler({
            NumberFormatException.class,
            IncorrectDateException.class,
            EventSortOrderNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError catchValidation(final Exception e) {
        return new ApiError("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage());
    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError catchNotFound(final RuntimeException e) {
        return new ApiError(
                "NOT_FOUND",
                "The required object was not found.",
                e.getMessage());
    }
}
