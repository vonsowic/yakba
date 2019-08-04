package io.bearcave.yakba;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dto.CreateUserRQ;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.BoardAccessLevel;
import io.bearcave.yakba.models.User;
import io.bearcave.yakba.models.UserBoardAccess;
import io.bearcave.yakba.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBoardIntegrationTest extends AbstractIntegrationTest {

    protected Board board;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        this.board = new Board("TEST_BOARD", TESTER_ID);
        board.setAccesses(List.of(board.getAccesses().get(0), new UserBoardAccess(TESTER2_ID)));
        updateTestBoard();
    }

    protected Mono<User> createUser(String username) {
        var user = new CreateUserRQ();
        user.setUsername(username);
        user.setEmail(username + "@yakba.com");
        user.setPassword("Password123@");
        return userService.registerNewUser(user);
    }

    protected UserBoardAccess addUserToBoard(User user) {
        var userBoardAccess = new UserBoardAccess();
        userBoardAccess.setUserId(user.getUsername());
        userBoardAccess.setAccessLevel(BoardAccessLevel.USER);

        var newUsersList = new ArrayList<>(board.getAccesses());
        newUsersList.add(userBoardAccess);
        board.setAccesses(newUsersList);
        updateTestBoard();

        return userBoardAccess;
    }

    protected void updateTestBoard() {
        boardRepository.save(board)
                .block();
    }
}
