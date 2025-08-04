import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class WarehouseManagementSystem {


    public static void main(String[] args) {

        try {
            String filename = "warehouse_data.txt";
            if (args.length > 0) {
                filename = args[0];
                System.out.println("Using file: " + filename);

                File file = new File(filename);
                if (file.exists()) {
                    System.out.println("File exists, loading warehouse from: " + filename);
                    Warehouse warehouse = new Warehouse();
                    warehouse.loadFromFile(filename);
                    warehouse.printSummary(LocalDate.now());
                    return;
                }
            }


            //CASE 1: File does not exist or no filename provided
            //Check for creating
            System.out.println("There is no file to load data from, building warehouse with 200 random packages...");
            Warehouse warehouse = new Warehouse();
            warehouse.generateRandomPackages(200);
            warehouse.saveToFile(filename);
            warehouse.printSummary(LocalDate.now());

            //CASE 2: File exists, load warehouse from disk
            //Check for loading from an existing file
            System.out.println("\n\nPART B: Loading warehouse from disk...");
            Warehouse loadedWarehouse = new Warehouse();
            loadedWarehouse.loadFromFile(filename);
            loadedWarehouse.printSummary(LocalDate.now());
        }
        catch (IOException e) {
            System.err.println("Error handling file operations: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

    }
}