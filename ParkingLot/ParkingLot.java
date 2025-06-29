package ParkingLot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PaymentDetails {
    String cardNumber;
    String cvv;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String zipCode;
    String phoneNumber;
}

interface Payment {
    boolean processPayment(long charge, PaymentDetails paymentDetails);
}

class GooglePayment implements Payment {

    @Override
    public boolean processPayment(long charge, PaymentDetails paymentDetails) {
        // process payment and return true if processed.
        return true;
    }

}

public class ParkingLot {
    private List<ParkingSpace> parkingSpaces;
    private int availableParkingSpaces = 0;

    public ParkingLot(int totalParkingSpaces) {
        this.availableParkingSpaces = totalParkingSpaces;
        this.parkingSpaces = new ArrayList<>();
        for (int i = 0; i < totalParkingSpaces; i++) {
            parkingSpaces.add(new ParkingSpace(i + 1));
        }
    }

    private boolean isConsecutive(List<ParkingSpace> spaces) {
        for(int i = 1; i < spaces.size(); i++) {
            ParkingSpace currentSpace = spaces.get(i);
            ParkingSpace previousSpace = spaces.get(i - 1);
            if (currentSpace.getSpaceId() != previousSpace.getSpaceId() + 1) {
                return false;
            }
        }
        return true;
    }

    public Ticket bookParking(Vehical vehical) {
        List<ParkingSpace> allotedSpaces = new ArrayList<>();
        if (vehical.getParkSpaceSize() > 3 || availableParkingSpaces < vehical.getParkSpaceSize()) {
            // Not allowed to park
            return null;
        } else {
            for (int i = 0; i < parkingSpaces.size(); i++) {
                ParkingSpace space = parkingSpaces.get(i);
                List<ParkingSpace> allotedPlusCurrent = new ArrayList<>(allotedSpaces);
                allotedPlusCurrent.add(space);
                if (space.isAvailable() && isConsecutive(allotedPlusCurrent)) {
                    allotedSpaces = allotedPlusCurrent;
                }
                if (allotedSpaces.size() == vehical.getParkSpaceSize()) {
                    break;
                }
            }
            for (ParkingSpace space: allotedSpaces) {
                space.setVehical(vehical); // multiple vehical with same spot in case it's bigger
            }
            availableParkingSpaces -= allotedSpaces.size();
            return new Ticket(vehical, allotedSpaces);
        }
    }

    public boolean exitParking(Ticket ticket, PaymentDetails paymentDetails) {
        // can check mode of payment for various cases
        Payment payment = new GooglePayment();
        Instant exitTime = Instant.now();
        long charge = (ticket.getEnterTime().toEpochMilli() - exitTime.toEpochMilli()) * 1000;
        if (payment.processPayment(charge, paymentDetails)) {
            ticket.setExitTime();
            availableParkingSpaces += ticket.getAllotedParkingSpaces();
            return true;
        } else {
            return false;
        }
    }



    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot(30);
        Vehical sedan = new Sedan(34232l);
        Vehical suv = new Suv(45435l);

        System.out.println(parkingLot.bookParking(sedan));
        System.out.println(parkingLot.bookParking(suv));
    }
}

class ParkingSpace {
    private int spaceId;
    public int getSpaceId() {
        return spaceId;
    }

    // if a vehical is parked then it then value is non null;
    private Vehical vehical;

    public Vehical getVehical() {
        return vehical;
    }

    public void setVehical(Vehical vehical) {
        this.vehical = vehical;
    }

    public ParkingSpace(int spaceId) {
        this.spaceId = spaceId;
    }

    public boolean isAvailable() {
        return vehical == null;
    }
}

interface ParkSize {
    int getParkSpaceSize();
}

class Ticket {
    UUID ticketId;
    private List<ParkingSpace> allotedParkingSpaces;
    public int getAllotedParkingSpaces() {
        return allotedParkingSpaces.size();
    }
    private Vehical vehical;
    private Instant enterTime;
    public Instant getEnterTime() {
        return enterTime;
    }

    private Instant exitTime;

    public void setExitTime() {
        for(ParkingSpace space : allotedParkingSpaces) {
            space.setVehical(null);
        }
        this.vehical = null;
        this.allotedParkingSpaces = null;
        this.exitTime = Instant.now();
    }

    public Ticket(Vehical vehical, List<ParkingSpace> allotedParkingSpaces) {
        this.ticketId = new UUID(8, 0);
        this.allotedParkingSpaces = allotedParkingSpaces;
        this.vehical = vehical;
        this.enterTime = Instant.now();
    }

    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", allotedParkingSpaces=" + allotedParkingSpaces + ", vehical="
                + vehical + ", enterTime=" + enterTime + ", exitTime=" + exitTime + "]";
    }

    

}

class Vehical implements ParkSize {

    Long vehicalId;

    public Vehical(Long vehicalId) {
        this.vehicalId = vehicalId;
    }

    @Override
    public int getParkSpaceSize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParkSpaceSize'");
    }
}

class Sedan extends Vehical {

    public Sedan(Long vehicalId) {
        super(vehicalId);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int getParkSpaceSize() {
        return 1;
    }
    
}

class Suv extends Vehical {
    public Suv(Long vehicalId) {
        super(vehicalId);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int getParkSpaceSize() {
        return 1;
    }
}

class Bus extends Vehical {
    public Bus(Long vehicalId) {
        super(vehicalId);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int getParkSpaceSize() {
        return 1;
    }
}

class MotorBike extends Vehical {
    public MotorBike(Long vehicalId) {
        super(vehicalId);
        //TODO Auto-generated constructor stub
    }

    @Override
    public int getParkSpaceSize() {
        return 1;
    }
}
