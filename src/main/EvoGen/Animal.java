package EvoGen;

import java.util.ArrayList;
import java.util.List;

import static EvoGen.MapDirection.intToDir;
import static EvoGen.Rand.rand;

public class Animal {
    private Vector2d position;
    private MapDirection orient;

    private JungleMap map;

    private Genotype genotype;
    private int energy;
    private int age;

    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public boolean alreadyChecked = false;

   private List<Animal> kidsList = new ArrayList<>();
    private int kids;

    public Animal(JungleMap map, Vector2d position, Genotype genotype) {
        this.map = map;
        this.energy = map.getStartEnergy();
        this.age = 0;
        this.kids = 0;
        this.orient = intToDir(rand(8));

        if(position == null)  position = this.map.getRandPosition();
        this.position = position;

        if(genotype == null) genotype = new Genotype();
        this.genotype = genotype;
    }

    private void changeOrient() {
        int dirAdd = this.genotype.getSingle();
        int dirInit = this.orient.toInt();
        this.orient = intToDir((dirAdd+dirInit)%8);
    }

    //Goes forward and stores old position
    private Vector2d goForward() {
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.orient.toVector());

        this.position = this.map.rebound(this.position); //in case anim is out of map
        return oldPosition;

    }

    public void move(){
        this.changeOrient();
        Vector2d oldPosition = this.goForward();
        this.positionChanged(oldPosition);
        this.energy -= map.getMoveEnergy();
        this.age++;
    }

    public void addObserver (IPositionChangeObserver observer) {
        observers.add(observer);
    }

    private void positionChanged(Animal this, Vector2d oldPosition) {
        for(IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(this, oldPosition);
        }
    }

    public boolean isAble() {
        return age > 0 && this.energy >= this.map.getStartEnergy() / 2;
    }
    public boolean isDead() {return energy<=0;}

    public void createWith (Animal mother) {
        Vector2d kidPosition = this.map.getTile(this.position).neighbourSpawnTile().getPosition();
        Genotype kidGene = this.genotype.mixGenes(mother.genotype);
        Animal kid = new Animal(this.map, kidPosition, kidGene);

        kid.map.place(kid);

        kid.energy = this.energy/4 + mother.energy/4;
        this.energy-=this.energy/4;
        mother.energy-=mother.energy/4;


        this.kids++;
        this.kidsList.add(kid);
        mother.kids++;
        mother.kidsList.add(kid);
    }

    public void eatGrass(int grassEnergy) {
        this.energy += grassEnergy;
    }

    public Genotype getGenotype() {
       return this.genotype;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    public String toString() {return this.position.toString();}
    public int getEnergy() {return this.energy;}
    public int getAge() {
        return this.age;
    }
    public int getKids() {
        return this.kids;
    }

    public int getHeritage() {
        int heritage = 0;
        for(Animal kid : kidsList) {
            if(!alreadyChecked) {
                heritage+=kid.getHeritage();
                kid.alreadyChecked=true;
            }
        }
        return heritage + this.kids;
    }
}
