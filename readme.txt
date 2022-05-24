Strumento Generale sviluppato nella tesi di laurea.

Per configurarlo su di una propria applicazione web, seguire i seguenti passi:

1) Fare la fork del seguente repository: https://github.com/gianlucat97/Tesi-StrumentoGenerale.git


2) Dopo aver creato il proprio repository (tramite la fork), fare la clone in locale del repository creato.


3) Inserire all'interno della directory "insert-here-yuor-web-app" la cartella contenente il progetto della propria applicazione web.


4) Creare il virtual environment “envForGithubActions” 


5) Inserire le seguenti 6 variabili d'ambiente, customizzando i valori di esempio qui riportati in base al proprio caso d'uso:

EMAIL_ACCOUNT_GITHUB: t*********@gmail.com
NOME_ACCOUNT_GITHUB: g*********
PASSWORD_ACCOUNT_GITHUB: *********
FE_EXTENSION_TYPE: .html
GRAMMAR_TYPE: angularjs
DIR_FILE_FE: /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/insert-here-your-web-app/root-frontend-web-app

NB: I GRAMMAR_TYPE consentiti sono: ['angularjs', 'html', 'php', 'smarty', 'twig', 'freemarker']


6) Customizzare i file startBackEnd.sh e startFrontEnd.sh, in base al proprio caso d'uso.
Vediamo degli esempi, 

startBackEnd.sh:
echo "Inizio comandi installazione precondizioni"
sudo apt update
sudo apt install openjdk-11-jdk openjdk-11-jre
echo "Installata versione di java numero"
java -version 

echo "Inizio comandi esecuzione Backend"
cd /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/insert-here-your-web-app/root/backend
mvn clean install
cd /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/insert-here-your-web-app/root/backend/target
echo "Vediamo quali file jar si trovano in cartella target"
ls -a
java -jar backend-0.0.1-SNAPSHOT.jar &


startFrontEnd.sh:
echo "Inizio comandi installazione precondizioni"
cd /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale
curl -sL https://deb.nodesource.com/setup_12.x -o nodesource_setup.sh
cat nodesource_setup.sh
sudo bash nodesource_setup.sh
sudo apt install nodejs
echo "versione di node installata"
node -v
echo "Installazione di npm"
sudo apt install npm
echo "Fine comandi installazione Node"

echo "Inizio comandi esecuzione Frontend"
cd /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/insert-here-your-web-app/root/frontend
echo "Siamo nella directory FE, proviamo a lanciarlo in esecuzione"
npm install
echo "Installazione npm effettuata, prossimo comando: npm start"
npm start &


7) Andare nel tab "Actions" ed attivare i "Workflows" cliccando sul button "I understand my workflows, go ahead and enable them".


8) Fare una modifica ad un file txt e pushare (e.g. aggiungere un carattere al readme.txt), in modo da triggerare l'esecuzione del file mainOnPush.yml


9) Attendere il termine dell'esecuzione del file mainOnPush.yml, il quale si occupa di fare la hook-injection all'interno dei file di FE.


10) Fare la pull in locale, in modo da poter avere anche sulla propria macchina i file di FE con i localizzatori iniettati.


11) Eseguire l'applicazione web in locale


12) Aprire Katalon Recorder e nella sezione "Extension script" aggiungere il file "attributeHooksLocators.js" reperibile nel corrente repository.


13) Registrare casi di test con Katalon Recorder ed esportarli in modalita (JUnit + WebDriver)


14) Si otterrà un file zip, estrarre il contenuto del file e pushare all'interno della directory ./project-test-headless/src/test/java/com/example/TesiIntegrazioneProgettoEsterno/
solamente i file di test (con estensione .java) estratti


15) Al push verrà triggerata l'esecuzione del file mainOnPush.yml che si occuperà di correggere il formato
dei file di test pushati (in modo da renderli eseguibili all'interno di un container), attendere quindi il termine della sua esecuzione


16) Creare una nuova release, triggerando quindi l'esecuzione del file main.yml


17) Al termine dell'esecuzione del file main.yml, all'interno del tag creato, nella directory ./TestSuite/nomeTagCreato si troveranno tutti i report autogenerati inerenti ai test di regressione eseguiti.

NB: Non bisogna inserire metacaratteri nel nome di un tag, come per esempio il punto "."
usare quindi tag del tipo "v1_0-Hooks" e non tag come "v1.0-Hooks"


Successive iterazioni nel ciclo di sviluppo dell'applicativo web:

1) Fare una pull, per avere sempre allineato il progetto in locale con il remote.

2) Scrivere nuovo codice web app.

3) Pushare le modifiche effettuate.

4) Attendere che il file mainOnPush termini la sua esecuzione (si occupa di iniettare nuovamente gli hooks a valle delle modifiche).

5) Eseguire la pull (per avere anche in locale i file di FE con gli hooks aggiornati).

6) Rieseguire applicazione web in locale.

7) Registrare nuovi casi di test con Katalon Recorder.

8) Esportare i nuovi casi di test (in Katalon in formato WebDriver+JUnit) e pushare i file (.java) ottenuti nella cartella
./project-test-headless/src/test/java/com/example/TesiIntegrazioneProgettoEsterno/

9) Attendere che il file mainOnPush.yml termini la sua esecuzione (si occupa di rendere i nuovi file di test eseguibili in modalità headless).

10) Creazione nuova release.

11) Attendere esecuzione file main.yml (che va ad eseguire ed autogenerare la reportistica dei test di regressione).

12) Nella directory ./TestSuite/nomeTagCreato si troveranno tutti i report autogenerati inerenti ai test di regressione eseguiti.
Analizzare il report in formato ".xls" ed andare a correggere e/o eliminare i test rotti.

13) Tornare al punto 1, se non è ancora terminato il ciclo di sviluppo dell'applicativo web.

