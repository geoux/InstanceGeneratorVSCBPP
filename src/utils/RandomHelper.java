package utils;

import java.util.Random;

/**
 * Created by g-ux on 25/03/18.
 */
public class RandomHelper {

    private Random randomGenerator;

    public RandomHelper() {
        this.randomGenerator = new Random();
    }

    public int randValue(int bound)
    {
        return (int) (Math.random() * bound);
    }

    public int randInterval(int low, int high)
    {
        return (int) (Math.random() * (high - low) + low);
    }

    public double randRealInterval(double low, double high)
    {
        return Math.random() * (high - low) + low;
    }

    public int randNormalDist(double mean, double variance){
        return (int) (randomGenerator.nextGaussian() * variance + mean);
    }
}
