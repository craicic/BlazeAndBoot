package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostFragmentRepositoryImpl implements PostFragmentRepository {

    @PersistenceContext
    private final EntityManager em;


    public PostFragmentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

}
