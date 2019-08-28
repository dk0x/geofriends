package net.thumbtack.geofriends.vkapiwrapper.forgetme;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionRepository;
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
