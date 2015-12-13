package com.pietrantuono.testapp.round;
 
 
import android.graphics.Color;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;

import com.pietrantuono.progress.CustomProgress;
import com.pietrantuono.progress.RoundProgress;
import com.pietrantuono.testapp.MainActivity;
import com.robotium.solo.Solo;


public class MethodsTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Solo solo;
    private int progress;
    private RoundProgress customProgress;
    private Handler h;

    public MethodsTest() {
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

    public void testSetProgress() throws InterruptedException {
        customProgress= new RoundProgress(mainActivity);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400,400);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addContentView(customProgress, layoutParams);
                customProgress.setProgress(50);
                customProgress.setBackgroundColor(Color.CYAN);
            }
        });
       Thread.sleep(5*1000);
    }

} 