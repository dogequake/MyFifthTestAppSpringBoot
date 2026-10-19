package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Response;

import java.util.UUID;

@Service
@Qualifier("ModifyOperationUidResponseService")
public class ModifyOperationUidResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setOperationUid(UUID.randomUUID().toString());
        return response;
    }
}