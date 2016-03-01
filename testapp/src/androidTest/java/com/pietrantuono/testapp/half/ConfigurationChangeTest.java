package com.pietrantuono.testapp.half;
 
 
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import com.pietrantuono.progress.CustomProgress;
import com.pietrantuono.progress.HalfProgress;
import com.pietrantuono.testapp.EmptyActivity;
import com.pietrantuono.testapp.HalfActivity;
import com.pietrantuono.testapp.MainActivity;
import com.pietrantuono.testapp.R;
import com.robotium.solo.Solo;


public class ConfigurationChangeTest extends ActivityInstrumentationTestCase2<HalfActivity> {
    private HalfActivity mainActivity;
    private Solo solo;
    private int progress;
    private HalfProgress customProgress;
    private Handler h;

    public ConfigurationChangeTest() {
        super(HalfActivity.class);
    } 

    @Override 
    protected void setUp() throws Exception {
        progress=0;
        mainActivity =getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
    } 
    @Override 
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        progress=0;
    }

    public void testRotation() throws InterruptedException {
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(2 * 1000);
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(2 * 1000);
        mainActivity= (HalfActivity) solo.getCurrentActivity();
        View v = mainActivity.findViewById(R.id.foo);
        assertTrue(v.getVisibility() == View.VISIBLE);
    }

    public void testLitmus() throws InterruptedException {
        solo.setActivityOrientation(Solo.LANDSCAPE);
        View v = mainActivity.findViewById(R.id.foo);
        assertTrue(v.getVisibility() == View.VISIBLE);
    }
}