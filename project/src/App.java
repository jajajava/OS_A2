import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class App {
    public static ArrayList<Integer> referenceString = new ArrayList<>();
    public static ArrayList<Integer> pageList = new ArrayList<>();
    public static HashMap<Integer, Integer> mapList = new HashMap<>();
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
            algorithm = 0;
            framesUsed = 0;
            pageFaultCounter = 0;

            userInputter(userInput);
            if (exitApp){
                return;
            } else if (framesUsed != 0){ // If it reached framesUsed, the other values had to have been set too
                System.out.println("Reference string: " + referenceString.toString());
                System.out.println("Number of frames: " + framesUsed);
                System.out.println("Content of frames after each page-fault: ");
                
                // Algorithm selector here
                algorithmSelector();
                System.out.println();
                System.out.println("Number of page faults: " + pageFaultCounter);
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
        switch(algorithm) {
            case 1:
                System.out.println();
                System.out.println("Algorithm: FIFO");
                FIFO();
                break;
            case 2:
                System.out.println();
                System.out.println("Algorithm: Optimal Replacement");
                optimalReplacement();
                break;
            case 3:
                System.out.println();
                System.out.println("Algorithm: LRU using time-of-use");
                LRUClock();
                break;
            case 4:
                System.out.println();
                System.out.println("Algorithm: LRU approximation using reference byte");
                //! Call LRUApprox()
                break;
            case 5:
                System.out.println();
                System.out.println("Algorithm: LFU");
                //! Call LFU()
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
        }
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
                // No page fault; update the last use time
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
}
