package modeldifference.calculator;

import modeldifference.models.Application;

public interface IDifferenceCalculator  {
    ApplicationDifferences findApplicationDifferences(Application application1, Application application2) throws DifferenceCalculatorException;
}
