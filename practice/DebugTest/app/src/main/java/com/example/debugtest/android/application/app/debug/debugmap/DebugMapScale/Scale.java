package com.example.debugtest.android.application.app.debug.debugmap.DebugMapScale;

class Scale {

    private final String text;
    private final float length;

    Scale(String text, float length) {
        this.text = text;
        this.length = length;
    }

    public String text() {
        return text;
    }

    public float length() {
        return length;
    }
}