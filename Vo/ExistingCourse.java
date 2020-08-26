package capstion.fluseo.Vo;

public class ExistingCourse {

    //보강 당하면 기존 대상
    private String existName;
    private String existDate;
    private String exitstStartTime;
    private String exitstEndTime;

    public ExistingCourse() {
        super();
    }

    public String getExistName() {
        return existName;
    }

    public void setExistName(String existName) {
        this.existName = existName;
    }

    public String getExistDate() {
        return existDate;
    }

    public void setExistDate(String existDate) {
        this.existDate = existDate;
    }

    public String getExitstStartTime() {
        return exitstStartTime;
    }

    public void setExitstStartTime(String exitstStartTime) {
        this.exitstStartTime = exitstStartTime;
    }

    public String getExitstEndTime() {
        return exitstEndTime;
    }

    public void setExitstEndTime(String exitstEndTime) {
        this.exitstEndTime = exitstEndTime;
    }
}
