package com.id1212.hw3.server.integration;

import com.id1212.hw3.server.model.CatalogFile;
import com.id1212.hw3.server.model.Participant;
import java.util.List;
import javax.persistence.*;


// This object encapsulates all database calls in the catalog application
public class CatalogDAO {

    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<EntityManager>();

    // Constructor
    public CatalogDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("CatalogPU");
    }

    private EntityManager beginTransaction() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        threadLocalEntityManager.set(entityManager);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        if(!entityTransaction.isActive()) {
            entityTransaction.begin();
        }
        return entityManager;
    }
    
    private void commitTransaction() {
        threadLocalEntityManager.get().getTransaction().commit();
    }
    
    public void update() {
        commitTransaction();
    }


    // Create new account
    public void createAcc(Participant participant) {
        try {
            EntityManager entitymanager = beginTransaction();
            entitymanager.persist(participant);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commitTransaction();
        }
    }

    // Find account by name
    public Participant findAccByName(String name) {
        try {
            EntityManager entitymanager = beginTransaction();
            return (Participant) entitymanager.createNamedQuery("findAccByName").setParameter("username", name).getSingleResult();
        } catch (Exception e) {
            return null;
        } 
        finally {
            commitTransaction();
        }
    }

    //////////////////////////////////////////////////////////////////////

    // Create file
    public void createCatalogFile(CatalogFile catalogFile) {
        try {
            EntityManager entitymanager = beginTransaction();
            entitymanager.persist(catalogFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            commitTransaction();
        }
    }

    // Retrieve all files
    public List<CatalogFile> retrieveFiles() {
        try {
            EntityManager entitymanager = beginTransaction();
            return entitymanager.createNamedQuery("findAllFiles").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            commitTransaction();
        }
    }

    // Retrieve files by user name
    public List<CatalogFile> findFileByOwner(String owner) {
        try {
            EntityManager entitymanager = beginTransaction();
            return entitymanager.createNamedQuery("findFileByOwner").setParameter("owner", owner).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            commitTransaction();
        }
    }

    // Retrieve file by name
    public CatalogFile findFileByName(String name) {
        try {
            EntityManager entitymanager = beginTransaction();
            return (CatalogFile) entitymanager.createNamedQuery("findFileByName").setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
        finally {
            commitTransaction();
        }
    }

    // update file size
    public void updateFileSize(String name, long size) {
        try {
            EntityManager entitymanager = beginTransaction();
            CatalogFile file = (CatalogFile) entitymanager.createNamedQuery("findFileByName").setParameter("name", name).getSingleResult();
            file.setSize(size);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            commitTransaction();
        }
    }

    // Delete file
    public void deleteFileByName(String name) {
        try {
            EntityManager entitymanager = beginTransaction();
            CatalogFile file = (CatalogFile) entitymanager.createNamedQuery("findFileByName").setParameter("name", name).getSingleResult();
            entitymanager.remove(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            commitTransaction();
        }
    }
}