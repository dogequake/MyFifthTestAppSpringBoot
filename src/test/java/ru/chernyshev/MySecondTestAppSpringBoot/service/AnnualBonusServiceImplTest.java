package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Positions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class AnnualBonusServiceImplTest {

    private final AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();

    @Test
    void calculate() {
        // given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = service.calculate(position, salary, bonus, workDays, 2025);

        // then
        double expected = 360493.8271604938;
        assertThat(result).isEqualTo(expected, within(0.001));
    }

    @Test
    void calculateInLeapYear() {
        double result = service.calculate(Positions.HR, 100000.00, 2.0, 243, 2024);

        assertThat(result).isEqualTo(361481.48148148146, within(0.001));
    }

    @Test
    void calculateQuarterlyForManager() {
        double result = service.calculateQuarterly(Positions.TL, 100000.00, 2.0);

        assertThat(result).isEqualTo(130000.0, within(0.001));
    }

    @Test
    void calculateQuarterlyForNotManager() {
        assertThatThrownBy(() -> service.calculateQuarterly(Positions.DEV, 100000.00, 2.0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}