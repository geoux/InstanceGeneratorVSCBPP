package configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g-ux on 7/04/18.
 */
public class BinSetConfiguration {
    private int binsNumber;
    private double rangeInit;
    private double rangeEnd;
    private int distribution;
    public List<Double> distributionParameters;
    private int costFuntionType;
    public List<Double> costFuntionParameters;
    private int UpperBoundType;
    public List<Integer> UpperBoundParameters;

    public BinSetConfiguration() {
        distributionParameters = new ArrayList<>();
        costFuntionParameters = new ArrayList<>();
        UpperBoundParameters = new ArrayList<>();
    }

    public int getBinsNumber() {
        return binsNumber;
    }

    public void setBinsNumber(int binsNumber) {
        this.binsNumber = binsNumber;
    }

    public double getRangeInit() {
        return rangeInit;
    }

    public void setRangeInit(double rangeInit) {
        this.rangeInit = rangeInit;
    }

    public double getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(double rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getDistribution() {
        return distribution;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    public List<Double> getDistributionParameters() {
        return distributionParameters;
    }

    public int getCostFuntionType() {
        return costFuntionType;
    }

    public void setCostFuntionType(int costFuntionType) {
        this.costFuntionType = costFuntionType;
    }

    public List<Double> getCostFuntionParameters() {
        return costFuntionParameters;
    }

    public int getUpperBoundType() {
        return UpperBoundType;
    }

    public void setUpperBoundType(int UpperBoundType) {
        this.UpperBoundType = UpperBoundType;
    }

    public List<Integer> getUpperBoundParameters() {
        return UpperBoundParameters;
    }
}
