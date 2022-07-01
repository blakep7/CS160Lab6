import java.io.*;
import java.util.HashMap;

public class PointSystem {

    //Declares the path of the rewards database file, MUST BE USED FOR PROGRAM TO RUN.
    private String pointsFile = "C:\\Users\\pears\\IdeaProjects\\CS160CoffeeProject\\src\\points.txt";
    private HashMap<Integer, Integer> pointsMap = readPointFile(pointsFile);
    private int customerID;
    private int pointAmount;

    /**
     * Default constructor
     */
    public PointSystem() {
    }

    /**
     * Constructor allows for user to input their customerID and generate their pointsAmount
     * @param customerID
     */
    public PointSystem(int customerID) {
       if (pointsMap.containsKey(customerID)) {
           this.customerID = customerID;
           pointAmount = pointsMap.get(customerID);
       } else {
           System.out.println("Rewards number does not exist.");
       }

    }

    /**
     * Adds points to a users account in the hashmap and updates the points database
     * @param orderCost the cost of the order that will be added ot the points (rounded down)
     */
    public void updatePoints(double orderCost) {
        pointAmount += Math.round(orderCost);
        pointsMap.replace(customerID, pointAmount);
        updatePointFile();
    }

    /**
     * Gets the amount of points that a user has.
     * @return Amount of points that the user ID references
     */
    public int getPoints() {
        return pointAmount;
    }

    /**
     * Removes points from an account when rewards are used and updates the hashmap and point file.
     * @param amount amount of points to be removed from account
     */
    public void removePoints(int amount) {
        pointAmount -= amount;
        pointsMap.replace(customerID, pointAmount);
        updatePointFile();
    }

    /**
     * Creates a rewards account for the user, they get to pick their rewards ID and it is checked to confirm that it is not a preexisting ID.
     * @param userID ID that customer picks for rewards account
     * @return true/false if the account is properly created.
     */
    public boolean createRewards(int userID) {
        if (pointsMap.containsKey(userID)) {
            return false;
        } else {
            this.customerID = userID;
            pointsMap.put(userID, 0);
            updatePointFile();
            return true;
        }
    }

    /**
     * Used to read in the point file database and read the point amount and userID to a hashmap
     * Points file is formatted in USERID=POINTS. Both USERID and POINTS can be any length of integer.
     * @param pointsFile the path of the points file
     * @return the hashmap storing the points file data.
     */
    public HashMap<Integer, Integer> readPointFile(String pointsFile) {
        BufferedReader in = new BufferedReader(openInput(pointsFile));
        HashMap<Integer, Integer> tempmap = new HashMap<>();

        String line;
        try {
            line = in.readLine();
        } catch (IOException e) {
            System.out.println("Point File is Empty");
            throw new RuntimeException(e);
        }

        //Loops until it has reached the end of the point file.
        while (line != null) {


            String pointAmt = null;
            String IDNum = null;


            //Used to read specific sections of the line that correspond to the pointAmt and IDNum
            for (int j = line.length(); j > 0; j--) {
                if (!Character.isDigit(line.charAt(j - 1))) {
                    pointAmt = line.substring(j, line.length());
                    IDNum = line.substring(0, j - 1);
                    break;
                }
            }
            tempmap.put(Integer.parseInt(IDNum), Integer.parseInt(pointAmt));
            try {
                line = in.readLine();
            } catch (IOException e) {
                line = null;
            }
        }
        return tempmap;
    }

    /**
     * Called after every method that preforms an action on the points data, used to update the points file with
     * the current hashmap. Maintains origional formatting so file can be read on next program launch.
     */
    public void updatePointFile() {
        FileWriter output = OpenOutPointFile(pointsFile);
        String pointOut = String.valueOf(pointsMap);
        pointOut = pointOut.substring(1, pointOut.length() - 1);
        pointOut = pointOut.replace(", ", "\n");
        try {
            output.write(pointOut);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Open input file and create file reader, used to catch exceptions
     * @param filename
     * @return
     */
    public FileReader openInput(String filename) {
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

    /**
     * Open output file and create file writer, used to catch exceptions
     * @param filename
     * @return
     */
    public FileWriter OpenOutPointFile(String filename) {
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

}
