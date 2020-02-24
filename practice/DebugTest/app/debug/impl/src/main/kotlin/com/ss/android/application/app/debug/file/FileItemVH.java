package com.ss.android.application.app.debug.file;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.i18n.business.debug.R;

public class FileItemVH extends RecyclerView.ViewHolder {

    private TextView textView;

    public FileItemVH(@NonNull View itemView) {
        super(itemView);
    }

    public void bindData(FileItem item) {
        textView = itemView.findViewById(R.id.file_item);

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(0, 5, 0, 0);
        textView.setLayoutParams(lp);

        if (item.getLevel() == 0) {
            textView.setText(item.getName());
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            String levelShow = "";
            for (int i = 1; i <= item.getLevel(); i++) {
                levelShow += "- ";
            }
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textView.setText(levelShow + item.getName() + "(" + item.getStringSize() + ")");
        }

        if (item.getColor() == 1) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLACK);
        }

        int modifyLevel = item.getLevel();
        if (modifyLevel > 3) {
            modifyLevel = 3;
        }
        textView.setTextSize(22 - 2 * modifyLevel);
    }
}
