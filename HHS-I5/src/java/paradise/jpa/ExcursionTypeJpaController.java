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
import paradise.model.Excursion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.ExcursionType;

/**
 *
 * @author Philipp
 */
public class ExcursionTypeJpaController implements Serializable {

    public ExcursionTypeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExcursionType excursionType) throws RollbackFailureException, Exception {
        if (excursionType.getExcursionList() == null) {
            excursionType.setExcursionList(new ArrayList<Excursion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TripType tripType = excursionType.getTripType();
            if (tripType != null) {
                tripType = em.getReference(tripType.getClass(), tripType.getId());
                excursionType.setTripType(tripType);
            }
            List<Excursion> attachedExcursionList = new ArrayList<Excursion>();
            for (Excursion excursionListExcursionToAttach : excursionType.getExcursionList()) {
                excursionListExcursionToAttach = em.getReference(excursionListExcursionToAttach.getClass(), excursionListExcursionToAttach.getId());
                attachedExcursionList.add(excursionListExcursionToAttach);
            }
            excursionType.setExcursionList(attachedExcursionList);
            em.persist(excursionType);
            if (tripType != null) {
                tripType.getExcursionTypeList().add(excursionType);
                tripType = em.merge(tripType);
            }
            for (Excursion excursionListExcursion : excursionType.getExcursionList()) {
                ExcursionType oldExcursionTypeOfExcursionListExcursion = excursionListExcursion.getExcursionType();
                excursionListExcursion.setExcursionType(excursionType);
                excursionListExcursion = em.merge(excursionListExcursion);
                if (oldExcursionTypeOfExcursionListExcursion != null) {
                    oldExcursionTypeOfExcursionListExcursion.getExcursionList().remove(excursionListExcursion);
                    oldExcursionTypeOfExcursionListExcursion = em.merge(oldExcursionTypeOfExcursionListExcursion);
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

    public void edit(ExcursionType excursionType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ExcursionType persistentExcursionType = em.find(ExcursionType.class, excursionType.getId());
            TripType tripTypeOld = persistentExcursionType.getTripType();
            TripType tripTypeNew = excursionType.getTripType();
            List<Excursion> excursionListOld = persistentExcursionType.getExcursionList();
            List<Excursion> excursionListNew = excursionType.getExcursionList();
            List<String> illegalOrphanMessages = null;
            for (Excursion excursionListOldExcursion : excursionListOld) {
                if (!excursionListNew.contains(excursionListOldExcursion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Excursion " + excursionListOldExcursion + " since its excursionType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tripTypeNew != null) {
                tripTypeNew = em.getReference(tripTypeNew.getClass(), tripTypeNew.getId());
                excursionType.setTripType(tripTypeNew);
            }
            List<Excursion> attachedExcursionListNew = new ArrayList<Excursion>();
            for (Excursion excursionListNewExcursionToAttach : excursionListNew) {
                excursionListNewExcursionToAttach = em.getReference(excursionListNewExcursionToAttach.getClass(), excursionListNewExcursionToAttach.getId());
                attachedExcursionListNew.add(excursionListNewExcursionToAttach);
            }
            excursionListNew = attachedExcursionListNew;
            excursionType.setExcursionList(excursionListNew);
            excursionType = em.merge(excursionType);
            if (tripTypeOld != null && !tripTypeOld.equals(tripTypeNew)) {
                tripTypeOld.getExcursionTypeList().remove(excursionType);
                tripTypeOld = em.merge(tripTypeOld);
            }
            if (tripTypeNew != null && !tripTypeNew.equals(tripTypeOld)) {
                tripTypeNew.getExcursionTypeList().add(excursionType);
                tripTypeNew = em.merge(tripTypeNew);
            }
            for (Excursion excursionListNewExcursion : excursionListNew) {
                if (!excursionListOld.contains(excursionListNewExcursion)) {
                    ExcursionType oldExcursionTypeOfExcursionListNewExcursion = excursionListNewExcursion.getExcursionType();
                    excursionListNewExcursion.setExcursionType(excursionType);
                    excursionListNewExcursion = em.merge(excursionListNewExcursion);
                    if (oldExcursionTypeOfExcursionListNewExcursion != null && !oldExcursionTypeOfExcursionListNewExcursion.equals(excursionType)) {
                        oldExcursionTypeOfExcursionListNewExcursion.getExcursionList().remove(excursionListNewExcursion);
                        oldExcursionTypeOfExcursionListNewExcursion = em.merge(oldExcursionTypeOfExcursionListNewExcursion);
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
                Integer id = excursionType.getId();
                if (findExcursionType(id) == null) {
                    throw new NonexistentEntityException("The excursionType with id " + id + " no longer exists.");
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
            ExcursionType excursionType;
            try {
                excursionType = em.getReference(ExcursionType.class, id);
                excursionType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The excursionType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Excursion> excursionListOrphanCheck = excursionType.getExcursionList();
            for (Excursion excursionListOrphanCheckExcursion : excursionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ExcursionType (" + excursionType + ") cannot be destroyed since the Excursion " + excursionListOrphanCheckExcursion + " in its excursionList field has a non-nullable excursionType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TripType tripType = excursionType.getTripType();
            if (tripType != null) {
                tripType.getExcursionTypeList().remove(excursionType);
                tripType = em.merge(tripType);
            }
            em.remove(excursionType);
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

    public List<ExcursionType> findExcursionTypeEntities() {
        return findExcursionTypeEntities(true, -1, -1);
    }

    public List<ExcursionType> findExcursionTypeEntities(int maxResults, int firstResult) {
        return findExcursionTypeEntities(false, maxResults, firstResult);
    }

    private List<ExcursionType> findExcursionTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExcursionType.class));
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

    public ExcursionType findExcursionType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExcursionType.class, id);
        } finally {
            em.close();
        }
    }

    public int getExcursionTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExcursionType> rt = cq.from(ExcursionType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
