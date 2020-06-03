package io.swagger.configuration;

public class BankConfig {
    private static double absoluteLimit;
    private static int dayLimit;
    private static double transactionLimit;

    public BankConfig() {
        absoluteLimit = 0.00;
        dayLimit = 25;
        transactionLimit = 2000;
    }

    public static double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public static int getDayLimit() {
        return dayLimit;
    }

    public static double getTransactionLimit() {
        return transactionLimit;
    }

    public boolean checkAbsoluteLimit(Double newBalance) {
        return newBalance >= absoluteLimit;
    }
}
