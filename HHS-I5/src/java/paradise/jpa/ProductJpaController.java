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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import paradise.jpa.exceptions.NonexistentEntityException;
import paradise.jpa.exceptions.RollbackFailureException;
import paradise.model.Product;

/**
 *
 * @author Philipp
 */
public class ProductJpaController implements Serializable {

    public ProductJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) throws RollbackFailureException, Exception {
        if (product.getTripTypeList() == null) {
            product.setTripTypeList(new ArrayList<TripType>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<TripType> attachedTripTypeList = new ArrayList<TripType>();
            for (TripType tripTypeListTripTypeToAttach : product.getTripTypeList()) {
                tripTypeListTripTypeToAttach = em.getReference(tripTypeListTripTypeToAttach.getClass(), tripTypeListTripTypeToAttach.getId());
                attachedTripTypeList.add(tripTypeListTripTypeToAttach);
            }
            product.setTripTypeList(attachedTripTypeList);
            em.persist(product);
            for (TripType tripTypeListTripType : product.getTripTypeList()) {
                tripTypeListTripType.getProductList().add(product);
                tripTypeListTripType = em.merge(tripTypeListTripType);
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

    public void edit(Product product) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Product persistentProduct = em.find(Product.class, product.getId());
            List<TripType> tripTypeListOld = persistentProduct.getTripTypeList();
            List<TripType> tripTypeListNew = product.getTripTypeList();
            List<TripType> attachedTripTypeListNew = new ArrayList<TripType>();
            for (TripType tripTypeListNewTripTypeToAttach : tripTypeListNew) {
                tripTypeListNewTripTypeToAttach = em.getReference(tripTypeListNewTripTypeToAttach.getClass(), tripTypeListNewTripTypeToAttach.getId());
                attachedTripTypeListNew.add(tripTypeListNewTripTypeToAttach);
            }
            tripTypeListNew = attachedTripTypeListNew;
            product.setTripTypeList(tripTypeListNew);
            product = em.merge(product);
            for (TripType tripTypeListOldTripType : tripTypeListOld) {
                if (!tripTypeListNew.contains(tripTypeListOldTripType)) {
                    tripTypeListOldTripType.getProductList().remove(product);
                    tripTypeListOldTripType = em.merge(tripTypeListOldTripType);
                }
            }
            for (TripType tripTypeListNewTripType : tripTypeListNew) {
                if (!tripTypeListOld.contains(tripTypeListNewTripType)) {
                    tripTypeListNewTripType.getProductList().add(product);
                    tripTypeListNewTripType = em.merge(tripTypeListNewTripType);
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
                Integer id = product.getId();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<TripType> tripTypeList = product.getTripTypeList();
            for (TripType tripTypeListTripType : tripTypeList) {
                tripTypeListTripType.getProductList().remove(product);
                tripTypeListTripType = em.merge(tripTypeListTripType);
            }
            em.remove(product);
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

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
