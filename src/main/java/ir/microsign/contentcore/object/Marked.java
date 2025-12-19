package ir.microsign.contentcore.object;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Marked extends BaseObject {
    public Integer autoid, marked, learned;
    public String address,id;

//	@Override
//	public String getJsonArrayName() {
//		return "order";
//	}

    public boolean isMarked() {
        return marked != null && marked > 0;
    }

    public boolean isLearned() {
        return learned != null && learned > 0;
    }
}
