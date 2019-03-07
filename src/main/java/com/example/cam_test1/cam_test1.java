package com.example.cam_test1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.content.res.ResourcesCompat;
import android.os.CountDownTimer;
import android.os.Build;

public class cam_test1 extends AppCompatActivity implements CvCameraViewListener2, OnFragmentInteractionListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private ViewPager mCameraScopePager;

    private boolean isWidgetsLayoutHidden = false;

    private ImageButton btnZoom = null;
    private final CountDownTimer timerZoomButton = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long l) {
            if (btnZoom!=null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    btnZoom.setAlpha(0.5f + 0.1f*l);
                }
            }
        }

        @Override
        public void onFinish() {
            if (btnZoom != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    btnZoom.setAlpha(0.4f);
                }
            }
        }
    };

 // properties specific for Camera block
   private CameraView mCameraView;
   private int mDisplayWidth;
   private int mDisplayHeight;
   private Mat mCameraBufInput;
   private Mat mCameraBufOutput;

   private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	    @Override
	    public void onManagerConnected(int status) {
	        switch (status) {
	            case LoaderCallbackInterface.SUCCESS:
	            	flashMessage("OpenCV loaded successfully");
	                if (null != mCameraView)
	                mCameraView.enableView();
	                System.loadLibrary("cam_test1");
	                if (!BgThread.isAlive())
	                	BgThread.start();
                   break;
	            default:
	                super.onManagerConnected(status);
                   break;
	        }
	    }
	};

    private void registerCamera() {
	    mCameraView = (CameraView) findViewById(R.id.surface_view);
	    mCameraView.setVisibility(SurfaceView.VISIBLE);
	    mCameraView.setCvCameraViewListener(this);
    }

    private void resumeCamera() {
        if (!OpenCVLoader.initDebug()) {
            Log.d("resumeCamera", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("resumeCamera", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }
    }

    private void pauseCamera() {
        if (mCameraView != null)
            mCameraView.disableView();
	 }

    public void onCameraViewStarted(int width, int height) {
        mCameraBufInput = new Mat(height, width, CvType.CV_8UC4);
        mCameraBufOutput = new Mat(height, width, CvType.CV_8UC4);
	 }

    public void onCameraViewStopped() {
    	mCameraBufInput.release();
    	mCameraBufOutput.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	mCameraBufInput = inputFrame.rgba();
    	return mCameraBufOutput;
	 }

    public int initCamera(double sampleTime, int location, int width, int height) {
      	try {
    		mCameraView.check(location, width, height);
            return 1;
    	} catch (CameraView.SetupException ex) {
      		mCameraView.showAlert(ex.mDescription);
            return 0;
    	}
	 }

    public int initVideoDisplay(int width, int height) {
        mDisplayWidth = width;
        mDisplayHeight = height;
        return 1;
    }

    public long getCameraInputBuffer() {
       return (mCameraBufInput != null ? mCameraBufInput.getNativeObjAddr() : 0);
    }

	 public long getCameraOutputBuffer() {
		return (mCameraBufOutput != null ? mCameraBufOutput.getNativeObjAddr() : 0);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Uncomment the following line to specify a custom App Title
        //toolbar.setTitle("My custom Title");
        setSupportActionBar(toolbar);

        // Create a FragmentPagerAdapter that returns individual fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(SectionsPagerAdapter.getNumTabs()-1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        thisClass = this;
     }

    private cam_test1 thisClass;
    private final Thread BgThread = new Thread() {
    @Override
    public void run() {
            String argv[] = new String[] {"MainActivity","cam_test1"};
            naMain(argv, thisClass);
        }
    };

    public void flashMessage(final String inMessage) {
        runOnUiThread(new Runnable() {
              public void run() {
                    Toast.makeText(getBaseContext(), inMessage, Toast.LENGTH_SHORT).show();
              }
        });
    }

    protected void onDestroy() {
         naOnAppStateChange(6);
         super.onDestroy();
         System.exit(0); //to kill all our threads.
    }

	@Override
    public void onFragmentCreate(String name) {

    }

    @Override
    public void onFragmentStart(String name) {
        switch (name) {
            case "Info":
               break;
            case "App":
                if (mCameraScopePager == null) {
                    registerCameraScopeLayout();
                }
                break;
            case "dot1":
        if (mCameraView == null) {
            registerCamera();
                }
                break;
            default:
                break;
    }
    }

    @Override
    public void onFragmentResume(String name) {
        switch (name) {
            case "App":
                break;
           case "dot1":
           resumeCamera();
               break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentPause(String name) {
           if (name.equalsIgnoreCase("dot1"))
           pauseCamera();
    }
    @Override
    protected void onResume() {
         super.onResume();
         if (BgThread.isAlive())
             naOnAppStateChange(3);
    }

    @Override
    protected void onPause() {
        if (BgThread.isAlive())
            naOnAppStateChange(4);
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void registerCameraScopeLayout() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        CameraScopeSectionsPagerAdapter mCameraScopeAdapter = new CameraScopeSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mCameraScopePager = (ViewPager) findViewById(R.id.cameraScopeContainer);
        mCameraScopePager.setOffscreenPageLimit(1);
        mCameraScopePager.setAdapter(mCameraScopeAdapter);

        TabLayout dotsLayout = (TabLayout) findViewById(R.id.dots);
        dotsLayout.setupWithViewPager(mCameraScopePager);

        ImageButton btnZoom = (ImageButton) findViewById(R.id.btnZoom);
        btnZoom.setVisibility(View.INVISIBLE);
    }
    private native int naMain(String[] argv, cam_test1 pThis);
    private native void naOnAppStateChange(int state);
}
