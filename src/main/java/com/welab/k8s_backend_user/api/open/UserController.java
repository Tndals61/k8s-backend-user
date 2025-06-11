package com.welab.k8s_backend_user.api.open;

import com.welab.k8s_backend_user.remote.alim.RemoteAlimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final RemoteAlimService remoteAlimService;

    @GetMapping(value = "/test")
    public String test() {
        String remoteMessage = remoteAlimService.hello().getData();

        return "Welcome to user service: Remote Message = " + remoteMessage;
    }
}
