package it.correzioneFormatoTest.function;

import java.io.File;
import java.util.LinkedList;
import java.util.Objects;

public class FunzioniUtili {
	
	
	public FunzioniUtili() {
		super();
	}
	
	
	public LinkedList<File> findAllFilesInFolder(File folder, String estensioneFile) {
		LinkedList<File> listaFile = new LinkedList<File>();
		
		for (File file : Objects.requireNonNull(folder.listFiles())) {
			if (!file.isDirectory()) {
				System.out.println(file.getName());
				if(file.getName().endsWith("."+estensioneFile)) {
				listaFile.add(file);
				}
			} else {
				findAllFilesInFolder(file,estensioneFile);
			}
		}
		return listaFile;
	}
	
	
	
	public int cercaSottoStringa(String stringa, String sottostringa){
		
	    int indiceMatching = 0; //indice al quale inizia la sottostringa cercata

	    // al momento non è stata effettuata alcuna ricerca
	    // ne conseguito alcun risultato 		
	    Boolean cerca = false;

	    // calcoliamo lunghezza di stringa e sottostringa
	    // la differenza sarà condizione di terminazione per il ciclo for
	    int max = (stringa.length() - sottostringa.length());

	    // ricerchiamo la sottostringa ciclando il contenuto di quest'ultima
	    test:
	    for (int i = 0; i <= max; i++) {
	      int n = sottostringa.length();
	      int j = i;
	      int k = 0;
	      while (n-- != 0) {
	        if (stringa.charAt(j++) != sottostringa.charAt(k++)) {
	          continue test;
	        }
	      }

	      // a questo punto è stata effettuata una ricerca
	      // sarà possibile produrre un output			
	      cerca = true;
	      indiceMatching = i; //indice in cui avviene il matching
	      break test;
	    }

	    // stampiamo l'output sulla base dell'esito della ricerca		
	    System.out.println(cerca ? "Tovata" : "Non Trovata");
		
	    if(cerca==true) {
	    	
	    	System.out.println("La sottostringa cercata inizia all'indice: "+indiceMatching);
	    	
	    }else {
	    	indiceMatching = -1;
	    }
	    
	    
	    return indiceMatching;
		
		}
	
	
	
	
	

}
