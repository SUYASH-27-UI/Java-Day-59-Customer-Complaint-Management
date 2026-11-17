
import java.io.*;
import java.util.*;

class Complaint {
    int id;
    String customerName;
    String category;
    String description;
    String status;

    Complaint(int id, String customerName,
              String category, String description,
              String status) {
        this.id = id;
        this.customerName = customerName;
        this.category = category;
        this.description = description;
        this.status = status;
    }

    void display() {
        System.out.println("\nComplaint ID: " + id);
        System.out.println("Customer: " + customerName);
        System.out.println("Category: " + category);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
    }
}

public class Main {
    static ArrayList<Complaint> complaints = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "complaints.txt";

    static void loadComplaints() {
        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", 5);

                if (data.length == 5) {
                    complaints.add(new Complaint(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        data[3],
                        data[4]
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            // First run: file will be created when saving.
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading complaint records.");
        }
    }

    static void saveComplaints() {
        try (PrintWriter writer =
                 new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Complaint c : complaints) {
                writer.println(c.id + "|" +
                    c.customerName + "|" +
                    c.category + "|" +
                    c.description + "|" +
                    c.status);
            }

        } catch (IOException e) {
            System.out.println("Error saving records.");
        }
    }

    static void addComplaint() {
        int id = 1;

        for (Complaint c : complaints) {
            if (c.id >= id)
                id = c.id + 1;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        if (name.isBlank() || category.isBlank() ||
            description.isBlank() ||
            name.contains("|") || category.contains("|") ||
            description.contains("|")) {
            System.out.println("Invalid or empty input!");
            return;
        }

        complaints.add(new Complaint(
            id, name, category, description, "Pending"
        ));

        saveComplaints();
        System.out.println("Complaint registered! ID: " + id);
    }

    static void viewComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }

        for (Complaint c : complaints) {
            c.display();
        }
    }

    static void searchComplaint() {
        System.out.print("Enter complaint ID: ");
        int id = readNumber();

        for (Complaint c : complaints) {
            if (c.id == id) {
                c.display();
                return;
            }
        }

        System.out.println("Complaint not found.");
    }

    static void updateStatus() {
        System.out.print("Enter complaint ID: ");
        int id = readNumber();

        for (Complaint c : complaints) {
            if (c.id == id) {
                System.out.println("1. Pending");
                System.out.println("2. In Progress");
                System.out.println("3. Resolved");
                System.out.print("Choose new status: ");

                int choice = readNumber();

                switch (choice) {
                    case 1:
                        c.status = "Pending";
                        break;
                    case 2:
                        c.status = "In Progress";
                        break;
                    case 3:
                        c.status = "Resolved";
                        break;
                    default:
                        System.out.println("Invalid status choice!");
                        return;
                }

                saveComplaints();
                System.out.println("Status updated successfully!");
                return;
            }
        }

        System.out.println("Complaint not found.");
    }

    static int readNumber() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void showSummary() {
        int pending = 0;
        int inProgress = 0;
        int resolved = 0;

        for (Complaint c : complaints) {
            switch (c.status) {
                case "Pending":
                    pending++;
                    break;
                case "In Progress":
                    inProgress++;
                    break;
                case "Resolved":
                    resolved++;
                    break;
            }
        }

        System.out.println("\n===== COMPLAINT SUMMARY =====");
        System.out.println("Total: " + complaints.size());
        System.out.println("Pending: " + pending);
        System.out.println("In Progress: " + inProgress);
        System.out.println("Resolved: " + resolved);
    }

    public static void main(String[] args) {
        loadComplaints();

        int choice;

        do {
            System.out.println("\n===== CUSTOMER COMPLAINT SYSTEM =====");
            System.out.println("1. Register Complaint");
            System.out.println("2. View Complaints");
            System.out.println("3. Search Complaint");
            System.out.println("4. Update Complaint Status");
            System.out.println("5. Complaint Summary");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = readNumber();

            switch (choice) {
                case 1:
                    addComplaint();
                    break;
                case 2:
                    viewComplaints();
                    break;
                case 3:
                    searchComplaint();
                    break;
                case 4:
                    updateStatus();
                    break;
                case 5:
                    showSummary();
                    break;
                case 6:
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }
}
