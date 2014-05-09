package classify;

import java.util.List;

import entities.*;

public class Evaluation {

	private Classifier classifier;
	private List<ProcEmail> evaluation;
	private	Double spam, ham, wrongSpam, wrongHam;

	/*
	 * Dado un clasificador y un conjunto de evaluacion, clasifica todo el conjunto
	 * y genera las estadisticas a partir de la clasificacion.
	 */
	public Evaluation (Classifier classifier, List<ProcEmail> emails){
		this.classifier = classifier;
		this.evaluation = emails;
		spam = 0.0;
		ham = 0.0;
		wrongHam = 0.0;
		wrongSpam = 0.0;
	}

	public void evaluate(){
		for(ProcEmail e : evaluation){
			VectEmail email = new VectEmail(e, classifier.getVector());
			if(email.isSpam()){
				spam++;
				if(classifier.isSpam(email) != email.isSpam()){
					wrongSpam++;
				}
			}else{
				ham++;
				if(classifier.isSpam(email) != email.isSpam()){
					wrongHam++;
				}
			}
			email = null;
			if((spam+ham)%100 == 0) System.out.println("Analizados "+(spam+ham)+" emails.");
		}
	}
	
	public String getStats(){
		String s = "";
		s +="\nResultados de la evaluación";
		s +="\n---";
		s +="\nTotal de mensajes evaluados: " +(ham+spam);
		s +="\nTotal de mensajes de spam: " +spam;
		s +="\nTotal de mensajes de ham: " +ham;
		s +="\nMensajes de spam incorrectamente clasificados: " +wrongSpam;
		s +="\nMensajes de ham incorrectamente clasficiados: " +wrongHam;
		s +="\nPorcentajes: ";
		s +="\n---";
		s +="\nHam correcto: "+ ((ham-wrongHam)/ham)*100 +"%";
		s +="\nHam incorrecto: "+ (wrongHam/ham)*100+"%";
		s +="\nSpam correcto "+ ((spam-wrongSpam)/spam)*100+"%";
		s +="\nSpam incorrecto "+ (wrongSpam/spam)*100 +"%";
		return s;
	}
	
}
