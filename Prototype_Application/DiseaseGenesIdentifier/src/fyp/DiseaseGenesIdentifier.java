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
import org.jgap.NaturalSelector;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.WeightedRouletteSelector;

public class DiseaseGenesIdentifier {
	private static final Logger LOG = Logger
			.getLogger(DiseaseGenesIdentifier.class);
	// No need to have this much evolutions but we do want the optimal solution
	// at the cost of cpu
	private static final int NUMBER_OF_EVOLUTIONS = 100;
	// The amount of boxes used to move things from one location to the other.
	// The number of boxes determines the number of genes.
	// The volume of the lorries in cubic meters
	// disease genes total score (thershold for disease)
	private static final double DISEASE_THRESHOLD = 3;

	// The minimum size of the width,height and depth of a box
	// The maximum random size. The maximum size of a box is thus MINIMUM_VOLUME
	// The size of the population (number of chromosomes in the genotype)
	private static final int SIZE_OF_POPULATION = 50;

	private DiseaseGene[] diseaseGenes;
	private double totalDiseaseGeneScore = 0.0D;

	private String fitnessValue = "";

	private String diseaseScore = "";

	private Map<String, List<String>> scoreInDisease;

	private List<Double> fitnessValues;

	public DiseaseGenesIdentifier(int seed) throws Exception {
		this.addDiseaseGenes(seed);

		// Genotypes are fixed-length populations of chromosomes.
		// As an instance of a Genotype is evolved, all of its Chromosomes are
		// also evolved.
		Genotype genotype = this.configureJGAP();
		this.evolve(genotype);
	}

	/**
	 * Setup JGAP.
	 */
	private Genotype configureJGAP() throws InvalidConfigurationException {
		Configuration diseaseGenesIdentifierConfig = new DefaultConfiguration();

		// Here we specify a fitness evaluator where lower values means a better
		// fitness
		Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);

		diseaseGenesIdentifierConfig
				.setFitnessEvaluator(new DeltaFitnessEvaluator());

		// Only use the swapping operator. Other operations makes no sense here
		// and the size of the chromosome must remain constant
		diseaseGenesIdentifierConfig.getGeneticOperators().clear();

		/*
		 * Swaps the genes instead of mutating them. This kind of operator is
		 * required by Traveling Salesman Problem. J. Grefenstette, R. Gopal, R.
		 * Rosmaita, and D. Gucht. Genetic algorithms for the traveling salesman
		 * problem. In Proceedings of the Second International Conference on
		 * Genetic Algorithms. Lawrence Eribaum Associates, Mahwah, NJ, 1985.
		 * and also Sushil J. Louis & Gong Li }
		 */
		/*
		 * NaturalSelector a_selector = new WeightedRouletteSelector();
		 * diseaseGenesIdentifierConfig.addNaturalSelector(a_selector,true);
		 */

		SwappingMutationOperator swappingMutationOperator = new SwappingMutationOperator(
				diseaseGenesIdentifierConfig);
		diseaseGenesIdentifierConfig
				.addGeneticOperator(swappingMutationOperator);

		// selection method
		// 92
		BestChromosomesSelector selector = new BestChromosomesSelector(
				diseaseGenesIdentifierConfig);

		// 97
		WeightedRouletteSelector we = new WeightedRouletteSelector(
				diseaseGenesIdentifierConfig);
		diseaseGenesIdentifierConfig.addNaturalSelector(selector, true);

		// We are only interested in the most fittest individual

		// Determines whether to save (keep) the fittest individual.
		diseaseGenesIdentifierConfig.setPreservFittestIndividual(true);

		// Allows to keep the population size constant during one evolution,
		// even if there is no appropriate instance of NaturalSelector
		// (such as WeightedRouletteSelector) registered with the Configuration.
		diseaseGenesIdentifierConfig.setKeepPopulationSizeConstant(false);

		diseaseGenesIdentifierConfig.setPopulationSize(SIZE_OF_POPULATION);
		// The number of chromosomes is the number of boxes we have. Every
		// chromosome represents one box.
		int chromeSize = this.diseaseGenes.length;

		Genotype genotype;

		// Setup the structure with which to evolve the solution of the problem.
		// An IntegerGene is used. This gene represents the index of a box in
		// the boxes array.
		IChromosome sampleChromosome = new Chromosome(
				diseaseGenesIdentifierConfig, new IntegerGene(
						diseaseGenesIdentifierConfig), chromeSize);
		diseaseGenesIdentifierConfig.setSampleChromosome(sampleChromosome);

		// Setup the fitness function
		DiseaseGeneIdentifierFitnessFunction fitnessFunction = new DiseaseGeneIdentifierFitnessFunction();
		fitnessFunction.setGenes(this.diseaseGenes);
		fitnessFunction.setDiseaseThreshold(DISEASE_THRESHOLD);
		diseaseGenesIdentifierConfig.setFitnessFunction(fitnessFunction);

		// Because the IntegerGenes are initialized randomly, it is neccesary to
		// set the values to the index. Values range from 0..boxes.length
		// Convenience method that returns a newly constructed Genotype instance
		// configured according to the given Configuration instance.
		/*
		 * Genotypes are fixed-length populations of chromosomes. As an instance
		 * of a Genotype is evolved, all of its Chromosomes are also evolved. A
		 * Genotype may be constructed normally via constructor, or the static
		 * randomInitialGenotype() method can be used to generate a Genotype
		 * with a randomized Chromosome population.
		 */
		genotype = Genotype.randomInitialGenotype(diseaseGenesIdentifierConfig);

		// genotype.getPopulation() - Returns the current population of
		// chromosomes
		// genotype.getPopulation().getChromosomes(); - the list of Chromosome's
		// in the Population.
		List chromosomes = genotype.getPopulation().getChromosomes();
		for (Object chromosome : chromosomes) {
			IChromosome chrom = (IChromosome) chromosome;
			for (int j = 0; j < chrom.size(); j++) {
				Gene gene = chrom.getGene(j);
				gene.setAllele(j);
			}
		}

		return genotype;
	}

	/**
	 * Creates the boxes which are needed for the move from one location to the
	 * other.
	 */
	private void addDiseaseGenes(int seed) {

		Data data = new Data();
		LinkedHashMap<String, Double> genes = data.getDiseaseGenes();
		int numberOfGenes = genes.size();

		this.diseaseGenes = new DiseaseGene[numberOfGenes];
		for (int i = 0; i < numberOfGenes; i++) {

			Double value = (new ArrayList<Double>(genes.values())).get(i);
			String key = (new ArrayList<String>(genes.keySet())).get(i);
			DiseaseGene diseaseGene = new DiseaseGene(value);
			diseaseGene.setGene(key);
			diseaseGene.setId(i);
			this.diseaseGenes[i] = diseaseGene;

		}

		// get the total volume
		double[] volumes = new double[this.diseaseGenes.length];
		for (int i = 0; i < this.diseaseGenes.length; i++) {
			LOG.debug("Disease gene [" + i + "]: " + this.diseaseGenes[i]);
			this.totalDiseaseGeneScore += this.diseaseGenes[i].getScore();
			volumes[i] = this.diseaseGenes[i].getScore();
		}
		LOG.info("The total score of the [" + numberOfGenes + "] genes is ["
				+ this.totalDiseaseGeneScore + "]");
	}

	/**
	 * Evolves the population.
	 */
	private void evolve(Genotype a_genotype) {
		List<Double> fitnessValues = new ArrayList<Double>();
		int optimalNumberOfLorries = (int) Math.ceil(this.totalDiseaseGeneScore
				/ DISEASE_THRESHOLD);
		LOG.info("The optimal number of solutions needed is ["
				+ optimalNumberOfLorries + "]");

		double previousFittest = a_genotype.getFittestChromosome()
				.getFitnessValue();
		int numberOfLorriesNeeded = Integer.MAX_VALUE;
		for (int i = 0; i < NUMBER_OF_EVOLUTIONS; i++) {
			if (i % 150 == 0) {
				LOG.info("Number of evolutions [" + i + "]");
			}
			a_genotype.evolve();

			// retrieves the Chromosome in the Population with the highest
			// fitness value.
			// Retrieves the fitness value of this Chromosome, as determined by
			// the active fitness function.
			double fittness = a_genotype.getFittestChromosome()
					.getFitnessValue();
			fitnessValues.add(fittness);
			int lorriesNeeded = this.numberOfSolution(
					a_genotype.getFittestChromosome().getGenes()).size();
			if (fittness < previousFittest
					&& lorriesNeeded < numberOfLorriesNeeded) {
				this.printSolution(a_genotype.getFittestChromosome());
				previousFittest = fittness;
				numberOfLorriesNeeded = lorriesNeeded;
			}

			// No more optimal solutions
			if (numberOfLorriesNeeded == optimalNumberOfLorries) {
				break;
			}
		}
		IChromosome fittest = a_genotype.getFittestChromosome();

		List<Disease> diseases = numberOfSolution(fittest.getGenes());
		DisplayGenesArrangementInDisease(diseases);
		this.printSolution(fittest);
		printFitnessValues(fitnessValues);
	}

	private void printFitnessValues(List<Double> fitnessValues) {
		int i = 1;
		for (Double fitnessValue : fitnessValues) {
			System.out.println(i + " evaluation " + " fitness value is "
					+ fitnessValue);
			i++;
		}
		this.fitnessValues = fitnessValues;

	}

	private void printSolution(IChromosome fittest) {
		// The optimal genes of the most optimal population
		Gene[] genes = fittest.getGenes();
		List<Disease> diseases = numberOfSolution(genes);
		fitnessValue = String.valueOf(fittest.getFitnessValue());
		diseaseScore = String.valueOf(diseases.size());
		System.out.println("Fitness value [" + fittest.getFitnessValue() + "]");
		System.out.println("The total number of solutions needed is ["
				+ diseases.size() + "]");
	}

	private void DisplayGenesArrangementInDisease(List<Disease> solutions) {
		scoreInDisease = new HashMap<String, List<String>>();
		List<String> listOfGenes = new ArrayList<String>();
		String totalVolume;
		String geneScore;
		int index = 1;
		for (Disease disease : solutions) {
			totalVolume = "Solution [" + index
					+ "] has genes with a total score of ["
					+ disease.getDiseaseGenesScore()
					+ "] and contains the following gene:";
			System.out.println(totalVolume);

			List<DiseaseGene> diseaseGenes = disease.getContents();
			for (DiseaseGene diseaseGene : diseaseGenes) {
				geneScore = "    " + diseaseGene;
				System.out.println(geneScore);
				listOfGenes.add(geneScore);
			}
			scoreInDisease.put(totalVolume, listOfGenes);
			System.out.println("-----------------------------");
			System.out.println();
			index++;
		}
	}

	private List<Disease> numberOfSolution(Gene[] genes) {
		List<Disease> diseases = new ArrayList();
		Disease disease = new Disease(DISEASE_THRESHOLD);
		for (Gene gene : genes) {
			// Retrieves the value represented by this Gene.
			int index = (Integer) gene.getAllele();

			if (!disease.addDiseaseGene((this.diseaseGenes[index]))) {
				// A new lorry is needed
				diseases.add(disease);
				disease = new Disease(DISEASE_THRESHOLD);
				disease.addDiseaseGene(this.diseaseGenes[index]);
			}
		}
		return diseases;
	}

	public static void main(String[] args) throws Exception {
		int seed = 37;
		new DiseaseGenesIdentifier(seed);
	}

	public List<Double> getFitnessValues() {
		return this.fitnessValues;
	}

	public String getFitnessValueToPrint() {
		return fitnessValue;
	}

	public String getDiseaseScoreToPrint() {
		return diseaseScore;
	}

	public Map<String, List<String>> getScore() {
		return scoreInDisease;

	}

}
