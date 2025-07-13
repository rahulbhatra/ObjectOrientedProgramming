package MoveTicketSystem;

import java.time.LocalDateTime;

public class Screen {
    private Movie movie;
    public Movie getMovie() {
        return movie;
    }
    private Room room;
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    private LocalDateTime starTime;
    private LocalDateTime endTime;

    public Screen(Movie movie, Room room, LocalDateTime starTime, LocalDateTime endTime) {
        this.movie = movie;
        this.room = room;
        this.starTime = starTime;
        this.endTime = endTime;
    }
}
