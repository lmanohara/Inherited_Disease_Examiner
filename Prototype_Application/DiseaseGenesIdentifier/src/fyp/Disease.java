package fyp;

import java.util.List;
import java.util.ArrayList;


public class Disease{
    private double diseaseThreshold;
    private List<DiseaseGene> diseaseGenes = new ArrayList();
    private double diseaseGenesScore = 0.0D;


    public Disease(double diseaseThreshold) {
        this.diseaseThreshold = diseaseThreshold;
    }


    public boolean addDiseaseGene(DiseaseGene diseaseGene) {
        if (this.diseaseGenesScore + diseaseGene.getScore() > this.diseaseThreshold) {
            return false;
        }

        this.diseaseGenes.add(diseaseGene);
        this.diseaseGenesScore += diseaseGene.getScore(); 
        return true;
    }

    public double getDiseaseGenesScore() {
        return diseaseGenesScore;
    }

  
    public List<DiseaseGene> getContents() {
        return diseaseGenes;
    }
}
