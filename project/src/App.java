import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static ArrayList<String> referenceString = new ArrayList<>();
    public static int algorithm;
    public static int framesUsed;
    public static boolean exitApp = false;
    
    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        while (exitApp != true){
            userInputter(userInput);
            if ((referenceString == null || referenceString.size() > 15) || (algorithm < 1 || algorithm > 5) || (framesUsed < 1 || framesUsed > 9)){
                System.out.println("Input incorrect. Try again.");
                continue;
            }
            System.out.println("temp");
        }
        //! Algorithm selector must follow
        userInput.close();
    }

    public static void userInputter(Scanner userInput){
        System.out.println();
        System.out.print("Enter a reference string separated with \", \": ");
        String tempReferenceString = userInput.nextLine();
        if (tempReferenceString.equals("9999")){
            userInput.close();
            referenceString = null;
            algorithm = 0;
            framesUsed = 0;
            System.out.println("Goodbye.");
            exitApp = true;
            return;
        }

        String[] tempRefStringArray = tempReferenceString.split(", ");
        for (String s : tempRefStringArray){
            referenceString.add(s);
        }

        System.out.println();
        System.out.println("1. FIFO");
        System.out.println("2. Optimal Replacement");
        System.out.println("3. LRU (using time of use)");
        System.out.println("4. LRU Approximation (using reference byte)");
        System.out.println("5. LFU");
        System.out.print("Select a page algorithm (enter a number): ");
        algorithm = userInput.nextInt();

        System.out.println();
        System.out.print("Enter the number of frames (2-9): ");
        framesUsed = userInput.nextInt();

        userInput.nextLine(); // This line fixes a loop bug (consumes the line break)

        //! YOU MIGHT WANT TO MOVE THIS TO MAIN LATER SO YOU CAN CONTROL OUTPUT BETTER
        System.out.println();
        System.out.println("Reference string: " + referenceString.toString());
        System.out.println("Algorithm: " + algorithm);
        System.out.println("Number of frames: " + framesUsed);
    }

    public static void algorithmSelector(){

    }
}