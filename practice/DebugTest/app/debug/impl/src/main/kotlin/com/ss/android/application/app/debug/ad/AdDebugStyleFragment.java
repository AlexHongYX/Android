package com.ss.android.application.app.debug.ad;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.i18n.business.debug.R;
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings;
import com.bytedance.i18n.claymore.ClaymoreServiceLoader;
import com.ss.android.framework.page.ArticleAbsFragment;

/**
 * Created by chengkai on 2017/7/5.
 */

public class AdDebugStyleFragment extends ArticleAbsFragment {
    private RadioGroup mGroupStyle;
    private RadioGroup mGroupSubStyle;
    private RadioGroup mGroupV38Style;

    private SparseIntArray styleArr = new SparseIntArray();
    private SparseIntArray subStyleArr = new SparseIntArray();
    private SparseIntArray v38StyleArr = new SparseIntArray();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debug_ad_style_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        styleArr.append(0, R.id.style_auto);
        styleArr.append(1, R.id.style_small);
        styleArr.append(2, R.id.style_large);

        subStyleArr.append(0, R.id.substyle_auto);
        subStyleArr.append(1, R.id.substyle_1);
        subStyleArr.append(2, R.id.substyle_2);
        subStyleArr.append(3, R.id.substyle_3);
        subStyleArr.append(4, R.id.substyle_4);
        subStyleArr.append(5, R.id.substyle_5);
        subStyleArr.append(6, R.id.substyle_6);
        subStyleArr.append(7, R.id.substyle_7);
        subStyleArr.append(8, R.id.substyle_8);
        subStyleArr.append(9, R.id.substyle_9);
        subStyleArr.append(10, R.id.substyle_10);
        subStyleArr.append(11, R.id.substyle_11);
        subStyleArr.append(12, R.id.substyle_12);

        v38StyleArr.append(0, R.id.v38_style_auto);
        v38StyleArr.append(1, R.id.force_v38_style);
        v38StyleArr.append(2, R.id.force_v37_style);

        mGroupStyle = (RadioGroup) view.findViewById(R.id.group_style);
        mGroupSubStyle = (RadioGroup) view.findViewById(R.id.group_substyle);
        mGroupV38Style = (RadioGroup) view.findViewById(R.id.v38_style);

        int style = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getAdStyle();
        if (style < 0 || style > 2) {
            style = 0;
        }
        int res = styleArr.get(style);
        mGroupStyle.check(res);

        int subStyle = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getAdSubStyle();
        if (subStyle < 0 || subStyle > 12) {
            subStyle = 0;
        }
        int res2 = subStyleArr.get(subStyle);
        mGroupSubStyle.check(res2);

        int v38Style = ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).getV38Style();
        if (v38Style < 0 || v38Style > 2) {
            v38Style = 0;
        }
        int res3 = v38StyleArr.get(v38Style);
        mGroupV38Style.check(res3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int index1 = styleArr.indexOfValue(mGroupStyle.getCheckedRadioButtonId());
        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAdStyle(index1 >= 0 ? index1 : 0);
        int index2 = subStyleArr.indexOfValue(mGroupSubStyle.getCheckedRadioButtonId());
        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setAdSubStyle(index2 >= 0 ? index2 : 0);
        int index3 = v38StyleArr.indexOfValue(mGroupV38Style.getCheckedRadioButtonId());
        ClaymoreServiceLoader.loadFirstOrNull(IDebugSettings.class).setV38Style(index3 >= 0 ? index3 : 0);

    }

}
