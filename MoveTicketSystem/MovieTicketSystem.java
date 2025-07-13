package MoveTicketSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieTicketSystem {
    private ScreeningManager screeningManager;
    private TicketManager ticketManager;
    private List<Cinema> cinemas = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    public List<Screen> getScreensForMovie(Movie movie) {
        return screeningManager.getAllMovieScreens(movie);
    }

    public List<Seat> getAvialableSeats(Screen screen) {
        return ticketManager.getAvailableSeats(screen);
    }

    public void addCinema(Cinema cinema) {
        this.cinemas.add(cinema);
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }

    public Ticket bookTicket(Screen screen, Seat seat) throws IllegalStateException {
        return ticketManager.bookTicketOptimistically(screen, seat);
    }
}

class ScreeningManager {
    private Map<Movie, List<Screen>> movieScreens;


    public void addScreen(Screen screen) {
        List<Screen> existingScreens = movieScreens.getOrDefault(screen.getMovie(), new ArrayList<>());
        existingScreens.add(screen);
        movieScreens.put(screen.getMovie(), existingScreens);
    }

    public List<Screen> getAllMovieScreens(Movie movie) {
        return movieScreens.getOrDefault(movie, new ArrayList<>());
    }
}

class TicketManager {
    private Map<Screen, List<Ticket>> screenTickets = new HashMap<>();

    public List<Ticket> bookedTickets(Screen screen) {
        return screenTickets.get(screen);
    }

    public List<Seat> getAvailableSeats(Screen screen) {
        List<Ticket> soldTickets = bookedTickets(screen);
        List<Seat> allSeats = screen.getRoom().getAllSeats();

        for(Ticket ticket: soldTickets) {
            Seat seat = ticket.getSeat();
            allSeats.remove(seat);
        }
        return allSeats;
    }

    public Integer availableSeatsCount(Screen screen) {
        List<Ticket> soldTickets = bookedTickets(screen);
        List<Seat> allSeats = screen.getRoom().getAllSeats();

        for(Ticket ticket: soldTickets) {
            Seat seat = ticket.getSeat();
            allSeats.remove(seat);
        }
        return allSeats.size();
    }

    public synchronized Ticket bookTicketOptimistically(Screen screen, Seat seat) throws IllegalStateException {
        List<Seat> seats = getAvailableSeats(screen);
        boolean isSeatAvaialbe = seats.stream().anyMatch(availableSeat -> availableSeat.getSeatNumber().equals(seat.getSeatNumber()));
        if (isSeatAvaialbe) {
            Ticket ticket = new Ticket(screen, seat);
            screenTickets.computeIfAbsent(screen, _ -> new ArrayList<>()).add(ticket);
            return ticket;
        } else {
            throw new IllegalStateException("The ticket is already book");
        }
    }
}