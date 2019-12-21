package EvoGen;

import java.util.Arrays;
import static EvoGen.Rand.rand;

public class Genotype {
    private static int size = 32;
    private int[] gene = new int[size];

    public Genotype(){
        //creates random genotype
        for(int i = 0; i< size; i++) {
            this.gene[i] = rand(8);
        }
    }

    private boolean hasGene (int gene) {
        for(int i=0; i<size; i++) {
            if(this.gene[i] == gene) return true;
        }
        return false;
    }

    //checks if genotype is missing any num
    private void fixGenotype() {
        int[] sample = {0,1,2,3,4,5,6,7};
        int missing = -1; //-1 means no num is missing
        for(int i : sample) {
            if(!this.hasGene(i)) {
                missing = i;
                System.out.println(i);
                break;
            }
        }
        if(missing != -1) {
            this.gene[rand(size)] = missing;
            this.fixGenotype();
        }
    }

    public int getSingle() {
        return gene[rand(size)];
    }

    public Genotype mixGenes(Genotype latter) {
        int i1 = rand(size);
        int i2 = rand(size);
        Genotype result = new Genotype();
        for(int i=0; i<i1; i++) {
            result.gene[i] = this.gene[i];
        }
        for(int i=i1; i<i2; i++) {
            result.gene[i] = latter.gene[i];
        }
        for(int i=i2; i<size; i++) {
            result.gene[i] = this.gene[i];
        }
        result.fixGenotype();
        return result;
    }

    public String toString(){
        Arrays.sort(this.gene);
        StringBuilder sequence = new StringBuilder();
        for(int i=0; i<size; i++)
            sequence.append(Integer.toString(this.gene[i]));
        return sequence.toString();
    }

}
