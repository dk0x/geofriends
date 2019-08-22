package net.thumbtack.geofriends.vkapiwrapper.forgotme;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ForgetMeService {

    private SessionRepository sessionRepository;

    public void forgetMe(String sessionId) {
        log.debug("Enter in ForgetMeService.forgetMe(sessionId = {})", sessionId);

        sessionRepository.deleteById(sessionId);

        log.debug("Exit from ForgetMeService.forgetMe()");
    }

}
