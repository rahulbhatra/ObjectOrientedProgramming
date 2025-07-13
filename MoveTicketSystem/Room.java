package MoveTicketSystem;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Layout layout;
    private List<Seat> allSeats;

    public Layout getLayout() {
        return layout;
    }

    public void initializeLayout(int row, int col) {
        this.layout = new Layout(row, col);
        this.allSeats = new ArrayList(this.layout.getAllSeats());
    }

    public List<Seat> getAllSeats() {
        return this.allSeats;
    }
}
