package entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import training.VectSpace;

import classify.VectSpaceMin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class VectEmail implements Serializable{
	
	private static final long serialVersionUID = -1119469429986211018L;
	private Boolean spam;
	private Map<String, Double> w;
	private Double w2;
	
	/*
	 * Esta clase modela la informacion relativa a un email tras haber sido procesada
	 * por un espacio de vectores. A este nivel de abstraccion las palabras y el contenido
	 * original en si del email es irrelevante, solo nos interesan los valores w y w^2 asociados
	 * a cada palabra, junto con su clasificacion original.
	 * 
	 * Implementa la interfaz Serializable para poder ser almacenado en forma de objeto binario.
	 */
	
	public VectEmail (ProcEmail email, VectSpace vector){
		this.spam = email.isSpam();
		this.w = Maps.newTreeMap();
		
		// El calculo de los valores de un email se obtiene cruzando la informacion estadistica
		// de un email con los datos obtenidos del espacio vectorial al que esta asociado.
		Double value = 0.0;
		for(String key : email.getWords().keySet()){
			if(!vector.getVocabulary().contains(key)){
				// Si la palabra no aparece en el vocabulario del espacio vectorial, quiere decir
				// que ni pertenece ni ha sido empleada en el entrenamiento, luego es una palabra
				// que aparece durante la evaluacion de un mensaje. Al no tener aparicion previa,
				// ni ser actualizado el tiempo real nuestro conjunto, su valor de idf sera 1.
				value = Math.log((vector.getN()/1));	
			} else {
				// Para obtener el valor w de cada palabra, se multiplica su 
				// numero de aparaciones por el idf asociado a la palabra.
				value = email.getWords().get(key)*vector.getIdf().get(key);
			}
			w.put(key, value);
		}
		
		this.w2 = this.calculateW2();
	}
	
	/*
	 * Este constructor es el mismo que el anterior, solo que en lugar de emplear un espacio
	 * vectorial usual emplea uno minimizado, al cual se le ha extraido la informacion necesaria
	 * para el entrenamiento dejando solo la necesaria para la evaluacion de los emails, haciendo
	 * que sea más optimo tanto para su uso como para su almacenamiento.
	 */
	public VectEmail (ProcEmail email, VectSpaceMin vector){
		this.w = Maps.newTreeMap();
		this.spam = email.isSpam();
		
		Double value = 0.0;
		for(String key : email.getWords().keySet()){
			if(!vector.getVocabulary().contains(key)){
				value = Math.log((vector.getN()/1));	
			} else {
				value = email.getWords().get(key)*vector.getIdf().get(key);
			}
			w.put(key, value);
		}
		
		this.w2 = this.calculateW2(); 
	}
	
	// Funciones auxiliares
	
	/*
	 * Esta funcion devuelve la distancia a un email respecto al actual.
	 * Realiza la interserccion de las palabras comunes, puesto que el resultado de la
	 * multiplicacion de las no comunes siempre es 0, y realiza un sumatorio de productos
	 * entre ambos para despues realizar la raiz cuadrada. De este modo, de dos vectores 
	 * asociados a dos email se obtiene un unico valor numerico escalar.
	 */
	public Double getDistance(VectEmail v){
		Double distance = 0.0;
		Set<String> commonWords = Sets.intersection(this.getW().keySet(), v.getW().keySet());
		
		for(String key : commonWords){
			distance += (this.getW().get(key) * v.getW().get(key));
		}
		
		distance = distance/(this.getW2()*v.getW2());
		
		return distance;
	}
	
	/*
	 * Valor precalculado de w^2 asociado a cada email. Es un valor derivado de los w
	 * de cada email, luego puede ser precalculado y almacenado para ahorrar recursos.
	 */
	private Double calculateW2(){
		Double total = 0.0;
		
		for(Double value : w.values()){
			total += Math.pow(value, 2.0);
		}
		
		total = Math.sqrt(total);
		return total;
	}

	// Getters
	public Boolean isSpam(){
		return spam;
	}
	
	public Map<String, Double> getW() {
		return w;
	}
	
	public Double getW2(){
		return w2;
	}	
	
}
