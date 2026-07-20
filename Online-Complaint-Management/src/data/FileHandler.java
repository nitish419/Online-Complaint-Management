package data;
import model.Complaint;
import model.User;
import model.Enums;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private static final String DIR = "datastore/";
    private static final String USERS_FILE = DIR + "users.dat";
    private static final String COMPLAINTS_FILE = DIR + "complaints.dat";

    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Complaint> complaints = new ArrayList<>();

    public static void initialize() {
        new File(DIR).mkdirs();
        loadData();
        // Create default admin if no users exist
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", Enums.Role.ADMIN));
            saveData();
        }
    }

    public static void saveData() {
        try (ObjectOutputStream oosU = new ObjectOutputStream(new FileOutputStream(USERS_FILE));
             ObjectOutputStream oosC = new ObjectOutputStream(new FileOutputStream(COMPLAINTS_FILE))) {
            oosU.writeObject(users);
            oosC.writeObject(complaints);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        try (ObjectInputStream oisU = new ObjectInputStream(new FileInputStream(USERS_FILE));
             ObjectInputStream oisC = new ObjectInputStream(new FileInputStream(COMPLAINTS_FILE))) {
            users = (ArrayList<User>) oisU.readObject();
            complaints = (ArrayList<Complaint>) oisC.readObject();
        } catch (Exception e) { System.out.println("No existing data found. Starting fresh."); }
    }
}