package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}