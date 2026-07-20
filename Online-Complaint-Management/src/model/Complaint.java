package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Complaint implements Serializable {
    private String complaintID;
    private String userID;
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;
    private ComplaintStatus status;
    private String createdDate;

    public Complaint(String complaintID, String title, String description, ComplaintCategory category, ComplaintPriority priority, String userID) {
        this.complaintID = complaintID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.userID = userID;
        this.status = ComplaintStatus.OPEN;
        this.createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getComplaintID() { return complaintID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public ComplaintCategory getCategory() { return category; }
    public ComplaintPriority getPriority() { return priority; }
    public ComplaintStatus getStatus() { return status; }
    public String getCreatedDate() { return createdDate; }

    public void setStatus(ComplaintStatus status) { this.status = status; }
}