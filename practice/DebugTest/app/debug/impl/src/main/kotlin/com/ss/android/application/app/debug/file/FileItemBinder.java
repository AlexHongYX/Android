package com.ss.android.application.app.debug.file;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.bytedance.i18n.business.debug.R;

import me.drakeet.multitype.ItemViewBinder;

public class FileItemBinder extends ItemViewBinder<FileItem, FileItemVH> {
    @NonNull
    @Override
    protected FileItemVH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View v = inflater.inflate(R.layout.activity_debug_file_item, parent, false);
        return new FileItemVH(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull FileItemVH fileItemVH, @NonNull FileItem fileItem) {
        fileItemVH.bindData(fileItem);
    }
}
