/*
 ********** SVU **********
 ********** Barea_27786 **********
 *********** Camera Manager **********
 *********** Open and release Camera **********
 */

package com.svu.ar.guide;

import android.content.Context;
import android.hardware.Camera;

public class CameraManager {

	private Camera camera;

	public CameraManager(Context context) {
		this.camera = getCameraInstance();
	}

	public Camera getCamera() {

		return camera;
	}

	private void releaseCamera() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	public void onPause() {
		releaseCamera();
	}

	public void onResume() {
		if (camera == null) {
			camera = getCameraInstance();
		}
	}

	private static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {

		}

		return c;
	}
}
