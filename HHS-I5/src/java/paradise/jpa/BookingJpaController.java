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
import paradise.model.Trip;
import paradise.model.Private;
import paradise.model.BookingExcursion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Booking;

/**
 *
 * @author Philipp
 */
public class BookingJpaController implements Serializable {

    public BookingJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Booking booking) throws RollbackFailureException, Exception {
        if (booking.getBookingExcursionList() == null) {
            booking.setBookingExcursionList(new ArrayList<BookingExcursion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Trip trip = booking.getTrip();
            if (trip != null) {
                trip = em.getReference(trip.getClass(), trip.getId());
                booking.setTrip(trip);
            }
            Private private1 = booking.getPrivate1();
            if (private1 != null) {
                private1 = em.getReference(private1.getClass(), private1.getCustomer());
                booking.setPrivate1(private1);
            }
            List<BookingExcursion> attachedBookingExcursionList = new ArrayList<BookingExcursion>();
            for (BookingExcursion bookingExcursionListBookingExcursionToAttach : booking.getBookingExcursionList()) {
                bookingExcursionListBookingExcursionToAttach = em.getReference(bookingExcursionListBookingExcursionToAttach.getClass(), bookingExcursionListBookingExcursionToAttach.getBookingExcursionPK());
                attachedBookingExcursionList.add(bookingExcursionListBookingExcursionToAttach);
            }
            booking.setBookingExcursionList(attachedBookingExcursionList);
            em.persist(booking);
            if (trip != null) {
                trip.getBookingList().add(booking);
                trip = em.merge(trip);
            }
            if (private1 != null) {
                private1.getBookingList().add(booking);
                private1 = em.merge(private1);
            }
            for (BookingExcursion bookingExcursionListBookingExcursion : booking.getBookingExcursionList()) {
                Booking oldBooking1OfBookingExcursionListBookingExcursion = bookingExcursionListBookingExcursion.getBooking1();
                bookingExcursionListBookingExcursion.setBooking1(booking);
                bookingExcursionListBookingExcursion = em.merge(bookingExcursionListBookingExcursion);
                if (oldBooking1OfBookingExcursionListBookingExcursion != null) {
                    oldBooking1OfBookingExcursionListBookingExcursion.getBookingExcursionList().remove(bookingExcursionListBookingExcursion);
                    oldBooking1OfBookingExcursionListBookingExcursion = em.merge(oldBooking1OfBookingExcursionListBookingExcursion);
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

    public void edit(Booking booking) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Booking persistentBooking = em.find(Booking.class, booking.getId());
            Trip tripOld = persistentBooking.getTrip();
            Trip tripNew = booking.getTrip();
            Private private1Old = persistentBooking.getPrivate1();
            Private private1New = booking.getPrivate1();
            List<BookingExcursion> bookingExcursionListOld = persistentBooking.getBookingExcursionList();
            List<BookingExcursion> bookingExcursionListNew = booking.getBookingExcursionList();
            List<String> illegalOrphanMessages = null;
            for (BookingExcursion bookingExcursionListOldBookingExcursion : bookingExcursionListOld) {
                if (!bookingExcursionListNew.contains(bookingExcursionListOldBookingExcursion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookingExcursion " + bookingExcursionListOldBookingExcursion + " since its booking1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tripNew != null) {
                tripNew = em.getReference(tripNew.getClass(), tripNew.getId());
                booking.setTrip(tripNew);
            }
            if (private1New != null) {
                private1New = em.getReference(private1New.getClass(), private1New.getCustomer());
                booking.setPrivate1(private1New);
            }
            List<BookingExcursion> attachedBookingExcursionListNew = new ArrayList<BookingExcursion>();
            for (BookingExcursion bookingExcursionListNewBookingExcursionToAttach : bookingExcursionListNew) {
                bookingExcursionListNewBookingExcursionToAttach = em.getReference(bookingExcursionListNewBookingExcursionToAttach.getClass(), bookingExcursionListNewBookingExcursionToAttach.getBookingExcursionPK());
                attachedBookingExcursionListNew.add(bookingExcursionListNewBookingExcursionToAttach);
            }
            bookingExcursionListNew = attachedBookingExcursionListNew;
            booking.setBookingExcursionList(bookingExcursionListNew);
            booking = em.merge(booking);
            if (tripOld != null && !tripOld.equals(tripNew)) {
                tripOld.getBookingList().remove(booking);
                tripOld = em.merge(tripOld);
            }
            if (tripNew != null && !tripNew.equals(tripOld)) {
                tripNew.getBookingList().add(booking);
                tripNew = em.merge(tripNew);
            }
            if (private1Old != null && !private1Old.equals(private1New)) {
                private1Old.getBookingList().remove(booking);
                private1Old = em.merge(private1Old);
            }
            if (private1New != null && !private1New.equals(private1Old)) {
                private1New.getBookingList().add(booking);
                private1New = em.merge(private1New);
            }
            for (BookingExcursion bookingExcursionListNewBookingExcursion : bookingExcursionListNew) {
                if (!bookingExcursionListOld.contains(bookingExcursionListNewBookingExcursion)) {
                    Booking oldBooking1OfBookingExcursionListNewBookingExcursion = bookingExcursionListNewBookingExcursion.getBooking1();
                    bookingExcursionListNewBookingExcursion.setBooking1(booking);
                    bookingExcursionListNewBookingExcursion = em.merge(bookingExcursionListNewBookingExcursion);
                    if (oldBooking1OfBookingExcursionListNewBookingExcursion != null && !oldBooking1OfBookingExcursionListNewBookingExcursion.equals(booking)) {
                        oldBooking1OfBookingExcursionListNewBookingExcursion.getBookingExcursionList().remove(bookingExcursionListNewBookingExcursion);
                        oldBooking1OfBookingExcursionListNewBookingExcursion = em.merge(oldBooking1OfBookingExcursionListNewBookingExcursion);
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
                Integer id = booking.getId();
                if (findBooking(id) == null) {
                    throw new NonexistentEntityException("The booking with id " + id + " no longer exists.");
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
            Booking booking;
            try {
                booking = em.getReference(Booking.class, id);
                booking.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The booking with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BookingExcursion> bookingExcursionListOrphanCheck = booking.getBookingExcursionList();
            for (BookingExcursion bookingExcursionListOrphanCheckBookingExcursion : bookingExcursionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Booking (" + booking + ") cannot be destroyed since the BookingExcursion " + bookingExcursionListOrphanCheckBookingExcursion + " in its bookingExcursionList field has a non-nullable booking1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Trip trip = booking.getTrip();
            if (trip != null) {
                trip.getBookingList().remove(booking);
                trip = em.merge(trip);
            }
            Private private1 = booking.getPrivate1();
            if (private1 != null) {
                private1.getBookingList().remove(booking);
                private1 = em.merge(private1);
            }
            em.remove(booking);
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

    public List<Booking> findBookingEntities() {
        return findBookingEntities(true, -1, -1);
    }

    public List<Booking> findBookingEntities(int maxResults, int firstResult) {
        return findBookingEntities(false, maxResults, firstResult);
    }

    private List<Booking> findBookingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Booking.class));
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

    public Booking findBooking(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Booking> rt = cq.from(Booking.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
