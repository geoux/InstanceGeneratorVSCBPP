package configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by g-ux on 5/04/18.
 */
public class ItemSetConfiguration implements Serializable{
    private int itemsNumber;
    private double rangeInit;
    private double rangeEnd;
    private int distribution;
    public List<Integer> roulettePercentils;
    public List<Double> distributionParameters;

    public ItemSetConfiguration() {
        roulettePercentils = new ArrayList<>();
        distributionParameters = new ArrayList<>();
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

    public int getItemsNumber() {
        return itemsNumber;
    }

    public List<Integer> getRoulettePercentils() {
        return roulettePercentils;
    }

    public void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    public List<Double> getDistributionParameters() {
        return distributionParameters;
    }
}
