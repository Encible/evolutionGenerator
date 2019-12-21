package Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputMenu implements ActionListener {
    public MainFrame frame;
    public JPanel menu;

    private int mapQuantity = 0;

    private JTextField width, height, jungleRatio, startEnergy,
        plantEnergy, moveEnergy, animalsNum, plantsPerDay, msDelay;

    private JButton button;

    public InputMenu(MainFrame frame) {
        this.frame = frame;
        this.menu = new JPanel();
        this.menu.setSize(new Dimension(100,600));
        this.menu.setLayout(new BoxLayout(this.menu,BoxLayout.Y_AXIS));

        //all the input text for creating map
        createLabel("Width:");
        this.width = new JTextField(Integer.toString(this.frame.width));
        this.menu.add(this.width);

        createLabel("Height:");
        this.height = new JTextField(Integer.toString(this.frame.height));
        this.menu.add(this.height);

        createLabel("JungleRatio:");
        this.jungleRatio = new JTextField(Double.toString(this.frame.jungleRatio));
        this.menu.add(this.jungleRatio);

        createLabel("StartEnergy:");
        this.startEnergy = new JTextField(Integer.toString(this.frame.startEnergy));
        this.menu.add(this.startEnergy);

        createLabel("MoveEnergy:");
        this.moveEnergy = new JTextField(Integer.toString(this.frame.moveEnergy));
        this.menu.add(this.moveEnergy);

        createLabel("PlantEnergy:");
        this.plantEnergy = new JTextField(Integer.toString(this.frame.plantEnergy));
        this.menu.add(this.plantEnergy);

        createLabel("Starting number of animals");
        this.animalsNum = new JTextField(Integer.toString(this.frame.animalsNum));
        this.menu.add(this.animalsNum);

        createLabel("Plants per day:");
        this.plantsPerDay = new JTextField(Integer.toString(this.frame.plantsPerDay));
        this.menu.add(this.plantsPerDay);

        createLabel("Miliseconds of delay:");
        this.msDelay = new JTextField(Integer.toString(this.frame.msDelay));
        this.menu.add(this.msDelay);
        //EOF input text

        this.button = new JButton("Make a map!");
        this.menu.add(this.button);
        this.button.addActionListener(this);
    }

    private void createLabel(String name) {
        JLabel label = new JLabel(name);
        this.menu.add(label);
    }

    private int getInt(JTextField field) {
        String temp = field.getText();
        return Integer.parseInt(temp);
    }

    //creates map after button is pressed
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int width = getInt(this.width);
        int height = getInt(this.height);
        int startEnergy = getInt(this.startEnergy);
        int plantEnergy = getInt(this.plantEnergy);
        int moveEnergy = getInt(this.moveEnergy);
        int animalsNum = getInt(this.animalsNum);
        int plantsPerDay = getInt(this.plantsPerDay);
        int msDelay = getInt(this.msDelay);
        double jungleRatio = Double.parseDouble(this.jungleRatio.getText());

        this.frame.drawMap = this.frame.createMap(width,height,jungleRatio,startEnergy,plantEnergy,moveEnergy,animalsNum,plantsPerDay,msDelay);
        this.mapQuantity++;

        GridBagConstraints con = new GridBagConstraints();

        con.weightx = 0.4;
        con.gridx = this.mapQuantity;
        con.gridy = 0;
        con.weighty = 0.7;
        this.frame.add(this.frame.drawMap, con);

        con.gridy = 1;
        con.weighty = 0;
        this.frame.add(this.frame.drawMap.mapStats, con);
        this.frame.setVisible(true);
    }
}
