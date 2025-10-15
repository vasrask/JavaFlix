import java.io.Serializable;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class Episode implements Serializable {
    private String Title;
    private int No;
    private int Duration;

    public Episode(String title, int no, int duration){
        this.Title = title;
        this.No = no;
        this.Duration = duration;
    }
    public Episode(){
        this.Title = "";
        this.No = 0;
        this.Duration = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode that = (Episode) o;
        return Title.equals(that.Title) &&
                Duration == that.Duration &&
                No == that.No;

    }
}
