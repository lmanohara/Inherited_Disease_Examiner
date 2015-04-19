package fyp;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 * Fitness function for the DiseaseGeneIdentifier.
 */
public class DiseaseGeneIdentifierFitnessFunction extends FitnessFunction {
	private DiseaseGene[] diseaseGenes;
	private double diseaseThreshold;

	public void setDiseaseThreshold(double diseaseThreshold) {
		this.diseaseThreshold = diseaseThreshold;
	}

	public void setGenes(DiseaseGene[] diseaseGenes) {
		this.diseaseGenes = diseaseGenes;
	}

	/**
	 * Fitness function -  A lower value value means wasted volume is small, which is better.
	 */
	@Override
	protected double evaluate(IChromosome a_subject) {
		double remaningScore = 0.0D;

		double diseaseScore = 0.0D;
		int numberOfNewSolution = 1;//if disease score over the disease threshold, next gene added to a new solution
		for (int i = 0; i < diseaseGenes.length; i++) {
			int index = (Integer) a_subject.getGene(i).getAllele();
			if ((diseaseScore + this.diseaseGenes[index].getScore()) <= diseaseThreshold) {
				diseaseScore += this.diseaseGenes[index].getScore();
			} else {
				// Compute the difference
				numberOfNewSolution++;
				remaningScore += Math.abs(diseaseThreshold - diseaseScore);
				// Make sure we put the box which did not fit in this lorry in the next lorry
				diseaseScore = this.diseaseGenes[index].getScore();
			}
		}
		// Take into account the number of lorries needed. More lorries produce a higher fitness value.
		return remaningScore * numberOfNewSolution;
    }
}
