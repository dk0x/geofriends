package net.thumbtack.geofriends.controller;

import net.thumbtack.geofriends.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
//    private final static Logger LOGGER = LoggerFactory.getLogger(VkApiController.class);

    private CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }


}
