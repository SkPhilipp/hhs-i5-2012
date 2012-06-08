package paradise.controller;

import java.util.ArrayList;
import java.util.List;
import paradise.model.Excursion;
import paradise.model.ExcursionType;

/**
 * @author Groep3
 * 
 * TODO: STUB, needs javax.persistence implementation
 */
public class ExcursionJPAController {

    private static List<Excursion> objects = new ArrayList<Excursion>(){{
        ExcursionType type = new ExcursionType(1, "Vissen", "Vissen in de Nijl.");
        Excursion excursion1 = new Excursion(1, 30, "Harry", 10.50);
        excursion1.setExcursionType(type);
        add(excursion1);
        Excursion excursion2 = new Excursion(1, 30, "Joe", 10.50);
        excursion1.setExcursionType(type);
        add(excursion1);
    }};

    public List<Excursion> findEntities() {
        return objects;
    }

    public Excursion findEntity(int id) {
        for(Excursion e : objects){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }

}
