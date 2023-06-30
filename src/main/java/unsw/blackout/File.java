package unsw.blackout;

public class File {
    private String filename;
    private String content;
    private int completedSize;
    private String direction; // SEND or RECV
    private BaseEntity relatedBaseEntity; // file send to or recv from which communication point
    private String status;

    public File(String filename, String content, int completedSize, String direction, BaseEntity relatedBase) {
        this.filename = filename;
        this.content = content;
        this.completedSize = completedSize;
        this.direction = direction;
        this.relatedBaseEntity = relatedBase;
    }

    public File(String filename, String content, int completedSize) {
        this.filename = filename;
        this.content = content;
        this.completedSize = completedSize;
        this.direction = null;
        this.relatedBaseEntity = null;
    }

    public void setCompletedSize(int completedSize) {
        this.completedSize = completedSize;
    }

    public int getCompletedSize() {
        return completedSize;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BaseEntity getRelatedBaseEntity() {
        return relatedBaseEntity;
    }

    public void setRelatedBaseEntity(BaseEntity relatedBaseEntity) {
        this.relatedBaseEntity = relatedBaseEntity;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return this.getContent().substring(0, this.getCompletedSize());
    }

    public int getFileSize() {
        return this.getContent().length();
    }

    public boolean getIsFileComplete() {
        return (this.getCompletedSize() == this.getFileSize());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
