package unsw.blackout;

public class File {
    private String filename;
    private String content;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    // public void setFilename(String filename) {
    //     this.filename = filename;
    // }

    // public void setContent(String content) {
    //     this.content = content;
    // }

    @Override
    public String toString() {
        return "File [filename=" + filename + ", content=" + content + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        File other = (File) obj;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        return true;
    }

    public String getData() {
        return content;
    }

    public int getFileSize() {
        int size = ((int) this.getContent().length());
        return size;
    }

    public boolean getIsFileComplete() {
        return true;
    }


}
