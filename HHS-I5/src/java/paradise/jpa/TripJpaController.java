/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import paradise.model.TripType;
import paradise.model.Booking;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Excursion;
import paradise.model.Trip;

/**
 *
 * @author Philipp
 */
public class TripJpaController implements Serializable {

    public TripJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trip trip) throws RollbackFailureException, Exception {
        if (trip.getBookingList() == null) {
            trip.setBookingList(new ArrayList<Booking>());
        }
        if (trip.getExcursionList() == null) {
            trip.setExcursionList(new ArrayList<Excursion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TripType tripType = trip.getTripType();
            if (tripType != null) {
                tripType = em.getReference(tripType.getClass(), tripType.getId());
                trip.setTripType(tripType);
            }
            List<Booking> attachedBookingList = new ArrayList<Booking>();
            for (Booking bookingListBookingToAttach : trip.getBookingList()) {
                bookingListBookingToAttach = em.getReference(bookingListBookingToAttach.getClass(), bookingListBookingToAttach.getId());
                attachedBookingList.add(bookingListBookingToAttach);
            }
            trip.setBookingList(attachedBookingList);
            List<Excursion> attachedExcursionList = new ArrayList<Excursion>();
            for (Excursion excursionListExcursionToAttach : trip.getExcursionList()) {
                excursionListExcursionToAttach = em.getReference(excursionListExcursionToAttach.getClass(), excursionListExcursionToAttach.getId());
                attachedExcursionList.add(excursionListExcursionToAttach);
            }
            trip.setExcursionList(attachedExcursionList);
            em.persist(trip);
            if (tripType != null) {
                tripType.getTripList().add(trip);
                tripType = em.merge(tripType);
            }
            for (Booking bookingListBooking : trip.getBookingList()) {
                Trip oldTripOfBookingListBooking = bookingListBooking.getTrip();
                bookingListBooking.setTrip(trip);
                bookingListBooking = em.merge(bookingListBooking);
                if (oldTripOfBookingListBooking != null) {
                    oldTripOfBookingListBooking.getBookingList().remove(bookingListBooking);
                    oldTripOfBookingListBooking = em.merge(oldTripOfBookingListBooking);
                }
            }
            for (Excursion excursionListExcursion : trip.getExcursionList()) {
                Trip oldTripOfExcursionListExcursion = excursionListExcursion.getTrip();
                excursionListExcursion.setTrip(trip);
                excursionListExcursion = em.merge(excursionListExcursion);
                if (oldTripOfExcursionListExcursion != null) {
                    oldTripOfExcursionListExcursion.getExcursionList().remove(excursionListExcursion);
                    oldTripOfExcursionListExcursion = em.merge(oldTripOfExcursionListExcursion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trip trip) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trip persistentTrip = em.find(Trip.class, trip.getId());
            TripType tripTypeOld = persistentTrip.getTripType();
            TripType tripTypeNew = trip.getTripType();
            List<Booking> bookingListOld = persistentTrip.getBookingList();
            List<Booking> bookingListNew = trip.getBookingList();
            List<Excursion> excursionListOld = persistentTrip.getExcursionList();
            List<Excursion> excursionListNew = trip.getExcursionList();
            List<String> illegalOrphanMessages = null;
            for (Booking bookingListOldBooking : bookingListOld) {
                if (!bookingListNew.contains(bookingListOldBooking)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Booking " + bookingListOldBooking + " since its trip field is not nullable.");
                }
            }
            for (Excursion excursionListOldExcursion : excursionListOld) {
                if (!excursionListNew.contains(excursionListOldExcursion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Excursion " + excursionListOldExcursion + " since its trip field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tripTypeNew != null) {
                tripTypeNew = em.getReference(tripTypeNew.getClass(), tripTypeNew.getId());
                trip.setTripType(tripTypeNew);
            }
            List<Booking> attachedBookingListNew = new ArrayList<Booking>();
            for (Booking bookingListNewBookingToAttach : bookingListNew) {
                bookingListNewBookingToAttach = em.getReference(bookingListNewBookingToAttach.getClass(), bookingListNewBookingToAttach.getId());
                attachedBookingListNew.add(bookingListNewBookingToAttach);
            }
            bookingListNew = attachedBookingListNew;
            trip.setBookingList(bookingListNew);
            List<Excursion> attachedExcursionListNew = new ArrayList<Excursion>();
            for (Excursion excursionListNewExcursionToAttach : excursionListNew) {
                excursionListNewExcursionToAttach = em.getReference(excursionListNewExcursionToAttach.getClass(), excursionListNewExcursionToAttach.getId());
                attachedExcursionListNew.add(excursionListNewExcursionToAttach);
            }
            excursionListNew = attachedExcursionListNew;
            trip.setExcursionList(excursionListNew);
            trip = em.merge(trip);
            if (tripTypeOld != null && !tripTypeOld.equals(tripTypeNew)) {
                tripTypeOld.getTripList().remove(trip);
                tripTypeOld = em.merge(tripTypeOld);
            }
            if (tripTypeNew != null && !tripTypeNew.equals(tripTypeOld)) {
                tripTypeNew.getTripList().add(trip);
                tripTypeNew = em.merge(tripTypeNew);
            }
            for (Booking bookingListNewBooking : bookingListNew) {
                if (!bookingListOld.contains(bookingListNewBooking)) {
                    Trip oldTripOfBookingListNewBooking = bookingListNewBooking.getTrip();
                    bookingListNewBooking.setTrip(trip);
                    bookingListNewBooking = em.merge(bookingListNewBooking);
                    if (oldTripOfBookingListNewBooking != null && !oldTripOfBookingListNewBooking.equals(trip)) {
                        oldTripOfBookingListNewBooking.getBookingList().remove(bookingListNewBooking);
                        oldTripOfBookingListNewBooking = em.merge(oldTripOfBookingListNewBooking);
                    }
                }
            }
            for (Excursion excursionListNewExcursion : excursionListNew) {
                if (!excursionListOld.contains(excursionListNewExcursion)) {
                    Trip oldTripOfExcursionListNewExcursion = excursionListNewExcursion.getTrip();
                    excursionListNewExcursion.setTrip(trip);
                    excursionListNewExcursion = em.merge(excursionListNewExcursion);
                    if (oldTripOfExcursionListNewExcursion != null && !oldTripOfExcursionListNewExcursion.equals(trip)) {
                        oldTripOfExcursionListNewExcursion.getExcursionList().remove(excursionListNewExcursion);
                        oldTripOfExcursionListNewExcursion = em.merge(oldTripOfExcursionListNewExcursion);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trip.getId();
                if (findTrip(id) == null) {
                    throw new NonexistentEntityException("The trip with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trip trip;
            try {
                trip = em.getReference(Trip.class, id);
                trip.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trip with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Booking> bookingListOrphanCheck = trip.getBookingList();
            for (Booking bookingListOrphanCheckBooking : bookingListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trip (" + trip + ") cannot be destroyed since the Booking " + bookingListOrphanCheckBooking + " in its bookingList field has a non-nullable trip field.");
            }
            List<Excursion> excursionListOrphanCheck = trip.getExcursionList();
            for (Excursion excursionListOrphanCheckExcursion : excursionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trip (" + trip + ") cannot be destroyed since the Excursion " + excursionListOrphanCheckExcursion + " in its excursionList field has a non-nullable trip field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TripType tripType = trip.getTripType();
            if (tripType != null) {
                tripType.getTripList().remove(trip);
                tripType = em.merge(tripType);
            }
            em.remove(trip);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trip> findTripEntities() {
        return findTripEntities(true, -1, -1);
    }

    public List<Trip> findTripEntities(int maxResults, int firstResult) {
        return findTripEntities(false, maxResults, firstResult);
    }

    private List<Trip> findTripEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trip.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Trip findTrip(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trip.class, id);
        } finally {
            em.close();
        }
    }

    public int getTripCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trip> rt = cq.from(Trip.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
