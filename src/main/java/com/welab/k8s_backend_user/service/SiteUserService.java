package com.welab.k8s_backend_user.service;


import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.NotFound;
import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.domain.dto.SiteUserLoginDto;
import com.welab.k8s_backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.domain.event.SiteUserInfoEvent;
import com.welab.k8s_backend_user.domain.repository.SiteUserRepository;
import com.welab.k8s_backend_user.event.producer.KafkaEventProducer;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import com.welab.k8s_backend_user.secret.jwt.TokenGenerator;
import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto){
        SiteUser siteUser = registerDto.toEntity();

        siteUserRepository.save(siteUser);

        SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity("Create", siteUser);
        kafkaEventProducer.send(SiteUserInfoEvent.Topic, event);
    }

    @Transactional(readOnly = true)
    public TokenDto.AcessRefreshToken login(SiteUserLoginDto loginDto) {
        SiteUser siteUser = siteUserRepository.findByUserId(loginDto.getUserId());
        if (siteUser == null) {
            throw new NotFound("User Not Found");
        }

        if (!SecureHashUtils.matches(loginDto.getPassword(), siteUser.getPassword())) {
            throw new BadParameter("User Not Found");
        }

        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");
    }
}
