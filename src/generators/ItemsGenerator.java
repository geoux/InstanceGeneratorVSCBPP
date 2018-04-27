package generators;

import org.apache.commons.math3.distribution.*;
import configuration.ItemSetConfiguration;
import utils.RandomHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by g-ux on 30/03/18.
 */
public class ItemsGenerator {

    private RandomHelper rhelper;

    public ItemsGenerator() {
        this.rhelper = new RandomHelper();
    }

    public ArrayList<Double> generateItemsWithSameWeight(int number, double weight){
        ArrayList<Double> result = new ArrayList<>();
        for(int i=0; i<=number; i++){
            result.add(weight);
        }
        return result;
    }

    public ArrayList<Double> generateItemsWithinRange(ItemSetConfiguration config){
        ArrayList<Double> result = new ArrayList<>();
        double[] distResult;
        switch (config.getDistribution()){
            case 1:
                UniformRealDistribution uniformDistribution = new UniformRealDistribution(config.getRangeInit(),config.getRangeEnd());
                distResult = uniformDistribution.sample(config.getItemsNumber());
                Arrays.stream(distResult).forEach(result::add);
                break;
            case 2:
                NormalDistribution normalDistribution = new NormalDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1));
                distResult = normalDistribution.sample(config.getItemsNumber());
                Arrays.stream(distResult).forEach(result::add);
                break;
            case 3:
                GammaDistribution gammaDistribution = new GammaDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1));
                distResult = gammaDistribution.sample(config.getItemsNumber());
                Arrays.stream(distResult).forEach(result::add);
                break;
            case 4:
                WeibullDistribution weibullDistribution = new WeibullDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1));
                distResult = weibullDistribution.sample(config.getItemsNumber());
                Arrays.stream(distResult).forEach(result::add);
                break;
            case 5:
                for(int i=0; i<config.getItemsNumber(); i++){
                    double[] interval = new double[3];
                    double amount = config.getRangeEnd() / 3;
                    interval[0] = config.getRangeInit();
                    interval[1] = config.getRangeInit() + amount;
                    interval[2] = interval[1] + amount;
                    int wheel = rhelper.randValue(100);
                    if(wheel > config.getRoulettePercentils().get(0) && wheel < config.getRoulettePercentils().get(1)){
                        result.add(rhelper.randRealInterval(interval[0], interval[1]));
                    }
                    if(wheel > config.getRoulettePercentils().get(1) && wheel < config.getRoulettePercentils().get(2)){
                        result.add(rhelper.randRealInterval(interval[1], interval[2]));
                    }
                    if(wheel > config.getRoulettePercentils().get(2)){
                        result.add(rhelper.randRealInterval(interval[2], config.getRangeEnd()));
                    }
                }
                break;
        }
        return result;
    }
}
