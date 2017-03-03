package com.example.elabelle.cp282final.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.elabelle.cp282final.R;
import com.example.elabelle.cp282final.utils.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by elabelle on 3/2/17.
 */

public class StorageHelper {

    public static boolean checkStorage() {
        boolean mExternalStorageAvailable;
        boolean mExternalStorageWriteable;
        String state = Environment.getExternalStorageState();

        switch (state) {
            case Environment.MEDIA_MOUNTED:
                // We can read and write the media
                mExternalStorageAvailable = mExternalStorageWriteable = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                // We can only read the media
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
                break;
            default:
                // Something else is wrong. It may be one of many other states, but
                // all we need
                // to know is we can neither read nor write
                mExternalStorageAvailable = mExternalStorageWriteable = false;
                break;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }


    public static String getStorageDir() {
        // return Environment.getExternalStorageDirectory() + File.separator +
        // Constants.TAG + File.separator;
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    }


    /**
     * Retrieves the folderwhere to store data to sync notes
     *
     * @param mContext
     * @return
     */
    public static File getDbSyncDir(Context mContext) {
        File extFilesDir = mContext.getExternalFilesDir(null);
        File dbSyncDir = new File(extFilesDir, Constants.APP_STORAGE_DIRECTORY_SB_SYNC);
        dbSyncDir.mkdirs();
        if (dbSyncDir.exists() && dbSyncDir.isDirectory()) {
            return dbSyncDir;
        } else {
            return null;
        }
    }


    public static boolean copyFile(File source, File destination) {
        try {
            return copyFile(new FileInputStream(source), new FileOutputStream(destination));
        } catch (FileNotFoundException e) {
            Log.e(Constants.TAG, "Error copying file", e);
            return false;
        }
    }


    /**
     * Generic file copy method
     *
     * @param is Input
     * @param os Output
     * @return True if copy is done, false otherwise
     */
    public static boolean copyFile(InputStream is, OutputStream os) {
        boolean res = false;
        byte[] data = new byte[1024];
        int len;
        try {
            while ((len = is.read(data)) > 0) {
                os.write(data, 0, len);
            }
            is.close();
            os.close();
            res = true;
        } catch (IOException e) {
            Log.e(Constants.TAG, "Error copying file", e);
        }
        return res;
    }


    public static boolean deleteExternalStoragePrivateFile(Context mContext, String name) {
        boolean res = false;

        // Checks for external storage availability
        if (!checkStorage()) {
            Toast.makeText(mContext, mContext.getString(R.string.storage_not_available), Toast.LENGTH_SHORT).show();
            return false;
        }

        File file = new File(mContext.getExternalFilesDir(null), name);
        file.delete();

        return true;
    }


    public static boolean delete(Context mContext, String name) {
        boolean res = false;

        // Checks for external storage availability
        if (!checkStorage()) {
            Toast.makeText(mContext, mContext.getString(R.string.storage_not_available), Toast.LENGTH_SHORT).show();
            return false;
        }

        File file = new File(name);
        if (file.isFile()) {
            res = file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                res = delete(mContext, file2.getAbsolutePath());
            }
            res = file.delete();
        }

        return res;
    }


    public static String getRealPathFromURI(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**
     * Create a path where we will place our private file on external
     *
     * @param //mContext
     * @param //uri
     * @return
     */
    public static File copyToBackupDir(File backupDir, File file) {

        // Checks for external storage availability
        if (!checkStorage()) {
            return null;
        }

        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        File destination = new File(backupDir, file.getName());

        try {
            copyFile(new FileInputStream(file), new FileOutputStream(destination));
        } catch (FileNotFoundException e) {
            Log.e(Constants.TAG, "Error copying file to backup", e);
            destination = null;
        }

        return destination;
    }


    public static File getCacheDir(Context mContext) {
        File dir = mContext.getExternalCacheDir();
        if (!dir.exists())
            dir.mkdirs();
        return dir;
    }


    public static File getExternalStoragePublicDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.TAG + File
                .separator);
        if (!dir.exists())
            dir.mkdirs();
        return dir;
    }


    public static File getBackupDir(String backupName) {
        File backupDir = new File(getExternalStoragePublicDir(), backupName);
        if (!backupDir.exists())
            backupDir.mkdirs();
        return backupDir;
    }


    public static File getSharedPreferencesFile(Context mContext) {
        File appData = mContext.getFilesDir().getParentFile();
        String packageName = mContext.getApplicationContext().getPackageName();
        return new File(appData
                + System.getProperty("file.separator")
                + "shared_prefs"
                + System.getProperty("file.separator")
                + packageName
                + "_preferences.xml");
    }


    /**
     * Returns a directory size in bytes
     */
    @SuppressWarnings("deprecation")
    public static long getSize(File directory) {
        StatFs statFs = new StatFs(directory.getAbsolutePath());
        long blockSize = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.getBlockSizeLong();
            } else {
                blockSize = statFs.getBlockSize();
            }
            // Can't understand why on some devices this fails
        } catch (NoSuchMethodError e) {
            Log.e(Constants.TAG, "Mysterious error", e);
        }
        return getSize(directory, blockSize);
    }


    private static long getSize(File directory, long blockSize) {
        File[] files = directory.listFiles();
        if (files != null) {

            // space used by directory itself
            long size = directory.length();

            for (File file : files) {
                if (file.isDirectory()) {
                    // space used by subdirectory
                    size += getSize(file, blockSize);
                } else {
                    // file size need to rounded up to full block sizes
                    // (not a perfect function, it adds additional block to 0 sized files
                    // and file who perfectly fill their blocks)
                    size += (file.length() / blockSize + 1) * blockSize;
                }
            }
            return size;
        } else {
            return 0;
        }
    }


    public static boolean copyDirectory(File sourceLocation, File targetLocation) {
        boolean res = true;

        // If target is a directory the method will be iterated
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                res = res && copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation,
                        children[i]));
            }

            // Otherwise a file copy will be performed
        } else {
            try {
                res = res && copyFile(new FileInputStream(sourceLocation), new FileOutputStream(targetLocation));
            } catch (FileNotFoundException e) {
                Log.e(Constants.TAG, "Error copying directory");
                res = false;
            }
        }
        return res;
    }


    /**
     * Retrieves uri mime-type using ContentResolver
     *
     * @param mContext
     * @param uri
     * @return
     */
    public static String getMimeType(Context mContext, Uri uri) {
        ContentResolver cR = mContext.getContentResolver();
        String mimeType = cR.getType(uri);
        if (mimeType == null) {
            mimeType = getMimeType(uri.toString());
        }
        return mimeType;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }


    /**
     * Retrieves uri mime-type between the ones managed by application
     *
     * @param mContext
     * @param uri
     * @return
     */
    public static String getMimeTypeInternal(Context mContext, Uri uri) {
        String mimeType = getMimeType(mContext, uri);
        mimeType = getMimeTypeInternal(mContext, mimeType);
        return mimeType;
    }


    /**
     * Retrieves mime-type between the ones managed by application from given string
     *
     * @param mContext
     * @param mimeType
     * @return
     */
    public static String getMimeTypeInternal(Context mContext, String mimeType) {
        if (mimeType != null) {
            if (mimeType.contains("image/")) {
                mimeType = Constants.MIME_TYPE_IMAGE;
            } else if (mimeType.contains("audio/")) {
                mimeType = Constants.MIME_TYPE_AUDIO;
            } else if (mimeType.contains("video/")) {
                mimeType = Constants.MIME_TYPE_VIDEO;
            } else {
                mimeType = Constants.MIME_TYPE_FILES;
            }
        }
        return mimeType;
    }





    /**
     * Retrieves a file from its web url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static File getFromHttp(String url, File file) throws IOException {
        URL imageUrl = new URL(url);
        // HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
        // conn.setConnectTimeout(30000);
        // conn.setReadTimeout(30000);
        // conn.setInstanceFollowRedirects(true);
        // InputStream is=conn.getInputStream();
        // OutputStream os = new FileOutputStream(f);
        // Utils.CopyStream(is, os);

        // File file = File.createTempFile("img", ".jpg");

        FileUtils.copyURLToFile(imageUrl, file);
        // os.close();
        return file;
    }
}
