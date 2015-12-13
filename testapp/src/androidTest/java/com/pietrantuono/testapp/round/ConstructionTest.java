package com.pietrantuono.testapp.round;
 
 
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pietrantuono.progress.CustomProgress;
import com.pietrantuono.progress.RoundProgress;
import com.pietrantuono.testapp.MainActivity;
import com.pietrantuono.testapp.R;
import com.robotium.solo.Solo;


public class ConstructionTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Solo solo;
    private int progress;
    private RoundProgress customProgress;
    private Handler h;

    public ConstructionTest() {
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

    public void testConstructor() {
        RoundProgress customProgress= new RoundProgress(mainActivity);
    }

    public void testConstructorFromXML() {
        LayoutInflater layoutInflater=mainActivity.getLayoutInflater();
        layoutInflater.inflate(R.layout.simple,null);
    }
    @UiThreadTest
    public void testSetProgress() throws InterruptedException {
        customProgress= new RoundProgress(mainActivity);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400,400);
        mainActivity.addContentView(customProgress,layoutParams);
        h =new Handler(mainActivity.getMainLooper());
        h.postDelayed(new ProgressRunnable(), 10);
        solo.sleep(1* 1000+20);
    }



    private class ProgressRunnable implements Runnable{
        @Override
        public void run() {
            if(progress>100)return;
            customProgress.setProgress(progress);
            progress++;
            h.postDelayed(new ProgressRunnable(),10);
        }
    }
 
} 