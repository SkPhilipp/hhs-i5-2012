/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paradise.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.IllegalOrphanException;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.ExcursionType;
import paradise.model.Product;
import paradise.model.Trip;
import paradise.model.TripType;

/**
 *
 * @author Philipp
 */
public class TripTypeJpaController implements Serializable {

    public TripTypeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TripType tripType) throws RollbackFailureException, Exception {
        if (tripType.getProductList() == null) {
            tripType.setProductList(new ArrayList<Product>());
        }
        if (tripType.getExcursionTypeList() == null) {
            tripType.setExcursionTypeList(new ArrayList<ExcursionType>());
        }
        if (tripType.getTripList() == null) {
            tripType.setTripList(new ArrayList<Trip>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : tripType.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            tripType.setProductList(attachedProductList);
            List<ExcursionType> attachedExcursionTypeList = new ArrayList<ExcursionType>();
            for (ExcursionType excursionTypeListExcursionTypeToAttach : tripType.getExcursionTypeList()) {
                excursionTypeListExcursionTypeToAttach = em.getReference(excursionTypeListExcursionTypeToAttach.getClass(), excursionTypeListExcursionTypeToAttach.getId());
                attachedExcursionTypeList.add(excursionTypeListExcursionTypeToAttach);
            }
            tripType.setExcursionTypeList(attachedExcursionTypeList);
            List<Trip> attachedTripList = new ArrayList<Trip>();
            for (Trip tripListTripToAttach : tripType.getTripList()) {
                tripListTripToAttach = em.getReference(tripListTripToAttach.getClass(), tripListTripToAttach.getId());
                attachedTripList.add(tripListTripToAttach);
            }
            tripType.setTripList(attachedTripList);
            em.persist(tripType);
            for (Product productListProduct : tripType.getProductList()) {
                productListProduct.getTripTypeList().add(tripType);
                productListProduct = em.merge(productListProduct);
            }
            for (ExcursionType excursionTypeListExcursionType : tripType.getExcursionTypeList()) {
                TripType oldTripTypeOfExcursionTypeListExcursionType = excursionTypeListExcursionType.getTripType();
                excursionTypeListExcursionType.setTripType(tripType);
                excursionTypeListExcursionType = em.merge(excursionTypeListExcursionType);
                if (oldTripTypeOfExcursionTypeListExcursionType != null) {
                    oldTripTypeOfExcursionTypeListExcursionType.getExcursionTypeList().remove(excursionTypeListExcursionType);
                    oldTripTypeOfExcursionTypeListExcursionType = em.merge(oldTripTypeOfExcursionTypeListExcursionType);
                }
            }
            for (Trip tripListTrip : tripType.getTripList()) {
                TripType oldTripTypeOfTripListTrip = tripListTrip.getTripType();
                tripListTrip.setTripType(tripType);
                tripListTrip = em.merge(tripListTrip);
                if (oldTripTypeOfTripListTrip != null) {
                    oldTripTypeOfTripListTrip.getTripList().remove(tripListTrip);
                    oldTripTypeOfTripListTrip = em.merge(oldTripTypeOfTripListTrip);
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

    public void edit(TripType tripType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TripType persistentTripType = em.find(TripType.class, tripType.getId());
            List<Product> productListOld = persistentTripType.getProductList();
            List<Product> productListNew = tripType.getProductList();
            List<ExcursionType> excursionTypeListOld = persistentTripType.getExcursionTypeList();
            List<ExcursionType> excursionTypeListNew = tripType.getExcursionTypeList();
            List<Trip> tripListOld = persistentTripType.getTripList();
            List<Trip> tripListNew = tripType.getTripList();
            List<String> illegalOrphanMessages = null;
            for (ExcursionType excursionTypeListOldExcursionType : excursionTypeListOld) {
                if (!excursionTypeListNew.contains(excursionTypeListOldExcursionType)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExcursionType " + excursionTypeListOldExcursionType + " since its tripType field is not nullable.");
                }
            }
            for (Trip tripListOldTrip : tripListOld) {
                if (!tripListNew.contains(tripListOldTrip)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trip " + tripListOldTrip + " since its tripType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            tripType.setProductList(productListNew);
            List<ExcursionType> attachedExcursionTypeListNew = new ArrayList<ExcursionType>();
            for (ExcursionType excursionTypeListNewExcursionTypeToAttach : excursionTypeListNew) {
                excursionTypeListNewExcursionTypeToAttach = em.getReference(excursionTypeListNewExcursionTypeToAttach.getClass(), excursionTypeListNewExcursionTypeToAttach.getId());
                attachedExcursionTypeListNew.add(excursionTypeListNewExcursionTypeToAttach);
            }
            excursionTypeListNew = attachedExcursionTypeListNew;
            tripType.setExcursionTypeList(excursionTypeListNew);
            List<Trip> attachedTripListNew = new ArrayList<Trip>();
            for (Trip tripListNewTripToAttach : tripListNew) {
                tripListNewTripToAttach = em.getReference(tripListNewTripToAttach.getClass(), tripListNewTripToAttach.getId());
                attachedTripListNew.add(tripListNewTripToAttach);
            }
            tripListNew = attachedTripListNew;
            tripType.setTripList(tripListNew);
            tripType = em.merge(tripType);
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    productListOldProduct.getTripTypeList().remove(tripType);
                    productListOldProduct = em.merge(productListOldProduct);
                }
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    productListNewProduct.getTripTypeList().add(tripType);
                    productListNewProduct = em.merge(productListNewProduct);
                }
            }
            for (ExcursionType excursionTypeListNewExcursionType : excursionTypeListNew) {
                if (!excursionTypeListOld.contains(excursionTypeListNewExcursionType)) {
                    TripType oldTripTypeOfExcursionTypeListNewExcursionType = excursionTypeListNewExcursionType.getTripType();
                    excursionTypeListNewExcursionType.setTripType(tripType);
                    excursionTypeListNewExcursionType = em.merge(excursionTypeListNewExcursionType);
                    if (oldTripTypeOfExcursionTypeListNewExcursionType != null && !oldTripTypeOfExcursionTypeListNewExcursionType.equals(tripType)) {
                        oldTripTypeOfExcursionTypeListNewExcursionType.getExcursionTypeList().remove(excursionTypeListNewExcursionType);
                        oldTripTypeOfExcursionTypeListNewExcursionType = em.merge(oldTripTypeOfExcursionTypeListNewExcursionType);
                    }
                }
            }
            for (Trip tripListNewTrip : tripListNew) {
                if (!tripListOld.contains(tripListNewTrip)) {
                    TripType oldTripTypeOfTripListNewTrip = tripListNewTrip.getTripType();
                    tripListNewTrip.setTripType(tripType);
                    tripListNewTrip = em.merge(tripListNewTrip);
                    if (oldTripTypeOfTripListNewTrip != null && !oldTripTypeOfTripListNewTrip.equals(tripType)) {
                        oldTripTypeOfTripListNewTrip.getTripList().remove(tripListNewTrip);
                        oldTripTypeOfTripListNewTrip = em.merge(oldTripTypeOfTripListNewTrip);
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
                Integer id = tripType.getId();
                if (findTripType(id) == null) {
                    throw new NonexistentEntityException("The tripType with id " + id + " no longer exists.");
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
            TripType tripType;
            try {
                tripType = em.getReference(TripType.class, id);
                tripType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tripType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ExcursionType> excursionTypeListOrphanCheck = tripType.getExcursionTypeList();
            for (ExcursionType excursionTypeListOrphanCheckExcursionType : excursionTypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TripType (" + tripType + ") cannot be destroyed since the ExcursionType " + excursionTypeListOrphanCheckExcursionType + " in its excursionTypeList field has a non-nullable tripType field.");
            }
            List<Trip> tripListOrphanCheck = tripType.getTripList();
            for (Trip tripListOrphanCheckTrip : tripListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TripType (" + tripType + ") cannot be destroyed since the Trip " + tripListOrphanCheckTrip + " in its tripList field has a non-nullable tripType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Product> productList = tripType.getProductList();
            for (Product productListProduct : productList) {
                productListProduct.getTripTypeList().remove(tripType);
                productListProduct = em.merge(productListProduct);
            }
            em.remove(tripType);
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

    public List<TripType> findTripTypeEntities() {
        return findTripTypeEntities(true, -1, -1);
    }

    public List<TripType> findTripTypeEntities(int maxResults, int firstResult) {
        return findTripTypeEntities(false, maxResults, firstResult);
    }

    private List<TripType> findTripTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TripType.class));
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

    public TripType findTripType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TripType.class, id);
        } finally {
            em.close();
        }
    }

    public int getTripTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TripType> rt = cq.from(TripType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
