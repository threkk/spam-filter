package training;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import entities.ProcEmail;

public class VectSpace {
	private List<ProcEmail> documents;
	private Set<String> vocabulary;
	private Map<String,Double> idf;
	
	/*
	 * El espacio de vectores se genera a partir de un conjunto de documentos, en este caso, 
	 * emails, y la informacion estadistica de las palabras asociadas a cada uno uno de ellos.
	 * El valor de cada palabra dentro del espacio de vectores se calcula a partir de contar
	 * el numero de apariciones totales y obtener el logaritmo del total de documentos entre
	 * ese numero.
	 */
	public VectSpace (){
		this.documents = Lists.newArrayList();
		this.vocabulary = Sets.newHashSet();
		this.idf = Maps.newHashMap();
	}

	/*
	 * Inicializamos el conjunto de vectores con todas las palabras de los emails.
	 * Al añadirlas todas a un conjunto, este por diseño se queda solamente con
	 * las palabras unicas al no admitir repeticiones en su diseño.
	 */
	public void init(List<ProcEmail> emails){
		this.documents.addAll(emails);
		
		for(ProcEmail email : emails){
			vocabulary.addAll(email.getWords().keySet());
		}
		
	}
	
	/*
	 * Esta funcion actualiza el espacio de vectores, realizando el calculo para cada palabra
	 * de su idf asociado. Primero cuenta el numero de ocurrencias de cada palabra, para despues
	 * realizar los calculos y añadirlos al mapa.
	 */
	public void update(){
		
		Double value = 0.0;
		for(String word : vocabulary){
			Integer counter = 0;
			for(ProcEmail email : documents){
				if(email.getWords().keySet().contains(word)){
					counter++;
				}
			}
			value = Math.log((this.getN()/counter));
			idf.put(word, value);
		}
	}
	
	/*
	 * Valor N asociado al espacio de vectores. Es el numero de documentos que se han empleado
	 * para el calculo del espacio de vectores. En caso de que nuestro espacio de vectores se 
	 * actualizara en tiempo de ejecucion, este valor se iria incrementando con la inclusion
	 * de nuevos emails.
	 */
	public Integer getN(){
		return documents.size();
	}
	
	/*
	 * Una de las funcionalidades no implementadas por los motivos explicados en la memoria es
	 * la actualizacion en tiempo real del espacio de vectores asociados al clasificador. El 
	 * principal motivo es por recursos en un entorno de desarrollo junto con la necesidad de la
	 * interaccion del usuario, pero su implementacion es trivial para el sistema desarrollado.
	 */
	public void addEmail(ProcEmail email){
		// Añade el nuevo email recibido al conjunto de emails, sus palabras al vocabulario
		// y actualiza todo el conjunto.
		documents.add(email);
		vocabulary.addAll(email.getWords().keySet());
		this.update();
	}
	
	// Getters
	public List<ProcEmail> getDocuments() {
		return documents;
	}

	public Set<String> getVocabulary() {
		return vocabulary;
	}

	public Map<String, Double> getIdf() {
		return idf;
	}
}
