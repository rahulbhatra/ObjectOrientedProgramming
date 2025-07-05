package AmazonLocker;

import java.util.Map;
import java.util.UUID;

public class Package {
    private UUID id;
    private String orderId;
    // package have partial products from the Order as some time all products can't be delivered together.
    private Map<String, Integer> products;

    public Package(String orderId, Map<String, Integer> products) {
        this.orderId = orderId;
        this.products = products;
    }
}

