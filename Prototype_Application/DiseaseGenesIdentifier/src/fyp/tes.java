package fyp;

import java.math.BigDecimal;
import java.util.*;

import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;

/**
 * In the mover example all things that should be moved from one location to the
 * other is put in boxes. The boxes vary in size and all have a different
 * volume. The mover also has a couple of vans in which the boxes are stored.
 * All vans have the same volume. Given is a number of boxes
 * with varying sizes. To reduce transport costs, it is crucial for the moving company to use as minimal vans as possible.
 * Given the size of the vans, what is the optimal
 * distribution of the boxes so that a minimal number of vans is needed?
 * 
 * Although one can calculate the total volume of the boxes and divide this with the volume of the vans,
 * this calculation says nothing about the arrangement of the boxes in the vans. The arrangement is
 * what this genetic program solves.
 *
 * The constants can be modified to experiment with different program settings.
 */
public class tes {
//	private static final Logger LOG = Logger.getLogger(DiseaseGenesIdentifier.class);
//	// No need to have this much evolutions but we do want the optimal solution at the cost of cpu
//	private static final int NUMBER_OF_EVOLUTIONS = 5000;
//	// The amount of boxes used to move things from one location to the other. The number of boxes determines the number of genes.
//	private static final int NUMBER_OF_BOXES = 300;
//	// The volume of the vans in cubic meters
//	private static final double VOLUME_OF_VANS = 5.4;
//	
//	private static final int TOTAL_KILOMETERS = 75;
//	private static final BigDecimal COSTS_KILOMETER = new BigDecimal("2.50");
//
//	// The minimum size of the width,height and depth of a box
//	private static final double MINIMUM_VOLUME = 0.25;
//	// The maximum random size. The maximum size of a box is thus MINIMUM_VOLUME + MAXIMUM_VOLUME
//	private static final double MAXIMUM_VOLUME = 2.75;
//    // The size of the population (number of chromosomes in the genotype)    
//    private static final int SIZE_OF_POPULATION = 50;
//
//    private TeaProductPack[] boxes;
//	private double totalVolumeOfBoxes = 0.0D;
//
//    public tes(int seed) throws Exception {
//		this.createBoxes(seed);
//		
//		//Genotypes are fixed-length populations of chromosomes.
//		//As an instance of a Genotype is evolved, all of its Chromosomes are also evolved.
//		Genotype genotype = this.configureTeaProducOptimizer();
//		this.evolve(genotype);
//	}
//
//    /**
//     * Setup JGAP.
//     */
//    private Genotype configureTeaProducOptimizer() throws InvalidConfigurationException {
//		Configuration gaConf = new DefaultConfiguration();
//		
//		// Here we specify a fitness evaluator where lower values means a better fitness
//		Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
//		
//		gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());
//
//		// Only use the swapping operator. Other operations makes no sense here
//		// and the size of the chromosome must remain constant
//		gaConf.getGeneticOperators().clear();
//		
//		/*
//		 * Swaps the genes instead of mutating them. 
//		 * This kind of operator is required by Traveling Salesman Problem. 
//		 *  J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht. 
//		 *  Genetic algorithms for the traveling salesman problem.
//		 *   In Proceedings of the Second International Conference on Genetic Algorithms.
//		 *    Lawrence Eribaum Associates, Mahwah, NJ, 1985. and also Sushil J. Louis & Gong Li }
//		 * 
//		 */
//		SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
//		gaConf.addGeneticOperator(swapper);
//
//        // We are only interested in the most fittest individual
//		
//		//Determines whether to save (keep) the fittest individual.
//        gaConf.setPreservFittestIndividual(true);
//        
//        // Allows to keep the population size constant during one evolution, 
//        //even if there is no appropriate instance of NaturalSelector 
//        //(such as WeightedRouletteSelector) registered with the Configuration.
//		gaConf.setKeepPopulationSizeConstant(false);
//
//		gaConf.setPopulationSize(SIZE_OF_POPULATION);
//        // The number of chromosomes is the number of boxes we have. Every chromosome represents one box.
//        int chromeSize = this.boxes.length;
//		Genotype genotype;
//
//		// Setup the structure with which to evolve the solution of the problem.
//        // An IntegerGene is used. This gene represents the index of a box in the boxes array.
//		IChromosome sampleChromosome = new Chromosome(gaConf, new IntegerGene(gaConf), chromeSize);
//		gaConf.setSampleChromosome(sampleChromosome);
//		
//        // Setup the fitness function
//		TeaProductPackTravellerFitnessFunction fitnessFunction = new TeaProductPackTravellerFitnessFunction();
//		fitnessFunction.setBoxes(this.boxes);
//		fitnessFunction.setLorryCapacity(VOLUME_OF_VANS);
//		gaConf.setFitnessFunction(fitnessFunction);
//
//		// Because the IntegerGenes are initialized randomly, it is neccesary to set the values to the index. Values range from 0..boxes.length
//		//Convenience method that returns a newly constructed Genotype instance configured according to the given Configuration instance.
//		/*
//		 * Genotypes are fixed-length populations of chromosomes. As an instance of a Genotype is evolved, all of its Chromosomes are also evolved. 
//		 * A Genotype may be constructed normally via constructor, 
//		 * or the static randomInitialGenotype() method can be used to generate a Genotype with a randomized Chromosome population.
//		 */
//		genotype = Genotype.randomInitialGenotype(gaConf);
//		
//		// genotype.getPopulation() - Returns the current population of chromosomes
//		// genotype.getPopulation().getChromosomes(); - the list of Chromosome's in the Population.
//		List chromosomes = genotype.getPopulation().getChromosomes();
//        for (Object chromosome : chromosomes) {
//            IChromosome chrom = (IChromosome) chromosome;
//            for (int j = 0; j < chrom.size(); j++) {
//                Gene gene = chrom.getGene(j);
//                gene.setAllele(j);
//            }
//        }
//
//		return genotype;
//	}
//
//	/**
//	 * Creates the boxes which are needed for the move from one location to the
//	 * other.
//	 */
//	private void createBoxes(int seed) {
//		Random r = new Random(seed);
//		this.boxes = new TeaProductPack[NUMBER_OF_BOXES];
//		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
//			TeaProductPack teaProductPack = new TeaProductPack(MINIMUM_VOLUME + (r.nextDouble() * MAXIMUM_VOLUME)); // set appropriate size
//            teaProductPack.setId(i);
//            this.boxes[i] = teaProductPack;
//        }
//
//		
//		// get the total volume
//        double[] volumes = new double[this.boxes.length];
//        for (int i = 0; i < this.boxes.length; i++) {
//			LOG.debug("TeaProductPack [" + i + "]: " + this.boxes[i]);
//			this.totalVolumeOfBoxes += this.boxes[i].getVolume();
//            volumes[i] = this.boxes[i].getVolume(); 
//        }
//		LOG.info("The total volume of the [" + NUMBER_OF_BOXES + "] boxes is [" + this.totalVolumeOfBoxes + "] cubic metres.");
//    }
//
//	/**
//	 * Evolves the population.
//	 */
//	private void evolve(Genotype a_genotype) {
//		int optimalNumberOfVans = (int) Math.ceil(this.totalVolumeOfBoxes / VOLUME_OF_VANS);
//		LOG.info("The optimal number of vans needed is [" + optimalNumberOfVans + "]");
//		
//		double previousFittest = a_genotype.getFittestChromosome().getFitnessValue();
//		int numberOfVansNeeded = Integer.MAX_VALUE;
//		for (int i = 0; i < NUMBER_OF_EVOLUTIONS; i++) {
//			if (i % 250 == 0) {
//				LOG.info("Number of evolutions [" + i + "]");
//			}
//			a_genotype.evolve();
//			
//			//retrieves the Chromosome in the Population with the highest fitness value.
//			//Retrieves the fitness value of this Chromosome, as determined by the active fitness function.
//			double fittness = a_genotype.getFittestChromosome().getFitnessValue();
//			
//			int vansNeeded = this.numberOfVansNeeded(a_genotype.getFittestChromosome().getGenes()).size();
//			if (fittness < previousFittest && vansNeeded < numberOfVansNeeded) {
//				this.printSolution(a_genotype.getFittestChromosome());
//				previousFittest = fittness;
//				numberOfVansNeeded = vansNeeded;
//			}
//			
//			// No more optimal solutions
//			if (numberOfVansNeeded == optimalNumberOfVans) {
//				break;
//			}
//		}
//		IChromosome fittest = a_genotype.getFittestChromosome();
//
//        List<Lorry> lorries = numberOfVansNeeded(fittest.getGenes());
//        printVans(lorries);
//        this.printSolution(fittest);
//	}
//
//	private void printSolution(IChromosome fittest) {
//		// The optimal genes of the most optimal population
//		Gene[] genes = fittest.getGenes();
//		List<Lorry> lorries = numberOfVansNeeded(genes);
//
//        System.out.println("Fitness value [" + fittest.getFitnessValue() + "]");
//		System.out.println("The total number of vans needed is [" + lorries.size() + "]");
////		System.out.println("The total costs are [" + (TOTAL_KILOMETERS * COSTS_KILOMETER.doubleValue()) * vans.size() + "]");
//	}
//
//    private void printVans(List<Lorry> lorries) {
//        int index = 1;
//        for (Lorry lorry : lorries) {
//            System.out.println("Lorry [" + index + "] has contents with a total volume of [" + lorry.getVolumeOfContents() + "] and contains the following boxes:");
//            List<TeaProductPack> boxes = lorry.getContents();
//            for (TeaProductPack teaProductPack : boxes) {
//                System.out.println("    " + teaProductPack);
//            }
//            index++;
//        }
//    }
//
//	private List<Lorry> numberOfVansNeeded(Gene[] genes) {
//        List<Lorry> lorries = new ArrayList();
//        Lorry lorry = new Lorry(VOLUME_OF_VANS);
//        for (Gene gene : genes) {
//        	 // Retrieves the value represented by this Gene.
//            int index = (Integer) gene.getAllele();
//           
//          
//            if (!lorry.addTeaProductPack(this.boxes[index])) {
//                // A new van is needed
//                lorries.add(lorry);
//                lorry = new Lorry(VOLUME_OF_VANS);
//                lorry.addTeaProductPack(this.boxes[index]);
//            }
//        }
//		return lorries;
//	}

    /**
     * Starts the moving example. A seed can be specified on the command line which generates a particular sequence of boxes. When no seed
     * is specified the seed 37 is used.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	Data data = new Data();
		
		//new TeaProductPackTraveller();
	}

    

}
