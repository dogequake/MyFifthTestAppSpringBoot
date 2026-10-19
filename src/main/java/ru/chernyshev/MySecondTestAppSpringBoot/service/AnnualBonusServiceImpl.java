package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        return calculate(positions, salary, bonus, workDays, Year.now().getValue());
    }

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays, int year) {
        int daysInYear = Year.isLeap(year) ? 366 : 365;
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterly(Positions positions, double salary, double bonus) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException(
                    "Квартальная премия рассчитывается только для менеджеров, а не для " + positions);
        }
        return salary * bonus * positions.getPositionCoefficient() / 4;
    }
}