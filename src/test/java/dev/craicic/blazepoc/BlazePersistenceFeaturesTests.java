package dev.craicic.blazepoc;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import dev.craicic.blazepoc.domain.Post;
import dev.craicic.blazepoc.domain.entityview.PostView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BlazePersistenceFeaturesTests {

    Logger log = LogManager.getLogger(this.getClass().getName());

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private CriteriaBuilderFactory cbf;

    @Autowired
    private EntityViewManager evm;

    @Test
    public void GetPostsEntityViewTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        CriteriaBuilder<Post> cb = cbf.create(em, Post.class);
        CriteriaBuilder<PostView> postViewBuilder = evm.applySetting(EntityViewSetting.create(PostView.class), cb);
        List<PostView> posts = postViewBuilder.getResultList();
        posts.forEach(e -> log.info(e.getTitle()));
    }

}
