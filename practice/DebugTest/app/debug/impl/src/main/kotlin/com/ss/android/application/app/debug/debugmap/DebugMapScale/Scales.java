package com.ss.android.application.app.debug.debugmap.DebugMapScale;

class Scales {
    private final Scale top, bottom;

    Scales(Scale top, Scale bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public Scale top() {
        return top;
    }

    public Scale bottom() {
        return bottom;
    }
}
