
package model;

public class FinGoal {
    private String id;
    private String targetId;
    private double progress;
    private String status;

    public FinGoal() {}

    public FinGoal(String id, String targetId, double progress, String status) {
        this.id = id;
        this.targetId = targetId;
        this.progress = progress;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
