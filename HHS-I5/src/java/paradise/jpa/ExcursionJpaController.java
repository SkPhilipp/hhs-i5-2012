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
import paradise.model.ExcursionType;
import paradise.model.Trip;
import paradise.model.BookingExcursion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Excursion;

/**
 *
 * @author Philipp
 */
public class ExcursionJpaController implements Serializable {

    public ExcursionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Excursion excursion) throws RollbackFailureException, Exception {
        if (excursion.getBookingExcursionList() == null) {
            excursion.setBookingExcursionList(new ArrayList<BookingExcursion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ExcursionType excursionType = excursion.getExcursionType();
            if (excursionType != null) {
                excursionType = em.getReference(excursionType.getClass(), excursionType.getId());
                excursion.setExcursionType(excursionType);
            }
            Trip trip = excursion.getTrip();
            if (trip != null) {
                trip = em.getReference(trip.getClass(), trip.getId());
                excursion.setTrip(trip);
            }
            List<BookingExcursion> attachedBookingExcursionList = new ArrayList<BookingExcursion>();
            for (BookingExcursion bookingExcursionListBookingExcursionToAttach : excursion.getBookingExcursionList()) {
                bookingExcursionListBookingExcursionToAttach = em.getReference(bookingExcursionListBookingExcursionToAttach.getClass(), bookingExcursionListBookingExcursionToAttach.getBookingExcursionPK());
                attachedBookingExcursionList.add(bookingExcursionListBookingExcursionToAttach);
            }
            excursion.setBookingExcursionList(attachedBookingExcursionList);
            em.persist(excursion);
            if (excursionType != null) {
                excursionType.getExcursionList().add(excursion);
                excursionType = em.merge(excursionType);
            }
            if (trip != null) {
                trip.getExcursionList().add(excursion);
                trip = em.merge(trip);
            }
            for (BookingExcursion bookingExcursionListBookingExcursion : excursion.getBookingExcursionList()) {
                Excursion oldExcursion1OfBookingExcursionListBookingExcursion = bookingExcursionListBookingExcursion.getExcursion1();
                bookingExcursionListBookingExcursion.setExcursion1(excursion);
                bookingExcursionListBookingExcursion = em.merge(bookingExcursionListBookingExcursion);
                if (oldExcursion1OfBookingExcursionListBookingExcursion != null) {
                    oldExcursion1OfBookingExcursionListBookingExcursion.getBookingExcursionList().remove(bookingExcursionListBookingExcursion);
                    oldExcursion1OfBookingExcursionListBookingExcursion = em.merge(oldExcursion1OfBookingExcursionListBookingExcursion);
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

    public void edit(Excursion excursion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Excursion persistentExcursion = em.find(Excursion.class, excursion.getId());
            ExcursionType excursionTypeOld = persistentExcursion.getExcursionType();
            ExcursionType excursionTypeNew = excursion.getExcursionType();
            Trip tripOld = persistentExcursion.getTrip();
            Trip tripNew = excursion.getTrip();
            List<BookingExcursion> bookingExcursionListOld = persistentExcursion.getBookingExcursionList();
            List<BookingExcursion> bookingExcursionListNew = excursion.getBookingExcursionList();
            List<String> illegalOrphanMessages = null;
            for (BookingExcursion bookingExcursionListOldBookingExcursion : bookingExcursionListOld) {
                if (!bookingExcursionListNew.contains(bookingExcursionListOldBookingExcursion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookingExcursion " + bookingExcursionListOldBookingExcursion + " since its excursion1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (excursionTypeNew != null) {
                excursionTypeNew = em.getReference(excursionTypeNew.getClass(), excursionTypeNew.getId());
                excursion.setExcursionType(excursionTypeNew);
            }
            if (tripNew != null) {
                tripNew = em.getReference(tripNew.getClass(), tripNew.getId());
                excursion.setTrip(tripNew);
            }
            List<BookingExcursion> attachedBookingExcursionListNew = new ArrayList<BookingExcursion>();
            for (BookingExcursion bookingExcursionListNewBookingExcursionToAttach : bookingExcursionListNew) {
                bookingExcursionListNewBookingExcursionToAttach = em.getReference(bookingExcursionListNewBookingExcursionToAttach.getClass(), bookingExcursionListNewBookingExcursionToAttach.getBookingExcursionPK());
                attachedBookingExcursionListNew.add(bookingExcursionListNewBookingExcursionToAttach);
            }
            bookingExcursionListNew = attachedBookingExcursionListNew;
            excursion.setBookingExcursionList(bookingExcursionListNew);
            excursion = em.merge(excursion);
            if (excursionTypeOld != null && !excursionTypeOld.equals(excursionTypeNew)) {
                excursionTypeOld.getExcursionList().remove(excursion);
                excursionTypeOld = em.merge(excursionTypeOld);
            }
            if (excursionTypeNew != null && !excursionTypeNew.equals(excursionTypeOld)) {
                excursionTypeNew.getExcursionList().add(excursion);
                excursionTypeNew = em.merge(excursionTypeNew);
            }
            if (tripOld != null && !tripOld.equals(tripNew)) {
                tripOld.getExcursionList().remove(excursion);
                tripOld = em.merge(tripOld);
            }
            if (tripNew != null && !tripNew.equals(tripOld)) {
                tripNew.getExcursionList().add(excursion);
                tripNew = em.merge(tripNew);
            }
            for (BookingExcursion bookingExcursionListNewBookingExcursion : bookingExcursionListNew) {
                if (!bookingExcursionListOld.contains(bookingExcursionListNewBookingExcursion)) {
                    Excursion oldExcursion1OfBookingExcursionListNewBookingExcursion = bookingExcursionListNewBookingExcursion.getExcursion1();
                    bookingExcursionListNewBookingExcursion.setExcursion1(excursion);
                    bookingExcursionListNewBookingExcursion = em.merge(bookingExcursionListNewBookingExcursion);
                    if (oldExcursion1OfBookingExcursionListNewBookingExcursion != null && !oldExcursion1OfBookingExcursionListNewBookingExcursion.equals(excursion)) {
                        oldExcursion1OfBookingExcursionListNewBookingExcursion.getBookingExcursionList().remove(bookingExcursionListNewBookingExcursion);
                        oldExcursion1OfBookingExcursionListNewBookingExcursion = em.merge(oldExcursion1OfBookingExcursionListNewBookingExcursion);
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
                Integer id = excursion.getId();
                if (findExcursion(id) == null) {
                    throw new NonexistentEntityException("The excursion with id " + id + " no longer exists.");
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
            Excursion excursion;
            try {
                excursion = em.getReference(Excursion.class, id);
                excursion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The excursion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BookingExcursion> bookingExcursionListOrphanCheck = excursion.getBookingExcursionList();
            for (BookingExcursion bookingExcursionListOrphanCheckBookingExcursion : bookingExcursionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Excursion (" + excursion + ") cannot be destroyed since the BookingExcursion " + bookingExcursionListOrphanCheckBookingExcursion + " in its bookingExcursionList field has a non-nullable excursion1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ExcursionType excursionType = excursion.getExcursionType();
            if (excursionType != null) {
                excursionType.getExcursionList().remove(excursion);
                excursionType = em.merge(excursionType);
            }
            Trip trip = excursion.getTrip();
            if (trip != null) {
                trip.getExcursionList().remove(excursion);
                trip = em.merge(trip);
            }
            em.remove(excursion);
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

    public List<Excursion> findExcursionEntities() {
        return findExcursionEntities(true, -1, -1);
    }

    public List<Excursion> findExcursionEntities(int maxResults, int firstResult) {
        return findExcursionEntities(false, maxResults, firstResult);
    }

    private List<Excursion> findExcursionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Excursion.class));
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

    public Excursion findExcursion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Excursion.class, id);
        } finally {
            em.close();
        }
    }

    public int getExcursionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Excursion> rt = cq.from(Excursion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
