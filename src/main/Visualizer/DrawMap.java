package Visualizer;

import EvoGen.Animal;
import EvoGen.Grass;
import EvoGen.JungleMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawMap extends JPanel implements MouseListener {

    public MapStats mapStats;
    public Animal specialOne;
    private JungleMap map;

    private int width;
    private int height;
    private double jungleRatio;
    private int startEnergy;
    private int plantEnergy;
    private int moveEnergy;
    private int animalsNum;
    private int plantsPerDay;
    private int msDelay;

    private Timer timer;

    public DrawMap(int width,int height, double jungleRatio, int startEnergy, int plantEnergy, int moveEnergy, int animalsNum, int plantsPerDay, int msDelay) {
        this.width=width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.animalsNum = animalsNum;
        this.msDelay = msDelay;
        this.plantsPerDay = plantsPerDay;
        int jHeight = (int) ((double)height*jungleRatio);
        int jWidth = (int) ((double)width*jungleRatio);

        this.map = new JungleMap(this.width,this.height, jWidth, jHeight, this.plantEnergy, this.moveEnergy, this.startEnergy);
        this.mapStats = new MapStats();

        for(int i=0; i<animalsNum; i++) {
            map.place(new Animal(map, null, null));
        }

        //sets timer and starts it
        this.timer = new Timer(msDelay, new GameLoop(this));
        this.timer.start();

        setLayout(new GridLayout(this.height, this.width, 0,0));
        for (int i=0; i < this.height; i++) {
            for(int j=0; j<this.width; j++) {
                JPanel tile = new DrawTile(map.tiles[j][i], this);
                add(tile);
            }
        }
    }

    public void doOneLoop() {
        this.map.removeBodies();
        this.map.moveAnimals();
        this.map.eatGrass();
        this.map.procreate();
        for(int i=0; i<this.plantsPerDay/2; i++) {
            this.map.spawnGrass(new Grass());
        }
        this.map.epoch++;
        repaint();

        this.updateStats();
    }

    private void updateStats() {
        this.map.updateAvg();
        this.mapStats.epoch.setText(Integer.toString(this.map.getEpoch()));
        this.mapStats.genotype.setText(this.map.mostFrequentGene());
        this.mapStats.grassNum.setText(Integer.toString(this.map.totalGrass()));
        this.mapStats.animNum.setText(Integer.toString(this.map.getTotalAnimals()));
        this.mapStats.energyAverage.setText(Integer.toString(this.map.getAvgEnergy()));
        this.mapStats.kidsAverage.setText(Integer.toString(this.map.getAvgKids()));
        this.mapStats.lifeAverage.setText(Integer.toString(this.map.getAvgLifetime()));

        if(this.specialOne!=null) {
            this.mapStats.hisKids.setText(Integer.toString(this.specialOne.getKids()));
            this.mapStats.hisHeritage.setText(Integer.toString(this.specialOne.getHeritage()));
            this.map.undoAnimalCheck();
            if(!this.specialOne.isDead())this.mapStats.watchTime++;
            this.mapStats.watchedSince.setText(Integer.toString(this.mapStats.watchTime));
            this.mapStats.hisEnergy.setText(Integer.toString(this.specialOne.getEnergy()));
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(this.timer.isRunning()) this.timer.stop();
        else {
            this.timer.start();
            mapStats.watchTime = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
