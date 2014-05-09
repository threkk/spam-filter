package classify;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import training.VectSpace;


public class VectSpaceMin implements Serializable{
	
	private static final long serialVersionUID = 3616884562739848540L;
	private Set<String> vocabulary;
	private Integer n;
	private Map<String,Double> idf;
	
	/*
	 * Esta clase es simplemente una version minimizada de un espacio de vectores normal.
	 * El objetivo es emplear un espacio de vectores mas ligero por motivos de rendimiento
	 * tanto para almacenarlo como para clasificar nuevos correos post entrenamiento.
	 * Si realizaramos la actualizacion en tiempo real de ejecucion de los correos, habria que
	 * volver a la clase original. Por lo demas, es identica a la original, con el extra de ser
	 * almacenable al implementar la interfaz Serializable.
	 */
	public VectSpaceMin(VectSpace original){
		this.vocabulary = original.getVocabulary();
		this.n = original.getN();
		this.idf = original.getIdf();
	}
	
	public Set<String> getVocabulary() {
		return vocabulary;
	}


	public Integer getN() {
		return n;
	}


	public Map<String, Double> getIdf() {
		return idf;
	}
	
}
