package configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by g-ux on 5/04/18.
 */
public class Configuration {
    private int itemsSetsNumber;
    private int binsSetsNumber;
    public List<ItemSetConfiguration> itemsBySets;
    public List<BinSetConfiguration> binSet;

    public Configuration() {
        itemsBySets = new ArrayList<>();
        binSet = new ArrayList<>();
    }

    public int getItemsSetsNumber() {
        return itemsSetsNumber;
    }

    public void setItemsSetsNumber(int itemsSetsNumber) {
        this.itemsSetsNumber = itemsSetsNumber;
    }

    public List<ItemSetConfiguration> getItemsBySets() {
        return itemsBySets;
    }

    public int getBinsSetsNumber() {
        return binsSetsNumber;
    }

    public void setBinsSetsNumber(int binsSetsNumber) {
        this.binsSetsNumber = binsSetsNumber;
    }

    public List<BinSetConfiguration> getBinSet() {
        return binSet;
    }
}
