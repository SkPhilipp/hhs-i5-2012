package paradise.controller;

import java.util.ArrayList;
import java.util.List;
import paradise.model.Excursion;

/**
 * @author Groep3
 * 
 * TODO: STUB, needs javax.persistence implementation
 */
public class ExcursionJPAController {

    private static List<Excursion> objects = new ArrayList<Excursion>(){{
        add(new Excursion(5, 20, "Harry", 10.50, "Vissen", null));
        add(new Excursion(16, 5, "Henk", 20, "Bootje varen", null));
    }};

    public List<Excursion> findEntities() {
        return objects;
    }

    public Excursion findEntity(int id) {
        for(Excursion e : objects){
            if(e.getID() == id){
                return e;
            }
        }
        return null;
    }

}
