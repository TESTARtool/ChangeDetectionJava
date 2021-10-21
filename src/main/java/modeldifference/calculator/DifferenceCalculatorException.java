package modeldifference.calculator;

public class DifferenceCalculatorException extends Exception {
    public DifferenceCalculatorException(){

    }

    public DifferenceCalculatorException(Exception innerException){
        super.initCause(innerException);
    }
}
