package com.ellactron.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ji.wang on 2017-07-13.
 */

public class PrivateStorage {
    private Context context;
    private String storagePath;

    PrivateStorage(Context context, String storagePath) {
        this.context = context;
        this.storagePath = storagePath;
    }

    public void save(byte[] data) throws IOException {
        save(this.context, data, storagePath);
    }

    public void append(byte[] data) throws IOException {
        append(this.context, data, storagePath);
    }

    protected synchronized static void save(Context context,
                                            byte[] data,
                                            String storagePath) throws IOException {
        save(context, data, storagePath, false);
    }

    protected synchronized static void append(Context context,
                                              byte[] data,
                                              String storagePath) throws IOException {
        save(context, data, storagePath, true);
    }

    protected synchronized static void save(Context context,
                                            byte[] data,
                                            String storagePath,
                                            boolean append) throws IOException {
        FileOutputStream fos;
        if (append) {
            fos = context.openFileOutput(storagePath, MODE_PRIVATE | MODE_APPEND);
        } else {
            fos = context.openFileOutput(storagePath, MODE_PRIVATE);
        }

        Log.d(PrivateStorage.class.getCanonicalName(), getAbsoluteFilePath(context, storagePath) + " is created.");

        fos.write(data, 0, data.length);
        fos.flush();
        fos.close();
    }

    public byte[] read() throws IOException {
        return read(context, storagePath, -1, 0);
    }

    public byte[] read(int length) throws IOException {
        return read(context, storagePath, length, 0);
    }

    public byte[] read(int length,
                       int offset) throws IOException {
        return read(context, storagePath, length, offset);
    }

    protected synchronized static byte[] read(Context context,
                                              String storagePath,
                                              int length) throws IOException {
        return read(context, storagePath, length, 0);
    }

    protected synchronized static byte[] read(Context context,
                                              String storagePath,
                                              int length,
                                              int offset) throws IOException {
        if (-1 == length) {
            length = (int) (new File(getAbsoluteFilePath(context, storagePath))).length();
        }

        byte[] buff = new byte[length];

        FileInputStream fis;
        fis = context.openFileInput(storagePath);
        fis.skip(offset);
        fis.read(buff, 0, length);
        fis.close();
        return buff;
    }

    public void delete() {
        delete(context, storagePath);
    }

    protected static void delete(Context context,
                                 String storagePath) {
        //new File(storagePath).delete();
        context.deleteFile(storagePath);
    }

    public boolean isExisting() {
        return isExisting(context, storagePath);
    }

    protected static boolean isExisting(Context context,
                                        String storagePath) {
        File file = new File(getAbsoluteFilePath(context, storagePath));
        return file.exists();
    }

    private static String getAbsoluteFilePath(Context context,
                                              String storagePath) {
        return context.getFilesDir()
                + (storagePath.startsWith("/") ? storagePath : "/" + storagePath);
    }
}
