package com.ss.android.application.app.debug;

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings
import com.bytedance.i18n.claymore.ClaymoreServiceLoader
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.material.tabs.TabLayout
import com.ss.android.application.social.account.client.google.GoogleAccountClient
import com.ss.android.framework.page.ArticleAbsFragment

/**
 * Created by yihuaqi on 10/26/16.
 */
class DebugFragment(fm:FragmentManager) : ArticleAbsFragment() {

    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var mRootView: View? = null
    private val mSupportFragmentManager = fm
    private var mGoogleAccountClient: GoogleAccountClient? = null


    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.debug_fragment, container, false)
        val tabLayout:TabLayout? = mRootView?.findViewById(R.id.tab_layout)
        val viewPager:ViewPager? = mRootView?.findViewById(R.id.view_pager)

        // 添加部门
        addOther()
        addUgc()
        addTest()

        viewPager?.adapter = DebugFragmentPagerAdapter(mSupportFragmentManager,fragments, titles)
        tabLayout?.setupWithViewPager(viewPager)
        return mRootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            mGoogleAccountClient!!.onActivityResult(requestCode, resultCode, data, object : GoogleAccountClient.Callback {
                override fun onFinish(result: GoogleSignInResult) {
                    if (result.isSuccess) {
                        Log.d(GoogleAccountClient.TAG, result.signInAccount!!.email!! + " login in google")
                    } else {
                        Log.w(GoogleAccountClient.TAG, "Login Failed: " + result.status.toString())
                    }
                }

                override fun onCanceled() {

                }
            })
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            mGoogleAccountClient!!.stopAutoManage(activity)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }

    /**
     * 添加部门页面
     */
    private fun addDepartments(title:String,list:List<DebugDataModel>){
        val fragment = DebugStandardFragment(list)
        fragments.add(fragment)
        titles.add(title)
    }

    private fun addUgc(){
        // ugc
        val ugcList = InitFragment.addUgc()
        addDepartments("UGC",ugcList)
    }

    private fun addOther(){
        // other
        val otherList = InitFragment.addOther()
        addDepartments("Other",otherList)
    }

    private fun addTest(){
        val testList = mutableListOf<DebugDataModel>()
        testList.add(DebugCheckBoxModel("item1"){
            it.mCheckbox.setOnClickListener {
                Toast.makeText(context,"test1",Toast.LENGTH_SHORT).show()
            }
        })
        testList.add(DebugTextViewModel("item2"){
            it.mText.setOnClickListener {
                Toast.makeText(context,"test2",Toast.LENGTH_SHORT).show()
            }
        })
        testList.add(DebugEditTextModel("item3"){
            it.mEditText.setOnClickListener {
                Toast.makeText(context,"test3",Toast.LENGTH_SHORT).show()
            }
        })
        testList.add(DebugSpinnerModel("item4"){
            it.mText.setOnClickListener {
                Toast.makeText(context,"test4",Toast.LENGTH_SHORT).show()
            }
        })
        addDepartments("Test",testList)
    }
}
