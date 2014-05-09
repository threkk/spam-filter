package classify;

import java.io.Serializable;
import java.util.Set;

import tools.KNN;
import training.Training;
import entities.VectEmail;

public class Classifier implements Serializable {
	
	private static final long serialVersionUID = -8032972167224755599L;
	private Set<VectEmail> corpus;
	private VectSpaceMin vector;
	private KNN knn;
	
	/*
	 * Esta clase pretende abstraer del entrenamiento la clasificacion de los documentos, y
	 * para ello, crea un objeto especificamente para ello, mejor adaptado a ser almacenado 
	 * en archivo y mas optimo en cuanto a rendimiento. 
	 */
	public Classifier (Training training){
		this.corpus = training.getCorpus();
		this.vector = new VectSpaceMin(training.getVector());
		this.knn = new KNN(3,corpus);
	}
	
	public Boolean isSpam(VectEmail email){
		return knn.isSpam(email);
	}

	public Set<VectEmail> getCorpus() {
		return corpus;
	}

	public VectSpaceMin getVector() {
		return vector;
	}
	
	
}
