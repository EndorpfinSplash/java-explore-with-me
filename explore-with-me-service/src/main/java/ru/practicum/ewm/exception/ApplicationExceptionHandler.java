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
//            ParticipantsLimitationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError catchUniqueConstraint(final Exception e) {
        return new ApiError("CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler({
            ParticipantsLimitationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError catchParticipantsChecks(final Exception e) {
        return new ApiError("CONFLICT",
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler({
            NotApplicableEvent.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError catchUniqueConstraint(final NotApplicableEvent e) {
        return new ApiError("FORBIDDEN",
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler({
//            MethodArgumentNotValidException.class,
            NumberFormatException.class,
            IncorrectStatusException.class,
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
            UserNotFoundException.class,
            ItemNotFoundException.class,
            EventCategoryNotFoundException.class,
            EventNotFoundException.class,
            RequestNotFoundException.class,
            BookingAccessDeniedException.class,
            BookingStatusCanChaneOnlyOwner.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError catchNotFound(final RuntimeException e) {
        return new ApiError(
                "NOT_FOUND",
                "The required object was not found.",
                e.getMessage());
    }


  /*  @ExceptionHandler({
            CommentForbidden.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final CommentForbidden e) {
        return new ErrorResponse("Item cannot be commented", e.getMessage());
    }

    @ExceptionHandler({
            ValidationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final ValidationException e) {
        return new ErrorResponse("Parameter validation exception", e.getMessage());
    }

    @ExceptionHandler({
            UnknownBookingState.class,
            BookingCouldntBeModified.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse catchValidation(final RuntimeException e) {
        return new ErrorResponse("Unknown state: " + e.getMessage(),
                "Incorrect booking state was send.");
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            ItemNotFoundException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class,
            BookingAccessDeniedException.class,
            BookingStatusCanChaneOnlyOwner.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse catchNotFound(final RuntimeException e) {
        return new ErrorResponse("Not found exception", e.getMessage());
    }

    @ExceptionHandler({
            ItemCouldntBeModified.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse catchCouldNotModifiedItem(final RuntimeException e) {
        return new ErrorResponse("You can not modify this item", e.getMessage());
    }

    @ExceptionHandler({
            NonUniqueEmail.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse catchNonUniqueEmailCreating(final RuntimeException e) {
        return new ErrorResponse("User with such email already exist", e.getMessage());
    }
*/
}
