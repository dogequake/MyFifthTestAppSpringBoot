package ru.chernyshev.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.chernyshev.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface AnnualBonusService {

    double calculate(Positions positions, double salary, double bonus, int workDays);

    double calculate(Positions positions, double salary, double bonus, int workDays, int year);

    double calculateQuarterly(Positions positions, double salary, double bonus);
}