package ru.practicum.statistica;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "APP")
    private String app;

    @Column(name = "URI")
    private String uri;

    @Column(name = "IP")
    private String ip;

    @Column(name = "CREATED")
    private LocalDateTime timestamp;
}
