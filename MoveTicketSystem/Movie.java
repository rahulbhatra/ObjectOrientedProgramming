package MoveTicketSystem;

import java.time.Duration;

public class Movie {
    private String name;
    private String description;
    private Duration duration;

    public Movie(String name, String description, Duration duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

}
