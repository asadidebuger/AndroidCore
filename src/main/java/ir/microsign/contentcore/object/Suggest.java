package ir.microsign.contentcore.object;

/**
 * Created by Mohammad on 30/04/2016.
 */
public class Suggest extends BaseObject {
    public String title;
    public Integer autoid;
public Suggest(){

}public Suggest(String title){
    this.title=title;
}
    @Override
    public String getOrderColumnName() {
        return "autoid";
    }

    @Override
    public boolean getReverseOrder() {
        return true;
    }

    @Override
    public String toString() {
        return title;
    }
}
