package MoveTicketSystem;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice();
}

class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice() {
        return new BigDecimal(10);
    }

}
