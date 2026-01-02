
package model;

public class FinGoal {
    private String id;
    private String targetId;
    private double progress;
    private String status;
    private String targetName; 
    private String userId; // Added
    private String userName; // Added

    public FinGoal() {}

    public FinGoal(String id, String targetId, double progress, String status) {
        this.id = id;
        this.targetId = targetId;
        this.progress = progress;
        this.status = status;
    }

    // Constructor with details
    public FinGoal(String id, String targetId, String targetName, String userId, String userName, double progress, String status) {
        this.id = id;
        this.targetId = targetId;
        this.targetName = targetName;
        this.userId = userId;
        this.userName = userName;
        this.progress = progress;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
