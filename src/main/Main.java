package main;

import java.util.List;
import java.util.Scanner;

import classify.Classifier;
import classify.Evaluation;

import tools.Utils;
import training.Training;
import training.VectSpace;
import entities.Email;
import entities.ProcEmail;
import entities.VectEmail;

public class Main {
	
	static Integer options = 0;
	private static Scanner reader;
	
	public static void main (String args[]){
		
			
		System.out.println("D e t e c c i o n   d e   C o r r e o   B a s u r a");
		System.out.println("---------------------------------------------------");
		System.out.println("");
		System.out.println("Por Alberto Martinez de Murga Ramirez");
		System.out.println("");
		System.out.println("Clasificador de ham-spam basado en tecnicas de inteligencia artificial.");
		System.out.println("Abstraccion basada en espacio de vectores y clasificacion basada en el algoritmo KNN.");
		System.out.println("");
		System.out.println("Por favor, antes de usar el programa tenga en consideracion las siguientes advertencias:");
		System.out.println("    - Siga las instrucciones que aparecen en pantalla.");
		System.out.println("    - Incluya la ruta completa a los archivos, prestando atencion a la extension.");
		System.out.println("    - Use en todo momento tanto para clasificacion como para evaluacion archivos del conjunto Enron.");
		System.out.println("    - Evite emplear espacios en las rutas.");
		System.out.println("");
		System.out.println("");
		
	
		String[] rutas = chooseOptions();

		switch (options){
		case 11 : trainAndEvaluate(rutas[0],rutas[1]);
			break;
		case 12 : trainAndClassify(rutas[0], rutas[1]);
			break;
		case 13 : trainAndSave(rutas[0], rutas[1]);
			break;
		case 21 : loadAndClassify(rutas[0], rutas[1]);
			break;
		case 22 : loadAndEvalute(rutas[0], rutas[1]);
			break;
		}
			
	}
	
	private static String[] chooseOptions(){
		String[] rutas = new String[2];
		reader = new Scanner(System.in);
		String train = ""; 
		String test = "";
		Integer input;

		try {
			System.out.println("Seleccione una opcion:");
			System.out.println("    1. Seleccionar una carpeta para realizar un entrenamiento.");
			System.out.println("    2. Seleccionar un entrenamiento previamente realizado.");
			
			input = reader.nextInt();
		
			switch(input){
				case 1 : System.out.println("Introduzca la ruta a la carpeta.");
						train = reader.next();
						options = 10;
						break;
				case 2 : System.out.println("Introduzca la ruta al archivo .clf con el entrenamiento.");
						train = reader.next();
						options = 20;
						break;
				default : System.out.println("Entrada incorrecta, por favor, seleccione una opcion de la lista.");
						chooseOptions();
			}
			
			rutas[0] = train;
			
			System.out.println("Seleccione una opcion:");
			System.out.println("    1. Clasificar un correo.");
			System.out.println("    2. Evaluar un conjunto de correos.");
			if(options != 20){
				System.out.println("    3. Almacenar el entrenamiento.");
			}
			input = reader.nextInt();
			
			switch(input){
				case 1 : System.out.println("Introduzca la ruta al archivo .txt que contiene el correo.");
						test = reader.next();
						options += 1;
						break;
				case 2 : System.out.println("Introduzca la ruta a la carpeta con los correos a evaluar.");
						test = reader.next();
						options += 2;
						break;
				case 3 : System.out.println("Introduzca la ruta y nombre con extension .clf donde almacenar el entrenamiento");
						test = reader.next();
						options += 3;
						break;
				default : System.out.println("Entrada incorrecta, por favor, seleccione una opcion de la lista.");
			}
			rutas[1] = test;
			
		} catch (Exception oops) {
			System.out.println("Entrada incorrecta, por favor, seleccione una opcion de la lista.");
			chooseOptions();
		}
		return rutas;
	}
	
	private static void trainAndEvaluate(String pathToTrain, String pathToTest){
		List<ProcEmail> emailsTrain;
		Long inicio, fin;
		
		try {			
			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+pathToTrain);
			emailsTrain = Utils.loadAndProcessFolder(pathToTrain);
			System.out.println("Procesados "+emailsTrain.size()+" emails.");

			VectSpace v = new VectSpace();
			v.init(emailsTrain);
			System.out.println("Espacio de vectores iniciado.");
			System.out.println("Comenzando el calculo del espacio de vectores.");
			v.update();
			System.out.println("Espacio de vectores actualizado.");
			
			Training training = new Training(v);
			System.out.println("Generando el conjunto de entrenamiento.");
			training.init();
			System.out.println("Comenzando el entrenamiento.");
			training.train();
			System.out.println("Terminado el entrenamiento.");
			
			System.out.println("Generando clasificador a partir del entrenamiento.");
			Classifier classifier = new Classifier(training);
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en el entrenamiento: " + ((fin.doubleValue()-inicio.doubleValue())/60000)+" minutos.");

			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+ pathToTest);
			List<ProcEmail> emailsTest = Utils.loadAndProcessFolder(pathToTest);
			System.out.println("Procesados "+emailsTest.size()+" emails.");
		
			System.out.println("Comenzando evaluación.");
			Evaluation e = new Evaluation(classifier, emailsTest);
			e.evaluate();
			System.out.println("Evaluación finalizada.");
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en la evaluacion: " + ((fin.doubleValue()-inicio.doubleValue())/60000)+" minutos.");
			System.out.println(e.getStats());

		} catch(Exception oops){
			System.out.println("Ha habido algun problema durante la ejecucion, por favor, vuelve a intentarlo.");
		}
	}
	
	private static void trainAndClassify(String pathToTrain, String pathToTest){
		List<ProcEmail> emailsTrain;
		Long inicio, fin;
		
		try {			
			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+pathToTrain);
			emailsTrain = Utils.loadAndProcessFolder(pathToTrain);
			System.out.println("Procesados "+emailsTrain.size()+" emails.");

			VectSpace v = new VectSpace();
			v.init(emailsTrain);
			System.out.println("Espacio de vectores iniciado.");
			System.out.println("Comenzando el calculo del espacio de vectores.");
			v.update();
			System.out.println("Espacio de vectores actualizado.");
			
			Training training = new Training(v);
			System.out.println("Generando el conjunto de entrenamiento.");
			training.init();
			System.out.println("Comenzando el entrenamiento.");
			training.train();
			System.out.println("Terminado el entrenamiento.");
			
			System.out.println("Generando clasificador a partir del entrenamiento.");
			Classifier classifier = new Classifier(training);
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en el entrenamiento: " + ((fin.doubleValue()-inicio.doubleValue())/60000)+" minutos.");

			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+ pathToTest);
			Email email = new Email(pathToTest);
			ProcEmail procEmail = new ProcEmail(email);
			VectEmail vectEmail = new VectEmail(procEmail, classifier.getVector());
			if(classifier.isSpam(vectEmail)){
				System.out.println("El email es spam.");
			} else {
				System.out.println("El email es ham.");
			}
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en la evaluacion: " + ((fin.doubleValue()-inicio.doubleValue())/1000)+" segundos.");

		} catch(Exception oops){
			System.out.println("Ha habido algun problema durante la ejecucion, por favor, vuelve a intentarlo.");
		}
	}
	
	private static void trainAndSave(String pathToTrain, String pathToSave){
		List<ProcEmail> emailsTrain;
		Long inicio, fin;
		
		try {			
			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+pathToTrain);
			emailsTrain = Utils.loadAndProcessFolder(pathToTrain);
			System.out.println("Procesados "+emailsTrain.size()+" emails.");

			VectSpace v = new VectSpace();
			v.init(emailsTrain);
			System.out.println("Espacio de vectores iniciado.");
			System.out.println("Comenzando el calculo del espacio de vectores.");
			v.update();
			System.out.println("Espacio de vectores actualizado.");
			
			Training training = new Training(v);
			System.out.println("Generando el conjunto de entrenamiento.");
			training.init();
			System.out.println("Comenzando el entrenamiento.");
			training.train();
			System.out.println("Terminado el entrenamiento.");
			
			System.out.println("Generando clasificador a partir del entrenamiento.");
			Classifier classifier = new Classifier(training);
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en el entrenamiento: " + ((fin.doubleValue()-inicio.doubleValue())/60000)+" minutos.");

			Utils.save(pathToSave, classifier);
			System.out.println("Archivo guardado en :" +pathToSave);
		} catch(Exception oops){
			System.out.println("Ha habido algun problema durante la ejecucion, por favor, vuelve a intentarlo.");
		}
	}
	
	private static void loadAndEvalute(String pathToFile, String pathToTest){
		Long inicio, fin;
		try {
			System.out.println("Cargando "+pathToFile);
			Classifier classifier = (Classifier)Utils.load(pathToFile);
			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+ pathToTest);
			List<ProcEmail> emailsTest = Utils.loadAndProcessFolder(pathToTest);
			System.out.println("Procesados "+emailsTest.size()+" emails.");
		
			System.out.println("Comenzando evaluación.");
			Evaluation e = new Evaluation(classifier, emailsTest);
			e.evaluate();
			System.out.println("Evaluación finalizada.");
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en la evaluacion: " + ((fin.doubleValue()-inicio.doubleValue())/60000)+" minutos.");
			System.out.println(e.getStats());
		} catch (Exception e){
			System.out.println("Ha habido algun problema durante la ejecucion, por favor, vuelve a intentarlo.");

		}
	}
	
	private static void loadAndClassify(String pathToFile, String pathToTest){
		Long inicio, fin;
		try {
			System.out.println("Cargando "+pathToFile);
			Classifier classifier = (Classifier)Utils.load(pathToFile);
			inicio = System.currentTimeMillis();

			inicio = System.currentTimeMillis();
			System.out.println("Cargando "+ pathToTest);
			Email email = new Email(pathToTest);
			ProcEmail procEmail = new ProcEmail(email);
			VectEmail vectEmail = new VectEmail(procEmail, classifier.getVector());
			if(classifier.isSpam(vectEmail)){
				System.out.println("El email es spam.");
			} else {
				System.out.println("El email es ham.");
			}
			fin = System.currentTimeMillis();
			System.out.println("Tiempo empleado en la evaluacion: " + ((fin.doubleValue()-inicio.doubleValue())/1000)+" segundos.");

		} catch (Exception e){
			System.out.println("Ha habido algun problema durante la ejecucion, por favor, vuelve a intentarlo.");
		}
	}
}
