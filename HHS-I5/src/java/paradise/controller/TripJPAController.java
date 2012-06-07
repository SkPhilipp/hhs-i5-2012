package paradise.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import paradise.model.*;

/**
 * @author Groep3
 * 
 * TODO: STUB, needs javax.persistence implementation
 */
public class TripJPAController {

    private static List<Trip> objects = new ArrayList<Trip>(){{
        TripType type = new TripType(0, 100, true, "Egypte", "Bergbeklimmen in Egypte.");
        // Add excursions
        type.setExcursionList(new ArrayList<Excursion>(){{
            add(new Excursion(0, 20, "Harry", 10.50, "Vissen"));
            add(new Excursion(1, 5, "Henk", 20, "Bootje varen"));
        }});
        Date start = new Date();
        Date end = Calendar.getInstance().getTime();
        // Trip with booking
        final Trip tempTrip = new Trip(1, start, end, type);
        tempTrip.setBookingList(new ArrayList<Booking>(){{
            add(new Booking(0, 4, 6, true, 100, null, tempTrip));
            add(new Booking(0, 13, 26, true, 100, null, tempTrip));
        }});
        // Add trips to object list
        add(tempTrip);
        add(new Trip(2, start, end, type));
        add(new Trip(3, start, end, type));
        add(new Trip(4, start, end, type));
    }};

    public List<Trip> findEntities() {
        return objects;
    }

}
