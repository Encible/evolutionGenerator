package Visualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    private DrawMap drawMap;

    public GameLoop(DrawMap drawMap) {
        this.drawMap = drawMap;
    }

    public void actionPerformed(ActionEvent arg) {
        this.drawMap.doOneLoop();
    }

}
