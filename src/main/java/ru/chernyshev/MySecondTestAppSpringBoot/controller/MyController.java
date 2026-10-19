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
import ru.chernyshev.MySecondTestAppSpringBoot.service.AnnualBonusService;
import ru.chernyshev.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.chernyshev.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.chernyshev.MySecondTestAppSpringBoot.service.ValidationService;
import ru.chernyshev.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySourceRequestService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService")
                        ModifyResponseService modifyResponseService,
                        @Qualifier("ModifySourceRequestService")
                        ModifyRequestService modifySourceRequestService,
                        @Qualifier("ModifySystemNameRequestService")
                        ModifyRequestService modifySystemNameRequestService,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.annualBonusService = annualBonusService;
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

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uid 123 не поддерживается");
            }
            modifySourceRequestService.modify(request);
            modifySystemNameRequestService.modify(request);
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
            bindingResult.getFieldErrors().forEach(error ->
                    log.error("bindingResult: поле '{}' – {}", error.getField(), error.getDefaultMessage()));
            return failed(response, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION,
                    HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый uid: {}", e.getMessage());
            return failed(response, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED,
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage());
            return failed(response, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setAnnualBonus(annualBonusService.calculate(request.getPosition(),
                request.getSalary(), request.getBonus(), request.getWorkDays()));

        Response modified = modifyResponseService.modify(response);
        log.info("Отправлен response: {}", modified);

        return new ResponseEntity<>(modified, HttpStatus.OK);
    }

    private ResponseEntity<Response> failed(Response response, ErrorCodes errorCode,
                                            ErrorMessages errorMessage, HttpStatus status) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.info("Response изменён (ошибка): {}", response);
        return new ResponseEntity<>(response, status);
    }
}