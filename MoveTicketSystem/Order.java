package MoveTicketSystem;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    List<Ticket> tickets;
    BigDecimal orderPrice;

    public Order(List<Ticket> tickets) {
        this.tickets = tickets;
        this.orderPrice = tickets.stream().map(ticket -> ticket.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Ticket> getOderTickets() {
        return this.tickets;
    }

    public BigDecimal getOderPrice() {
        return this.orderPrice;
    }
}
