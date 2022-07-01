/**
 * Final Lab Program
 * Description: A coffee ordering system. NEW FEATURE: Rewards points system.
 * 6/30/2022
 * @author Blake Pearson
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    //class array inventory list that all methods in Main can access to determine if coffee can be made.
    public static int[] inventoryList = new int[6];

    //class object that marks the current customers rewards account to be used by mains methods.
    public static PointSystem currentCustomer;

    public static void main(String[] args) {

        /**
         * Path for the log and inventory files. Required to run program properly.
         */
        String logFilePath = "C:\\Users\\pears\\IdeaProjects\\CS160CoffeeProject\\src\\LogFile.txt";
        String inventoryFile = "C:\\Users\\pears\\IdeaProjects\\CS160CoffeeProject\\src\\inventory.txt";

        //Creation of stack for pushing orders.
        Stack<String> order = new Stack<String>();
        Scanner CafeApplication = new Scanner(System.in);
        System.out.println("Cafe Application Running...");

        /*
         * Inventory file is read in initially to allow user to begin ordering coffees. Without having to manually read inventory on program start.
         */
        inventoryList = inventoryReader(inventoryFile, false);

        /**
         * Menu area, allows user to choose between 6 options
         * Option 1: Will read the inventoryFile and display the current inventory amounts for each item
         * Option 2: Will create a coffee order, also asks the user if they have a rewards account number to utilize with their order to earn or use points
         *          Customer is able to earn 1 point per dollar spent (cents rounded down) and use 500 points for 50% off their order.
         * Option 3: Updates the inventory file
         * Option 4: Updates the log file
         * Option 5: Creates a rewards account with 0 points and saves the customers reward ID and points to points.txt
         * Option 6: Exits application, updating log and inventory file.
         */
        int input = 0;
        while (input != 1) {
            System.out.printf("Press 1 : Read Inventory\nPress 2 : Create Coffee Order\nPress 3 : Update Inventory\nPress 4 : Update log file\nPress 5 : Create rewards account\nPress 6 : Exit the application\n");
            switch (CafeApplication.nextLine()) {
                case "1":
                    //Calls read inventory with the display tag set to true so it outputs the inventory info.
                    inventoryList = inventoryReader(inventoryFile, true);
                    break;
                case "2":
                    //Prompts user to enter rewards number if they select that they have an account
                    System.out.println("Does customer have a rewards number? (y/n)");
                    boolean rewardsUsed = false;
                    if (CafeApplication.nextLine().equals("y")) {
                        System.out.println("Enter rewards number");
                        currentCustomer = new PointSystem(CafeApplication.nextInt());
                        CafeApplication.nextLine();
                        rewardsUsed = true;
                    }

                    //Creates coffee order
                    if (inventoryList[0] != 0) {
                        ArrayList<String> Item = new ArrayList<>();
                        ArrayList<Double> price = new ArrayList<>();
                        ArrayList<String> Temp2 = new ArrayList<>();
                        System.out.println("Coffee order created. Select toppings for the first coffee");
                        String line = "yes";
                        do {
                            Temp2 = CreateOrder();
                            inventoryList[0] -= 1;
                            Iterator<String> it = Temp2.iterator();
                            for (int i = 0; i < Temp2.size(); i += 2) {
                                Item.add(it.next());
                                price.add(Double.valueOf(it.next()));
                            }
                            if (inventoryList[0] != 0) {
                                System.out.println("Do you want to add another coffee to this order? - yes or no");
                            } else {
                                System.out.println("Order Completed. No more coffees.");
                                break;
                            }
                        } while (!(line = CafeApplication.nextLine()).equals("no"));

                        /* If the customer used a rewards number, this section will allow them to use their points if they do have any, if they do not then it
                         * will update the account with the amount of points earned.
                         * Points are NOT earned on orders which 50% off reward is used.
                         */
                        if (rewardsUsed) {
                            if (currentCustomer.getPoints() >= 500) {
                                System.out.println("Total rewards points: " + currentCustomer.getPoints() + "\nWould you like to use 500 for 50% off your order? (y/n)");
                                if (CafeApplication.nextLine().equals("y")) {
                                    order.push(PrintOrderPoints(Item, price, false));
                                } else {
                                    order.push(PrintOrderPoints(Item, price, true));
                                }
                            } else {
                                order.push(PrintOrderPoints(Item, price, true));
                            }
                        } else {
                            order.push(PrintOrder(Item, price));
                        }
                    } else {
                        System.out.println("Out of Coffee. Visit us later.");
                    }
                    break;
                case "3":
                    inventoryUpdate(inventoryList, inventoryFile);
                    break;
                case "4":
                    logWriter(logFilePath, order);
                    break;
                case "5":
                    /*
                     * Prompts user to enter a number that will be their permanent rewards number. Does not allow for multiple rewards numbers for different accounts.
                     */
                    System.out.println("Please enter a ID number to use as a rewards number.");
                    currentCustomer = new PointSystem();
                    boolean success = currentCustomer.createRewards(CafeApplication.nextInt());
                    if (success) {
                        System.out.println("Success! Welcome to rewards program!");
                    } else {
                        System.out.println("Account creation failed, please select a different ID number");
                    }
                    CafeApplication.nextLine();
                    break;
                case "6":
                    try {
                        inventoryUpdate(inventoryList, inventoryFile);
                        if (!order.empty()) {
                            logWriter(logFilePath, order);
                        }
                    } catch (Exception e) {
                    }
                    input = 1;
                    break;
                default:
                    System.out.println("Invalid Selection. Please Try Again");

            }
        }

    }

    /**
     * This method formats the string that is pushed to the order stack. Taking in the items purchased and their price
     * it will use a single string and mutiple append commands to properly format the receipt.
     * @param Item Item list that has all the coffees ordered in it
     * @param price Price list that has those coffees prices in it
     * @return A string to be pushed to the stack with the properly formatted receipt
     */
    public static String PrintOrder(ArrayList<String> Item, ArrayList<Double> price){
        StringBuilder str = new StringBuilder();
        Iterator<Double> itOrder = price.iterator();
        double orderPrice = 0.0;
        Iterator<Double> it = price.iterator();
        for (int i = 0; i < price.size(); i++) {
            orderPrice += it.next();
        }
        Iterator<String> itItem = Item.iterator();
        Iterator<Double> itprice = price.iterator();
        str.append("\nRECEIPT\n");
        for (int i = 0; i < Item.size(); i++) {
            str.append(String.format("Item %d: %s | Cost: %.2f\n", (i+1), itItem.next(), itprice.next()));
        }
        str.append(String.format("TOTAL COST OF ORDER: %.2f", orderPrice));
        return str.toString();
    }

    /**
     * Same method as above, but takes into account if the customer used a rewards account.
     * It will format the string with the proper discount if applied and tell the user their points.
     * This method formats the string that is pushed to the order stack. Taking in the items purchased and their price
     * it will use a single string and mutiple append commands to properly format the receipt.
     * @param Item Item list that has all the coffees ordered in it
     * @param price Price list that has those coffees prices in it
     * @return A string to be pushed to the stack with the properly formatted receipt
     */
    public static String PrintOrderPoints(ArrayList<String> Item, ArrayList<Double> price, boolean addPoints){
        StringBuilder str = new StringBuilder();
        Iterator<Double> itOrder = price.iterator();
        double orderPrice = 0.0;
        Iterator<Double> it = price.iterator();
        for (int i = 0; i < price.size(); i++) {
            orderPrice += it.next();
        }
        Iterator<String> itItem = Item.iterator();
        Iterator<Double> itprice = price.iterator();
        str.append("\nRECEIPT\n");
        for (int i = 0; i < Item.size(); i++) {
            str.append(String.format("Item %d: %s | Cost: %.2f\n", (i+1), itItem.next(), itprice.next()));
        }
        if (addPoints) {
            currentCustomer.updatePoints(orderPrice);
            str.append("You earned " + Math.round(orderPrice) + " points on this order and have " + currentCustomer.getPoints() + " points in your account.\n");
        } else {
            orderPrice *= .5;
            currentCustomer.removePoints(500);
            str.append("You used 500 points on this order and have " + currentCustomer.getPoints() + " points remaining in your account.\n");
        }
        str.append(String.format("TOTAL COST OF ORDER: %.2f", orderPrice));
        return str.toString();
    }

    /**
     * Updates the inventory file with the current inventory array.
     * @param list1
     * @param inventoryFile
     */
    public static void inventoryUpdate(int[] list1, String inventoryFile){
        FileWriter output = OpenInvFile(inventoryFile);
        String invout = String.format("Black Coffee = %d\nMilk = %d\nHotWater = %d\nEspresso = %d\nSugar = %d\nWhippedCream = %d", list1[0], list1[1], list1[2], list1[3], list1[4], list1[5]);
        try {
            output.write(invout);
            output.flush();
            System.out.println("Successfully updated the inventory");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an order for coffees, allows user to pick toppings, users are restricted to inventory amounts.
     * @return
     */
    public static ArrayList<String> CreateOrder(){
        Scanner userFeedback = new Scanner(System.in);
        ArrayList<String> coffeeOrder = new ArrayList<String> ();
        Coffee basicCoffee = new BasicCoffee();
        int in = 0;
        while (in != 1) {
            System.out.println("Enter the following values to add toppings: 1.) milk, 2.) hot water, 3.) espresso, 4.) sugar, 5.) whipped cream, e - to complete the order");
            switch (userFeedback.nextLine()) {
                case "1":
                    if (inventoryList[1] != 0) {
                        basicCoffee = new Milk(basicCoffee);
                        inventoryList[1] -= 1;
                    } else {
                        System.out.println("Out of milk. Try a different topping.");
                    }
                    break;
                case "2":
                    if (inventoryList[2] != 0) {
                        basicCoffee = new hotwater(basicCoffee);
                        inventoryList[2] -= 1;
                    } else {
                        System.out.print("Out of hot water. Try a different topping.");
                    }
                    break;
                case "3":
                    if (inventoryList[3] != 0) {
                        basicCoffee = new espresso(basicCoffee);
                        inventoryList[3] -= 1;
                    } else {
                        System.out.println("Out of espresso. Try a different topping.");
                    }
                    break;
                case "4":
                    if (inventoryList[4] != 0) {
                        basicCoffee = new Sugar(basicCoffee);
                        inventoryList[4] -= 1;
                    } else {
                        System.out.println("Out of sugar. Try a different topping.");
                    }
                    break;
                case "5":
                    if (inventoryList[5] != 0) {
                        basicCoffee = new WhippedCream(basicCoffee);
                        inventoryList[5] -= 1;
                    } else {
                        System.out.println("Out of whipped cream. Try a different topping.");
                    }
                    break;
                case "e":
                    in = 1;
                    break;
                default: System.out.println("Invalid Input");
            }
        }
        coffeeOrder.add(basicCoffee.printCoffee());
        coffeeOrder.add(String.valueOf(basicCoffee.Cost()));
        return coffeeOrder;
    }

    /**
     * Outputs orders to the log file.
     * @param outputFilePath
     * @param order
     */
    public static void logWriter(String outputFilePath, Stack<String> order) {
        FileWriter output = openOutput(outputFilePath);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            output.write("\n\nWriting orders from stack " + formatter.format(date));
            int ordersize = order.size();
            for (int i = 0; i < ordersize; i++) {
                output.write(order.pop());
            }
            output.flush();
            System.out.println("Successfully updated the log file");
        } catch (IOException e) {
            System.out.println("Nothing to log. Stack is empty.");
        }
    }

    /**
     * Reads the inventory file and passes the amounts to an array.
     * @param inventoryFile
     * @param display
     * @return
     */
    public static int[] inventoryReader(String inventoryFile, boolean display) {
        BufferedReader in = new BufferedReader(openInput(inventoryFile));
        int[] tempInv = new int[6];
        if (display) {
            System.out.println("Current items in the inventory:");
        }
        for (int i = 0; i < 6; i++) {

            String line;

            try {
                line = in.readLine();
            } catch (IOException e) {
                System.out.println("Inventory File is Empty");
                throw new RuntimeException(e);
            }

            if (display) {
                System.out.println(line);
            }

            for (int j = line.length(); j > 0; j--) {
                if (!Character.isDigit(line.charAt(j - 1))) {
                    line = line.substring(j, line.length());
                    break;
                }
            }
            int num = Integer.parseInt(line);
            tempInv[i] = num;
        }
        return tempInv;
    }

    /**
     * Used to open a FileWriter output and catch exceptions (this one appends to files)
     * @param filename
     * @return
     */
    public static FileWriter openOutput(String filename) {
        FileWriter out = null;
        try {
            File outfile = new File(filename);
            out = new FileWriter(outfile, true);
        } catch (IOException e) {
            System.out.println(filename + " could not be found");
            System.exit(0);
        }
        return out;
    }

    /**
     * Used to open a FileWriter output and catch exceptions (this one overrides files)
     * @param filename
     * @return
     */
    public static FileWriter OpenInvFile(String filename) {
        FileWriter out = null;
        try {
            File outfile = new File(filename);
            out = new FileWriter(outfile);
        } catch (IOException e) {
            System.out.println(filename + " could not be found");
            System.exit(0);
        }
        return out;
    }

    /**
     * Used to open a file reader and catch exceptions.
     * @param filename
     * @return
     */
    public static FileReader openInput(String filename) {
        FileReader in = null;
        try {
            File infile = new File(filename);
            in = new FileReader(infile);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println(filename + " could not be found");
            System.exit(0);
        }
        return in;
    }
}