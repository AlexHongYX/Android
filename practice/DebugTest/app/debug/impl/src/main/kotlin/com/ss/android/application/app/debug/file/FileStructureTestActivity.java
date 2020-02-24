package com.ss.android.application.app.debug.file;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.bytedance.i18n.business.debug.R;
import com.ss.android.buzz.view.VerticalRecView;
import com.ss.android.framework.page.slideback.AbsSlideBackActivity;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.drakeet.multitype.MultiTypeAdapter;

public class FileStructureTestActivity extends AbsSlideBackActivity {

    private EditText filterEt;
    private RadioGroup radioGroup;
    private CheckBox hideFile;
    private Button search;
    private VerticalRecView verticalRecView;

    private int itemLevel = 0;
    private int checkedButton = 1;
    private int hideFileShow = 0;

    private MultiTypeAdapter adapter = new MultiTypeAdapter();
    private List<FileItem> fileItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_file_structure);
        init();
        initView();

        adapter.register(FileItem.class, new FileItemBinder());
        verticalRecView.setAdapter(adapter);

        filterSize(Long.MAX_VALUE);
    }

    private void filterSize(long minSize) {
        fileItemList = new ArrayList<>();
        showData(minSize);
        showSdCard(minSize);
        adapter.setItems(fileItemList);
        adapter.notifyDataSetChanged();
    }

    private void showSdCard(long minSize) {
        String sdCardLocation = "";
        try {
            sdCardLocation = getApplicationContext().getExternalFilesDir(null).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sdCardData = sdCardLocation.split("/files")[0];

        addFileItem(new File(sdCardData), minSize, itemLevel + 1);
        fileItemList.add(0, new FileItem(sdCardData, 0, 0, itemLevel));
    }

    private void showData(long minSize) {
        String dataLocation = "";
        try {
            dataLocation = getApplicationContext().getFilesDir().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = dataLocation.split("/files")[0];

        addFileItem(new File(data), minSize, itemLevel + 1);
        fileItemList.add(0, new FileItem(data, 0, 0, itemLevel));
    }

    /**
     * 递归由底层开始计算文件大小, 可选择是否需要计算隐藏的文件
     */
    @NotNull
    private long addFileItem(File file, long minSize, int level) {
        long sumSize = 0;
        File[] files = file.listFiles();
        List<FileItem> fileList = new ArrayList<>();
        for (File f : files) {
            if (hideFileShow == 0 && f.getName().startsWith(".")) continue;

            if (f.isDirectory()) {
                long subSumSize = addFileItem(f, minSize, level + 1);
                int color = subSumSize < minSize ? 0 : 1;
                fileItemList.add(0, new FileItem(f.getName(), subSumSize, color, level));
            } else {
                int color = f.length() < minSize ? 0 : 1;
                fileList.add(new FileItem(f.getName(), f.length(), color, level));
            }
        }
        for (FileItem item : fileList) {
            fileItemList.add(0, item);
        }
        for (int i = 0; i < fileItemList.size(); i++) {
            FileItem item = fileItemList.get(i);
            if (item.getLevel() < level) break;
            if (item.getLevel() == level) {
                sumSize += fileItemList.get(i).getSize();
            }
        }
        return sumSize;
    }

    private void initView() {
        filterEt = findViewById(R.id.size_filter_et);
        search = findViewById(R.id.filter_begin);
        radioGroup = findViewById(R.id.size_radio_group);
        hideFile = findViewById(R.id.hide_file_show);
        verticalRecView = findViewById(R.id.file_recycler_view);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.kb_btn) {
                checkedButton = 1;
            } else if (checkedId == R.id.mb_btn) {
                checkedButton = 2;
            }
        });

        hideFile.setOnCheckedChangeListener((buttonView, isChecked) -> hideFileShow = isChecked ? 1 : 0);

        search.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(filterEt.getText().toString())) {
                int size = Integer.valueOf(filterEt.getText().toString());
                long finalSize = size * (checkedButton == 1 ? 1024 : 1024 * 1024);
                filterSize(finalSize);
            } else {
                filterSize(Long.MAX_VALUE);
            }
        });
    }
}
