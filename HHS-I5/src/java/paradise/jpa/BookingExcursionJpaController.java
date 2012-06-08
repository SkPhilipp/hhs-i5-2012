/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.PreexistingEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Booking;
import paradise.model.BookingExcursion;
import paradise.model.BookingExcursionPK;
import paradise.model.Excursion;

/**
 *
 * @author Philipp
 */
public class BookingExcursionJpaController implements Serializable {

    public BookingExcursionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookingExcursion bookingExcursion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (bookingExcursion.getBookingExcursionPK() == null) {
            bookingExcursion.setBookingExcursionPK(new BookingExcursionPK());
        }
        bookingExcursion.getBookingExcursionPK().setBooking(bookingExcursion.getBooking1().getId());
        bookingExcursion.getBookingExcursionPK().setExcursion(bookingExcursion.getExcursion1().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Booking booking1 = bookingExcursion.getBooking1();
            if (booking1 != null) {
                booking1 = em.getReference(booking1.getClass(), booking1.getId());
                bookingExcursion.setBooking1(booking1);
            }
            Excursion excursion1 = bookingExcursion.getExcursion1();
            if (excursion1 != null) {
                excursion1 = em.getReference(excursion1.getClass(), excursion1.getId());
                bookingExcursion.setExcursion1(excursion1);
            }
            em.persist(bookingExcursion);
            if (booking1 != null) {
                booking1.getBookingExcursionList().add(bookingExcursion);
                booking1 = em.merge(booking1);
            }
            if (excursion1 != null) {
                excursion1.getBookingExcursionList().add(bookingExcursion);
                excursion1 = em.merge(excursion1);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findBookingExcursion(bookingExcursion.getBookingExcursionPK()) != null) {
                throw new PreexistingEntityException("BookingExcursion " + bookingExcursion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookingExcursion bookingExcursion) throws NonexistentEntityException, RollbackFailureException, Exception {
        bookingExcursion.getBookingExcursionPK().setBooking(bookingExcursion.getBooking1().getId());
        bookingExcursion.getBookingExcursionPK().setExcursion(bookingExcursion.getExcursion1().getId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            BookingExcursion persistentBookingExcursion = em.find(BookingExcursion.class, bookingExcursion.getBookingExcursionPK());
            Booking booking1Old = persistentBookingExcursion.getBooking1();
            Booking booking1New = bookingExcursion.getBooking1();
            Excursion excursion1Old = persistentBookingExcursion.getExcursion1();
            Excursion excursion1New = bookingExcursion.getExcursion1();
            if (booking1New != null) {
                booking1New = em.getReference(booking1New.getClass(), booking1New.getId());
                bookingExcursion.setBooking1(booking1New);
            }
            if (excursion1New != null) {
                excursion1New = em.getReference(excursion1New.getClass(), excursion1New.getId());
                bookingExcursion.setExcursion1(excursion1New);
            }
            bookingExcursion = em.merge(bookingExcursion);
            if (booking1Old != null && !booking1Old.equals(booking1New)) {
                booking1Old.getBookingExcursionList().remove(bookingExcursion);
                booking1Old = em.merge(booking1Old);
            }
            if (booking1New != null && !booking1New.equals(booking1Old)) {
                booking1New.getBookingExcursionList().add(bookingExcursion);
                booking1New = em.merge(booking1New);
            }
            if (excursion1Old != null && !excursion1Old.equals(excursion1New)) {
                excursion1Old.getBookingExcursionList().remove(bookingExcursion);
                excursion1Old = em.merge(excursion1Old);
            }
            if (excursion1New != null && !excursion1New.equals(excursion1Old)) {
                excursion1New.getBookingExcursionList().add(bookingExcursion);
                excursion1New = em.merge(excursion1New);
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
                BookingExcursionPK id = bookingExcursion.getBookingExcursionPK();
                if (findBookingExcursion(id) == null) {
                    throw new NonexistentEntityException("The bookingExcursion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BookingExcursionPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            BookingExcursion bookingExcursion;
            try {
                bookingExcursion = em.getReference(BookingExcursion.class, id);
                bookingExcursion.getBookingExcursionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookingExcursion with id " + id + " no longer exists.", enfe);
            }
            Booking booking1 = bookingExcursion.getBooking1();
            if (booking1 != null) {
                booking1.getBookingExcursionList().remove(bookingExcursion);
                booking1 = em.merge(booking1);
            }
            Excursion excursion1 = bookingExcursion.getExcursion1();
            if (excursion1 != null) {
                excursion1.getBookingExcursionList().remove(bookingExcursion);
                excursion1 = em.merge(excursion1);
            }
            em.remove(bookingExcursion);
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

    public List<BookingExcursion> findBookingExcursionEntities() {
        return findBookingExcursionEntities(true, -1, -1);
    }

    public List<BookingExcursion> findBookingExcursionEntities(int maxResults, int firstResult) {
        return findBookingExcursionEntities(false, maxResults, firstResult);
    }

    private List<BookingExcursion> findBookingExcursionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookingExcursion.class));
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

    public BookingExcursion findBookingExcursion(BookingExcursionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookingExcursion.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookingExcursionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookingExcursion> rt = cq.from(BookingExcursion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
