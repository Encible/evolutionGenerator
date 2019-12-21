package Visualizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;


public class Parameters {

    //no getters for brevity
    public int width;
    public int height;
    public double jungleRatio;
    public int startEnergy;
    public int plantEnergy;
    public int moveEnergy;
    public int animalsNum;
    public int plantsPerDay;
    public int msDelay;


    public Parameters() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("parameters.json")) {

            Object obj = jsonParser.parse(reader);
            JSONObject parameters = (JSONObject) obj;

            parseData(parameters);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void parseData(JSONObject parameters) {
        this.width = Integer.parseInt(parameters.get("width").toString());
        this.height = Integer.parseInt(parameters.get("height").toString());
        this.startEnergy = Integer.parseInt(parameters.get("startEnergy").toString());
        this.plantEnergy = Integer.parseInt(parameters.get("plantEnergy").toString());
        this.moveEnergy = Integer.parseInt(parameters.get("moveEnergy").toString());
        this.animalsNum = Integer.parseInt(parameters.get("animalsNum").toString());
        this.plantsPerDay = Integer.parseInt(parameters.get("plantsPerDay").toString());
        this.msDelay = Integer.parseInt(parameters.get("msDelay").toString());
        this.jungleRatio = (double) parameters.get("jungleRatio");
    }
}
