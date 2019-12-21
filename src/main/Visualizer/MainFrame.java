package Visualizer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public int width;
    public int height;
    public double jungleRatio;
    public int startEnergy;
    public int plantEnergy;
    public int moveEnergy;
    public int animalsNum;
    public int plantsPerDay;
    public int msDelay;

    public DrawMap drawMap;

    public MainFrame() {
        super("Evolution Generator");

        Parameters parameters = new Parameters();
        this.width=parameters.width;
        this.height = parameters.height;
        this.jungleRatio = parameters.jungleRatio;
        this.startEnergy = parameters.startEnergy;
        this.plantEnergy = parameters.plantEnergy;
        this.moveEnergy = parameters.moveEnergy;
        this.animalsNum = parameters.animalsNum;
        this.plantsPerDay = parameters.plantsPerDay;
        this.msDelay = parameters.msDelay;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600,900);

        setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        InputMenu inputMenu = new InputMenu(this);
        add(inputMenu.menu,con);

        setVisible(true);
    }

    public DrawMap createMap(int width,int height, double jungleRatio, int startEnergy, int plantEnergy, int moveEnergy, int animalsNum, int plantsPerDay,int msDelay) {
        System.out.println("Map has been created");
        return new DrawMap(width,height,jungleRatio,startEnergy,plantEnergy,moveEnergy,animalsNum,plantsPerDay,msDelay);
    }
}
