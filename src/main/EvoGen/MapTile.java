package EvoGen;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static EvoGen.MapDirection.intToVec;

public class MapTile {
    public boolean isJungle;
    private Vector2d position;
    private List<Animal> animals = new ArrayList<>();
    private List<ITileStateObserver> observers = new ArrayList<>();

    private Grass grass = null;
    private JungleMap map;

    public MapTile(Vector2d position, JungleMap map) {
        this.position = position;
        this.map = map;
        this.isJungle = position.belongs(map.getLlJungle(), map.getUrJungle());
    }

    public Vector2d getPosition() {
        return position;
    }

    public void addObserver (ITileStateObserver observer) { observers.add(observer); }

    public void addGrass(Grass grass) {
        this.grass = grass;
    }

    //gets called after being sure its occupied
    //cant be empty after grass got eaten by animal
    public void eatGrass() {
        animals.sort(Comparator.comparingInt(Animal::getEnergy));
        int index=0;
        int maxEnergy=animals.get(0).getEnergy();
        //finds all highest energy animals
        while(animals.size() > index && animals.get(index).getEnergy() == maxEnergy) {
            index++;
        }
        //all of them get fraction of grass
        for(int i=0; i<index; i++) {
            animals.get(i).eatGrass(this.map.getPlantEnergy()/index);
        }
        this.grass = null;
    }

    public boolean hasGrass() {
        return this.grass != null;
    }

    /*@Override
    public String toString(){
        if(animals.size() > 0) return "X"; //#TODO
        else if(grass!=null) return "+";
        else if(isJungle) return ",";
        else return ".";
    }*/

    private void gotEmpty() {
        for(ITileStateObserver observer : this.observers) {
            observer.gotEmpty(this);
        }
    }

    public boolean isOccupied() {
        return animals.size() > 0;
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }

    public void removeAnimal(Animal animal){
        animals.remove(animal);
        if(!isOccupied()) this.gotEmpty();
    }

    private static void shuffleArray(int[] arr){
        Random rand = ThreadLocalRandom.current();
        for(int i=arr.length - 1; i>0; i--) {
            int index = rand.nextInt(i+1);
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }

    //permutates array [0..7] and finds first non-occupied tile. If fails it takes first element of shuffled array
    public MapTile neighbourSpawnTile() {
        int[] arr = {0,1,2,3,4,5,6,7};
        shuffleArray(arr);
        for(int i : arr){
            Vector2d versor = intToVec(i);
            MapTile neighbour = this.map.getAdjTile(this, versor);
            if(!neighbour.isOccupied()) return neighbour;
        }
        return this.map.getAdjTile(this, intToVec(arr[0]));
    }


    public void createAnimal() {
        animals.sort(Comparator.comparingInt(Animal::getEnergy));
        Animal father=null, mother=null;
        for(Animal animal : animals) {
            if(father==null) {
                if(animal.isAble()) father = animal;
            }
            else if(animal.isAble()) {
                mother = animal;
                break;
            }
        }
        if(mother!=null) father.createWith(mother);
    }

    public Animal getFirstAnim() {
            return animals.get(0);
    }

    //made for observing animal that has lower energy than other animal on same tile
    public boolean isThereAnimal(Animal animal) {
        return animals.contains(animal);
    }
}
