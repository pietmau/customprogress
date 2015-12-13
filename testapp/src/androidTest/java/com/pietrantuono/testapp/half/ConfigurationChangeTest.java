package com.pietrantuono.testapp.half;
 
 
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;

import com.pietrantuono.progress.CustomProgress;
import com.pietrantuono.progress.HalfProgress;
import com.pietrantuono.testapp.MainActivity;
import com.robotium.solo.Solo;


public class ConfigurationChangeTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Solo solo;
    private int progress;
    private HalfProgress customProgress;
    private Handler h;

    public ConfigurationChangeTest() {
        super(MainActivity.class); 
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
        customProgress= new HalfProgress(mainActivity);
        customProgress.setProgress(50);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400,400);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addContentView(customProgress, layoutParams);
            }
        });
        Thread.sleep(2*1000);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertEquals(customProgress.getProgress(), 50);
    }

    public void testLitmus() throws InterruptedException {
        customProgress= new HalfProgress(mainActivity);
        customProgress.setProgress(50);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400,400);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addContentView(customProgress, layoutParams);
            }
        });
        Thread.sleep(2 * 1000);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertFalse(customProgress.getProgress()== 20);
    }
}