package ir.microsign.contentcore.object;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Ordering extends BaseObject {
    public Integer autoid, id, ordering;

    @Override
    public String getJsonArrayName() {
        return "order";
    }
}
