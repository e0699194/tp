package seedu.duke;


import seedu.exceptions.DeliveryAlreadyCompletedException;
import seedu.exceptions.DeliveryOutOfBoundsException;

import java.util.ArrayList;

/**
 * Handles general user interaction aspects of Diliveri, including
 *
 * @author Manika Hennedige
 * @version 1
 * @since 13-03-2021
 */
public class Ui {
    protected static final String DIVIDER = "-------------------------------------";
    protected static final String HELP_MESSAGE =
            "The following are several accepted commands by Diliveri:\n\n" +
                    "'help': Displays this help message\n" +
                    "'profile': Displays your profile\n" +
                    "'edit': Allows you to edit your profile details\n" +
                    "'start': Loads up an allocated delivery assignment into the delivery list\n" +
                    "'list': Displays the list of deliveries in your assignment\n" +
                    "'view <number>': Displays details of the selected delivery\n" +
                    "'complete <number>': Marks the selected delivery as completed\n" +
                    "'record': Displays the list of completed deliveries and the respective income earned\n" +
                    "'route': Displays optimised delivery path";

    /**
     * Empty constructor for the Ui object
     */
    public Ui() {}

    /**
     * This method is only callable from within the Ui class
     */
    protected void printDivider() {
        System.out.println(DIVIDER);
    }


    /**
     * Prints welcome screen
     */
    public void showWelcomeScreen() {
        printDivider();
        System.out.println("Welcome to Diliveri");
        System.out.println(HELP_MESSAGE);
        printDivider();
    }

    /**
     * Prints goodbye screen
     */
    public void showFarewellScreen() {
        printDivider();
        System.out.println("Safe travels! Goodbye!");
        printDivider();
    }

    /**
     * Shows help message with accepted commands
     */
    public void showHelpMessage() {
        printDivider();
        System.out.println(HELP_MESSAGE);
        printDivider();
    }

    /**
     * Asks for user input
     */
    public void promptUserInput() {
        printDivider();
        System.out.println("How can Diliveri help you today?");
        printDivider();
    }

    /**
     * Prints list of deliveries present in delivery list
     */
    public void showDeliveryList() {

        System.out.println("No. || Delivery ID || Status || Address || Recipient");
        int i = 1;
        for (Delivery delivery : DeliveryList.deliveries) {
            System.out.println(i + ". " + delivery);
            i++;
        }
    }

    /**
     * shows deliveryman's completed deliveries together with total earnings
     * @param records the ArrayList of completed deliveries to print
     */
    public void showRecords(ArrayList<Delivery> records) {
        System.out.println("Congratulations on completing the following deliveries:");
        System.out.println(" Number | ID | Location | Earned Amount ");
        int i = 1;
        double total = 0;
        for (Delivery delivery : records) {
            total += delivery.getDeliveryFee();
            System.out.println(i + " | "
                    + delivery.getDeliveryID() + " | "
                    + delivery.getAddress() + " | " + delivery.getDeliveryFee());
            i++;
        }
        System.out.println("Total Earnings: " + total);
    }

    /**
     * Shows details about a single delivery order
     * @param deliveryNumber is the index of the delivery in the ArrayList that is to be displayed
     */
    public void showDeliveryDetails(int deliveryNumber) {
        Delivery delivery = DeliveryList.deliveries.get(deliveryNumber);
        System.out.println(delivery);
        int i = 1;
        for (Item item : delivery.getItems()) {
            System.out.println(i + ": \n" + item);
            i++;
        }
    }

    /**
     * Displays to a user that a delivery has been completed
     * @param deliveryNumber is the index of the delivery to be marked as completed
     */
    public void showCompletedDelivery(int deliveryNumber) {
        Delivery delivery = DeliveryList.deliveries.get(deliveryNumber);
        System.out.println("The following delivery has been marked as completed:");
        System.out.println(delivery);
    }

    /**
     * Prints shortest path for the driver
     * @param sortedDeliveries list of deliveries sorted by distance
     */
    public void printMap(ArrayList<Delivery> sortedDeliveries){
        for (int i = 0; i < sortedDeliveries.size(); i++){
            System.out.println(sortedDeliveries.get(i).getAddress());
            System.out.println("\t|");
            System.out.println("\tV");
            if (i + 1 >= sortedDeliveries.size()){
                System.out.println("END OF JOB!!");
            }
        }
        if (sortedDeliveries.size() < 1){
            System.out.println("No deliveries loaded!!");
        }
    }

    /**
     * @param deliveryman deliveryman to show details about
     */
    public void showProfile(Deliveryman deliveryman) {
        System.out.println(deliveryman);
    }

    public void processViewDelivery(String userArguments, Deliveryman deliveryman, Parser parser) {
        int deliveryNumber = Integer.parseInt(parser.parseInput("view", userArguments, deliveryman));
        try {
            parser.validateDeliveryNumber(deliveryNumber);
            showDeliveryDetails(deliveryNumber);
        } catch (DeliveryOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void processCompleteDelivery(String userArguments, Deliveryman deliveryman, Parser parser) {
        int deliveryNumber = Integer.parseInt(parser.parseInput("complete", userArguments, deliveryman));
        try {
            parser.validateDeliveryNumber(deliveryNumber);
            parser.validateCompleteDelivery(deliveryNumber);
            Delivery.completeDelivery(deliveryman, deliveryNumber);
            showCompletedDelivery(deliveryNumber);
            DataManager.saveDeliveries();
        } catch (DeliveryOutOfBoundsException e) {
            System.out.println(e.getMessage());
        } catch (DeliveryAlreadyCompletedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void processDeliveryRoute() {
        Filter deliveryFilter = new Filter();
        Map deliveryMap = new Map();
        ArrayList<Delivery> uncompletedDeliveries = deliveryFilter.uncompletedDeliveriesFilter(DeliveryList.deliveries);
        ArrayList<Delivery> sortedDeliveries = deliveryMap.shortestPathGenerator(uncompletedDeliveries);
        printMap(sortedDeliveries);
    }


}
