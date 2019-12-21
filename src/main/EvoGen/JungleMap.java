package EvoGen;

import java.util.*;

import static EvoGen.Rand.rand;

public class JungleMap implements IPositionChangeObserver, ITileStateObserver {
    //size
    private int width;
    private int height;
    private int jWidth;
    private int jHeight;

    //Vectors
    private Vector2d llJungle;
    private Vector2d urJungle;

    //Misc
    public MapTile[][] tiles;  //stores crucial info about single tile
    private List<MapTile> emptyMeadow = new ArrayList<>();  //
    private List<MapTile> emptyJungle = new ArrayList<>();  //both store tiles without grass nor animals
    private List<MapTile> occupiedTiles = new ArrayList<>();
    private List<Animal> animals = new ArrayList<>();


    //Stats
    public int epoch = 0;
    private int plantEnergy;
    private int moveEnergy;
    private int startEnergy;

    private int avgEnergy;
    private int avgKids;
    private List<Integer> lifetime = new ArrayList<>();


    public JungleMap(int width, int height, int jWidth, int jHeight, int plantEnergy, int moveEnergy, int startEnergy) {
        this.width = width;
        this.height = height;
        this.jHeight = Math.min(jHeight, height);
        this.jWidth = Math.min(jWidth, width);
        tiles = new MapTile[width][height];
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.startEnergy = startEnergy;

        llJungle = new Vector2d((this.width - this.jWidth) / 2, (this.height - this.jHeight) / 2);
        urJungle = llJungle.add(new Vector2d(this.jWidth - 1, this.jHeight - 1));

        //adds all tiles to empty tiles list
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                tiles[j][i] = new MapTile(new Vector2d(j, i), this);
                if (tiles[j][i].isJungle) emptyJungle.add(tiles[j][i]);
                else emptyMeadow.add(tiles[j][i]);
                tiles[j][i].addObserver(this);
            }
        }
    }

//GETTERS
    public int getPlantEnergy() {
        return this.plantEnergy;
    }
    public int getStartEnergy() {
        return this.startEnergy;
    }
    public int getMoveEnergy() {
        return this.moveEnergy;
    }
    public Vector2d getLlJungle() {
        return this.llJungle;
    }
    public Vector2d getUrJungle() {
        return this.urJungle;
    }
    public int getTotalAnimals() {
        return animals.size();
    }
    public int getEpoch() {
        return this.epoch;
    }
    public int getAvgEnergy() {
        return avgEnergy;
    }
    public int getAvgKids() {
        return avgKids;
    }
//EOF GETTERS

//GAME CYCLE
    private void removeAnimal(Animal animal) {
        this.getTile(animal.getPosition()).removeAnimal(animal);
    }
    public void removeBodies() {
        List<Animal> toDelete = new ArrayList<>();
        for (Animal animal : animals) {
            if(animal.isDead()) {
                toDelete.add(animal); //adds dead animals to another list, as otherwise it would have lost next pointer;
                this.removeAnimal(animal);
            }
        }
        for(Animal animal : toDelete) {
            this.lifetime.add(animal.getAge());
            animals.remove(animal);
        }
    }

    public void moveAnimals() {
        for (Animal animal : animals) {
            animal.move();
        }
    }

    public void eatGrass() {
        for (MapTile tile : occupiedTiles) {
            if(tile.hasGrass())
                tile.eatGrass();
        }
    }

    public void procreate() {
        for (MapTile tile : occupiedTiles) {
            tile.createAnimal();
        }
    }

    //Random tile gets grass, gets added to grassTiles, gets removed from empty tiles;
    private void grassToList(Grass grass, List<MapTile> list){
        if(list.size()>0) {
            int index = rand(list.size());
            list.get(index).addGrass(grass);
            list.remove(index);
        }
    }
    public void spawnGrass(Grass grass) {
        grassToList(grass, this.emptyMeadow);
        grassToList(grass, this.emptyJungle);
    }
//EOF GAME CYCLE


//VECTORS / TILES
    public Vector2d rebound (Vector2d coords) {
        coords.x = (coords.x + this.width)% this.width;
        coords.y = (coords.y + this.width)% this.height;
        return coords;
    }
    public MapTile getTile(Vector2d position) {
        return tiles[position.x][position.y];
    }
    public MapTile getAdjTile(MapTile tile, Vector2d versor) {
        return tiles[(tile.getPosition().x + versor.x+this.width)%this.width][(tile.getPosition().y + versor.y+this.height)%this.height];
    }
    public Vector2d getRandPosition() {
        return new Vector2d(rand(this.width), rand(this.height));
    }
//EOF VECTORS / TILES

    public void place(Animal animal) {
        this.getTile(animal.getPosition()).addAnimal(animal);
        animal.addObserver(this);
        animals.add(animal);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition){
        this.getTile(oldPosition).removeAnimal(animal);

        MapTile temp =this.getTile(animal.getPosition());
        temp.addAnimal(animal);
        if(!occupiedTiles.contains(temp)) {
            this.occupiedTiles.add(temp);
        }
    }

    @Override
    public void gotEmpty(MapTile tile) {
        if(tile.isJungle) this.emptyJungle.add(tile);
        else  this.emptyMeadow.add(tile);
        this.occupiedTiles.remove(tile);
    }

    public int totalGrass() {
        int grassAmount = 0;
        for (int i = 0; i < this.height; i++)
            for (int j = 0; j < this.width; j++)
                if(this.tiles[j][i].hasGrass()) grassAmount++;
        return grassAmount;
    }


    public String mostFrequentGene() {
        Map<String, Integer> map = new HashMap<>();
        for (Animal animal : this.animals) {
            String key = animal.getGenotype().toString();
            if (map.containsKey(key)) {
                int freq = map.get(key);
                freq++;
                map.put(key, freq);
            } else {
                map.put(key, 1);
            }
        }
        String mostDominant = null;
        int max_count = 0;
        for(Map.Entry<String, Integer> val : map.entrySet()) {
            if (max_count < val.getValue()) {
                mostDominant = val.getKey();
                max_count = val.getValue();
            }
        }
        return mostDominant;
    }

    //updates all average stats in one cycle
    public void updateAvg() {
        int totalEnergy=0;
        int totalKids = 0;
        for(Animal animal: animals) {
            totalEnergy+= animal.getEnergy();
            totalKids+= animal.getKids();
        }
        if(animals.size()>0) {
            this.avgKids = totalKids/animals.size();
            this.avgEnergy = totalEnergy/animals.size();
        }
        else{
            this.avgEnergy = 0;
            this.avgKids = 0;
        }
    }

    public int getAvgLifetime() {
        int totalLifetime = 0;
        for(Integer age : this.lifetime) { //lifetime of dead animals
            totalLifetime+=age;
        }
        for(Animal animal : this.animals) { //lifetime of alive animals
            totalLifetime+=animal.getAge();
        }
       if(this.lifetime.size()+animals.size()>0) return totalLifetime/(this.lifetime.size()+animals.size());
       return -1;
    }

    //made purely for findingHeriage in animal class
    public void undoAnimalCheck() {
        for(Animal animal : animals) {
            animal.alreadyChecked = false;
        }
    }

}
