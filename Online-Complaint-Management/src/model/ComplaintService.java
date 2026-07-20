package model;

import data.FileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintService {
    private static ComplaintService instance;
    private List<Complaint> complaints;

    private ComplaintService() {
        complaints = new ArrayList<>();
    }

    public static ComplaintService getInstance() {
        if (instance == null) {
            instance = new ComplaintService();
        }
        return instance;
    }

    public void loadComplaints() throws IOException, ClassNotFoundException {
        complaints = FileManager.loadComplaints();
        if (complaints == null) {
            complaints = new ArrayList<>();
        }
    }

    public List<Complaint> getAllComplaints() {
        return complaints;
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaints.stream()
                .filter(c -> c.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void registerNewComplaint(String title, String desc, ComplaintCategory category, ComplaintPriority priority, String userID) throws IOException {
        String newId = "CMP-" + System.currentTimeMillis();
        Complaint newComplaint = new Complaint(newId, title, desc, category, priority, userID);
        complaints.add(newComplaint);
        FileManager.saveComplaints(complaints);
    }
}