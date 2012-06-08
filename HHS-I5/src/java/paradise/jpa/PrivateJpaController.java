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
import paradise.model.Booking;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.PreexistingEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Private;

/**
 *
 * @author Philipp
 */
public class PrivateJpaController implements Serializable {

    public PrivateJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Private private1) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (private1.getBookingList() == null) {
            private1.setBookingList(new ArrayList<Booking>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Booking> attachedBookingList = new ArrayList<Booking>();
            for (Booking bookingListBookingToAttach : private1.getBookingList()) {
                bookingListBookingToAttach = em.getReference(bookingListBookingToAttach.getClass(), bookingListBookingToAttach.getId());
                attachedBookingList.add(bookingListBookingToAttach);
            }
            private1.setBookingList(attachedBookingList);
            em.persist(private1);
            for (Booking bookingListBooking : private1.getBookingList()) {
                Private oldPrivate1OfBookingListBooking = bookingListBooking.getPrivate1();
                bookingListBooking.setPrivate1(private1);
                bookingListBooking = em.merge(bookingListBooking);
                if (oldPrivate1OfBookingListBooking != null) {
                    oldPrivate1OfBookingListBooking.getBookingList().remove(bookingListBooking);
                    oldPrivate1OfBookingListBooking = em.merge(oldPrivate1OfBookingListBooking);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPrivate(private1.getCustomer()) != null) {
                throw new PreexistingEntityException("Private " + private1 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Private private1) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Private persistentPrivate = em.find(Private.class, private1.getCustomer());
            List<Booking> bookingListOld = persistentPrivate.getBookingList();
            List<Booking> bookingListNew = private1.getBookingList();
            List<String> illegalOrphanMessages = null;
            for (Booking bookingListOldBooking : bookingListOld) {
                if (!bookingListNew.contains(bookingListOldBooking)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Booking " + bookingListOldBooking + " since its private1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Booking> attachedBookingListNew = new ArrayList<Booking>();
            for (Booking bookingListNewBookingToAttach : bookingListNew) {
                bookingListNewBookingToAttach = em.getReference(bookingListNewBookingToAttach.getClass(), bookingListNewBookingToAttach.getId());
                attachedBookingListNew.add(bookingListNewBookingToAttach);
            }
            bookingListNew = attachedBookingListNew;
            private1.setBookingList(bookingListNew);
            private1 = em.merge(private1);
            for (Booking bookingListNewBooking : bookingListNew) {
                if (!bookingListOld.contains(bookingListNewBooking)) {
                    Private oldPrivate1OfBookingListNewBooking = bookingListNewBooking.getPrivate1();
                    bookingListNewBooking.setPrivate1(private1);
                    bookingListNewBooking = em.merge(bookingListNewBooking);
                    if (oldPrivate1OfBookingListNewBooking != null && !oldPrivate1OfBookingListNewBooking.equals(private1)) {
                        oldPrivate1OfBookingListNewBooking.getBookingList().remove(bookingListNewBooking);
                        oldPrivate1OfBookingListNewBooking = em.merge(oldPrivate1OfBookingListNewBooking);
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
                Integer id = private1.getCustomer();
                if (findPrivate(id) == null) {
                    throw new NonexistentEntityException("The private with id " + id + " no longer exists.");
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
            Private private1;
            try {
                private1 = em.getReference(Private.class, id);
                private1.getCustomer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The private1 with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Booking> bookingListOrphanCheck = private1.getBookingList();
            for (Booking bookingListOrphanCheckBooking : bookingListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Private (" + private1 + ") cannot be destroyed since the Booking " + bookingListOrphanCheckBooking + " in its bookingList field has a non-nullable private1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(private1);
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

    public List<Private> findPrivateEntities() {
        return findPrivateEntities(true, -1, -1);
    }

    public List<Private> findPrivateEntities(int maxResults, int firstResult) {
        return findPrivateEntities(false, maxResults, firstResult);
    }

    private List<Private> findPrivateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Private.class));
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

    public Private findPrivate(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Private.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrivateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Private> rt = cq.from(Private.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
