package tools;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.collect.Sets;

import entities.VectEmail;


public class KNN implements Serializable {

	private static final long serialVersionUID = 895328897459131613L;
	private Integer k;
	private Set<VectEmail> corpus;
	
	/*
	 * Clase que modela el funcionamiento de un algortimo KNN. Recibe como parametro un corpus
	 * de referencia respecto al cual clasificar los emails que recibe, y el k empleado para
	 * su calculo.
	 */
	public KNN (Integer k, Set<VectEmail> corpus){
		this.k = k;
		this.corpus = corpus;
	}
	
	/*
	 * El clasificador en si. Genera primero un comparador basado en la distancia y 
	 * tomando como punto de referencia el email que queremos clasificar. A continuacion,
	 * ordena el conjunto del corpus en funcion de ese orden, y coge los k primeros.
	 * Con esos k emails, realiza un conteo, devolviendo si es spam o no en funcion de
	 * si esta mas proximo de spam o de ham.
	 */
	public Boolean isSpam (VectEmail email){
		Comparator<VectEmail> nearest = new DistanceFrom(email);
		SortedSet<VectEmail> sortedEmails = Sets.newTreeSet(nearest); 
		Integer counter = 0;
		
		sortedEmails.addAll(corpus);
		
		VectEmail[] arraySortedEmails = sortedEmails.toArray(new VectEmail[0]);
		
		for(int i = 0; i < this.k; i++){
			if(arraySortedEmails[i].isSpam()){
				counter--;
			} else{
				counter++;
			}
		}
		
		if(counter<0){
			return true;
		} else {
			return false;
		}
	}


	// Getters
	public Integer getK() {
		return k;
	}



	public void setK(Integer k) {
		this.k = k;
	}	
	
	public Set<VectEmail> getCorpus() {
		return corpus;
	}



	public void setCorpus(Set<VectEmail> corpus) {
		this.corpus = corpus;
	}

	/*
	 * Clase auxiliar. Genera un comparador basado en la distancia. Dados dos emails y uno de
	 * referencia, devuelve establece un orden en funcion de aquel que este mas proximo al de 
	 * referencia.
	 */
	private class DistanceFrom implements Comparator<VectEmail>{
		private VectEmail email;
		
		public DistanceFrom (VectEmail email){
			this.email = email;
		}

		public int compare(VectEmail o1, VectEmail o2) {
			if(email.getDistance(o1) > email.getDistance(o2)){
				return -1;
			} else {
				return 1;
			}		
		}
	}
	
}
