package entities;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ProcEmail {
	private Boolean spam;
	private Map<String,Integer> words;
	
	/*
	 * Esta clase transforma un email en un email preprocesado, del cual se han
	 * obtenido ya sus estadisticas para el calculo posterior del espacio de vectores.
	 * El contenido original del email pasa valores estadisticos: las palabras
	 * son irrelevantes más allá de sus valores asociados.
	 */
	public ProcEmail (Email e){
		this.spam = e.getSpam();
		this.words = this.countWords(e);
	}
	
	public Boolean isSpam(){
		return spam;
	}
	
	public Map<String,Integer> getWords(){
		return words;
	}

	// Funciones auxiliares.
	private Map<String, Integer> countWords (Email e){
		// Genera un conjunto con todas las palabras unicas. Esto se hace por defecto
		// al emplear un Set, dado que este no contiene repeticiones.
		Set<String> uniqueWords = Sets.newHashSet(e.getMsg());
		Function<String,Integer> count = new Count(e);
		// Usando la clase descrita mas abajo, devuelve el conjunto de palabras unicas
		// del email junto y el numero de apariciones que posee cada palabra asociado.
		Map<String,Integer> map = Maps.asMap(uniqueWords, count);
		
		return map;
		
	}

	/*
	 * Esta clase cuenta todas las ocurrencias de una palabra dentro de un email.
	 * Permite generalizar todo el proceso, pasando como parametro simplemente
	 * la palabra y devolviendo el numero de apariciones.
	 */
	private class Count implements Function<String,Integer>{
		
		private Email email;
		
		public Count (Email e){
			this.email = e;
		}
		
		public Integer apply(String s){
			return Iterables.frequency(email.getMsg(), s);
		}
	}

}
