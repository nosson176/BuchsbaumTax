package com.buchsbaumtax.core.model;


public class Shekelator {

    private long id;
    private long taxYearId;
    private String filingStatus;
    private long childrenUnder16;
    private long childrenOver16;
    private long childcareCount;
    private double childcareExpenses;
    private double primaryIncome;
    private double primaryForeignTax;
    private double secondaryIncome;
    private double secondaryForeignTax;
    private double selfEmploymentIncome;
    private double selfEmploymentForeignTax;
    private double passiveIncome;
    private double passiveForeignTax;
    private double total;
    private String primaryCurrency;
    private String secondaryCurrency;
    private boolean passiveExclusion2555;
    private boolean secondaryExclusion2555;
    private boolean primaryExclusion2555;
    private boolean selfEmploymentExclusion2555;

    public Shekelator(String filingStatus, long childrenUnder16, long childrenOver16, long childcareCount, double childcareExpenses, double primaryIncome, double primaryForeignTax, double secondaryIncome, double secondaryForeignTax, double selfEmploymentIncome, double selfEmploymentForeignTax, double passiveIncome, double passiveForeignTax, double total, String primaryCurrency, String secondaryCurrency, boolean passiveExclusion2555, boolean secondaryExclusion2555, boolean primaryExclusion2555, boolean selfEmploymentExclusion2555) {
        this.filingStatus = filingStatus;
        this.childrenUnder16 = childrenUnder16;
        this.childrenOver16 = childrenOver16;
        this.childcareCount = childcareCount;
        this.childcareExpenses = childcareExpenses;
        this.primaryIncome = primaryIncome;
        this.primaryForeignTax = primaryForeignTax;
        this.secondaryIncome = secondaryIncome;
        this.secondaryForeignTax = secondaryForeignTax;
        this.selfEmploymentIncome = selfEmploymentIncome;
        this.selfEmploymentForeignTax = selfEmploymentForeignTax;
        this.passiveIncome = passiveIncome;
        this.passiveForeignTax = passiveForeignTax;
        this.total = total;
        this.primaryCurrency = primaryCurrency;
        this.secondaryCurrency = secondaryCurrency;
        this.passiveExclusion2555 = passiveExclusion2555;
        this.secondaryExclusion2555 = secondaryExclusion2555;
        this.primaryExclusion2555 = primaryExclusion2555;
        this.selfEmploymentExclusion2555 = selfEmploymentExclusion2555;
    }


    public long getId() {
        return id;
    }

    public long getTaxYearId() {
        return taxYearId;
    }

    public String getFilingStatus() {
        return filingStatus;
    }

    public long getChildrenUnder16() {
        return childrenUnder16;
    }

    public long getChildrenOver16() {
        return childrenOver16;
    }

    public long getChildcareCount() {
        return childcareCount;
    }

    public double getChildcareExpenses() {
        return childcareExpenses;
    }

    public double getPrimaryIncome() {
        return primaryIncome;
    }

    public double getPrimaryForeignTax() {
        return primaryForeignTax;
    }

    public double getSecondaryIncome() {
        return secondaryIncome;
    }

    public double getSecondaryForeignTax() {
        return secondaryForeignTax;
    }

    public double getSelfEmploymentIncome() {
        return selfEmploymentIncome;
    }

    public double getSelfEmploymentForeignTax() {
        return selfEmploymentForeignTax;
    }

    public double getPassiveIncome() {
        return passiveIncome;
    }

    public double getPassiveForeignTax() {
        return passiveForeignTax;
    }

    public double getTotal() {
        return total;
    }

    public String getPrimaryCurrency() {
        return primaryCurrency;
    }

    public String getSecondaryCurrency() {
        return secondaryCurrency;
    }

    public boolean isPassiveExclusion2555() {
        return passiveExclusion2555;
    }

    public boolean isSecondaryExclusion2555() {
        return secondaryExclusion2555;
    }

    public boolean isPrimaryExclusion2555() {
        return primaryExclusion2555;
    }

    public boolean isSelfEmploymentExclusion2555() {
        return selfEmploymentExclusion2555;
    }
}
