package ru.practicum.ewm.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.event.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPILATION")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PINNED")
    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "COMPILATION_EVENT",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Event> events = new HashSet<>();

}
