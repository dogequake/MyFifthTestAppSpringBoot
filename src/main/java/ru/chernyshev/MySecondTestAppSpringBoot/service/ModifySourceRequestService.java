package ru.chernyshev.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
@Qualifier("ModifySourceRequestService")
public class ModifySourceRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        request.setSource("Service 1");
        log.info("В request изменено поле source: {}", request.getSource());
    }
}