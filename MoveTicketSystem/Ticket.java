package MoveTicketSystem;

import java.math.BigDecimal;

public class Ticket {
    private Screen screen;
    private Seat seat;
    private BigDecimal price;

    public Ticket(Screen screen, Seat seat) {
        this.screen = screen;
        this.seat = seat;
        this.price = seat.getPrice();
    }

    public Seat getSeat() {
        return seat;
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
