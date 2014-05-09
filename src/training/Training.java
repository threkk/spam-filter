package training;

import java.util.List;
import java.util.Set;

import tools.KNN;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import entities.*;

public class Training {
	private VectSpace vector;
	private List<VectEmail> emails;
	private Set<VectEmail> corpus;
	
	/*
	 * Esta clase modela el entrenamiento del algoritmo. Parte de un conjunto de emails
	 * procesados, el vector generado por ese conjunto, y genera un subconjunto de vectores
	 * asociados a emails conocido como corpus, el cual es el conjunto de emails a partir del
	 * cual se clasificaran el resto.
	 */
	public Training (VectSpace vector){
		this.vector = vector;
		this.emails = Lists.newArrayList();
		this.corpus = Sets.newHashSet();
	}
	
	/*
	 * Se inicializa el corpus con hasta 1000 emails procedentes del conjunto de entrenamiento.
	 * Se escoge mitad y mitad aleatoriamente del conjunto de ham y de spam, tal que haya una 
	 * cantidad relevante de ambos como para encontrar diversidad de casos.
	 * 
	 * La cifra elegida se debe a, tras varias pruebas, comprobar que los resultados a partir
	 * de 1000 emails son aproximadamente los mismos, y por tanto, no supone mucha diferencia
	 * en cuanto al proceso de clasificacion el aumento del tamaño del corpus, aunque si una
	 * traba al rendimiento.
	 */
	public void init(){
		Integer caseBase = Math.min(1000,2*Math.round(this.vector.getDocuments().size()*0.1f));
		
		for(int i = 0; i < caseBase/2; i++){
			
			ProcEmail randomEmail = this.generateRandomEmail();
			// Nos aseguramos de que el email generado es de ham, y viceversa posteriormente
			while(randomEmail.isSpam() != true){
				randomEmail = this.generateRandomEmail();
			}
			// Generamos el vector del email y lo añadimos al corpus.
			VectEmail vectEmail= new VectEmail(randomEmail, vector);
			corpus.add(vectEmail);
		}
		
		for(int i = 0; i < caseBase/2; i++){
			
			ProcEmail randomEmail = this.generateRandomEmail();
			while(randomEmail.isSpam() == true){
				randomEmail = this.generateRandomEmail();
			}
			
			VectEmail vectEmail= new VectEmail(randomEmail, vector);
			corpus.add(vectEmail);
		}
	}
	
	/*
	 * Una vez tenemos el corpus incial, toca entrenarlo. El procedimiento es simple:
	 * partiendo del conjunto inicial, comenzamos a clasificar de todos los emails del conjunto,
	 * excluyendo los que ya se encuentran en el conjunto. Si la clasificacion es incorrecta,
	 * lo añadimos al corpus, puesto que su informacion es relevante.
	 */
	public void train(){
		KNN knn = new KNN(3, corpus);
		List<VectEmail> wrongMails = Lists.newArrayList();
		
		for(VectEmail email : emails){
			if(!corpus.contains(email)){
				if(knn.isSpam(email) != email.isSpam()){
					if((wrongMails.size()+corpus.size()) < (emails.size()*0.25)){
						wrongMails.add(email);
					} else {
						break;
					}
				} else {
					email = null;
				}
			}
		}
		
		for(VectEmail email : wrongMails){
			corpus.add(email);
		}
	}
	
	/*
	 * El metodo crea un clasificador knn a partir del corpus y el k seleccionado,
	 * permitiendo clasificar los emails.
	 */
	public Boolean isSpam(VectEmail email){
		KNN knn = new KNN(3,corpus);
		return knn.isSpam(email);
	}
	
	// Utilidades
	
	/*
	 * Generador de emails aleatorios.
	 */
	private ProcEmail generateRandomEmail(){
		Integer random = Math.round(Math.round(Math.random()*this.vector.getDocuments().size()));
		if(random < 0) random = 0;
		if(random > this.vector.getDocuments().size()-1) random = this.vector.getDocuments().size()-1;
		return this.vector.getDocuments().get(random);
	}

	// Getters
	public VectSpace getVector() {
		return vector;
	}

	public List<VectEmail> getEmails() {
		return emails;
	}

	public Set<VectEmail> getCorpus() {
		return corpus;
	}

}
