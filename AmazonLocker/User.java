package AmazonLocker;

import java.util.List;
import java.util.UUID;

enum Role {
    CUSTOMER, AGENT
}

abstract public class User {
    private UUID id;
    private String name;
    private String email;
    private List<Role> roles; // A user can be both agent and customer in some scenario
}
