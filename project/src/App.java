import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class App {
    public static ArrayList<Integer> referenceString = new ArrayList<>();
    public static ArrayList<Integer> pageList = new ArrayList<>();
    public static HashMap<Integer, Integer> mapList = new HashMap<>();
    public static HashMap<Integer, String> referenceBytes = new HashMap<>();
    public static HashMap<Integer, Integer> frequencyMap = new HashMap<>();
    public static int algorithm;
    public static int framesUsed;
    public static int pageFaultCounter;
    public static boolean exitApp = false;

    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        while (!exitApp){
            referenceString.clear();
            pageList.clear();
            mapList.clear();
            referenceBytes.clear();
            frequencyMap.clear();
            algorithm = 0;
            framesUsed = 0;
            pageFaultCounter = 0;

            userInputter(userInput);
            if (exitApp){
                return;
            } else if (framesUsed != 0){ // If it reached framesUsed, the other values had to have been set too
                System.out.println();
                switch (algorithm){
                    case 1:
                        System.out.println();
                        System.out.println("Algorithm: FIFO");
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("Algorithm: Optimal Replacement");
                        break;
                    case 3:
                        System.out.println();
                        System.out.println("Algorithm: LRU using time-of-use");
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("Algorithm: LRU approximation using reference byte");
                        break;
                    case 5:
                        System.out.println();
                        System.out.println("Algorithm: LFU");
                        break;
                }
                System.out.print("Reference string: ");
                for (int i = 0; i < referenceString.size(); i++){
                    if (i != referenceString.size()-1){
                        System.out.print(referenceString.get(i) + ", ");
                    } else {
                        System.out.println(referenceString.get(i));
                    }
                }
                System.out.println("Number of frames: " + framesUsed);
                System.out.println("Content of frames after each page-fault: ");
                
                // Algorithm selector here
                algorithmSelector();
                System.out.println();
                System.out.println("Number of page faults: " + pageFaultCounter);
                System.out.println();
            }
            System.out.println("---------------------------------------------");
        }
        userInput.close();
    }

    public static void userInputter(Scanner userInput){
        // Reference string input
        System.out.println();
        System.out.print("Enter a reference string separated with \", \": ");
        String tempReferenceString = userInput.nextLine();
        if (tempReferenceString.equals("9999")){
            System.out.println("Goodbye.");
            exitApp = true;
            return;
        }

        String[] tempRefStringArray = tempReferenceString.split(", ");

        // Reference string error handling
        if (tempRefStringArray.length > 15){
            System.out.println("You cannot have more than 15 references.");
            return;
        }
        int toInt = -1;
        for (String s : tempRefStringArray){
            if (s.length() > 1){
                System.out.println("Each reference must be one digit long.");
                return;
            }
            try {
                toInt = Integer.parseInt(s);
                referenceString.add(toInt);
            } catch (NumberFormatException n) {
                System.out.println("Please enter a number.");
                return;
            }
        }

        // Algorithm input
        System.out.println();
        System.out.println("1. FIFO");
        System.out.println("2. Optimal Replacement");
        System.out.println("3. LRU (using time of use)");
        System.out.println("4. LRU Approximation (using reference byte)");
        System.out.println("5. LFU");
        System.out.print("Select a page replacement algorithm (enter a number): ");
        algorithm = userInput.nextInt();
        userInput.nextLine(); // This line fixes a loop bug (consumes the line break)

        // Algorithm error handling
        if (algorithm < 1 || algorithm > 5){
            System.out.println("You must input a valid algorithm index.");
            return;
        }

        // Frame input
        System.out.println();
        System.out.print("Enter the number of frames (2-9): ");
        framesUsed = userInput.nextInt();
        userInput.nextLine(); // This line fixes a loop bug (consumes the line break)

        // Frame error handling
        if (framesUsed < 2 || framesUsed > 9){
            System.out.println("You must input a valid number of frames.");
            return;
        }
    }

    public static void algorithmSelector(){
        switch (algorithm) {
            case 1:
                System.out.println();
                FIFO();
                break;
            case 2:
                System.out.println();
                optimalReplacement();
                break;
            case 3:
                System.out.println();
                LRUClock();
                break;
            case 4:
                System.out.println();
                LRUApprox();
                break;
            case 5:
                System.out.println();
                LFU();
                break;
        }
    }

    public static void framePrinter(){
        if (algorithm < 3){
            for (int i = 0; i < framesUsed; i++){
                if (i >= pageList.size()){
                    System.out.print("|   ");
                    continue;
                }
                System.out.print("| " + pageList.get(i) + " ");
            }
            System.out.println("|");
            pageFaultCounter++;
        } else if (algorithm == 3){
            for (int i = 0; i < framesUsed; i++){
                if (i >= pageList.size()){
                    System.out.print("|       ");
                    continue;
                }
                int page = pageList.get(i);
                int lastUseTime = mapList.get(page);
                System.out.print("| " + page + " : " + lastUseTime + " "); 
            }
            System.out.println("|");
            pageFaultCounter++;
        } else if (algorithm == 4){
            for (int i = 0; i < framesUsed; i++){
                if (i >= pageList.size()){
                    System.out.print("|              ");
                    continue;
                }
                int page = pageList.get(i);
                String refByteString = referenceBytes.get(page);
                System.out.print("| " + page + " : " + refByteString + " ");
            }
            System.out.println("|");
            pageFaultCounter++;
        } else if (algorithm == 5){
            for (int i = 0; i < framesUsed; i++){
                if (i >= pageList.size()){
                    System.out.print("|       ");
                    continue;
                }
                int page = pageList.get(i);
                int freq = frequencyMap.get(page);
                System.out.print("| " + page + " : " + freq + " ");
            }
            System.out.println("|");
            pageFaultCounter++;
        }
    }

    private static void printReferenceBytes() {
        for (int i = 0; i < framesUsed; i++){
            if (i >= pageList.size()){
                System.out.print("|            ");
                continue;
            }
            int page = pageList.get(i);
            String refByteString = referenceBytes.get(page);
            System.out.print("| " + page + " : " + refByteString + " ");
        }
        System.out.println("|");
    }

    public static void FIFO(){
        for (int i = 0; i < Math.min(framesUsed, referenceString.size()); i++){
            pageList.add(referenceString.get(i));
            framePrinter();
        }

        // Actual FIFO operation
        for (int i = framesUsed; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);
            if (pageList.contains(currentPage)) { // No page fault if the page is already in pageList
                continue;
            }
            pageList.remove(0);
            pageList.add(currentPage);
            framePrinter();
        }
    }

    public static void optimalReplacement() {
        for (int i = 0; i < Math.min(framesUsed, referenceString.size()); i++) {
            pageList.add(referenceString.get(i));
            framePrinter();
        }
    
        for (int i = framesUsed; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);
    
            // If the page is already in memory, no page fault occurs
            if (pageList.contains(currentPage)) {
                continue;
            }
    
            int[] tempIndexArray = new int[framesUsed];
            int highestIndex = -1;
            int frameToReplace = -1;
    
            for (int j = 0; j < pageList.size(); j++){
                List<Integer> shortenedList = referenceString.subList(i + 1, referenceString.size());
                tempIndexArray[j] = shortenedList.indexOf(pageList.get(j));
    
                if (tempIndexArray[j] == -1){ // If the page isn't found again in reference String, it gets selected
                    pageList.set(j, currentPage);
                    frameToReplace = -1;
                    break;
                } else if (tempIndexArray[j] > highestIndex){ // If all values are present, select the one with the highest index (furthest away)
                    highestIndex = tempIndexArray[j];
                    frameToReplace = j;
                }
            }
            if (frameToReplace != -1){ // Gets updated only if there IS a frame to replace
                pageList.set(frameToReplace, currentPage);
            }
            
            framePrinter();
        }
    }

    public static void LRUClock(){
        int initialFrames = Math.min(framesUsed, referenceString.size());
        for (int i = 0; i < initialFrames; i++){
            mapList.put(referenceString.get(i), i + 1);
            pageList.add(referenceString.get(i));
            framePrinter();
        }

        for (int i = initialFrames; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);

            if (pageList.contains(currentPage)) {
                mapList.replace(currentPage, i + 1);
                continue;
            }

            int lruPage = -1;
            int minTime = 10000;

            for (int page : pageList) {
                int lastUsedTime = mapList.get(page);
                if (lastUsedTime < minTime) {
                    minTime = lastUsedTime;
                    lruPage = page;
                }
            }

            int lruIndex = pageList.indexOf(lruPage);
            pageList.set(lruIndex, currentPage);

            mapList.remove(lruPage);
            mapList.put(currentPage, i + 1);

            framePrinter();
        }
    }

    
    public static void LRUApprox(){
        HashSet<Integer> referencedPagesSinceLastShift = new HashSet<>();
        int pagesMetSinceLastShift = 0;
    
        for (int i = 0; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);
            pagesMetSinceLastShift++;
    
            if (pageList.contains(currentPage)) {
                referencedPagesSinceLastShift.add(currentPage);
            } else {
                if (pageList.size() < framesUsed) {
                    pageList.add(currentPage);
                    referenceBytes.put(currentPage, "11111111");
                } else {
                    int minRefByteValue = Integer.MAX_VALUE;
                    int pageToReplace = -1;
    
                    for (int page : pageList) {
                        String refByteString = referenceBytes.get(page);
                        int refByteValue = Integer.parseInt(refByteString, 2);
                        if (refByteValue < minRefByteValue) {
                            minRefByteValue = refByteValue;
                            pageToReplace = page;
                        }
                    }
    
                    pageList.remove((Integer) pageToReplace);
                    referenceBytes.remove(pageToReplace);
    
                    pageList.add(currentPage);
                    referenceBytes.put(currentPage, "11111111");
                }
                referencedPagesSinceLastShift.add(currentPage);
    
                framePrinter();
            }
    
            if (pagesMetSinceLastShift == 3 || i == referenceString.size() - 1) {
                System.out.println("\nReference bytes before shifting:");
                printReferenceBytes();
    
                for (int page : pageList) {
                    String refByteString = referenceBytes.get(page);
                    refByteString = refByteString.substring(0, 7);
                    if (referencedPagesSinceLastShift.contains(page)) {
                        refByteString = '1' + refByteString;
                    } else {
                        refByteString = '0' + refByteString;
                    }
                    referenceBytes.put(page, refByteString);
                }
                System.out.println("Reference bytes after shifting:");
                printReferenceBytes();
    
                pagesMetSinceLastShift = 0;
                referencedPagesSinceLastShift.clear();
            }
        }
    }    

    public static void LFU(){
        int i = 0;
        while (pageList.size() < framesUsed && i < referenceString.size()){
            int currentPage = referenceString.get(i);
    
            if (pageList.contains(currentPage)){
                frequencyMap.put(currentPage, frequencyMap.get(currentPage) + 1);
            } else {
                pageList.add(currentPage);
                frequencyMap.put(currentPage, 1);
                framePrinter();
            }
            i++;
        }
    
        for (; i < referenceString.size(); i++){
            int currentPage = referenceString.get(i);
    
            if (pageList.contains(currentPage)){
                frequencyMap.put(currentPage, frequencyMap.get(currentPage) + 1);
                continue;
            } else {
                int minFrequency = Integer.MAX_VALUE;
                for (int page : pageList){
                    int freq = frequencyMap.get(page);
                    if (freq < minFrequency){
                        minFrequency = freq;
                    }
                }
    
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int page : pageList){
                    if (frequencyMap.get(page) == minFrequency){
                        candidates.add(page);
                    }
                }
    
                int pageToReplace = -1;
                int indexToReplace = -1;
                for (int j = 0; j < pageList.size(); j++){
                    int page = pageList.get(j);
                    if (candidates.contains(page)){
                        pageToReplace = page;
                        indexToReplace = j;
                        break;
                    }
                }
                pageList.set(indexToReplace, currentPage);
    
                frequencyMap.remove(pageToReplace);
                frequencyMap.put(currentPage, 1);

                framePrinter();
            }
        }
    }    
}