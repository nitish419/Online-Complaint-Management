package data;

import model.Complaint;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DIR = "datastore/";
    private static final String COMPLAINTS_FILE = DIR + "complaints.dat";

    public static void initializeFiles() throws IOException {
        File directory = new File(DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(COMPLAINTS_FILE);
        if (!file.exists()) {
            saveComplaints(new ArrayList<>());
        }
    }

    public static void saveComplaints(List<Complaint> complaints) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMPLAINTS_FILE))) {
            oos.writeObject(complaints);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Complaint> loadComplaints() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COMPLAINTS_FILE))) {
            return (List<Complaint>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }
}