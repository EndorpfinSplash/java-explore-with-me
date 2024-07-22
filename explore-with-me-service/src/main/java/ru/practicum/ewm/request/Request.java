package ru.practicum.ewm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REQUEST")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "CREATED_ON", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RequestStatus status = RequestStatus.PENDING;


}
