import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class Individual {

    private String chromosome;
    private int fitness;

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
        this.fitness = calculateFitness();
    }

    public Individual(String parentX, String parentY) {
        this.chromosome = parentX.concat(parentY);
        this.fitness = calculateFitness();
    }

    public String getChromosome() {
        return this.chromosome;
    }

    public Individual() {
        this.chromosome = fillChromosome();
        this.fitness = calculateFitness();
    }

    public Individual(String chromosome) {
        this.chromosome = chromosome;
        this.fitness = calculateFitness();
    }

    public int calculateFitness() {
        int value = 0;

        for (int i = 0; i < Main.TARGET.length(); i++) {
            if (Main.TARGET.charAt(i) != this.chromosome.charAt(i)) {
                value++;
            }
        }

        return value;
    }

    public static char randomGen() {
        Random random = new Random();
        int index = random.nextInt(Main.ALPHABET.length);
        return Main.ALPHABET[index];
    }

    public static String fillChromosome() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Main.TARGET.length(); i++) {
            sb.append(randomGen());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome='" + chromosome + '\'' +
                ", fitness=" + fitness +
                '}';
    }
    public int getFitness(){
        return this.fitness;
    }
}

class Generation {

    public static final double smallPercentage = 0.15;
    public static final int SIZE = 16;
    public Random random = new Random();
    private ArrayList<Individual> generation;
    public static int counter=0;

    public Generation() {
        this.generation = new ArrayList<>();
    }


    public Generation populate(){
        Generation newGeneration = new Generation();
        for (int j = 0; j < 3; j++){
            newGeneration.add(generation.get(j));
        }
        for (int i=3; i<SIZE; i++){
            Individual parent1 = RankSelection(this.generation);
            Individual parent2 = RankSelection(this.generation);
            Individual child = reproduce(parent1,parent2);

            double probability = random.nextDouble();

            if(probability <= smallPercentage){
                child=mutate(child);
            }
            newGeneration.generation.add(child);

        }
        counter++;
        return newGeneration;
    }


    public void add(Individual individual) {
        generation.add(individual);
    }

    public ArrayList<Individual> getGeneration() {
        return generation;
    }


    public Individual RankSelection(ArrayList<Individual> gnrtion){
        double size = ((double) SIZE);
        double factor = 200/(size*(size+1)); //((n*(n+1))/2)*100
        ArrayList<Double> probabilities = new ArrayList<>();
        double probability=0.0;
        int i=0;
        for ( i = 0; i<SIZE; i++){
            probability += (SIZE-i)*factor;
            probabilities.add(probability);
        }
        double randomDouble = random.nextDouble()*100;
        for ( i = 0; i<SIZE; i++){
            if(randomDouble < probabilities.get(i)) {
                break;
            }
        }
        return gnrtion.get(i);
    }


    public Individual reproduce(Individual x, Individual y) {
        int n = Main.TARGET.length();

        int index = random.nextInt(n);


        return new Individual(x.getChromosome().substring(0, index), y.getChromosome().substring(index));
    }

    public Individual mutate(Individual child) {

        StringBuilder sb = new StringBuilder(child.getChromosome());

        int index = random.nextInt(child.getChromosome().length());
        char ch = Main.ALPHABET[random.nextInt(Main.ALPHABET.length)];

        sb.setCharAt(index, ch);
        child.setChromosome(sb.toString());
        return child;
    }



}

public class Main {

    public static final char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', ' ', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9',
            '-'
    };

    public static final String TARGET = "ChatGPT and GPT-4";

    public static void main(String[] args) {
        int sumofgeneration = 0;
        Generation generation = new Generation();
        ArrayList<Integer> numofgenerations = new ArrayList<>();
        ArrayList<Long> timeList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            long start = System.nanoTime();
            genetic(generation);
            long finish = System.nanoTime();
            timeList.add((finish-start)/1000000);
            numofgenerations.add(Generation.counter);
            Generation.counter=0;
        }
        for (int i = 0; i < 3; i++) {
            System.out.println("Solution "+(i+1)+": "+numofgenerations.get(i)+" Time: "+timeList.get(i)+"ms");
            sumofgeneration+=numofgenerations.get(i);
        }
        System.out.println("Mean of solutions: "+sumofgeneration/3);
        System.out.println("Number of chromosome: "+Generation.SIZE);

    } // MAIN   MAIN   MAIN   MAIN

    public static void genetic(Generation generation){
        for (int i = 0; i < Generation.SIZE; i++) {
            generation.add(new Individual());
        }

        boolean success = false;
        while (!success) {

            for (Individual in : generation.getGeneration()) {
                if (in.getFitness() <= 0) {


                    System.out.print("Password is found: ");
                    success = true;
                    break;

                }
            }
            Collections.sort(generation.getGeneration(), new Comparator<Individual>() {
                @Override
                public int compare(Individual o1, Individual o2) {
                    return o1.getFitness() > o2.getFitness() ? 1 : (o1.getFitness() < o2.getFitness()) ? -1 : 0;
                }
            });
            generation = generation.populate();
            System.out.println(generation.getGeneration().get(0).getChromosome());
        }

    }
}