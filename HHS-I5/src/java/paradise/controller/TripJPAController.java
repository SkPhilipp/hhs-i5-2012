package paradise.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import paradise.model.Trip;
import paradise.model.TripType;

/**
 * @author Groep3
 * 
 * TODO: STUB, needs javax.persistence implementation
 */
public class TripJPAController {

    private static List<Trip> objects = new ArrayList<Trip>(){{
    
        TripType type = new TripType(1, "Egypte", "Bergbeklimmen in Egypte.", 120, true);

        ExcursionJPAController excursionController = new ExcursionJPAController();

        Trip trip1 = new Trip(1, new Date(0), Calendar.getInstance().getTime(), 60.50);
        trip1.setTripType(type);
        Trip trip2 = new Trip(2, new Date(0), Calendar.getInstance().getTime(), 60.50);
        trip2.setTripType(type);
        add(trip1);
        add(trip2);

        //TODO: Set excursion list

    }};

    public List<Trip> findEntities() {
        return objects;
    }

    public Trip findEntity(int id) {
        return objects.get(id);
    }

}
