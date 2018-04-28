package generators;

import configuration.BinSetConfiguration;
import configuration.ItemSetConfiguration;
import org.apache.commons.math3.distribution.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by g-ux on 31/03/18.
 */
public class BinsGenerator {

    private double calculateDemand(ArrayList<Double> items, Double capacity, int type){
        final double[] result = {0};
        int i = 0;
        boolean flag = true;
        while (i < items.size() && flag){
            if (items.get(i) <= capacity){
                switch (type){
                    case 1:
                        result[0] += items.get(i);
                        break;
                    case 2:
                        result[0]++;
                        break;
                }
            }else{
                flag = false;
            }
            i++;
        }
        return result[0];
    }

    public ArrayList<Double> generateLowerBound(BinSetConfiguration binSetConfiguration, ArrayList<Double> binTypes, ArrayList<Double> items){
        ArrayList<Double> result = new ArrayList<>();
        switch (binSetConfiguration.getLowerBoundType()){
            case 0:
                // No upper bound
                result.addAll(binTypes);
                break;
            case 1:
                // Automatic upper bound
                binTypes.forEach(aDouble -> {
                    double demand = this.calculateDemand(items, aDouble,1);
                    if (demand > 0){
                        int amount = (int) (demand / aDouble);
                        int i = 0;
                        while (i < amount){
                            result.add(aDouble);
                            i++;
                        }
                    }else{
                        result.add(aDouble);
                    }
                });
                break;
            case 2:
                // Stricted upper bound
                binTypes.forEach(aDouble -> {
                    double demand = this.calculateDemand(items, aDouble,2);
                    if (demand > 0){
                        int i = 0;
                        while (i < demand){
                            result.add(aDouble);
                            i++;
                        }
                    }else{
                        result.add(aDouble);
                    }
                });
                break;
            case 3:
                // Fixed upper bound
                int index = 0;
                for(Integer i: binSetConfiguration.getLowerBoundParameters()){
                    int j = 0;
                    while (j < i){
                        result.add(binTypes.get(index));
                        j++;
                    }
                    index++;
                }
                break;
        }
        return result;
    }

    public ArrayList<Double> generateBinTypeCost(ArrayList<Double> bins, int function, List<Double> parameters){
        ArrayList<Double> result = new ArrayList<>();
        if (bins.parallelStream().anyMatch(Objects::isNull)){
            bins.removeIf(Objects::isNull);
        }
        bins.forEach(integer -> {
            switch (function){
                case 1:
                    double evaluated = (parameters.get(0) * integer) + parameters.get(1);
                    result.add(evaluated);
                    break;
                case 2:
                    double evaluatedInverse = 1 / ((parameters.get(0) * integer) + parameters.get(1));
                    result.add(evaluatedInverse);
                    break;
                case 3:
                    result.add(10*Math.sqrt(integer));
                    break;
                case 4:
                    result.add(0.1*Math.pow(integer,3/2));
                    break;
            }
        });
        return result;
    }

    public ArrayList<Double> generateBinsCapacities(BinSetConfiguration config){
        ArrayList<Double> result = new ArrayList<>();
        switch (config.getDistribution()){
            case 1:
                result.addAll(this.generateCapacitiesWithUniformDistribution(config.getRangeInit(),config.getRangeEnd(),config.getBinsNumber()));
                break;
            case 2:
                result.addAll(this.generateCapacitiesWithNormalDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1),config.getBinsNumber()));
                break;
            case 3:
                result.addAll(this.generateCapacitiesWithGammaDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1),config.getBinsNumber()));
                break;
            case 4:
                result.addAll(this.generateCapacitiesWithWeibullDistribution(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1),config.getBinsNumber()));
                break;
            case 5:
                result.addAll(this.generateCapacitiesWithIncrement(config.getDistributionParameters().get(0),config.getDistributionParameters().get(1),config.getBinsNumber()));
                break;
            case 6:
                result.addAll(config.getDistributionParameters());
                break;
        }
        return result;
    }

    public ArrayList<Double> generateCapacitiesWithNormalDistribution(double mean, double std, int quantity){
        ArrayList<Double> result = new ArrayList<>();
        NormalDistribution normalDistribution = new NormalDistribution(mean,std);
        double[] distResult = normalDistribution.sample(quantity);
        Arrays.stream(distResult).forEach(result::add);
        return result;
    }

    public ArrayList<Double> generateCapacitiesWithUniformDistribution(double ini, double end, int quantity){
        ArrayList<Double> result = new ArrayList<>();
        UniformRealDistribution uniformIntegerDistribution = new UniformRealDistribution(ini,end);
        double[] uResult = uniformIntegerDistribution.sample(quantity);
        Arrays.stream(uResult).forEach(result::add);
        return result;
    }

    public ArrayList<Double> generateCapacitiesWithGammaDistribution(double ini, double end, int quantity){
        ArrayList<Double> result = new ArrayList<>();
        GammaDistribution gammaDistribution = new GammaDistribution(ini,end);
        double[] uResult = gammaDistribution.sample(quantity);
        Arrays.stream(uResult).forEach(result::add);
        return result;
    }

    public ArrayList<Double> generateCapacitiesWithWeibullDistribution(double ini, double end, int quantity){
        ArrayList<Double> result = new ArrayList<>();
        WeibullDistribution weibullDistribution = new WeibullDistribution(ini,end);
        double[] uResult = weibullDistribution.sample(quantity);
        Arrays.stream(uResult).forEach(result::add);
        return result;
    }

    public ArrayList<Double> generateCapacitiesWithIncrement(double ini, double increment, int quantity){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++){
            result.add(ini + (increment * i));
        }
        return result;
    }
}
