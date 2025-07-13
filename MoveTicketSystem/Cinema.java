package MoveTicketSystem;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String name;
    private String location;
    private List<Room> rooms = new ArrayList<>();


    public Cinema(String name, String location, List<Room> rooms) {
        this.name = name;
        this.location = location;
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
