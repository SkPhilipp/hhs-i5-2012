package paradise.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import paradise.model.Booking;
import paradise.model.Trip;
import paradise.model.TripType;

/**
 * @author Groep3
 * 
 * TODO: STUB, needs javax.persistence implementation
 */
public class TripJPAController {

    private static List<Trip> objects = new ArrayList<Trip>(){{
        TripType type = new TripType(0, 100, true, "Egypte", "Bergbeklimmen in Egypte.");
        ExcursionJPAController excursionController = new ExcursionJPAController();
        Date start = new Date();
        Date end = Calendar.getInstance().getTime();

        final Trip tempTrip1 = new Trip(0, start, end, type, 12.50);
        tempTrip1.setBookingList(new ArrayList<Booking>(){{
            add(new Booking(0, 10, 20, true, 100, null, tempTrip1));
            add(new Booking(0, 10, 60, true, 100, null, tempTrip1));
        }});

        final Trip tempTrip2 = new Trip(1, start, end, type, 15.50);
        tempTrip2.setExcursionList(excursionController.findEntities());


        // Add trips to object list
        add(tempTrip1);
        add(tempTrip2);
    }};

    public List<Trip> findEntities() {
        return objects;
    }

    public Trip findEntity(int id) {
        return objects.get(id);
    }

}
