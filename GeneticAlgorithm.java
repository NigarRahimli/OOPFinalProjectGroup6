import java.util.*;

public class GeneticAlgorithm {
    private static final int STUDENT_COUNT = 40;
    private static final int MAX_NUMBER_OF_DESTINATIONS_PER_STUDENT = 5;
    private static final int MAX_STUDENTS_PER_DESTINATION = 5;
    private static final int ND = 5;
    private static Random random = new Random();
    private static int student;
    private static int destinationId;
    public List<Integer> preferences;
    int a;
    ArrayList<Integer> destinationScore = new ArrayList<>();

    public GeneticAlgorithm(int student, List<Integer> preferences, int destinationId) {
        GeneticAlgorithm.student = student;
        this.preferences = preferences;
        GeneticAlgorithm.destinationId = destinationId;
    }
    
    private void mutation() {
        double mutationProbability = 0.1;
        if (random.nextDouble() < mutationProbability) {
            int mutationIndex = random.nextInt(preferences.size());
            int newGene = random.nextInt(10);
            preferences.set(mutationIndex, newGene);
        }
    }

    public void setPreferences(List<Integer> preferences) {
        this.preferences = preferences;
    }


    private GeneticAlgorithm crossover(GeneticAlgorithm parent1, GeneticAlgorithm parent2) {
        int crossoverPoint = random.nextInt(parent1.preferences.size());
        List<Integer> child1Preferences = new ArrayList<>();
        Set<Integer> selectedDestinations = new HashSet<>();
        for (int i = 0; i < crossoverPoint; i++) {
            int preference = parent1.preferences.get(i);
            child1Preferences.add(preference);
            selectedDestinations.add(preference);
        }
        for (int i = crossoverPoint; i < parent1.preferences.size(); i++) {
            int preference = parent2.preferences.get(i);
            if (!selectedDestinations.contains(preference)) {
                child1Preferences.add(preference);
                selectedDestinations.add(preference);
            }
        }
        while (child1Preferences.size() < MAX_NUMBER_OF_DESTINATIONS_PER_STUDENT) {
            int preference = parent1.preferences.get(child1Preferences.size());
            if (!selectedDestinations.contains(preference)) {
                child1Preferences.add(preference);
                selectedDestinations.add(preference);
            }
        }
        return new GeneticAlgorithm(student, child1Preferences, destinationId);
    }

    public void start() {
        // Initialize the population
        List<GeneticAlgorithm> population = new ArrayList<>();
        for (int i = 0; i < STUDENT_COUNT; i++) {
            GeneticAlgorithm stud = new GeneticAlgorithm(student, preferences, destinationId);
            population.add(stud);
        }
List<Integer> fitness = new ArrayList<>();

for (GeneticAlgorithm student : population) {
    int cost = 0;
    int destinationCount = 0;
    for (int i = 0; i < student.preferences.size(); i++) {
        int preference = student.preferences.get(i);
        if (preference == GeneticAlgorithm.destinationId && student.preferences.indexOf(preference) == i) {
            // Destination assigned to the student
            destinationCount++;
            if (destinationCount > MAX_STUDENTS_PER_DESTINATION) {
                cost += 10 * ND * 2; // Exceeding the limit, add penalty
            } else {
                cost += 0; // No penalty, destination assigned successfully
            }
        } else {
            cost += 10 * ND * 2; // Destination not assigned, add penalty
        }
    }
    fitness.add(cost);
}

// Select the best chromosomes
int population_size = 40;
List<GeneticAlgorithm> bestChromosomes = new ArrayList<>();
for (int i = 0; i < population_size; i++) {
    int minIndex = fitness.indexOf(Collections.min(fitness));
    bestChromosomes.add(population.get(minIndex));
    fitness.set(minIndex, Integer.MAX_VALUE);
}


// Crossover
List<GeneticAlgorithm> newPopulation = new ArrayList<>();
for (int i = 0; i < bestChromosomes.size(); i += 2) {
    GeneticAlgorithm parent1 = bestChromosomes.get(i);
    GeneticAlgorithm parent2 = bestChromosomes.get(i + 1);
    int crossoverPoint = random.nextInt(parent1.preferences.size());
    List<Integer> child1Preferences = new ArrayList<>();
    for (int j = 0; j < crossoverPoint; j++) {
        child1Preferences.add(parent1.preferences.get(j));
    }
    for (int j = crossoverPoint; j < parent1.preferences.size(); j++) {
        child1Preferences.add(parent2.preferences.get(j));
    }
    GeneticAlgorithm child1 = new GeneticAlgorithm(student, child1Preferences, destinationId);
    child1.mutation();
    newPopulation.add(child1);

    List<Integer> child2Preferences = new ArrayList<>();

    for (int j = 0; j < crossoverPoint; j++) {
        child2Preferences.add(parent2.preferences.get(j));
    }
    for (int j = crossoverPoint; j < parent2.preferences.size(); j++) {
        child2Preferences.add(parent2.preferences.get(j));
    }
    GeneticAlgorithm child2 = new GeneticAlgorithm(student, child2Preferences, destinationId);
    child2.mutation();
    newPopulation.add(child2);
}

// Mutation.
double mutationProbability = 0.1;
for (GeneticAlgorithm student : newPopulation) {
    student.mutation();
}

// Evaluate the new population
List<Integer> newFitness = new ArrayList<>();

for (GeneticAlgorithm student : newPopulation) {
    int cost = 0;
    int si = 0;
    for (int i = 0; i < student.preferences.size(); i++) {
        int preference = student.preferences.get(i);
        if (preference == GeneticAlgorithm.destinationId && student.preferences.indexOf(preference) == i) {
            cost += 0;
            
        } else {
            cost += 10 * ND * 2;
        }
        
    }

    newFitness.clear();
    newFitness.add(cost);
}

// Select the best chromosomes
List<GeneticAlgorithm> bestNewChromosomes = new ArrayList<>();
for (int i = 0; i < population_size; i++) {
    int minIndex = newFitness.indexOf(Collections.min(newFitness));
    bestNewChromosomes.add(newPopulation.get(minIndex));
    newFitness.set(minIndex, Integer.MAX_VALUE);
}

// Replace the old population with the new population
population = new ArrayList<>(bestNewChromosomes);

// Repeat until a solution is found
boolean found = false;
while (!found) {
    // Crossover
    List<GeneticAlgorithm> offspring = new ArrayList<>();
    for (int i = 0; i < bestChromosomes.size(); i += 2) {
        GeneticAlgorithm child1 = crossover(bestChromosomes.get(i), bestChromosomes.get(i + 1));
        GeneticAlgorithm child2 = crossover(bestChromosomes.get(i + 1), bestChromosomes.get(i));
        offspring.add(child1);
        offspring.add(child2);
    }

    // Mutation
    for (GeneticAlgorithm student : offspring) {
        if (random.nextDouble() < mutationProbability) {
            int mutationIndex = random.nextInt(student.preferences.size());
            int newGene = random.nextInt(10);
            student.preferences.set(mutationIndex, newGene);
        }
    }

   // Evaluate the new population
for (GeneticAlgorithm student : offspring) {
    int cost = 0;
    for (int i = 0; i < MAX_NUMBER_OF_DESTINATIONS_PER_STUDENT; i++) {
        int preference = student.preferences.get(i);
        if (preference == student.destinationId && student.preferences.indexOf(preference) == i) {
            cost = 0;
        } else {
            cost += 10 * ND * 2;
        }
        if(i >=4)
        {
            break;
        }
    }
    newFitness.add(cost);
}

// Select the best chromosomes
for (GeneticAlgorithm student : offspring) {
    int cost = 0;
    for (int i = 0; i < student.preferences.size(); i++) {
        int preference = student.preferences.get(i);
//        System.out.println(preference + " costtt  " + i);
        if (preference == GeneticAlgorithm.destinationId && student.preferences.indexOf(preference) <= i) {
            cost = 0;
//            System.out.println("HERKLk");
        } else {
            cost += 10 * ND * 2;
        }
    }
    newFitness.add(cost);
}
found = true;
    break;
}





System.out.println("Solution found:");
System.out.println("Student " + GeneticAlgorithm.student + " assigned to "+GeneticAlgorithm.destinationId);
    }
    
}
