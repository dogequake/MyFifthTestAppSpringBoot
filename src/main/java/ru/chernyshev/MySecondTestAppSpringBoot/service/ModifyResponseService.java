package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}