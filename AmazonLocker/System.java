package AmazonLocker;

import java.util.List;

public class System {
    private List<LockerLocation> lockerLocations;

    Locker findLocker(Product product) {
        return new Locker();
    }

    void insertProduct(Product product) {
        Locker locker = findLocker(product);
    }
}

enum Size {
    SMALL, MEDIUM, LARGE
}

interface Product {
    Size getProductSize();
}

class ShoeProduct implements Product {

    @Override
    public Size getProductSize() {
        return Size.SMALL;
    }

}


