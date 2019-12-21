package Visualizer;

import javax.swing.*;
import java.awt.*;

public class MapStats extends JPanel {

    public JLabel epoch, genotype, grassNum, animNum, energyAverage, kidsAverage, lifeAverage;

    public int watchTime = 0;
    public JLabel watchedSince, hisKids, hisHeritage, hisEnergy;

    //helps managing data placement under map
    private GridBagConstraints setup(int row, int column,GridBagConstraints c, String name) {
        c.weightx=0.1;
        c.gridy = row;
        c.gridx=column;
        add(new JLabel(name),c);
        c.weightx=0.4;
        c.gridx = column+1;
        return c;
    }

    public MapStats() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c = setup(0,0,c,"  Epoch: ");
        this.epoch = create(c);
        c = setup(0,3,c,"  Total grass number: ");
        this.grassNum = create(c);
        c = setup(1,0, c,"  Total animal number: ");
        this.animNum = create(c);
        c = setup(1, 3, c, "  Average Energy: ");
        this.energyAverage = create(c);
        c = setup(2, 0, c, "  Kids average: ");
        this.kidsAverage = create(c);
        c = setup(2,3,c,"  Lifetime average: ");
        this.lifeAverage = create(c);

        c = setup(3, 0, c, "  Genotype: ");
        c.weightx = 0.9;
        c.gridx = 3;
        this.genotype = create(c);

        c = setup(4,0,c," "); //makes gap between stats

        c = setup(5,0,c,"  Observed one stats: ");
        c = setup(6,0,c,"  His kids: ");
        this.hisKids = create(c);
        c = setup(6,3,c,"  His total heritage: ");
        this.hisHeritage = create(c);
        c = setup(7,0,c,"  Total watchtime: ");
        this.watchedSince = create(c);
        c = setup(7,3,c, "  His energy: ");
        this.hisEnergy = create(c);
    }

    //add label and modifies constraint c
    private JLabel create(GridBagConstraints c) {
        JLabel label = new JLabel();
        add(label,c);
        return label;
    }
}
