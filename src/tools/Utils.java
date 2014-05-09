package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.google.common.collect.Lists;

import entities.Email;
import entities.ProcEmail;


public class Utils {
	
	/*
	 * Esta utilidad permite guardar archivos en formato binario, mucho mas ligeros
	 * y faciles de procesar. Permite almacenar los entrenamientos existentes.
	 */
	public static<T> void save(String path, T object) throws IOException{
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
	}
	
	/*
	 * Esta utilidad permite cargar archivos en formato binario de clasificadores existentes.
	 * Aunque es una funcion cuyo diseño es generico, su uso es exclusivo para archivos .clf
	 * propios de una clasificacion. 
	 */
	@SuppressWarnings("unchecked")
	public static<T> T load(String path) throws IOException, ClassNotFoundException{
		T object;
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		object = (T)ois.readObject();
		ois.close();
		return object;
	}

	/*
	 * Funcion empleada para cargar una carpeta que contiene los emails en bruto para el
	 * entrenamiento. Actua de manera recursiva, permitiendo cargarlos facilente.
	 */
	public static List<File> loadFolder (File folder){
		List<File> files = Lists.newLinkedList();
		
		File[] listOfFiles = folder.listFiles();
		
		for(int i = 0; i < listOfFiles.length; i++){
			if(listOfFiles[i].isDirectory()){
				List<File> subfiles = Utils.loadFolder(listOfFiles[i]);
				files.addAll(subfiles);
			} else {
				if(listOfFiles[i].getName().endsWith(".txt")) files.add(listOfFiles[i]);
			}
		}
		return files;
	}
	
	/*
	 * Funcion empleada para cargar y procesar emails en bruto para su evaluacion.
	 * Su funcionamiento es recursivo, permitiendo cargar grandes cantidades de 
	 * archivos facilmente.
	 */
	public static List<ProcEmail> loadAndProcessFolder (String path){
		File folder = new File(path);
		List<File> files = Utils.loadFolder(folder);
		List<Email> emails = Lists.newLinkedList();
		List<ProcEmail> processedEmails = Lists.newLinkedList();	

		for(File file : files){
			emails.add(new Email(file.toString()));
		}
		
		for(Email email : emails){
			processedEmails.add(new ProcEmail(email));
		}
		
		return processedEmails;
	}
}
