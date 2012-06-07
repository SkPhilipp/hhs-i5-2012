package paradise.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import paradise.model.Excursion;
import paradise.model.Trip;
import paradise.model.TripType;

/**
 * @author Groep3
 * 
 * STUB
 * TODO: javax.persistence implementation
 */
public class TripJPAController {

    private static List<Trip> objects = new ArrayList<Trip>(){{
        TripType type = new TripType(0, 100, true, "Egypte", "Bergbeklimmen in Egypte.");
        type.setExcursionList(new ArrayList<Excursion>(){{
            add(new Excursion(0, 20, "Harry", 10.50, "Vissen"));
            add(new Excursion(1, 5, "Henk", 20, "Bootje varen"));
        }});
        Date start = new Date();
        Date end = Calendar.getInstance().getTime();
        add(new Trip(1, start, end, type));
        add(new Trip(2, start, end, type));
        add(new Trip(3, start, end, type));
        add(new Trip(4, start, end, type));
    }};

    private List<Trip> findEntities() {
        return objects;
    }

}
