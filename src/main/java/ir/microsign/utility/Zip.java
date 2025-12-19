package ir.microsign.utility;

import android.util.Log;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 10/10/13
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class Zip {
    InputStream mSourceFileStream = null;
    public Zip(){}
    public Zip(InputStream zipInputStream){mSourceFileStream=zipInputStream;}
    public void Unzip(String zipFilePath, String extractPath) {
        Unzip(zipFilePath, extractPath, 1024 * 10);

    }

    public static void UnzipAll(String zipFilePath, String extractPath,String password) {

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            if(zipFile.isEncrypted()){
                zipFile.setPassword(password.toCharArray());
            }

        zipFile.extractAll(extractPath);
        } catch (ZipException e) {
        e.printStackTrace();
    }
    }

    public static String UnzipOne(String zipFilePath, String extractPath,String newFileName,String password,int fileIndex) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            if(zipFile.isEncrypted()){
                zipFile.setPassword(password.toCharArray());
            }

//            zipFile.extractFile((FileHeader) zipFile.getFileHeaders().get(fileIndex),extractPath,null,newFileName);
            zipFile.extractFile(zipFile.getFileHeaders().get(fileIndex),extractPath,newFileName);
            return extractPath+"/"+newFileName;
        } catch (Exception e) {
           Log.e("Zip",e.getMessage());
        }
        return null;
    }
    public void Unzip(InputStream zipFileStream, String extractPath) {
        mSourceFileStream = zipFileStream;
        Unzip("", extractPath, 1024 * 10);

    }
    public void Unzip(String zipFilePath, String extractPath, int bufferSize) {
        Unzip(zipFilePath, extractPath, bufferSize,null);
    }
    public void Unzip(String zipFilePath, String extractPath, int bufferSize,OnExtractListener l) {
        try {
            dirChecker(extractPath, "");
            if (mSourceFileStream == null) mSourceFileStream = new FileInputStream(zipFilePath);
            ZipInputStream zin = new ZipInputStream(mSourceFileStream);


            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                boolean succeed=true;
                Log.v("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    File.Delete(extractPath + ze.getName());
                    dirChecker(extractPath, ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(extractPath + ze.getName());
                    byte b[] = new byte[bufferSize];
                    int n;
                    int totalRead=0;
                    while ((n = zin.read(b, 0, bufferSize)) > 0) {
                        fout.write(b, 0, n);
                        totalRead+=n;
                        if (l!=null&&(!l.extracting(totalRead))){succeed =false;break;}
                    }

                    zin.closeEntry();
                    fout.close();

                }

            }
            zin.close();
            if(l!=null)l.finished(true);
        } catch (Exception e) {
            if(l!=null)l.finished(false);
            Log.e("Decompress", "unzip", e);
        }

    }

    private void dirChecker(String root, String dir) {
        java.io.File f = new java.io.File(root + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public interface OnExtractListener{
        boolean extracting(int extracted);
        void finished(boolean succeed);

    }
}
