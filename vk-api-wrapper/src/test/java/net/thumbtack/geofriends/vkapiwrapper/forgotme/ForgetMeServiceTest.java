package net.thumbtack.geofriends.vkapiwrapper.forgotme;

import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ForgetMeServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Test
    public void forgetMe_mustCallDeleteFromStorage() {
        ForgetMeService forgetMeService = new ForgetMeService(sessionRepository);

        forgetMeService.forgetMe(TestHelper.TEST_SESSION_ID);

        verify(sessionRepository).deleteById(eq(TestHelper.TEST_SESSION_ID));
    }
}