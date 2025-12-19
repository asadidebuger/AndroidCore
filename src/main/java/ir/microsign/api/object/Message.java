package ir.microsign.api.object;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

import ir.microsign.api.Utils;
import ir.microsign.api.view.MessageView;
import ir.microsign.dbhelper.Const;
import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.net.DownloadFile;
import ir.microsign.utility.Display;
import ir.microsign.utility.File;
import ir.microsign.utility.Text;

public class Message extends BaseObject {
    public String _id, message, ticket, date, file;
    public Boolean fromUser;
    public Long last_pdate;
    Attachment attachment;

    static class Attachment extends BaseObject {
        public String[] files;

        //   public String filename,mimetype,originalname,url;
//    public Integer size;
        static Attachment fromFile(String file) {
            Attachment attachment = new Attachment();
//attachment.files=file.split("[\\[\\],\"]+");
            try {
                JSONArray jsonArray = new JSONArray(file);
                attachment.files = new String[jsonArray.length()];
                for (int i = 0; i < attachment.files.length; i++) {
                    attachment.files[i] = jsonArray.getString(i);
                }
                return attachment;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
//        "file" : {
//            "filename" : "4w0S5LINhmHj1eU4.jpg",
//                    "size" : 11544,
//                    "mimetype" : "images/jpeg",
//                    "originalname" : "Folder.jpg",
//                    "url" : "/uploads/4w0S5LINhmHj1eU4.jpg"
//        }
        }
    }

    @Override
    public List<String> getExceptionFields() {
//    return Arrays.asList("attachment");
        List<String> list = super.getExceptionFields();
        list.add("attachment");
        return list;
    }

    public String getFileDescription() {
        return null;
//    if (getAttachment()==null)return null;
//    return getAttachment().originalname;
    }

//    @Override
//    public String[] getMapColumns() {
//        return new String[]
//    }

    //    @Override
//    public String[] getAllColumns() {
//        return super.getAllColumns();
//    }


    @Override
    public Class getViewClass() {
        return MessageView.class;
    }

    @Override
    public Const.fieldType getSQLFieldType(String name) {
        if (name.equals("_id")) return Const.fieldType.STRING_UNIQUE;
        return super.getFieldType(name);
    }

    public String getDate() {
        return Utils.getDate(date);
    }

    Attachment getAttachment() {
        if (Text.isNullOrEmpty(file)) return null;

        if (attachment == null) attachment = Attachment.fromFile(file);
        return attachment;
    }

    public void setImage(final LinearLayout llIFiles) {
        if (getAttachment() == null) return;

        for (String file : attachment.files) {
            String localPath = getFileLocalPath(llIFiles.getContext(), file);

            if (File.Exist(localPath)) {
                setFileUi(llIFiles, localPath);
                continue;
            }
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.downloadFile(getFileOnlinePath(file), localPath, 2024, new DownloadFile.OnDownloadListener() {
                @Override
                public boolean downloading(int downloaded, int total) {
                    return true;
                }

                @Override
                public void OnDownloadCompleted(boolean succeed, final String path) {
                    if (!succeed) return;
                    setFileUi(llIFiles, path);
                }
            });
        }
    }

    private void setFileUi(final LinearLayout llImage, final String file) {
        llImage.post(new Runnable() {
            public void run() {

                if (file.matches(".*\\.(bmp|png|gif|jpg|jpeg)$")) {
                    ImageView imageView = new ImageView(llImage.getContext());
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setMaxWidth((int) (Display.getWidth(llImage.getContext()) * .7));
                    imageView.setMaxHeight((int) (Display.getHeight(llImage.getContext()) * .6));
                    llImage.addView(imageView, -1, -1);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(getFileLocalPath(imageView.getContext(), file)));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            File.openFile(view.getContext(), file, view.getContext().getString(ir.microsign.R.string.txt_open_file_chooser_title));
                        }
                    });
                } else {
                    TextView button=new TextView(llImage.getContext());
                    button.setText(file);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        button.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,llImage.getContext().getResources().getDrawable(android.R.drawable.ic_menu_view),null);
                    }
//                    button.setCompoundDrawables(null,null,llImage.getContext().getResources().getDrawable(android.R.drawable.ic_menu_view),null);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            File.openFile(view.getContext(), file, view.getContext().getString(ir.microsign.R.string.txt_open_file_chooser_title));
                        }
                    });
                    llImage.addView(button, -1, -1);

                }

            }
        });
    }

    public String getFileLocalPath(Context context, String file) {
        if (getAttachment() == null) return null;
        return File.GetRoot(context) + "/tickets/" + File.GetFileName(file);

    }

    public String getFileOnlinePath(String file) {
        if (file.startsWith("http")) return file;
        return Utils.getUrl(Utils.getConnectionProtocol(), Utils.getServerAddress(), Utils.getServerPort(), file);

    }
}
