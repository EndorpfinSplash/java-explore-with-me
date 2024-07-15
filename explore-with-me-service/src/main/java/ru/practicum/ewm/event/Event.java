package ru.practicum.ewm.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event_category.EventCategory;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ANNOTATION", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private EventCategory category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @Column(name = "LOCATION_LAT")
    private double locationLat;

    @Column(name = "LOCATION_LON")
    private double locationLon;

    @Column(name = "PAID")
    private boolean paid;

    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;

    @Column(name = "REQUEST_MODERATION")
    private boolean requestModeration;

    @Column(name = "TITLE")
    private String title;

    @Builder.Default
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EventStatus eventStatus;
}
