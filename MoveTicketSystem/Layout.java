package MoveTicketSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Layout {
    private int rows;
    private int columns;
    private Map<Integer, Map<Integer, Seat>> seats = new HashMap<>();

    public Layout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String seatNumber = i + "-" + j;
                seats.computeIfAbsent(j, k -> new HashMap<>()).put(j, new Seat(seatNumber, new StandardPricingStrategy()));
            }
        }
    }

    public List<Seat> getAllSeats() {
        List<Seat> seatList = new ArrayList<>();
        for(Map<Integer, Seat> rowSeats: seats.values()) {
            for(Seat seat: rowSeats.values()) {
                seatList.add(seat);
            }
        }
        return seatList;
    }
}
