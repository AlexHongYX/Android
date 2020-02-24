package com.example.debugtest.android.application.app.debug.sharedpreference;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.ss.android.utils.GsonProvider;
import com.ss.android.framework.sharedpref.MultiProcessSharedPrefModel;
import com.ss.android.utils.kit.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yihuaqi on 1/25/17.
 */

public class DemoMultiProcessPrefModel extends MultiProcessSharedPrefModel {
    public static final String TAG = "DemoMultiProcessPrefModel";
    public static DemoMultiProcessPrefModel sInstance = new DemoMultiProcessPrefModel();

    @Override
    protected String getPrefName() {
        return "demo_multi_process_pref_model";
    }

    @Override
    protected int getMigrationVersion() {
        return 1;
    }

    @Override
    protected void onMigrate(int version) {
        Logger.d(TAG, "onMigrate: "+version);
    }

    public StringProperty mStringProperty = new StringProperty("string_test", "default");
    // Uncommenting this line will throw an runtime exception during checking duplicat key.
//    public StringProperty mString2Property = new StringProperty("string_test", "default");
    public IntProperty mIntProperty = new IntProperty("int_test", 0);
    public BooleanProperty mBooleanProperty = new BooleanProperty("bool_test", false);
    public LongProperty mLongProperty = new LongProperty("long_test", 1000L);
    public FloatProperty mFloatProperty = new FloatProperty("float_test",124f);
    public IntProperty mIntMultiProperty = new IntProperty("multi_process_test", 0);
    public ObjectProperty<List<String>> mListProperty = new ObjectProperty<>("list_property",
            null,
            new Singleton<TypeToken<List<String>>>() {
                @Override
                protected TypeToken<List<String>> create() {
                    return new TypeToken<List<String>>(){};
                }
            });
    public ObjectProperty<List<String>> mEmptyListProperty = new ObjectProperty<>("empty_list_property",
            null,
            new Singleton<TypeToken<List<String>>>() {
                @Override
                protected TypeToken<List<String>> create() {
                    return new TypeToken<List<String>>(){};
                }
            });
    Random r = new Random();
    public void test() {
        testListProperty();
    }

    public void testListProperty() {
        List<String> list1 = new ArrayList<>();
        list1.add("item1");
        list1.add("item2");
        list1.add("item3");
        List<String> list2 = null;
        mListProperty.setValue(list1);
        mEmptyListProperty.setValue(list2);
        assert mListProperty.getValue().size() == 3;
        mListProperty.setValue(null);
        assert mListProperty.getValue().size() == 3;
        mListProperty.setValue(new ArrayList<String>());
        assert mListProperty.getValue() == null;
        Logger.d(TAG, "testListProperty");
    }

    public void testListToString() {
        Gson gson = GsonProvider.getDefaultGson();
        JSONArray array = new JSONArray();
        array.put("item1");
        array.put("item2");
        array.put("item3");
        JSONObject object = new JSONObject();
        try {
            object.put("array1", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TestObject testObject = gson.fromJson(object.toString(), TestObject.class);
    }
    class TestObject {
        @SerializedName("array1")
        List<String> array1;
        @SerializedName("array2")
        List<String> array2;
    }



    public void testWrongBulk() {
        bulk(new BulkEdit() {
            @Override
            public void run(BulkContentValues bulkValues) {
                mBooleanProperty.setBulkValue(true, bulkValues);
//                DemoModel2.sInstance.mBooleanProperty.setBulkValue(true, bulkValues);
            }
        });
    }

    public void testDynamicProperty() {
        IntProperty mDynamicDefaultProperty = new IntProperty("dynamic_default", generateRandomInt());
        Logger.d(TAG, "Dynamic Property: "+mDynamicDefaultProperty.getValue());
    }

    private int generateRandomInt() {
        return r.nextInt(100);
    }

    public void testSameProcess() {
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++) {
            boolean bool = mBooleanProperty.getValue();
        }
        Logger.d(TAG, "testSameProcess cost time: "+(System.currentTimeMillis() - start)+"ms");
    }

    public void testBasic() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Logger.d(TAG, "testBasic: "+i);
            mBooleanProperty.setValue(true);
            mBooleanProperty.getValue();
            mStringProperty.setValue("yes");
            mStringProperty.getValue();
            mIntProperty.setValue(132);
            mIntProperty.getValue();
            mLongProperty.setValue(123123L);
            mLongProperty.getValue();
            mFloatProperty.setValue(12312.214F);
            mFloatProperty.getValue();
        }
        Logger.d(TAG, "testBasic cost time: "+(System.currentTimeMillis() - start)+"ms");
    }

    public void testMultiProcess() {
        Logger.d(TAG, "testMultiProcess Process: "+android.os.Process.myPid());
        Logger.d(TAG, "testMultiProcess start: "+mIntMultiProperty.getValue());
        int value = mIntMultiProperty.getValue()+1;
        mIntMultiProperty.setValue(value);
        Logger.d(TAG, "testMultiProcess end: "+mIntMultiProperty.getValue());
    }

    public void testBulk() {
        bulk(new BulkEdit() {
            @Override
            public void run(BulkContentValues values) {
                for(int i = 0; i < 1000; i++) {
                    mIntProperty.setBulkValue(i, values);
                }
            }
        });
        bulk(new BulkEdit() {
            @Override
            public void run(BulkContentValues values) {
                for(int i = 0; i < 1000; i++) {
                    mIntProperty.setBulkValue(i, values);
                }
            }
        });
    }

    public void testForNull() {
        mStringProperty.setValue("Test");
        Logger.d(TAG, "testForNull1: "+mStringProperty.getValue());
        // Note: passing null in setValue won't have any effect on the property.
        mStringProperty.setValue(null);
        Logger.d(TAG, "testForNull2: "+mStringProperty.getValue());
    }
}
