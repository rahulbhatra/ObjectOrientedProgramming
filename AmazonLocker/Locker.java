package AmazonLocker;

import java.util.List;


public class Locker {
    Location location;
    List<Storage> storages;
}

class Location {
    private int lat;
    private int lon;

    public Location(int lat, int lon) {
        this.lat = lat;
        this.lon = lon;
    }
}

interface Storage {
    Size getStorageSize();
}
