package it.correzioneFormatoTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.correzioneFormatoTest.function.FunzioniUtili;

@SpringBootApplication
public class CorrezioneFormatoTestApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CorrezioneFormatoTestApplication.class, args);
		
		
		File directoryTestJUnit = new File(args[0]);	//in args[0] si mette il path dove si trovano i test JUnit
		FunzioniUtili funzioni = new FunzioniUtili();
		String chromeDriverPath = new String(args[1]);	//in args[1] si mette il path nel quale si trova il chromedriver
		String testsPackage = new String(args[2]);	//in args[2] si mette il nome del package dove si trovano i test JUnit
		//dell'utente che intende utilizzare l'applicativo
		
		LinkedList<File> listaTestJUnit = new LinkedList<File>();
		listaTestJUnit = funzioni.findAllFilesInFolder(directoryTestJUnit,"java");
		
		LinkedList<File> listaTestJUnit_daCorregere = new LinkedList<File>();
		
		final String headerFileCorretti = "//File risulta attualmente aggiornato per webdriver chrome headless!";
		
		
		System.out.println("Vediamo lista file .java presenti in directory di riferimento");
		int iteraz = 1;
		for(File file: listaTestJUnit) {
			System.out.println("File numero "+iteraz+"!");
			System.out.println(file);
			iteraz++;
				}
		
		
		for(File fileTest: listaTestJUnit) {
			BufferedReader reader = new BufferedReader(new FileReader(fileTest));
			
			//Lettura file riga per riga
			String line = reader.readLine();
			int numRiga = 0;
			
			while(line!=null && numRiga==0) {
				 System.out.println("Riga numero: "+numRiga+"!!!");
			     line = reader.readLine();
			     System.out.println("Vediamo la riga numero "+numRiga+":");
			     System.out.println(line);
			     
			     if(line.equals(headerFileCorretti)) {
			    	 //Se il secondo rigo (la prima lettura viene fatta prima del while, ecco perchè secondo rigo) 
			    	 //coincide con l'headerFileCorretti
			    	 //Allora il seguente file di test .java è già stato precendentemente
			    	 //posto nel formato corretto per i test Chrome headless
			    	 System.out.println("Il corrente file non va aggiunto alla lista dei file da corregere");
			     } else {
			    	 System.out.println("Il corrente file va aggiunto alla lista dei file da corregere!");
			    	 listaTestJUnit_daCorregere.add(fileTest);
			    	 System.out.println("File aggiunto!");
			     }
			     numRiga++; //Dato che lo incrementa subito, legge solo il primo rigo ovvero l'header
			}
		}
		//Qui fuori dal for, abbiamo in listaTestJUnit_daCorregere l'insieme dei file che sono ancora da corregere
		
		//Mi salvo i file in una variabile temporanea di appoggio
		LinkedList<File> listaTemp = new LinkedList<File>();
		listaTemp = listaTestJUnit_daCorregere;
		
		
		
		//Dichiarazione parti di codice da Inserire per la correzione dei file di test
		final String header_librerie = "\n"+headerFileCorretti+"\n"
				+ "package "+testsPackage+";"
				+ "\n"
				+ "\n"
				+ "import java.util.concurrent.TimeUnit;\n"
				+ "\n"
				+ "import org.junit.*;\n"
				+ "\n"
				+ "import static org.junit.Assert.*;\n"
				+ "import org.openqa.selenium.*;\n"
				+ "import org.openqa.selenium.chrome.ChromeDriver;\n"
				+ "import org.openqa.selenium.support.ui.Select;\n"
				+ "import org.openqa.selenium.chrome.ChromeOptions;\n";

		
		final String attributi_beforeAll = "private static WebDriver driver;\n"
				+ "private boolean acceptNextAlert = true;\n"
				+ "private static StringBuffer verificationErrors = new StringBuffer();\n"
				+ "\n"
				+ "	  @Before\n"
				+ "	  public void setUp() throws Exception {\n"
				+ "		\n"
				+ "		  // Init chromedriver\n"
				+ "		  String chromeDriverPath = \""+chromeDriverPath+"\";\n"
				+ "		  System.setProperty(\"webdriver.chrome.driver\", chromeDriverPath);\n"
				+ "		  System.setProperty(\"webdriver.chrome.whitelistedIps\", \"\");\n"
				+ "		  ChromeOptions options = new ChromeOptions();\n"
				+ "		  options.addArguments(\"--headless\", \"--disable-gpu\", \"--window-size=1920,1200\",\"--no-sandbox\",\"--ignore-certificate-errors\");\n"
				+ "		  driver = new ChromeDriver(options);  \n"
				+ "		  \n"
				+ "		  \n"
				+ "		  \n"
				+ "	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);\n"
				+ "	  }";
		
		
		final String afterAll_finoAllaFine = " @After\n"
				+ "	  public void tearDown() throws Exception {\n"
				+ "	    driver.quit();\n"
				+ "	    String verificationErrorString = verificationErrors.toString();\n"
				+ "	    if (!\"\".equals(verificationErrorString)) {\n"
				+ "	      fail(verificationErrorString);\n"
				+ "	    }\n"
				+ "	  }\n"
				+ "\n"
				+ "	  private boolean isElementPresent(By by) {\n"
				+ "	    try {\n"
				+ "	      driver.findElement(by);\n"
				+ "	      return true;\n"
				+ "	    } catch (NoSuchElementException e) {\n"
				+ "	      return false;\n"
				+ "	    }\n"
				+ "	  }\n"
				+ "\n"
				+ "	  private boolean isAlertPresent() {\n"
				+ "	    try {\n"
				+ "	      driver.switchTo().alert();\n"
				+ "	      return true;\n"
				+ "	    } catch (NoAlertPresentException e) {\n"
				+ "	      return false;\n"
				+ "	    }\n"
				+ "	  }\n"
				+ "\n"
				+ "	  private String closeAlertAndGetItsText() {\n"
				+ "	    try {\n"
				+ "	      Alert alert = driver.switchTo().alert();\n"
				+ "	      String alertText = alert.getText();\n"
				+ "	      if (acceptNextAlert) {\n"
				+ "	        alert.accept();\n"
				+ "	      } else {\n"
				+ "	        alert.dismiss();\n"
				+ "	      }\n"
				+ "	      return alertText;\n"
				+ "	    } finally {\n"
				+ "	      acceptNextAlert = true;\n"
				+ "	    }\n"
				+ "	  }\n"
				+ "\n"
				+ "}\n"
				+ "";
		
		
		for(File file: listaTestJUnit_daCorregere) {
			//Per ogni file mi estraggo il nome della classe come prima cosa
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			//Lettura file riga per riga
			String line = reader.readLine();
			Boolean nomeClasseEstratto = false;
			String nomeClasse = new String();
			String codiceTestsCases = new String();
			
			while(line!=null && nomeClasseEstratto==false) {
			     line = reader.readLine();
			     //Verificare se nella linea si trova la parola "class"
			     int indiceInizioParola = funzioni.cercaSottoStringa(line, "class");
			     if(indiceInizioParola>=0) {
			     //entra nell'if solo se nella stringa è presente la parola class
			     nomeClasse = line.substring(indiceInizioParola);
			     nomeClasseEstratto = true;
			     }
			}
			
			//A questo punto in nomeClasse abbiamo la riga contenente la dicitura 
			// " class NomeClasse{ "
			System.out.println("La linea corrispondente al nome della classe del corrente file è:");
			System.out.println(nomeClasse);
			
			
			
			//Adesso estrarre il codice dei casi di test presenti nel corrente file
			//mettere il suddetto codice in "codiceTestsCases"
			//prendere quindi tutto ciò che va dal primo @Test fino a ciò che viene prima @AfterAll
			
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			Boolean fermati = false; //diventa true quando incontra @AfterAll
			Boolean primoTestIncontrato = false;
			
			while(line!=null && fermati==false) {
			     line = reader.readLine();
			     
			     if(primoTestIncontrato==false) {
			     int indiceInizioParola = funzioni.cercaSottoStringa(line, "@Test");
			     	if(indiceInizioParola>=0) {
			     		primoTestIncontrato=true;
			     		System.out.println("Ho messo a true il flag primoTestIncontrato");
			     	}
			     }
			     
			     if(primoTestIncontrato==true) {
			     //Dato che il primo test è stato incontrato
			     //Verificare se nella linea si trova la parola "@AfterAll"
			     int indiceInizioParola = funzioni.cercaSottoStringa(line, "@After");
			     if(indiceInizioParola>=0) {
			     //entra nell'if solo se nella stringa è presente la parola @AfterAll
			     System.out.println("Ho incontrato la parola @AfterAll");
			     fermati = true;
			     System.out.println("Ho quindi posto a true il flag fermati");
			     }else {
			     //Se nella riga non si trova @AfterAll, possiamo quindi aggiungere la line
			     //alla variabile String codiceTestsCases
			     codiceTestsCases = codiceTestsCases+line+"\n";
			     //System.out.println("Per ora il valore della variabile codiceTestsCases è:");
			     //System.out.println(codiceTestsCases);
			     }
			     }
			}
			//Fuori dal While
			
			//A questo punto abbiamo ottenuto sia il nome della classe che il codice di tutti i Test Case
			//i valori sono in "String nomeClasse" e "String codiceTestsCases"
			System.out.println("Sono appena uscito dall'ultimo while, vediamo quindi il valore della stringa codiceTestsCases:");
			System.out.println(codiceTestsCases);
			
			
			//Azzero contenuto file
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			//Definisco Stringa finale, contenente il test nel formato corretto Chrome Headless
			final String testFormatoCorretto = header_librerie+"\n"+"public "+nomeClasse+"\n"+attributi_beforeAll+"\n"+codiceTestsCases+"\n"+afterAll_finoAllaFine;
			
			writer.write(testFormatoCorretto);
			writer.flush();
			writer.close();
			
		}
		
		
		
		
	}

}
