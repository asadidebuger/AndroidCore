package ir.microsign.utility;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.microsign.R;

/**
 * Created by Mohammad on 9/25/14.
 */
public class File {
    final protected static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    //	static  boolean rootExist=false;
    static String root = null;

    //	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
//	public static String bytesToHex(byte[] bytes) {
//		char[] hexChars = new char[bytes.length * 2];
//		for ( int j = 0; j < bytes.length; j++ ) {
//			int v = bytes[j] & 0xFF;
//			hexChars[j * 2] = hexArray[v >>> 4];
//			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//		}
//		return new String(hexChars);
//	}
    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static Object bytesToObject(byte[] yourBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    public static byte[] objectToBytes(Object target) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(target);
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }
    public static void writeToFile(String path, String data) {
         writeToFile(path,data,false);
    }
    public static void writeToFile(String path, String data,boolean append) {
        try {
            if (!append)Delete(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
public static void copyAssetFolder(Context context, String src,String dst) {
//    String root= dst;//= context.getDatabasePath("db").getParent();
    AppendDir(dst);
    ArrayList<String> fileList = getAssetFiles(context, src);
    for (String file : fileList)
    {

        if (pathIsDirectory(file)) {
//            AppendDir(dst + "/" + file);
            copyAssetFolder(context,src+"/"+file,dst+"/"+file);
        }else
        copyAssets(context.getAssets(), src + "/" + file, dst + "/" + file);}

}
    public static void copyAssetToDatabase(Context context, String dbFolderName) {
        String root = context.getDatabasePath("db").getParent();
//        new java.io.File(root).list()
        AppendDir(root);
        ArrayList<String> fileList = getAssetFiles(context, dbFolderName);
        for (String file : fileList)
            copyAssets(context.getAssets(), dbFolderName + "/" + file, root + "/" + file);

    }

    public static void copyDatabases(Context context) {
        java.io.File path = context.getDatabasePath("db");

        java.io.File root = new java.io.File(path.getParent());
        java.io.File[] dbs = root.listFiles();
        String dest = GetRoot(context) + "/dbs/";
        AppendDir(dest);
        for (java.io.File db : dbs)
            try {
                copyFile(new FileInputStream(db), new FileOutputStream(dest + db.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static ArrayList<String> getAssetFiles(Context context, String folder) {
        try {
            return new ArrayList<String>(Arrays.asList(context.getAssets().list(folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static String readFromFile(String path) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString + "\r\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    public static List<String> readFileLines(String path) {

        List<String> lines=new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    lines.add(receiveString);
                }

                inputStream.close();
//                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return lines;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void copyAssets(AssetManager assetManager, String assetFilePath, String filename) {
        InputStream in = null;
        OutputStream out = null;
//			String s = assetFilePath;
//			java.io.File f = new java.io.File();
        try {
            in = assetManager.open(assetFilePath);
            java.io.File outFile = new java.io.File(filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + assetFilePath, e);
        }
    }

    public static String GetFileName(String patch) {
        java.io.File f = new java.io.File(patch);
        return f.getName();
    }

    public static String GetFileDir(String patch) {
        java.io.File f = new java.io.File(patch);
        return f.getParent();
    }

    public static boolean pathIsFile(String path) {
        java.io.File f=new java.io.File(path);
        if (!f.exists()) {
      return   path.matches("^.*\\.[^.]{2,4}$");
//            int sub = path.lastIndexOf("/");
//            return sub < 0 ? path.contains(".") : path.substring(sub).contains(".");
        }

        return f.isFile();
//		return path.contains(".jpg");
    }

    public static boolean pathIsDirectory(String patch) {
        return !pathIsFile(patch);
    }

    public static boolean Exist(String patch) {
        return new java.io.File(patch).exists();
//		return f.exists();
    }
//	public static void GetFile() {
//
//	}

    public static boolean Delete(String patch) {
        if (Exist(patch)) return new java.io.File(patch).delete();
        return true;
//		return f.exists();
    }
public static void openFile(Context context,String filePath,String chooserTitle){
if (Text.isNullOrEmpty(filePath))return;

    MimeTypeMap myMime = MimeTypeMap.getSingleton();
    Intent newIntent = new Intent(Intent.ACTION_VIEW);
    int i;
    String mimeType = myMime.getMimeTypeFromExtension((i=filePath.lastIndexOf('.'))>-1?filePath.substring(i+1):filePath);


    Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".microsign", new java.io.File(filePath));



//    newIntent.setDataAndType(Uri.fromFile(new java.io.File(filePath)),mimeType);
    newIntent.setDataAndType(fileUri,mimeType);
    newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Intent intent = Intent.createChooser(newIntent, chooserTitle==null?context.getString(R.string.txt_open_file_chooser_title):chooserTitle);
    try {
        context.startActivity(intent);
    }catch (Exception ex){
        ex.printStackTrace();
    }

}
    public static boolean AppendDir(String patch) {
        String dir = pathIsFile(patch) ? GetFileDir(patch) : patch;
        java.io.File folder = new java.io.File(dir);
//        boolean success = true;
        if (!folder.exists()) {
            return folder.mkdirs();
        }
        return true;
    }

    public static boolean RecursiveDelete(String path) {
        java.io.File root = new java.io.File(path);
        if (!root.exists()) return false;
        if (root.isFile())
            return root.delete();
        if (root.isDirectory()) {
            String[] list = root.list();
            for (String file : list)
                RecursiveDelete(path + java.io.File.separatorChar + file);
            return root.delete();
        }
        return root.delete();
    }

//    public static String GetOldRoot2() {
//        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//
//        if (isSDPresent)
//            return Environment.getExternalStorageDirectory().toString() + "/." + activity.getContext().getPackageName();
//        return activity.getContext().getFilesDir().getPath();
//    }

    public static String GetRoot(Context context) {
        if (root != null)
            return root;
        root = GetRoot(context,true);
        if (!File.Exist(root)) File.AppendDir(root);
        return root;
    }
    public static String ConvertUrlToStoragePath(String url)
    {
        if (url==null)return null;
        try {
            url=url.replaceAll("(^https?:/*|^/+|^\\\\+)","");
            url=url.replace(":","_");
            url=url.replaceAll("(\\\\+|/+)",java.io.File.separator);
        }catch (Exception e){
            e.printStackTrace();
        }

//        url=url.replace(" ", java.io.File.separator);
        return url;
    }

    public static String GetRoot(Context context,boolean withSuffix) {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent)
            return Environment.getExternalStorageDirectory().toString() + (withSuffix ? GetRootSuffix(context) : "");
        return context.getFilesDir().getPath();
    }
    public static String GetRoot(Context context,String root , String suffix) {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            if (root == null) root = Environment.getExternalStorageDirectory().toString();
            if (suffix==null)suffix="";
            return root + suffix;

        }
//            return  + suffix;
        return context.getFilesDir().getPath();
    }
    public static String GetRoot(Context context,String root) {
        return GetRoot(context,root,"/microsign/" +context.getPackageName());
    }

//    public static String GetRoot(Context context,String root) {
////        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//
//        String r= root + "/microsign/." +context.getPackageName();
//        if (!File.Exist(r)) File.AppendDir(r);
//        return r;
//
//    }

    public static String GetRootSuffix(Context context) {
        return "/microsign/" + context.getPackageName();

    }

    public static String GetOldRoot(Context context) {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (isSDPresent)
            return Environment.getExternalStorageDirectory().toString() + "/" + context.getPackageName();
        return context.getFilesDir().getPath() + "/";
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void copyFromAssets(AssetManager assetManager, String assetFilePath, String destFilename) {

        String[] files = null;

//        for(String filename : files) {
        InputStream in = null;
        OutputStream out = null;
        String s = destFilename;
        java.io.File f = new java.io.File(s);
        try {
            out = new FileOutputStream(f);
            in = assetManager.open(assetFilePath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + s, e);
        }
    }

    void ExtractImages(AssetManager assetManager, String zipPath, String destPath) {

        Zip decompress = new Zip();
        InputStream fis = null;
        try {
            fis = assetManager.open(zipPath);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        decompress.Unzip(fis, destPath + "/");
    }
}
