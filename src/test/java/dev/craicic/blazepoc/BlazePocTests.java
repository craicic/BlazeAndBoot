package dev.craicic.blazepoc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlazePocTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void fillData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Post post = new Post();
        post.setTitle("Blaze Persistence in Boot app");
        em.persist(post);

        Image image = new Image();
        image.setPost(post);
        em.persist(image);

        ImageBlob blob = new ImageBlob();
        blob.setContent("A huge succession of random characters !!!".getBytes());
        blob.setImage(image);
        em.persist(blob);

        Post post2 = em.find(Post.class, post.getId());
        Image image2 = em.find(Image.class, image.getId());
        ImageBlob blob2 = em.find(ImageBlob.class, blob.getId());
        byte[] content = blob2.getContent();
        log.info(content);
        log.info(blob2.toString());

        em.getTransaction().commit();
        em.close();

        emf.close();
    }

}
