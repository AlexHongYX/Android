package com.ss.android.application.app.debug;

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.bytedance.i18n.business.debug.R
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.material.tabs.TabLayout
import com.ss.android.application.social.account.client.google.GoogleAccountClient
import com.ss.android.framework.page.ArticleAbsFragment

/**
 * Created by yihuaqi on 10/26/16.
 */
class DebugFragment(fm:FragmentManager) : ArticleAbsFragment() {


    private var mRootView: View? = null
    private val mSupportFragmentManager = fm
    private var mGoogleAccountClient: GoogleAccountClient? = null
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.debug_fragment, container, false)
        val tabLayout:TabLayout? = mRootView?.findViewById(R.id.tab_layout)
        val viewPager:ViewPager? = mRootView?.findViewById(R.id.view_pager)

        // 添加部门
        addUgc()
        addOther()
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
    private fun addDepartments(title:String,adapter:DebugStandardAdapter){
        val fragment = DebugStandardFragment(adapter)
        fragments.add(fragment)
        titles.add(title)
    }

    private fun addUgc(){
        // ugc
        val ugcList = mutableListOf<DebugDataModel>()
        ugcList.add(DebugDataModel("Force Jump To Post", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Music", ViewType.TextView))
        ugcList.add(DebugDataModel("UGC Entrance Type(-1,0,1,2)", ViewType.EditText))
        ugcList.add(DebugDataModel("ForceQuickUpload", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Enable UgcChallenge", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Enable Test Effect", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Use UGC Preload", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Use default mv preload config(first check Use UGC Preload)", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Use Old Ugc Tools", ViewType.CheckBox))
        ugcList.add(DebugDataModel("UGC disable image compression", ViewType.CheckBox))
        ugcList.add(DebugDataModel("UGC use new Luban compute size", ViewType.CheckBox))
        ugcList.add(DebugDataModel("Force show MV", ViewType.CheckBox))
        val ugcAdapter = UgcAdapter(ugcList,context)
        addDepartments("UGC",ugcAdapter)
    }

    private fun addOther(){
        // other
        val otherList = mutableListOf<DebugDataModel>()
        otherList.add(DebugDataModel("Disable Configurable Channel", ViewType.CheckBox))
        otherList.add(DebugDataModel("Disable Stream Preload", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Backup DNS", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Stream Protobuf", ViewType.CheckBox))
        otherList.add(DebugDataModel("enable multi language", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Image Translation", ViewType.CheckBox))
        otherList.add(DebugDataModel("mock android id last three numbers", ViewType.EditText))
        otherList.add(DebugDataModel("Little App Test", ViewType.TextView))
        otherList.add(DebugDataModel("QR Code H5 Jumper", ViewType.TextView))
        otherList.add(DebugDataModel("bind paytm", ViewType.TextView))
        otherList.add(DebugDataModel("TEST ANR", ViewType.TextView))
        otherList.add(DebugDataModel("TEST OOM", ViewType.TextView))
        otherList.add(DebugDataModel("TEST Finalize Timeout", ViewType.TextView))
        otherList.add(DebugDataModel("preview_url", ViewType.TextView))
        otherList.add(DebugDataModel("enable live permission", ViewType.CheckBox))
        otherList.add(DebugDataModel("enable x2c", ViewType.CheckBox))
        otherList.add(DebugDataModel("enable async inflate", ViewType.CheckBox))
        otherList.add(DebugDataModel("ImmortalMediaDownload", ViewType.TextView))
        otherList.add(DebugDataModel("Show live guide dialog", ViewType.CheckBox))
        otherList.add(DebugDataModel("Debug Live H5", ViewType.TextView))
        otherList.add(DebugDataModel("show live debug info", ViewType.CheckBox))
        otherList.add(DebugDataModel("watch_live", ViewType.EditText))
        otherList.add(DebugDataModel("Debug VE Video Editor", ViewType.TextView))
        otherList.add(DebugDataModel("SuperCatMan", ViewType.TextView))
        otherList.add(DebugDataModel("double_gap", ViewType.EditText))
        otherList.add(DebugDataModel("Enable Image Request Debug", ViewType.CheckBox))
        otherList.add(DebugDataModel("EnableNewImmersiveVideoCard", ViewType.CheckBox))
        otherList.add(DebugDataModel("CrazyVideoDownload", ViewType.CheckBox))
        otherList.add(DebugDataModel("EnableMainFeedVideoCard", ViewType.CheckBox))
        otherList.add(DebugDataModel("EnableNewMediaViewer", ViewType.CheckBox))
        otherList.add(DebugDataModel("FeedBack", ViewType.TextView))
        otherList.add(DebugDataModel("UETool", ViewType.TextView))
        otherList.add(DebugDataModel("Language Dialog Style", ViewType.EditText))
        otherList.add(DebugDataModel("choose city", ViewType.Spinner))
        otherList.add(DebugDataModel("start BDLocation Test", ViewType.TextView))
        otherList.add(DebugDataModel("set longitude", ViewType.EditText))
        otherList.add(DebugDataModel("set latitude", ViewType.EditText))
        otherList.add(DebugDataModel("Nearby Style", ViewType.Spinner))
        otherList.add(DebugDataModel("Enable Translation Tool", ViewType.TextView))
        otherList.add(DebugDataModel("Use new login style", ViewType.CheckBox))
        otherList.add(DebugDataModel("Video Use MediaLoader To PreLoad", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Video Preload Radical", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Video Preload", ViewType.CheckBox))
        otherList.add(DebugDataModel("TTVideo Use TTNet", ViewType.CheckBox))
        otherList.add(DebugDataModel("ImageLoader Use TTNet", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always login as new user", ViewType.CheckBox))
        otherList.add(DebugDataModel("Disable AppLog Encryption", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Screen Shot for BuzzShare", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Debug Console", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Article Web Type(Amp, Server TransCode or Original Web)", ViewType.CheckBox))
        otherList.add(DebugDataModel("Fabric Debug", ViewType.TextView))
        otherList.add(DebugDataModel("Auto Clean Delay 5-10s", ViewType.CheckBox))
        otherList.add(DebugDataModel("In App Billing", ViewType.TextView))
        otherList.add(DebugDataModel("Repost Debug", ViewType.TextView))
        otherList.add(DebugDataModel("Migrate Nr Saved Article", ViewType.TextView))
        otherList.add(DebugDataModel("Wake Up Times", ViewType.TextView))
        otherList.add(DebugDataModel("Short Cut Badge", ViewType.EditText))
        otherList.add(DebugDataModel("Async Event", ViewType.EditText))
        otherList.add(DebugDataModel("Google Login", ViewType.CheckBox))
        otherList.add(DebugDataModel("Use Http", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Native Ads Priority", ViewType.CheckBox))
        otherList.add(DebugDataModel("Native Ads Priority", ViewType.TextView))
        otherList.add(DebugDataModel("Enable Interstitial Ads Priority", ViewType.CheckBox))
        otherList.add(DebugDataModel("Interstitial Ads Priority", ViewType.TextView))
        otherList.add(DebugDataModel("Show Ad Provider Id", ViewType.CheckBox))
        otherList.add(DebugDataModel("Ads Preload Status", ViewType.TextView))
        otherList.add(DebugDataModel("Ad Style", ViewType.TextView))
        otherList.add(DebugDataModel("App Usage Summ", ViewType.TextView))
        otherList.add(DebugDataModel("Enable FPS Meter", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always show login popup", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always get app settings", ViewType.CheckBox))
        otherList.add(DebugDataModel("Force Switch Session", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always send sample http log", ViewType.CheckBox))
        otherList.add(DebugDataModel("Fix Ok Http Proxy issue", ViewType.CheckBox))
        otherList.add(DebugDataModel("Apply MD Design on TabLayout", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always show video tab tip", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always show bottom tab refresh tip", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always jump to detail comment section", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always Show Video Error", ViewType.CheckBox))
        otherList.add(DebugDataModel("Always show pull to refresh guide", ViewType.CheckBox))
        otherList.add(DebugDataModel("Use SurfaceView", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Used MediaPlayer", ViewType.CheckBox))
        otherList.add(DebugDataModel("Use async MediaPlayer", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Video Bitrate Info", ViewType.CheckBox))
        otherList.add(DebugDataModel("MediaPlayer Type Switch", ViewType.CheckBox))
        otherList.add(DebugDataModel("MediaPlayer Type", ViewType.TextView))
        otherList.add(DebugDataModel("Test App Current Active Event", ViewType.CheckBox))
        otherList.add(DebugDataModel("Local Push Enable Local Time Test", ViewType.CheckBox))
        otherList.add(DebugDataModel("Don't Bind Ad", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Two Lines Relative News", ViewType.CheckBox))
        otherList.add(DebugDataModel("Test Preference Property", ViewType.EditText))
        otherList.add(DebugDataModel("Update App Config", ViewType.EditText))
        otherList.add(DebugDataModel("Try NetChannelSelect", ViewType.EditText))
        otherList.add(DebugDataModel("Gcm Debug", ViewType.TextView))
        otherList.add(DebugDataModel("Dynamics Debug", ViewType.TextView))
        otherList.add(DebugDataModel("TTNet Debug", ViewType.TextView))
        otherList.add(DebugDataModel("Show AddtoDebug", ViewType.CheckBox))
        otherList.add(DebugDataModel("Push Detail Back Strategy", ViewType.EditText))
        otherList.add(DebugDataModel("Show Feed Time", ViewType.CheckBox))
        otherList.add(DebugDataModel("Swip To Next Page", ViewType.CheckBox))
        otherList.add(DebugDataModel("Swip To Related Page", ViewType.CheckBox))
        otherList.add(DebugDataModel("NetGetNetwork", ViewType.TextView))
        otherList.add(DebugDataModel("NetGetStream", ViewType.TextView))
        otherList.add(DebugDataModel("NetSearchSuggestion", ViewType.EditText))
        otherList.add(DebugDataModel("Net SDK Version", ViewType.EditText))
        otherList.add(DebugDataModel("TTNet Cronet Enable", ViewType.CheckBox))
        otherList.add(DebugDataModel("Trigger native crash", ViewType.EditText))
        otherList.add(DebugDataModel("Collect native crash", ViewType.EditText))
        otherList.add(DebugDataModel("Degree Youtube Leech", ViewType.CheckBox))
        otherList.add(DebugDataModel("Reset User", ViewType.CheckBox))
        otherList.add(DebugDataModel("Http1 Only", ViewType.CheckBox))
        otherList.add(DebugDataModel("Get app log config", ViewType.TextView))
        otherList.add(DebugDataModel("Block Time", ViewType.EditText))
        otherList.add(DebugDataModel("Start Search", ViewType.TextView))
        otherList.add(DebugDataModel("Community Debug", ViewType.TextView))
        otherList.add(DebugDataModel("Always show buzz intro", ViewType.CheckBox))
        otherList.add(DebugDataModel("Show Invitation style", ViewType.CheckBox))
        otherList.add(DebugDataModel("Separator", ViewType.Separator))
        otherList.add(DebugDataModel("Demand Test", ViewType.TextView))
        otherList.add(DebugDataModel("Buzz always select language", ViewType.CheckBox))
        otherList.add(DebugDataModel("Router Manager", ViewType.EditText))
        otherList.add(DebugDataModel("ScriptUpdate", ViewType.EditText))
        otherList.add(DebugDataModel("Enable immersive", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable ImageView Debug", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable ImageView Reuse", ViewType.CheckBox))
        otherList.add(DebugDataModel("immersive video vertical", ViewType.CheckBox))
        otherList.add(DebugDataModel("Enable Cricket Always Pull", ViewType.CheckBox))
        otherList.add(DebugDataModel("enter IM setting page", ViewType.TextView))
        otherList.add(DebugDataModel("InnerCode-0619-21:30", ViewType.TextView))
        otherList.add(DebugDataModel("Print Apk Inject Info", ViewType.TextView))
        otherList.add(DebugDataModel("Print Apk file structure", ViewType.TextView))
        otherList.add(DebugDataModel("Use Voice Search", ViewType.CheckBox))
        otherList.add(DebugDataModel("Debug with google map", ViewType.TextView))
        otherList.add(DebugDataModel("Preload Original Image Threshold", ViewType.Spinner))
        val otherAdapter = OtherAdapter(otherList,context)
        addDepartments("Other",otherAdapter)
    }

    private fun addTest(){
        val testList = mutableListOf<DebugDataModel>()
        testList.add(DebugDataModel("item1", ViewType.CheckBox))
        testList.add(DebugDataModel("item2", ViewType.TextView))
        testList.add(DebugDataModel("item3", ViewType.EditText))
        testList.add(DebugDataModel("item4", ViewType.Spinner))
        addDepartments("Test",object: DebugStandardAdapter(testList,context){
            override fun bindCheckBoxViewHolder(holder: CheckBoxViewHolder, item: DebugDataModel) {
                when (item.text){
                    "item1"->{
                        // TODO
                        holder.mCheckbox.setOnClickListener {
                            Toast.makeText(context,"test1",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun bindTextViewViewHolder(holder: TextViewViewHolder, item: DebugDataModel) {
                when (item.text){
                    "item2"->{
                        // TODO
                        holder.mText.setOnClickListener {
                            Toast.makeText(context,"test2",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun bindEditTextViewHolder(holder: EditTextViewHolder, item: DebugDataModel) {
                when (item.text){
                    "item3"->{
                        // TODO
                        holder.mTestButton.setOnClickListener {
                            Toast.makeText(context,"test3",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun bindSpinnerViewHolder(holder: SpinnerViewHolder, item: DebugDataModel) {
                when (item.text){
                    "item4"->{
                        // TODO
                        holder.mText.setOnClickListener {
                            Toast.makeText(context,"test4",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
