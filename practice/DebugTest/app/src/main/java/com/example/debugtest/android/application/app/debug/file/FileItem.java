package com.example.debugtest.android.application.app.debug.file;

import java.text.DecimalFormat;

public class FileItem {
    private String name;
    private long size;
    private int color;
    private int level;

    public FileItem(String name, long size, int color, int level) {
        this.name = name;
        this.size = size;
        this.color = color;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public String getStringSize() {
        return calculateSize(getSize());
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private String calculateSize(long size) {
        DecimalFormat format = new DecimalFormat("0.0");
        if (size < 1024L) return size + "B";
        if (size >= 1048576L) return format.format(size / 1048576D) + "MB";
        else return format.format(size / 1024D) + "KB";
    }
}
