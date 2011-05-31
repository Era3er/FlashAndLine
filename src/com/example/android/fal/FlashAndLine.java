package com.example.android.fal;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

public class FlashAndLine extends Activity 
{
	private static final int COLOR_DARK = 0xCC000000;
	private static final int COLOR_LIGHT = 0xCCBFBFBF;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	
	private Camera camera;
	private boolean lightOn;
	private boolean previewOn;
	private View button;
	
	private void getCamera() 
	{
	    if (camera == null) 
	    {
	        camera = Camera.open(); 
	    }
	}
	
	public void toggleLight(View view) 
	{
	    if (lightOn) 
	    {
	      turnLightOff();
	    } 
	    else 
	    {
	      turnLightOn();
	    }
	}
	
	private void turnLightOn() 
	{
	    if (camera == null) 
	    {
	    	Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG);
	    	button.setBackgroundColor(COLOR_WHITE);
	    	return;
	    }
	    lightOn = true;
	    Parameters parameters = camera.getParameters();
	    if (parameters == null) 
	    {
	    	button.setBackgroundColor(COLOR_WHITE);
	    	return;
	    }
	    List<String> flashModes = parameters.getSupportedFlashModes();
	    if (flashModes == null) 
	    {
	    	button.setBackgroundColor(COLOR_WHITE);
	    	return;
	    }
	    String flashMode = parameters.getFlashMode();
	    if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) 
	    {
	    	if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) 
	    	{
	    		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    		camera.setParameters(parameters);
	    		button.setBackgroundColor(COLOR_LIGHT);
	    	} 
	    	else 
	    	{
	    		Toast.makeText(this, "Flash mode (torch) not supported", Toast.LENGTH_LONG);
	    		button.setBackgroundColor(COLOR_WHITE);
	    	}
	    }
	}
	
	private void turnLightOff() 
	{
		if (lightOn) 
		{
			button.setBackgroundColor(COLOR_DARK);
			lightOn = false;
			if (camera == null) 
			{
				return;
			}
			Parameters parameters = camera.getParameters();
			if (parameters == null) 
			{
				return;
			}
			List<String> flashModes = parameters.getSupportedFlashModes();
			String flashMode = parameters.getFlashMode();
			if (flashModes == null) 
			{
				return;
			}
			if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) 
			{
				if (flashModes.contains(Parameters.FLASH_MODE_OFF)) 
				{
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
				}
		    }
		}
	}
	
	private void startPreview() 
	{
		if (!previewOn && camera != null) 
		{
			camera.startPreview();
			previewOn = true;
		}
	}

	private void stopPreview() 
	{
	    if (previewOn && camera != null) 
	    {
	    	camera.stopPreview();
		    previewOn = false;
	    }
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = findViewById(R.id.flash);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) 
            {
                if (lightOn) 
                {
                	turnLightOff();
                }
                else 
                {
                	turnLightOn();
                }
            }
        });

    }
    
    @Override
    public void onStart() 
    {
    	super.onStart();
    	getCamera();
    	startPreview();
    	turnLightOn();
    }
    
    @Override
    public void onStop() 
    {
    	super.onStop();
    	if (camera != null) 
    	{
    		stopPreview();
    		camera.release();
    		camera = null;
    	}
    }
}