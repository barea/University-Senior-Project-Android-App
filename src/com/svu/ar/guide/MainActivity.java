/*
 ********** SVU **********
 ********** Barea_27786 **********
 *********** Main Activity **********
 *********** Open Camera preview to detect QR Code and Decode it **********
 */

package com.svu.ar.guide;

import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.svu.ar.guide.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;

public class MainActivity extends Activity {
	
	private CameraPreview mPreview;
    private CameraManager mCameraManager;
    private CaptureView captureview; 
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Display display = getWindowManager().getDefaultDisplay();
		captureview = (CaptureView)findViewById(R.id.capture_view);
		captureview.Update(display.getWidth(), display.getHeight());
		mCameraManager = new CameraManager(this);
		
		mPreview = new CameraPreview(this, mCameraManager.getCamera());
		mPreview.setArea(captureview.getCaptureLeft(), captureview.getCaptureTop(), captureview.getcaptureWidth(), display.getWidth());
		 
	    
		 FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	     preview.addView(mPreview);
	     
	     getActionBar().hide();		
		
	}
	
	
	 @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
        mCameraManager.onPause(); 
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCameraManager.onResume();
		mPreview.setCamera(mCameraManager.getCamera());
	}
	
	@Override
	public void onBackPressed() {
		 moveTaskToBack(true); 
	      MainActivity.this.finish();
        
	}
	
	/*
	 ********** SVU **********
	 ********** Barea_27786 **********
	 *********** Main Activity **********
	 *********** Detect QR Code and Decode it **********
	 */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		
		
		
		private SurfaceHolder mHolder;
		private Camera mCamera;
		private static final String TAG = "camera";
		private int mWidth, mHeight;
		private Context mContext;
		private MultiFormatReader mMultiFormatReader;
	    private AlertDialog mDialog;
	    private int mLeft, mTop, mAreaWidth, mAreaHeight;
	    private APIService obj;	    
	    
	    public CameraPreview(Context context, Camera camera)
	    {
	    	super(context);
	    	mCamera = camera;
	    	mContext = context;
	    	mHolder = getHolder();
	    	mHolder.addCallback(this);
	    	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    	 Parameters params = mCamera.getParameters();
	         
	         mWidth = 640;
	         mHeight = 480;
	         
	         params.setPreviewSize(mWidth, mHeight);
	         mCamera.setParameters(params);
	         
	         mMultiFormatReader = new MultiFormatReader();
	         
	         mDialog =  new AlertDialog.Builder(mContext).create();
	         
	    }
	    
	    @Override
	   	public void surfaceCreated(SurfaceHolder holder) {
	           try {
	               mCamera.setPreviewDisplay(holder);
	               mCamera.startPreview();
	           } catch (IOException e) {
	               Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	           }
	       }

	       @Override
	   	public void surfaceDestroyed(SurfaceHolder holder) {

	       }

	       @Override
	   	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

	           if (mHolder.getSurface() == null){
	             return;
	           }

	           try {
	               mCamera.stopPreview();
	               
	           } catch (Exception e){

	           }

	           try {
	               mCamera.setPreviewCallback(mPreviewCallback);
	               mCamera.setPreviewDisplay(mHolder);
	               mCamera.startPreview();

	           } catch (Exception e){
	               Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	           }
	       }
	       
	       public void setCamera(Camera camera) {
	       	mCamera = camera;
	       }
	       
	       public void onPause() {
	       	if (mCamera != null) {
	       		mCamera.setPreviewCallback(null);
	       		mCamera.stopPreview();
	       	}
	       }
	       	private Camera.PreviewCallback mPreviewCallback = new PreviewCallback()
	       	{
	       	 @Override
	         public void onPreviewFrame(byte[] data, Camera camera)
	       	 {
	       		 
	       	    //Crop   	        	
	        	LuminanceSource source = new PlanarYUVLuminanceSource(data, mWidth, mHeight, mLeft, mTop, mAreaWidth, mAreaHeight, false);
	           
	        	//Convert to binary Image
	        	BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
	              source));
	            Result result;
	          
	            try {
	            	//Decoding
					result = mMultiFormatReader.decode(bitmap, null);
					if (result != null) {
						String qrText = result.getText().toString();
						
						//decrypt url result
						String itemId = UrlDecode(qrText);
						
					//	if(url.startsWith("http://virtimg.somee.com/"))
						//{
						 //start new activity to open URL
						 Intent i = new Intent(mContext, ApiConnectActivity.class);
						 i.putExtra("itemId", itemId);
						 startActivity(i);
						 finish();					
						//}
						/*else
						{
							//alert Unsupported QR
							 mDialog.setTitle("Œÿ√");
				             mDialog.setMessage("Â–« «·—„“ €Ì— „œ⁄Ê„ Ê·« Ì— »ÿ »√Ì „⁄—Ê÷");
				             mDialog.show();	
						}*/
					}
					
					 
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
	        	           
	       	 }
	       	 
	       	};
	       	
	       	//Set crop parameters
	        public void setArea(int left, int top, int areaWidth, int width)
	        {
	        	//left top and areaWidth: red rectangle dimensions, width: display width.
	        	double ratio = width / mWidth;
	        	mLeft = (int) (left / (ratio + 1));
	        	mTop = (int) (top / (ratio + 1));
	            mAreaWidth = mWidth - mLeft * 2;
	        	mAreaHeight =  mHeight - mLeft * 2;
	        }
	        
	       
	        //Decrypt Function
			public String UrlDecode(String text)
	    	{
	        	Hashtable ta = new Hashtable();
	    		
	    		ta.put("N", "a");
	    		ta.put("1", "b");
	    		ta.put("0", "c");
	    		ta.put("O", "d");
	    		ta.put("2", "e");
	    		ta.put("4", "f");
	    		ta.put("T", "g");
	    		ta.put("R", "h");
	    		ta.put("B", "i");
	    		ta.put("3", "j");
	    		ta.put("A", "k");
	    		ta.put("/", "l");
	    		ta.put("E", "m");
	    		ta.put("C", "n");
	    		ta.put("F", "o");
	    		ta.put("5", "p");
	    		ta.put(":", "q");
	    		ta.put("U", "r");
	    		ta.put("X", "s");
	    		ta.put("6", "t");
	    		ta.put("D", "u");
	    		ta.put("Q", "v");
	    		ta.put("7", "w");
	    		ta.put("G", "x");
	    		ta.put("8", "y");
	    		ta.put("Y", "z");
	    		ta.put("9", "0");
	    		ta.put("L", "1");
	    		ta.put("K", "2");
	    		ta.put("H", "3");
	    		ta.put("V", "4");
	    		ta.put("$", "5");
	    		ta.put("J", "6");
	    		ta.put("M", "7");
	    		ta.put("P", "8");
	    		ta.put("Z", "9");
	    		ta.put("S", ":");
	    		ta.put("W", "/");
	    		ta.put("I", ".");
	    		
	    		String upcase = text.toUpperCase();
	    		String[] spl = new String[upcase.length()];
	    		String character;
	            String ciphertext = "";
	            
	            for(int i = 0; i < upcase.length() ; i++)
	            {
	            	character = upcase.substring(i,i+1);
	            	 spl[i] = character;
	            }
	            for(int x = 0; x < spl.length ; x++)
	            {
	            	 String curntChar = spl[x];
	            	 String result = (String)ta.get(curntChar).toString();
	            	ciphertext += result;
	            }
	           
	            String s = ciphertext;
	           
	        		return s;
	           
	            }       
	       
	}

}
