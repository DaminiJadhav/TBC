package com.truckbhejob2b.truckbhejocustomer.Utils;



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.truckbhejob2b.truckbhejocustomer.R;


/**
 * CustomProgressDialog
 * <p/>
 * Shows custom progress dialog with animation.
 */

public class CustomProgressDialog extends Dialog {

    private TextView mTvProgressTitle;
    //    private ProgressBar mIvAnimation;
    private ImageView mIvAnimation;
    private AnimationDrawable mCloverAnimation;

    public CustomProgressDialog(Context context) {
        super(context, R.style.Theme_Transparent);

        this.setContentView(R.layout.progress_dialog);
//        mTvProgressTitle = (TextView) this.findViewById(R.id.tvProgressTitle);
//        mTvProgressTitle.setVisibility(View.GONE);
        this.setCancelable(false);
//        mIvAnimation = (ProgressBar) findViewById(R.id.ivAnim);
        mIvAnimation = (ImageView) findViewById(R.id.ivAnim);
        mCloverAnimation = (AnimationDrawable) mIvAnimation.getDrawable();

    }

    /**
     * Sets custom progress title
     *
     * @param progressTitle
     */
    public void setTitle(final String progressTitle) {
        if (mTvProgressTitle.getVisibility() == View.GONE) {
            mTvProgressTitle.setVisibility(View.VISIBLE);
            mTvProgressTitle.setText(progressTitle);
        }
    }

    /**
     * Shows progress dialog with custom progress title
     *
     * @param progressTitle
     */
    public void show(final String progressTitle) {
        if (mTvProgressTitle.getVisibility() == View.GONE) {
            mTvProgressTitle.setVisibility(View.VISIBLE);
            mTvProgressTitle.setText(progressTitle);
        }
        CustomProgressDialog.this.show();
    }

    @Override
    public void show() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                mCloverAnimation.start();
//                mIvAnimation.setVisibility(View.VISIBLE);

            }
        }, 20);
        super.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //Log.v("Dialog focus change", "" + hasFocus);
		if (!hasFocus) this.dismiss();
        super.onWindowFocusChanged(hasFocus);
    }

}

