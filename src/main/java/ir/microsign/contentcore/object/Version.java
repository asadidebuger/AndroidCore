package ir.microsign.contentcore.object;

/**
 * Created by Mohammad on 6/15/14.
 */
public class Version extends BaseObject {
    public Integer autoid, id, version;

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Content) {
            Content content = (Content) o;
            if (id.equals(content._id)) {
                if (content.version < 0) {
                    if (content.isUserAccess()) return false;
                    else return true;
                }
                return this.version.equals(content.version);

            }
//			return ((||!()));
//			return (_id.equals(content._id)&&version.equals(content.version));
        }
        return false;
    }

    @Override
    public String getJsonArrayName() {
        return "version";
    }
}
