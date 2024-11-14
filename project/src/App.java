import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static ArrayList<String> referenceString = new ArrayList<>();
    public static ArrayList<String> pageList = new ArrayList<>();
    //! might need separate int to count page faults
    public static int algorithm;
    public static int framesUsed;
    public static boolean exitApp = false;
    
    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        while (exitApp != true){
            referenceString.clear();
            pageList.clear();
            algorithm = 0;
            framesUsed = 0;
            userInputter(userInput);
            if (exitApp == true){
                return;
            }
            FIFO();
        }
        //! Algorithm selector must follow
        userInput.close();
    }

    public static void userInputter(Scanner userInput){
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
        //! See if you can go without a clause to have at least one input. If your algos work with empty array, keep it that way
        if (tempRefStringArray.length > 15){
            System.out.println("You cannot have more than 15 references");
            return;
        }
        for (String s : tempRefStringArray){
            if (s.length() > 1){
                System.out.println("Each reference must be one digit long");
                return;
            }
            referenceString.add(s);
        }

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
            System.out.println("You must input a valid algorithm index");
            return;
        }

        System.out.println();
        System.out.print("Enter the number of frames (2-9): ");
        framesUsed = userInput.nextInt();
        userInput.nextLine(); // This line fixes a loop bug (consumes the line break)

        // Frames error handling
        if (framesUsed < 2 || framesUsed > 9){
            System.out.println("You must input a valid number of frames");
            return;
        }

        //! YOU MIGHT WANT TO MOVE THIS TO MAIN LATER SO YOU CAN CONTROL OUTPUT BETTER
        System.out.println();
        System.out.println("Reference string: " + referenceString.toString());
        System.out.println("Algorithm: " + algorithm);
        System.out.println("Number of frames: " + framesUsed);
    }

    public static void algorithmSelector(){
        switch(algorithm) {
            case 1:
                //! CALL FIFO()
                break;
            case 2:
                //! CALL OptimalReplacement()
                break;
            case 3:
                //! CALL LRUClock()
                break;
            case 4:
                //! CALL LRUApprox()
                break;
            case 5:
                //! CALL LFU()
                break;
        }
    }

    public static void FIFO(){
        for (int i = 0; i < framesUsed; i++){
                pageList.add(referenceString.get(i));
                framePrinter();
                // System.out.printf("| %s ", pageList.get(i));
        }
        // System.out.println("|");
        // System.out.println();

        // for (int i = 0; i < framesUsed; i++){
        //     pageList.add(referenceString.get(i));
        //     System.out.printf("| %s ", pageList.get(i));
        // }
        // System.out.println("|");
        // System.out.println();
    }

    public static void framePrinter(){
        if (framesUsed >= pageList.size()){
            for (int i = 0; i < framesUsed; i++){
                if (i >= pageList.size()){
                    System.out.print("|  ");
                    continue;
                }
                System.out.print("| " + pageList.get(i) + " ");
            }
        }
        System.out.println("|");
        System.out.println("Number of page faults: " + pageList.get(pageList.size() - 1));
        System.out.println("---------------------------------------------");
    }

}