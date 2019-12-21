package Visualizer;

import EvoGen.MapTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

//paints single tile
public class DrawTile extends JPanel implements MouseListener {
    private MapTile tile;
    private DrawMap drawMap;

    DrawTile(MapTile tile, DrawMap map) {
        setPreferredSize(new Dimension(20,20));
        this.tile = tile;
        this.drawMap = map;

        addMouseListener(this);
        addMouseListener(this.drawMap);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(this.tile.isJungle)setBackground(new Color(45, 179, 0));
        else setBackground(new Color(102, 255, 51));

        if(this.tile.isOccupied()) {
            Rectangle2D rectangle = new Rectangle2D.Double(0, 0,  this.getWidth(), this.getHeight());

            //highlights the one being observed
            if(this.tile.isThereAnimal(this.drawMap.specialOne)) {
              g2d.setColor(new Color (255,255,255));
            }
            else  g2d.setColor(colorOfEnergy());
            g2d.fill(rectangle);
        }
        //ellipses are smaller than tile size
        else if(this.tile.hasGrass()) {
            Ellipse2D circle = new Ellipse2D.Double(this.getWidth()/10,this.getHeight()/10,
                        this.getWidth()-this.getWidth()/5, this.getHeight() - this.getHeight()/5);
            g2d.setColor(new Color(20, 52, 19));
            g2d.fill(circle);
        }
    }

    //colors animals by amount of energy (brightest color for 255 energy)
    private Color colorOfEnergy() {
        int energy = this.tile.getFirstAnim().getEnergy();
        int blue = Math.max(Math.min(255,energy),0);
        int red = Math.max(Math.min(255,energy),0);
        return new Color (red,0,blue);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(this.tile.isOccupied())
            this.drawMap.specialOne = this.tile.getFirstAnim();
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
