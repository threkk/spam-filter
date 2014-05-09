package entities;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class Email {
	private String uri;
	private Boolean spam;
	private List<String> msg;
	
	/*
	 * Esta clase pretende modelar cada uno de los email preprocesados de
	 * Enron, ya formen parte del conjunto de entrenamiento o del evaluacion.
	 * El objetivo es a partir de su ruta simplemente obtener el contenido del
	 * fichero en forma de objeto junto con otra informacion relevante.
	 */
		
	public Email(String path){
		this.uri = URI.create(path).toString();
		this.spam = Email.isSpam(path);
		this.msg = Lists.newArrayList();
		
		// Supondremos que en todo momento la ruta a cada archivo es correcta y conduce a 
		// un fichero de texto de Enron.
		try {
			List<String> lines = this.loadMail(path);
			Pattern separators = Pattern.compile("[\\s,;:-_.]");
			
			// En este bucle se recorre todo el email, separando por palabras, poniendolas 
			// en minuscula y eliminando aquellas irrelevantes.
			for(String l:lines){
				List<String> separated = Lists.newArrayList(Splitter.on(separators).omitEmptyStrings().trimResults().split(l.toLowerCase()));
				List<String> filtered = Lists.newArrayList(Iterables.filter(separated, new StopWordsFilter()));
				msg.addAll(filtered);
			}	
			
		} catch (IOException oops) {
			System.err.println("Error de entrada al cargar el archivo: "+path);
			System.err.println("-Traza: "+oops.getStackTrace());
		}
	
	}

	// Utilidades
	public static Boolean isSpam(String path){
		Boolean res = true;
		// Un poco chapuza, pero efectivo: todos los mensajes de ham contienen la palabra en su nombre.
		// A partir de este del nombre del fichero por tanto tendremos la clasificacion real del mensaje.
		if(path.matches(".*ham.*")) res = false;
		return res;
	}
	
	private List<String> loadMail(String path) throws IOException{
		// Empleamos la libreria Guava para facilitar el proceso.
		File email = new File(path);
		return Files.readLines(email, Charsets.UTF_8);
	}

	// Getters y otras metodos puramente auxiliares.
	public String getUri() {
		return uri;
	}

	public Boolean getSpam() {
		return spam;
	}

	public List<String> getMsg(){
		return msg;
	}
	
	/*
	 * Esta clase auxiliar se encarga de generar un filtro para las stop words.
	 * Una vez generado el filtro, este se aplica a cada email, eliminando del mismo
	 * todas las palabras irrelevantes para su clasificacion.
	 */
	
	private class StopWordsFilter implements Predicate<String>{
		
		// Fuente http://norm.al/2009/04/14/list-of-english-stop-words/
		private String[] stopwords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"};
		// Otras stop word propias de los emails.
		private	String [] aditionalStopWords = {"from","to","cc","cco","re","subject","forwarded"};
		// Los signos de puntuacion tampoco son relevantes.
		private String [] symbols = {".",",",":",";","-","_","/","\\","?","!","|","@","\"","#","+","'","(",")","{","}","[","]","*"};
		
		Set<String> stops = Sets.newHashSet();
		
		public StopWordsFilter(){
			stops.addAll(Arrays.asList(stopwords));
			stops.addAll(Arrays.asList(aditionalStopWords));
			stops.addAll(Arrays.asList(symbols));
		}
		
		public boolean apply(String arg0) {
			return !(stops.contains(arg0));
		}
	
	}
	
}
