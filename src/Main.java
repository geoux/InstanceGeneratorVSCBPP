/**
 * Created by g-ux on 30/03/18.
 */

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import configuration.BinSetConfiguration;
import generators.BinsGenerator;
import generators.ItemsGenerator;
import configuration.Configuration;
import sun.security.util.BitArray;
import utils.ExcelGenerator;
import utils.ExcelTranslator;
import configuration.ItemSetConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.*;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        out.println("*******************************************************************");
        out.println("********* VARIABLE SIZE AND COST BIN PACKING PROBLEM **************");
        out.println("********************** INSTANCE GENERATOR *************************");
        out.println("*******************************************************************");
        out.println("");
        out.println("");
        out.println("Generate instances using: ");
        out.println("[1] - Configuration file ");
        out.println("[2] - New settings");
        Scanner tmp = new Scanner(in);
        int option = tmp.nextInt();
        switch (option){
            case 1:
                generateFromExistingFile();
                break;
            case 2:
                generateFromScratch();
                break;
        }
    }

    static private void generateFromExistingFile(){
        try{
            out.println("Reading data...");
            /******************** Setup section ****************************/
            ArrayList<ArrayList<Double>> itemSets = new ArrayList<>();
            ArrayList<ArrayList<Double>> binSetsCapacities = new ArrayList<>();
            ItemsGenerator ig = new ItemsGenerator();
            BinsGenerator bg = new BinsGenerator();
            YamlReader reader = new YamlReader(new FileReader("configuration.yml"));
            Configuration config = (Configuration) reader.read();
            reader.close();
            config.getItemsBySets().parallelStream().forEach(itemSetConfiguration -> itemSets.add(ig.generateItemsWithinRange(itemSetConfiguration)));
            config.getBinSet().forEach(binSetConfiguration -> binSetsCapacities.add(bg.generateBinsCapacities(binSetConfiguration)));
            out.println("Generating...");
            /***************** Generator section **************************/
            int binGroup = 0;
            for (BinSetConfiguration binSetConfiguration: config.getBinSet()) {
                int instanceID = 0;
                for(ArrayList<Double> itemSet : itemSets) {
                    File excelFolder = new File("generated/Excel");
                    File mpFolder = new File("generated/MathProg");
                    if(!excelFolder.exists())
                        excelFolder.mkdirs();
                    if(!mpFolder.exists())
                        mpFolder.mkdirs();
                    String setName = "generated/Excel/G"+(binGroup + 1)+"I"+(instanceID + 1)+"N"+itemSet.size()+"B"+binSetConfiguration.getBinsNumber();
                    String mpName = "generated/MathProg/G"+(binGroup + 1)+"I"+(instanceID + 1)+"N"+itemSet.size()+"B"+binSetConfiguration.getBinsNumber()+".dat";
                    String baseExcel = setName+".xls";
                    ArrayList<Double> lowerBound = new ArrayList<>();
                    lowerBound.addAll(bg.generateLowerBound(binSetConfiguration,binSetsCapacities.get(binGroup),itemSet));
                    ArrayList<Double> binsCost = new ArrayList<>();
                    binsCost.addAll(bg.generateBinTypeCost(lowerBound,binSetConfiguration.getCostFuntionType(),binSetConfiguration.getCostFuntionParameters()));
                    ExcelGenerator.writeInExcel(lowerBound,binsCost, itemSet,setName);
                    ExcelTranslator.Translate(baseExcel,mpName);
                    instanceID++;
                }
                binGroup++;
            }
            /*************************************************************/
            out.println("Done.");
            out.println("Exiting generator, thanks for using it!!! ");
        }catch (IOException e){
            out.println(e.getMessage());
        }
    }

    static private void generateFromScratch(){
        Configuration config = new Configuration();
        ArrayList<ArrayList<Double>> itemSets = new ArrayList<>();
        ItemsGenerator ig = new ItemsGenerator();
        out.println("********* ITEMS SECTION **************");
        out.println("Items set number: ");
        Scanner tmp = new Scanner(in);
        config.setItemsSetsNumber(tmp.nextInt());
        double maxweight = 0;
        double minweight = (int) Math.exp(0xf423f);
        int i = 0;
        while(i < config.getItemsSetsNumber()){
            ItemSetConfiguration itemSetConfiguration = new ItemSetConfiguration();
            ArrayList<Double> items = new ArrayList<>();
            out.println("Items number of set "+(i + 1)+": ");
            Scanner itn = new Scanner(in);
            itemSetConfiguration.setItemsNumber(itn.nextInt());
            boolean ok = false;
            String range = "";
            int toSplit = -1;
            while (!ok){
                out.println("Items weights range [x-X]: ");
                out.println("(For items with the same weight, write just one number without separator)");
                Scanner itr = new Scanner(in);
                String typed = itr.nextLine();
                if (typed != ""){
                    range = typed.replace(" ","");
                    toSplit = range.indexOf("-");
                    ok = true;
                }

            }

            if(toSplit < 0){
                itemSetConfiguration.setRangeInit(Integer.parseInt(range));
                itemSetConfiguration.setRangeEnd(itemSetConfiguration.getRangeInit());
            }else{
                itemSetConfiguration.setRangeInit(Integer.parseInt(range.substring(0,toSplit)));
                if (toSplit + 1 > range.length()){
                    itemSetConfiguration.setRangeEnd(itemSetConfiguration.getRangeInit());
                }else{
                    itemSetConfiguration.setRangeEnd(Integer.parseInt(range.substring(toSplit+1,range.length())));
                }
            }

            if(itemSetConfiguration.getRangeInit() < minweight){
                minweight = itemSetConfiguration.getRangeInit();
            }
            if(itemSetConfiguration.getRangeEnd() > maxweight){
                maxweight = itemSetConfiguration.getRangeEnd();
            }
            if(itemSetConfiguration.getRangeInit() != itemSetConfiguration.getRangeEnd()){
                out.println("Weight distribution: ");
                out.println("[1] - Uniform");
                out.println("[2] - Normal");
                out.println("[3] - Gamma");
                out.println("[4] - Weibull");
                out.println("[5] - Roulette");
                out.print("Select your option: ");
                Scanner itwd = new Scanner(in);
                itemSetConfiguration.setDistribution(itwd.nextInt());

                switch (itemSetConfiguration.getDistribution()){
                    case 2:
                        out.println("NORMAL DISTRIBUTION PARAMETERS");
                        out.println("Specify mean within range "+itemSetConfiguration.getRangeInit()+" - "+itemSetConfiguration.getRangeEnd());
                        Scanner mean = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(mean.nextDouble());
                        out.println("Specify standard deviation: ");
                        Scanner std = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(std.nextDouble());
                        break;
                    case 3:
                        out.println("GAMMA DISTRIBUTION PARAMETERS");
                        out.println("(For shape = 1, gamma behaves as exponential distribution) ");
                        out.println("Specify shape: ");
                        Scanner shape = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(shape.nextDouble());
                        out.println("Specify scale: ");
                        Scanner scale = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(scale.nextDouble());
                        break;
                    case 4:
                        out.println("WEIBULL DISTRIBUTION PARAMETERS");
                        out.println("(For beta = 1, weibull behaves as exponential distribution and when beta > 1, it approximates to normal distribution) ");
                        out.println("Specify alpha: ");
                        Scanner alpha = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(alpha.nextDouble());
                        out.println("Specify beta: ");
                        Scanner beta = new Scanner(in);
                        itemSetConfiguration.getDistributionParameters().add(beta.nextDouble());
                        break;
                    case 5:
                        out.println("ROULETTE WHEEL PARAMETERS");
                        out.println("Probability by weight in percent (use integers in range 1 - 100): ");
                        out.print("1 - For the heaviest: ");
                        Scanner itwd1 = new Scanner(in);
                        itemSetConfiguration.getRoulettePercentils().add(itwd1.nextInt());
                        out.println("Available probability range so far: "+(100 - itemSetConfiguration.getRoulettePercentils().get(0))+"%");
                        out.print("2 - For the medium ones: ");
                        Scanner itwd2 = new Scanner(in);
                        itemSetConfiguration.getRoulettePercentils().add(itwd2.nextInt());
                        int rest = 100 - Math.abs(itemSetConfiguration.getRoulettePercentils().get(0) - itemSetConfiguration.getRoulettePercentils().get(1));
                        out.println("3 - For the smallers: "+rest+"%");
                        itemSetConfiguration.getRoulettePercentils().add(rest);
                        break;
                }
                items.addAll(ig.generateItemsWithinRange(itemSetConfiguration));
            }else{
                items.addAll(ig.generateItemsWithSameWeight(itemSetConfiguration.getItemsNumber(),itemSetConfiguration.getRangeInit()));
            }
            config.getItemsBySets().add(itemSetConfiguration);
            itemSets.add(items);
            i++;
        }
        out.println("********* BIN TYPES SECTION **************");
        boolean toEnd = false;
        config.setBinsSetsNumber(1);
        int group = 1;
        while (!toEnd){
            BinSetConfiguration binSetConfiguration = new BinSetConfiguration();
            ArrayList<Double> binsCapacity = new ArrayList<>();
            ArrayList<Double> binsTypeLowerBound = new ArrayList<>();
            ArrayList<Integer> lowerBoundAmount = new ArrayList<>();
            BinsGenerator bg = new BinsGenerator();
            out.println("Bin types number: ");
            Scanner bt = new Scanner(in);
            binSetConfiguration.setBinsNumber(bt.nextInt());
            out.println("Set bin types capacity: ");
            out.println("[1] - Uniform distribution");
            out.println("[2] - Normal distribution");
            out.println("[3] - Gamma distribution");
            out.println("[4] - Weibull distribution");
            out.println("[5] - Discrete with increment in range");
            out.println("[6] - Manual");
            out.print("Select your option: ");
            Scanner bctg = new Scanner(in);
            binSetConfiguration.setDistribution(bctg.nextInt());
            boolean flag;
            switch (binSetConfiguration.getDistribution()){
                case 1:
                    flag = false;
                    out.println("UNIFORM DISTRIBUTION PARAMETERS FOR BIN TYPES CAPACITIES");
                    while(!flag){
                        binsCapacity.clear();
                        out.println("Bin types capacities range [x-X]: ");
                        out.println("(Heavier bin type must be greater than "+maxweight+")");
                        Scanner rgn = new Scanner(in);
                        String btrange = rgn.nextLine();
                        int toSplit = btrange.indexOf("-");
                        binSetConfiguration.setRangeInit(Integer.parseInt(btrange.substring(0,toSplit)));
                        binSetConfiguration.setRangeEnd(Integer.parseInt(btrange.substring(toSplit+1,btrange.length())));
                        if(binSetConfiguration.getRangeEnd() > binSetConfiguration.getRangeInit()){
                            if(binSetConfiguration.getRangeEnd() < maxweight){
                                out.println("Bigger bin type capacity is smaller that the bigger item size, so, the problem have no feasible solution at least in one of the possible instances. Fix the range before...");
                            }else{
                                flag = true;
                            }
                        }else{
                            out.println("Range is not well formed, try again...");
                        }
                    }
                    binsCapacity.addAll(bg.generateCapacitiesWithUniformDistribution(binSetConfiguration.getRangeInit(),binSetConfiguration.getRangeEnd(),binSetConfiguration.getBinsNumber()));
                    break;
                case 2:
                    out.println("NORMAL DISTRIBUTION PARAMETERS FOR BIN TYPES CAPACITIES");
                    binsCapacity.clear();
                    binSetConfiguration.getDistributionParameters().clear();
                    out.println("Specify mean: ");
                    Scanner mean = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(mean.nextDouble());
                    out.println("Specify standard deviation: ");
                    Scanner std = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(std.nextDouble());
                    binsCapacity.addAll(bg.generateCapacitiesWithNormalDistribution(binSetConfiguration.getDistributionParameters().get(0),binSetConfiguration.getDistributionParameters().get(1),binSetConfiguration.getBinsNumber()));
                    double greaterBinValidation = binsCapacity.stream().max(Comparator.comparing(aDouble -> aDouble)).get();
                    if (greaterBinValidation < maxweight){
                        BinSetConfiguration fixer = fixDistributionParameters(binSetConfiguration,binsCapacity,maxweight);
                        if (fixer != null){
                            binSetConfiguration = fixer;
                        }
                    }
                    break;
                case 3:
                    out.println("GAMMA DISTRIBUTION PARAMETERS FOR BIN TYPES CAPACITIES");
                    binsCapacity.clear();
                    binSetConfiguration.getDistributionParameters().clear();
                    out.println("Specify shape: ");
                    Scanner shape = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(shape.nextDouble());
                    out.println("Specify scale: ");
                    Scanner scale = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(scale.nextDouble());
                    binsCapacity.addAll(bg.generateCapacitiesWithGammaDistribution(binSetConfiguration.getDistributionParameters().get(0),binSetConfiguration.getDistributionParameters().get(1),binSetConfiguration.getBinsNumber()));
                    double bGammaValidation = binsCapacity.stream().max(Comparator.comparing(aDouble -> aDouble)).get();
                    if (bGammaValidation < maxweight){
                        BinSetConfiguration fixer = fixDistributionParameters(binSetConfiguration,binsCapacity,maxweight);
                        if (fixer != null){
                            binSetConfiguration = fixer;
                        }
                    }
                    break;
                case 4:
                    out.println("WEIBULL DISTRIBUTION PARAMETERS FOR BIN TYPES CAPACITIES");
                    binsCapacity.clear();
                    binSetConfiguration.getDistributionParameters().clear();
                    out.println("Specify alpha: ");
                    Scanner alpha = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(alpha.nextDouble());
                    out.println("Specify beta: ");
                    Scanner beta = new Scanner(in);
                    binSetConfiguration.getDistributionParameters().add(beta.nextDouble());
                    binsCapacity.addAll(bg.generateCapacitiesWithWeibullDistribution(binSetConfiguration.getDistributionParameters().get(0),binSetConfiguration.getDistributionParameters().get(1),binSetConfiguration.getBinsNumber()));
                    double bWeibullinValidation = binsCapacity.stream().max(Comparator.comparing(aDouble -> aDouble)).get();
                    if (bWeibullinValidation < maxweight){
                        BinSetConfiguration fixer = fixDistributionParameters(binSetConfiguration,binsCapacity,maxweight);
                        if (fixer != null){
                            binSetConfiguration = fixer;
                        }
                    }
                    break;
                case 5:
                    binSetConfiguration.getDistributionParameters().clear();
                    out.println("Starting value of the interval: ");
                    Scanner sv = new Scanner(in);
                    double st = sv.nextDouble();
                    out.println("Increment value of the interval: ");
                    Scanner iv = new Scanner(in);
                    double maxint = iv.nextDouble();
                    binSetConfiguration.getDistributionParameters().add(st);
                    binSetConfiguration.getDistributionParameters().add(maxint);
                    binsCapacity.addAll(bg.generateCapacitiesWithIncrement(binSetConfiguration.getDistributionParameters().get(0),binSetConfiguration.getDistributionParameters().get(1),binSetConfiguration.getBinsNumber()));
                    break;
                case 6:
                    int j = 0;
                    while (j < binSetConfiguration.getBinsNumber()){
                        out.println("Bins capacity number of type "+(j + 1)+": ");
                        Scanner bn = new Scanner(in);
                        double bngen = bn.nextDouble();
                        binSetConfiguration.getDistributionParameters().add(bngen);
                        binsCapacity.add(bngen);
                        j++;
                    }
                    break;
            }

            out.println("Bin types cost function: ");
            out.println("[1] - Linear");
            out.println("[2] - Inverse");
            out.println("[3] - Concave");
            out.println("[4] - Convex");
            out.print("Select your option: ");
            Scanner bctf = new Scanner(in);
            int costFunction = bctf.nextInt();
            binSetConfiguration.setCostFuntionType(costFunction);
            if (binSetConfiguration.getCostFuntionType() == 1 || binSetConfiguration.getCostFuntionType() == 2){
                out.println("Set parameters for this type of cost function: ");
                out.print("Pendent: ");
                Scanner pend = new Scanner(in);
                Double pendent = pend.nextDouble();
                out.print("Shift: ");
                Scanner desp = new Scanner(in);
                Double shift = desp.nextDouble();
                binSetConfiguration.getCostFuntionParameters().add(pendent);
                binSetConfiguration.getCostFuntionParameters().add(shift);
            }
            int lbType = 0;
            int its = 0;
            out.println("Generate lower bound? [Yes | No]: ");
            Scanner lw = new Scanner(in);
            String lb = lw.next();
            if (lb.equalsIgnoreCase( "Y")) {
                out.println("Lower bound type: ");
                out.println("[1] - Automatic");
                out.println("[2] - Stricted");
                out.println("[3] - Fixed");
                out.print("Select your option: ");
                Scanner lwt = new Scanner(in);
                lbType = lwt.nextInt();
                binSetConfiguration.setLowerBoundType(lbType);
            }else{
                binSetConfiguration.setLowerBoundType(0);
            }
            if(binSetConfiguration.getLowerBoundType() > 0){
                binsTypeLowerBound.clear();
                if (binSetConfiguration.getLowerBoundType() == 3){
                    out.println("Set the maximum number of bins per type");
                    out.print("Same upper bound for every bin type? [Yes | No]");
                    Scanner slb = new Scanner(in);
                    String sl = slb.next();
                    if (sl.equalsIgnoreCase("Y")){
                        out.print("Maximum number of bins per type: ");
                        Scanner cx = new Scanner(in);
                        int bqx = cx.nextInt();
                        int ly = 0;
                        while (ly < binSetConfiguration.getBinsNumber()){
                            lowerBoundAmount.add(bqx);
                            binSetConfiguration.getLowerBoundParameters().add(bqx);
                            ly++;
                        }
                    }else{
                        int z = 0;
                        while (z < binsCapacity.size()){
                            out.print("For bin type with capacity "+binsCapacity.get(z)+" : ");
                            Scanner c = new Scanner(in);
                            int bq = c.nextInt();
                            binSetConfiguration.getLowerBoundParameters().add(bq);
                            lowerBoundAmount.add(bq);
                            z++;
                        }
                    }
                }
            }
            out.println("Generating...");
            while (its < config.getItemsSetsNumber()){
                binsTypeLowerBound.addAll(bg.generateLowerBound(binSetConfiguration,binsCapacity,itemSets.get(its)));
                File excelFolder = new File("generated/Excel");
                File mpFolder = new File("generated/MathProg");
                if(!excelFolder.exists())
                    excelFolder.mkdirs();
                if(!mpFolder.exists())
                    mpFolder.mkdirs();
                String setName = "generated/Excel/G"+group+"I"+its+"N"+itemSets.get(its).size()+"B"+binSetConfiguration.getBinsNumber();
                String mpName = "generated/MathProg/G"+group+"I"+its+"N"+itemSets.get(its).size()+"B"+binSetConfiguration.getBinsNumber()+".dat";
                String baseExcel = setName+".xls";
                ExcelGenerator.writeInExcel(binsTypeLowerBound,bg.generateBinTypeCost(binsTypeLowerBound,costFunction, binSetConfiguration.getCostFuntionParameters()), itemSets.get(its),setName);
                ExcelTranslator.Translate(baseExcel,mpName);
                its++;
            }
            config.getBinSet().add(binSetConfiguration);
            out.println("Done!");
            out.print("Do you want to generate another bin type set? [Yes | No]: ");
            Scanner a = new Scanner(in);
            String another = a.next();
            if(another.equalsIgnoreCase("N")){
                toEnd = true;
                out.println("Exiting generator, thanks for using it!!! ");
            }else{
                group++;
                config.setBinsSetsNumber(config.getBinsSetsNumber() + 1);
            }
            try{
                YamlWriter writer = new YamlWriter(new FileWriter("configuration.yml"));
                writer.getConfig().setPropertyElementType(Configuration.class, "itemsBySets", ItemSetConfiguration.class);
                writer.getConfig().setPropertyElementType(Configuration.class, "binSet", BinSetConfiguration.class);
                writer.write(config);
                writer.close();
            }catch (IOException e){
                out.println(e.getMessage());
            }
        }
    }

    static private BinSetConfiguration fixDistributionParameters(BinSetConfiguration binSetConfiguration, ArrayList<Double> binsCapacity, double maxweight){
        boolean flag = true;
        out.println("Bigger bin type capacity is smaller that the bigger item size, so, the problem have no feasible solution at least in one of the possible instances.");
        out.println("How would you like to proceed?:");
        out.println("[1] - Adjust automatically the capacity of the heavier bin");
        out.println("[2] - Add one bin type that fulfill the capacity constrain");
        out.println("[3] - Redefine parameters for this distribution");
        out.print("Choice: ");
        Scanner ch = new Scanner(in);
        switch (ch.nextInt()){
            case 1:
                out.print("Units to increase heavier bin type capacity: ");
                Scanner grow = new Scanner(in);
                Double fix = grow.nextDouble();
                binsCapacity.set(binsCapacity.size()-1,maxweight+fix);
                binSetConfiguration.getDistributionParameters().set(0,binSetConfiguration.getDistributionParameters().get(0) + fix);
                break;
            case 2:
                out.print("New bin type capacity: ");
                Scanner nw = new Scanner(in);
                Double fixedBin = nw.nextDouble();
                binsCapacity.add(fixedBin);
                binSetConfiguration.setBinsNumber(binSetConfiguration.getBinsNumber() + 1);
                double reduced = binsCapacity.stream().reduce((aDouble, aDouble2) -> aDouble + aDouble2).get() / binsCapacity.size();
                binSetConfiguration.getDistributionParameters().set(0,reduced);
                break;
            case 3:
                flag = false;
                break;
        }
        return (flag)?binSetConfiguration:null;
    }
}