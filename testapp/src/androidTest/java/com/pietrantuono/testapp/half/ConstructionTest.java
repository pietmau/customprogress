package com.pietrantuono.testapp.half;

import android.os.AsyncTask;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.pietrantuono.progress.HalfProgress;
import com.pietrantuono.testapp.MainActivity;
import com.pietrantuono.testapp.R;
import com.robotium.solo.Solo;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class ConstructionTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Solo solo;
    private int progress;
    private HalfProgress customProgress;
    private Handler h;
    private CountDownLatch countDownLatch;

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

    @SuppressWarnings("UnusedAssignment")
    public void testConstructor() {
        HalfProgress customProgress= new HalfProgress(mainActivity);
    }

    public void testConstructorFromXML() {
        LayoutInflater layoutInflater=mainActivity.getLayoutInflater();
        layoutInflater.inflate(R.layout.simple,null);
    }

    public void testSetProgress() throws InterruptedException {
        customProgress= new HalfProgress(mainActivity);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400,400);
        countDownLatch = new CountDownLatch(1);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addContentView(customProgress, layoutParams);
                new ProgressTask().execute();
            }
        });
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    private class ProgressTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= 100; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            customProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            countDownLatch.countDown();
        }
    }
} 