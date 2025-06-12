package com.welab.k8s_backend_user.service;


import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.domain.event.SiteUserInfoEvent;
import com.welab.k8s_backend_user.domain.repository.SiteUserRepository;
import com.welab.k8s_backend_user.event.producer.KafkaEventProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final KafkaEventProducer kafkaEventProducer;

    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto){
        SiteUser siteUser = registerDto.toEntity();

        siteUserRepository.save(siteUser);

        SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity("Create", siteUser);
        kafkaEventProducer.send(SiteUserInfoEvent.Topic, event);
    }
}
