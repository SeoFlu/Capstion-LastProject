package capstion.fluseo.Vo;

public class NoticeList implements Comparable<NoticeList> {
    private String noticeNum;
    private String noticeTitle;
    private String noticeUser;
    private String URL;
    private boolean noticeFile;
    private String noticeDate;
    private String key;

    @Override
    public int compareTo(NoticeList notice) {
        return this.noticeNum.compareTo(notice.noticeNum);
    }

    public NoticeList() {

    }

    public NoticeList(String noticeNum, String noticeTitle, String noticeUser, String noticeDate,String URL) {
        this.noticeNum = noticeNum;
        this.noticeTitle = noticeTitle;
        this.noticeUser = noticeUser;
        this.URL = URL;

        this.noticeDate = noticeDate;

    }

    public NoticeList(String noticeNum, String noticeTitle, String noticeUser, String noticeDate) {
        this.noticeNum = noticeNum;
        this.noticeTitle = noticeTitle;
        this.noticeUser = noticeUser;
        this.noticeDate = noticeDate;
        this.key=null;
    }

    public NoticeList(String noticeNum, String noticeTitle, String noticeUser, boolean noticeFile, String noticeDate) {
        this.noticeNum = noticeNum;
        this.noticeTitle = noticeTitle;
        this.noticeUser = noticeUser;
        this.noticeFile = noticeFile;
        this.noticeDate = noticeDate;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(String noticeNum) {
        this.noticeNum = noticeNum;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser;
    }

    public boolean isNoticeFile() {
        return noticeFile;
    }

    public void setNoticeFile(boolean noticeFile) {
        this.noticeFile = noticeFile;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "NoticeList{" +
                "noticeNum='" + noticeNum + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeUser='" + noticeUser + '\'' +
                ", noticeFile=" + noticeFile +
                ", noticeDate=" + noticeDate +
                '}';
    }


}
