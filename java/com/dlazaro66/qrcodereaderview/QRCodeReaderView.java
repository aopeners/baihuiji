package com.dlazaro66.qrcodereaderview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.open.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/*
 * Copyright 2014 David Lázaro Esparcia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * QRCodeReaderView - Class which uses ZXING lib and let you easily integrate a QR decoder view.
 * Take some classes and made some modifications in the original ZXING - Barcode Scanner project.
 *
 * @author David Lázaro
 */
public class QRCodeReaderView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    public interface OnQRCodeReadListener {

        public void onQRCodeRead(String text, PointF[] points);

        public void cameraNotFound();

        public void QRCodeNotFoundOnCamImage();
    }

    private OnQRCodeReadListener mOnQRCodeReadListener;

    private static final String TAG = QRCodeReaderView.class.getName();

    private QRCodeReader mQRCodeReader;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private SurfaceHolder mHolder;
    private CameraManager mCameraManager;

    public QRCodeReaderView(Context context) {
        super(context);
        if (checkCameraHardware(getContext())) {
            new MyAsy().execute(context);
        } else {
            Log.e(TAG, "Error: Camera not found");
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.cameraNotFound();
            }
        }
        // init();
    }

    public QRCodeReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (checkCameraHardware(getContext())) {
            new MyAsy().execute(context);
        } else {
            Log.e(TAG, "Error: Camera not found");
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.cameraNotFound();
            }
        }
        //  init();
    }

    public void setOnQRCodeReadListener(OnQRCodeReadListener onQRCodeReadListener) {
        mOnQRCodeReadListener = onQRCodeReadListener;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    @SuppressWarnings("deprecation")
    private void init() {
        if (checkCameraHardware(getContext())) {
            mCameraManager = new CameraManager(getContext());
            if (mCameraManager == null) {
                mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            }
            //new HandlerThread();
            mHolder = this.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  // Need to set this flag despite it's deprecated
        } else {
            Log.e(TAG, "Error: Camera not found");
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.cameraNotFound();
            }
        }
    }


    /****************************************************
     * SurfaceHolder.Callback,Camera.PreviewCallback
     * 视图建立时调用
     ****************************************************/
    SurfaceHolder holder;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // Indicate camera, our View dimensions
            mCameraManager.openDriver(holder, this.getWidth(), this.getHeight());
        } catch (Exception e) {
            Log.w(TAG, "Can not openDriver: " + e.getMessage());
            mCameraManager.closeDriver();
        }

        try {
            mQRCodeReader = new QRCodeReader();
            mCameraManager.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            mCameraManager.closeDriver();
        }
    }

    //视图销毁时自动调用
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.holder = holder;
        Log.d(TAG, "surfaceDestroyed");
        //切屏时回收相机
        if (mCameraManager != null) {
            mCameraManager.getCamera().setPreviewCallback(null);
            mCameraManager.getCamera().stopPreview();
            mCameraManager.getCamera().release();
            mCameraManager.closeDriver();
        }
    }

    // Called when camera take a frame
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        PlanarYUVLuminanceSource source = mCameraManager.buildLuminanceSource(data, mPreviewWidth, mPreviewHeight);

        HybridBinarizer hybBin = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(hybBin);

        try {
            Result result = mQRCodeReader.decode(bitmap);

            // Notify we found a QRCode
            if (mOnQRCodeReadListener != null) {
                // Transform resultPoints to View coordinates
                PointF[] transformedPoints = transformToViewCoordinates(result.getResultPoints());
                mOnQRCodeReadListener.onQRCodeRead(result.getText(), transformedPoints);
            }

        } catch (ChecksumException e) {
            Log.d(TAG, "ChecksumException");
            e.printStackTrace();
        } catch (NotFoundException e) {
            // Notify QR not found
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.QRCodeNotFoundOnCamImage();
            }
        } catch (FormatException e) {
            Log.d(TAG, "FormatException");
            e.printStackTrace();
        } finally {
            mQRCodeReader.reset();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");

        if (mHolder.getSurface() == null) {
            Log.e(TAG, "Error: preview surface does not exist");
            return;
        }

        //preview_width = width;
        //preview_height = height;
        try {
            if (mCameraManager != null) {
                mPreviewWidth = mCameraManager.getPreviewSize().x;
                mPreviewHeight = mCameraManager.getPreviewSize().y;
                mCameraManager.stopPreview();
                mCameraManager.getCamera().setPreviewCallback(this);
                mCameraManager.getCamera().setDisplayOrientation(90); // Portrait mode

                // Fix the camera sensor rotation
                setCameraDisplayOrientation(this.getContext(), mCameraManager.getCamera());

                mCameraManager.startPreview();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform result to surfaceView coordinates
     * <p/>
     * This method is needed because coordinates are given in landscape camera coordinates.
     * Now is working but transform operations aren't very explained
     * <p/>
     * TODO re-write this method explaining each single value
     *
     * @return a new PointF array with transformed points
     */
    private PointF[] transformToViewCoordinates(ResultPoint[] resultPoints) {

        PointF[] transformedPoints = new PointF[resultPoints.length];
        int index = 0;
        if (resultPoints != null) {
            float previewX = mCameraManager.getPreviewSize().x;
            float previewY = mCameraManager.getPreviewSize().y;
            float scaleX = this.getWidth() / previewY;
            float scaleY = this.getHeight() / previewX;

            for (ResultPoint point : resultPoints) {
                PointF tmppoint = new PointF((previewY - point.getY()) * scaleX, point.getX() * scaleY);
                transformedPoints[index] = tmppoint;
                index++;
            }
        }
        return transformedPoints;

    }


    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has a front camera
            return true;
        } else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has any camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Fix for the camera Sensor no some devices (ex.: Nexus 5x)
     * http://developer.android.com/intl/pt-br/reference/android/hardware/Camera.html#setDisplayOrientation(int)
     */
    @SuppressWarnings("deprecation")
    public static void setCameraDisplayOrientation(Context context, Camera camera) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int rotation = windowManager.getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(result);
        }
    }

    private class MyAsy extends AsyncTask<Context, String, String> {

        @Override
        protected String doInBackground(Context... strings) {

            mCameraManager = new CameraManager(strings[0]);
            if (mCameraManager == null) {
                Log.i("QRCodeReaderView", "cameraManager==null");
                mCameraManager = (CameraManager) strings[0].getSystemService(Context.CAMERA_SERVICE);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);
            if (mCameraManager != null) {
                mCameraManager.startPreview();
                if (mCameraManager.getCamera() != null) {
                    if (!mCameraManager.isOpen()) {
                        if (holder != null) {
                            surfaceCreated(holder);
                        } else {
                            holder = getHolder();
                            if (holder != null) {
                                surfaceCreated(holder);
                            } else {
                                Log.i("QRcodeview", "can't ge Holder");
                            }
                        }
                    }
                }
                inis();
            }
        }
    }

    private void inis() {
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  // Need to set this flag despite it's deprecated
    }

    //形成新的Cameramanager实例
    public void getCameramanager() {
        if (checkCameraHardware(getContext())) {
            new MyAsy().execute(getContext());
        } else {
            Log.e(TAG, "Error: Camera not found");
            if (mOnQRCodeReadListener != null) {
                mOnQRCodeReadListener.cameraNotFound();
            }
        }
    }

}
