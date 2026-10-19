package ru.chernyshev.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Уникальный идентификатор сообщения
     */
    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32)
    private String uid;

    /**
     * Уникальный идентификатор операции
     */
    @NotBlank
    @Size(max = 32)
    private String operationUid;

    /**
     * Имя системы отправителя
     */
    private Systems systemName;

    /**
     * Время создания сообщения
     */
    @NotBlank
    private String systemTime;

    /**
     * Наименование ресурса
     */
    private String source;

    /**
     * Должность сотрудника
     */
    @NotNull(message = "position не может быть пустым")
    private Positions position;

    /**
     * Оклад сотрудника
     */
    @NotNull(message = "salary не может быть пустым")
    private Double salary;

    /**
     * Коэффициент премии
     */
    @NotNull(message = "bonus не может быть пустым")
    private Double bonus;

    /**
     * Количество рабочих дней в году
     */
    @NotNull(message = "workDays не может быть пустым")
    @Min(1)
    @Max(366)
    private Integer workDays;

    /**
     * Уникальный идентификатор коммуникации
     */
    @Min(1)
    @Max(100000)
    private Integer communicationId;

    /**
     * Уникальный идентификатор шаблона
     */
    private Integer templateId;

    /**
     * Код продукта
     */
    private Integer productCode;

    /**
     * Смс код
     */
    private Integer smsCode;

    @Override
    public String toString() {
        return "{uid='" + uid + "', operationUid='" + operationUid + "', systemName=" + systemName
                + ", systemTime='" + systemTime + "', source='" + source + "', position=" + position
                + ", salary=" + salary + ", bonus=" + bonus + ", workDays=" + workDays
                + ", communicationId=" + communicationId + ", templateId=" + templateId
                + ", productCode=" + productCode + ", smsCode=" + smsCode + "}";
    }
}