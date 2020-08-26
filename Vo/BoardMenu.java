package capstion.fluseo.Vo;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class BoardMenu implements Serializable {

    private String boardName;
    private String boardContent;
    private Boolean mapsBool;
    private String boardDate;
    private String boardAuthor;
    private String boardPasswd;
    private String boardKey;
    private int boardHitCount;
    private Double xPosition;

    public Double getxPosition() {
        return xPosition;
    }

    public void setPosition(Double x,Double y){
        this.xPosition = x;
        this.yPosition = y;
    }

    public void setxPosition(Double xPosition) {
        this.xPosition = xPosition;
    }

    public Double getyPosition() {
        return yPosition;
    }

    public void setyPosition(Double yPosition) {
        this.yPosition = yPosition;
    }

    private Double yPosition;

    public BoardMenu() {
        super();
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public Boolean getMapsBool() {
        return mapsBool;
    }

    public void setMapsBool(Boolean mapsBool) {
        this.mapsBool = mapsBool;
    }

    public String getBoardDate() {
        return boardDate;
    }

    public void setBoardDate(String boardDate) {
        this.boardDate = boardDate;
    }

    public String getBoardAuthor() {
        return boardAuthor;
    }

    public void setBoardAuthor(String boardAuthor) {
        this.boardAuthor = boardAuthor;
    }

    public int getBoardHitCount() {
        return boardHitCount;
    }

    public void setBoardHitCount(int boardHitCount) {
        this.boardHitCount = boardHitCount;
    }

    public String getBoardPasswd() {
        return boardPasswd;
    }

    public void setBoardPasswd(String boardPasswd) {
        this.boardPasswd = boardPasswd;
    }

    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

}
