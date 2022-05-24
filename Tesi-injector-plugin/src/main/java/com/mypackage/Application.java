package com.mypackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args){
//        String[] args = new String[5];
//        args[0] = ".html";
//        args[1] = "angularjs";
//        args[2] = "C:\\Users\\talit\\Desktop\\test-hooks-master\\test-hooks-master\\frontend";
//        args[3] = "correct";
//        args[4] = "nomeTag";

        System.out.println("Inizio");
        Path start = Paths.get(args[2]);
        try {
            Stream<Path> streamPath = Files.walk(start, Integer.MAX_VALUE);
            List<String> filePathList = streamPath
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());
            //ottenere lista di file di FE
            List<String> feFilePathList = filePathList
                    .stream()
                    .filter(fileName -> fileName.contains(args[0]))
                    .collect(Collectors.toList());

            System.out.println("La lista dei file di FE è:");
            System.out.println(feFilePathList);
            List<String> commmandList = createHookInjectionShContent(feFilePathList, args[1]);
            commmandList.add(0,"cd /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/test-hooks/test-guard");
            System.out.println("La lista dei comandi da inserire nel file hookInjection.sh:");
            System.out.println(commmandList);

            //Creare file hookInjection.sh
            FileWriter myWriter = new FileWriter("hookInjection.sh");
            commmandList.forEach(feFile -> {
                try {
                    myWriter.write(feFile+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Successfully wrote to the file.");
            myWriter.close();

            try{
                ProcessBuilder pb = new ProcessBuilder();
                pb.command("bash", "-c", "bash /home/runner/work/Tesi-StrumentoGenerale/Tesi-StrumentoGenerale/Tesi-injector-plugin/target/hookInjection.sh");
                Process p = pb.start();
                p.waitFor();
                System.out.println("Script executed..");
            }catch(Exception e){
                e.printStackTrace();
            }

            //parte di correzione dei file di frontend
            if(args.length>=4 && args[3].equals("correct")){
                feFilePathList.forEach(filePath -> {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
                        String line = bufferedReader.readLine();
                        StringBuilder fileContent = new StringBuilder(line);
                        while(line != null) {
                            System.out.println(line);
                            line = bufferedReader.readLine();
                            if(line != null) fileContent.append(line).append("\n");
                        }
                        System.out.println("[Correzione File FE] il valore originale è:");
                        System.out.println(fileContent);
                        System.out.println("[Correzione File FE] il valore con l'aggiunta dei timestamp è:");
                        String newContentFeFile = fileContent.toString();
//                        Date date = new Date();
                        long timeNano = System.nanoTime();
                        System.out.println("[Correzione File FE] timestamp da aggiungere: "+timeNano);
//                        System.out.println("[Correzione File FE] args[4] (tag) da aggiungere: "+args[4]);
//                        newContentFeFile = newContentFeFile.replaceAll(" x-test"," "+args[4]+"-"+timeNano+"-x-test");
                        newContentFeFile = newContentFeFile.replaceAll(" x-test"," x"+timeNano+"-x-test");
                        System.out.println(newContentFeFile);
                        //TODO: Sezione elimina doppioni
                        List<String> wordBlackList = new ArrayList<>();
                        //TODO: fare una replaceAll in newContentFeFile con un carattere vuoto di tutte le parole nella black list
                        String[] temp = newContentFeFile.split("<");
                        List<String> tempList = Arrays.asList(temp);
                        tempList.forEach(elem -> {
                                //eliminare occorrenza con timestamp più grande
                                String[] words = elem.split(" ");
                                List<String> wordsList = Arrays.asList(words);
                                wordsList = wordsList.stream().filter(word -> word.contains("-x-test")).collect(Collectors.toList());
                                int sizeList = wordsList.size();
                                for(int i=0; i < sizeList; i++){
                                    if(i!=0) wordBlackList.add(wordsList.get(i));
                                }
                        });
                        List<String> realBlackWordList = new ArrayList<>();
                        for(String blackWord: wordBlackList){
                            if(blackWord.contains(">")){
                                String[] word = blackWord.split(">");
                                blackWord = word[0];
                            }
                            realBlackWordList.add(blackWord);
                        }
                        for(String blackWord: realBlackWordList){
                            newContentFeFile = newContentFeFile.replaceAll(blackWord, "");
                        }

                        //TODO: Eliminare successivi due caratteri ad ogni parola contenente -x-test
                        newContentFeFile = newContentFeFile.replaceAll(" >", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 0>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 1>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 2>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 3>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 4>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 5>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 6>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 7>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 8>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" 9>", ">");
                        newContentFeFile = newContentFeFile.replaceAll(" >", ">");


                        //Aggiornare contenuto file
                        FileWriter writerToUpdateFe = new FileWriter(filePath);
                        writerToUpdateFe.write(newContentFeFile);
                        writerToUpdateFe.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public static List<String> createHookInjectionShContent(List<String> fileFeList, String grammarType){
        List<String> commandList = new ArrayList<>();
        fileFeList.forEach(
                content -> {
                    content = "node main.js inject-hooks "
                            + content
                            + " --grammar "
                            + grammarType;
                    commandList.add(content);
                }
        );
        return commandList;
    }

}
