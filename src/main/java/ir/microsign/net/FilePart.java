package ir.microsign.net;

import java.io.File;

import ir.microsign.utility.Text;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Mohammad on 07/12/2017.
 */

public class FilePart {
//    public final static MediaType MediaTypePNG
    String name,fileName;
    MediaType type;
    File file;

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public MediaType getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public FilePart(String partName, String fileName, String filePath, String mediaType){
        name=partName;

        type=MediaType.parse(mediaType);
        file=new File(filePath);
        this.fileName= Text.isNullOrEmpty(fileName)?file.getName():fileName;
    }
    public FilePart(String partName, String filePath, String mediaType){
        name=partName;
        type=MediaType.parse(mediaType);
        file=new File(filePath);
        this.fileName= file.getName();
    }
    public RequestBody getFileRequestBody(){
        return RequestBody.create(type,file);
    }
}
