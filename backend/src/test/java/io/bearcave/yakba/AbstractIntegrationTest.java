package io.bearcave.yakba;

import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.User;
import io.bearcave.yakba.models.UserBoardAccess;
import io.bearcave.yakba.security.YakbaUserDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YakbaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    protected static final String TESTER_ID = "ID";

    private User tester;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        dropAll();

        tester = new User();
        tester.setId(TESTER_ID);
        tester.setUsername("test");
        tester.setPassword("password");
        userRepository.insert(tester).block();
    }

    @AfterEach
    public void cleanup() {
        dropAll();
    }

    private void dropAll() {
        mongoTemplate.dropCollection(Board.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(UserBoardAccess.class);
    }

    protected User getTester() {
        return tester;
    }
}
