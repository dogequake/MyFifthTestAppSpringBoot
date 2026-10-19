package ru.chernyshev.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.chernyshev.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.chernyshev.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Codes;
import ru.chernyshev.MySecondTestAppSpringBoot.model.ErrorCodes;
import ru.chernyshev.MySecondTestAppSpringBoot.model.ErrorMessages;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Request;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Response;
import ru.chernyshev.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.chernyshev.MySecondTestAppSpringBoot.service.ValidationService;
import ru.chernyshev.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService")
                        ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("Получен request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Сформирован начальный response: {}", response);

        try {
            validationService.isValid(bindingResult);
            log.info("Валидация request пройдена успешно");
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uid 123 не поддерживается");
            }
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
            bindingResult.getFieldErrors().forEach(error ->
                    log.error("bindingResult: поле '{}' – {}", error.getField(), error.getDefaultMessage()));
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response изменён (ошибка валидации): {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый uid: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response изменён (неподдерживаемый uid): {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response изменён (непредвиденная ошибка): {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Response modified = modifyResponseService.modify(response);
        log.info("Response после modifyResponseService: {}", modified);

        return new ResponseEntity<>(modified, HttpStatus.OK);
    }
}