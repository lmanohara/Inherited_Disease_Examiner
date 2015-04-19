package fyp;

public class DiseaseGene {
	private int id;
	private double score;
	private String gene;

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public DiseaseGene(double score) {
		this.score = score;
	}

	public double getScore() {
		return this.score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "GeneId:" + this.id +", Gene:"+ this.gene +", Score [" + this.getScore()
				+ "] ";
	}
}
