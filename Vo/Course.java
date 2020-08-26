package capstion.fluseo.Vo;

import android.os.Parcelable;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseName;
    private String courseStartTime1;
    private String courseEndTime1;
    private String courseDate1;
    private String courseStartTime2;
    private String courseEndTime2;
    private String courseDate2;
    private String coursePlace;
    private String courseProfessor;
    private Boolean check1Time=false;
    private Boolean check2Time=false;
    // C일경우 일반 과목 , E일결우 기존과목 , S일경우 보충 어짜피 클래스 다 다르게 만드는데 이게 필요한가? 뭐지
    private String courseType="C";


    public Course() {
        super();
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public Course(String courseName, String courseStartTime1, String courseEndTime1, String courseDate1, String courseStartTime2, String courseEndTime2, String courseDate2) {
        this.courseName = courseName;
        this.courseStartTime1 = courseStartTime1;
        this.courseEndTime1 = courseEndTime1;
        this.courseDate1 = courseDate1;
        this.courseStartTime2 = courseStartTime2;
        this.courseEndTime2 = courseEndTime2;
        this.courseDate2 = courseDate2;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStartTime1() {
        return courseStartTime1;
    }

    public void setCourseStartTime1(String courseStartTime1) {
        this.courseStartTime1 = courseStartTime1;
    }

    public String getCourseEndTime1() {
        return courseEndTime1;
    }

    public void setCourseEndTime1(String courseEndTime1) {
        this.courseEndTime1 = courseEndTime1;
    }

    public String getCourseDate1() {
        return courseDate1;
    }

    public void setCourseDate1(String courseDate1) {
        this.courseDate1 = courseDate1;
    }

    public String getCourseStartTime2() {
        return courseStartTime2;
    }

    public void setCourseStartTime2(String courseStartTime2) {
        this.courseStartTime2 = courseStartTime2;
    }

    public String getCourseEndTime2() {
        return courseEndTime2;
    }

    public void setCourseEndTime2(String courseEndTime2) {
        this.courseEndTime2 = courseEndTime2;
    }

    public String getCourseDate2() {
        return courseDate2;
    }

    public void setCourseDate2(String courseDate2) {
        this.courseDate2 = courseDate2;
    }

    public String getCoursePlace() {
        return coursePlace;
    }

    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    public String getCourseProfessor() {
        return courseProfessor;
    }

    public void setCourseProfessor(String courseProfessor) {
        this.courseProfessor = courseProfessor;
    }

    public Boolean getCheck1Time() {
        return check1Time;
    }

    public void setCheck1Time(Boolean check1Time) {
        this.check1Time = check1Time;
    }

    public Boolean getCheck2Time() {
        return check2Time;
    }

    public void setCheck2Time(Boolean check2Time) {
        this.check2Time = check2Time;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
