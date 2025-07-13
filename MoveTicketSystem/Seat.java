package MoveTicketSystem;

import java.math.BigDecimal;

public class Seat {
    private String seatNumber;
    public String getSeatNumber() {
        return seatNumber;
    }

    private PricingStrategy pricingStrategy;

    public Seat(String seatNumber, PricingStrategy pricingStrategy) {
        this.seatNumber = seatNumber;
        this.pricingStrategy = pricingStrategy;
    }

    public BigDecimal getPrice() {
        return pricingStrategy.calculatePrice();
    }
}
