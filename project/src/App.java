import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static ArrayList<String> referenceString = new ArrayList<>();
    public static int algorithm;
    public static int framesUsed;
    public static boolean exitApp = false;
    
    public static void main(String[] args) throws Exception {
        userInput();
        //! Algorithm selector must follow

    }

    public static void userInput(){
        Scanner userInput = new Scanner(System.in);
        
        System.out.print("Enter a reference string separated with \", \": ");
        String tempReferenceString = userInput.nextLine();
        if (tempReferenceString.equals("9999")){
            System.out.println("Goodbye.");
            exitApp = true;
        }
        //! parse it before assigning to arrayList

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

        userInput.close();

        System.out.println(tempReferenceString + ", " + algorithm + ", " + framesUsed);
    }
}