package com.truckbhejob2b.truckbhejocustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.truckbhejob2b.truckbhejocustomer.Fragments.DashboardFragment;
import com.truckbhejob2b.truckbhejocustomer.Fragments.NotificationFragment;
import com.truckbhejob2b.truckbhejocustomer.Fragments.OrderDetailsFragment;
import com.truckbhejob2b.truckbhejocustomer.Fragments.SettingFragment;
import com.truckbhejob2b.truckbhejocustomer.Fragments.TrackOrderFragment;
import com.truckbhejob2b.truckbhejocustomer.SharedPref.*;
import com.truckbhejob2b.truckbhejocustomer.Utils.CustomProgressDialog;
import com.truckbhejob2b.truckbhejocustomer.Utils.NetworkUtil;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestKeys;
import com.truckbhejob2b.truckbhejocustomer.Utils.RestUrl;
import com.truckbhejob2b.truckbhejocustomer.Volley.VolleySingleTone;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Fragment currentFragment;
    private FrameLayout frm_MenuHeader;
    private CustomProgressDialog cpd;
    private String TAG = "ActivityDashboard";
    private Context mContext;
    private boolean backButtonEnabled = false;
    private static long ON_BACK_PRESSED = 0;
    private TextView tv_MenuDashboard, tv_MenuLogout, tv_MenuSetting, tv_MenuNotification;
    private TextView tv_MenuUserName;
    private boolean drawerClose = false;
    private sharedPreference sharedPreference;
    private CustomProgressDialog customProgressDialog;
    private TextView txtViewToolBarText;
    private ImageView imageViewToolbarImage;

    private Switch notificationOnOff;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            if (type.equals("order")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String orderId = bundle.getString("orderId");
                        OrderDetailsFragment detailsFragment = new OrderDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, orderId);
                        detailsFragment.setArguments(bundle);
                        // clearBackStack();
                        changeFragment(detailsFragment, "FragmentOrderDOc");
                    }
                }, 1000);
            } else if (type.equals("track")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String orderId = bundle.getString("orderId");
                        TrackOrderFragment trackOrderFragment = new TrackOrderFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, orderId);
                        trackOrderFragment.setArguments(bundle);
                        // clearBackStack();
                        changeFragment(trackOrderFragment, "FragmentTrack");
                    }
                }, 1000);
            }
        }
        sharedPreference = new sharedPreference(this);
        mContext = DashboardActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        /* Menu Logo set here */
        // Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.menu_ic, mContext.getTheme());
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_black_24dp, mContext.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
//        toolbar.setLogo(R.mipmap.app_ic);
        drawer.setDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
        cpd = new CustomProgressDialog(mContext);
        setUI(savedInstanceState);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String type = intent.getStringExtra("type");
//            String orderId = intent.getStringExtra("orderId");
//            Log.d(TAG, "onNewIntent: "+orderId);
            if (type.equals("order")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String orderId = intent.getStringExtra("orderId");
                        OrderDetailsFragment detailsFragment = new OrderDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, orderId);
                        detailsFragment.setArguments(bundle);
                        // clearBackStack();
                        changeFragment(detailsFragment, "FragmentOrderDOc");
                    }
                }, 1000);
            } else if (type.equals("track")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String orderId = intent.getStringExtra("orderId");
                        TrackOrderFragment trackOrderFragment = new TrackOrderFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(RestKeys.RES_TRIPS_ORDER_ID, orderId);
                        trackOrderFragment.setArguments(bundle);
                        //clearBackStack();
                        changeFragment(trackOrderFragment, "FragmentTrack");
                    }
                }, 1000);
            }
        }
    }

    private void setUI(Bundle savedInstanceState) {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_MenuUserName = (TextView) navigationView.findViewById(R.id.tv_MenuUserName);
        frm_MenuHeader = (FrameLayout) navigationView.findViewById(R.id.frm_MenuHeader);
        tv_MenuDashboard = (TextView) navigationView.findViewById(R.id.menuDashboard);
        tv_MenuLogout = (TextView) navigationView.findViewById(R.id.menuLogout);
        tv_MenuNotification = (TextView) navigationView.findViewById(R.id.menuNotification);

        tv_MenuSetting = navigationView.findViewById(R.id.menuSetting);

        txtViewToolBarText = findViewById(R.id.toolbar_title);
        imageViewToolbarImage = findViewById(R.id.imageviewToolbarIcon);

        notificationOnOff = findViewById(R.id.switchNotification);
        if (sharedPreference.getCustomerNotification()) {
            notificationOnOff.setChecked(true);
//            Log.d(TAG, "setUI:  Notification is On");
        }

        tv_MenuDashboard.setBackgroundColor(Color.parseColor("#EEEEEE"));
        frm_MenuHeader.setOnClickListener(this);
        tv_MenuDashboard.setOnClickListener(this);
        tv_MenuNotification.setOnClickListener(this);
        tv_MenuSetting.setOnClickListener(this);
        tv_MenuLogout.setOnClickListener(this);


        tv_MenuUserName.setText(sharedPreference.getCustomerName());
        changeFragment(new DashboardFragment(), "FragmentDashBoard");

        notificationOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreference.setCustomerNotification(true);
//                    Log.d(TAG, "setUI:  Notification is now "+sharedPreference.getCustomerNotification());

                } else {
                    sharedPreference.setCustomerNotification(false);
//                    Log.d(TAG, "setUI:  Notification is now "+sharedPreference.getCustomerNotification());

                }
            }
        });
    }

    public void Restart() {
        this.recreate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuDashboard:
                drawer.closeDrawer(GravityCompat.START);
                clearBackStack();
                tv_MenuDashboard.setBackgroundColor(Color.parseColor("#EEEEEE"));
                tv_MenuNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuLogout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                changeFragment(new DashboardFragment(), "FragmentDashboard");
                break;

            case R.id.menuNotification:
                drawer.closeDrawer(GravityCompat.START);
//                clearBackStack();
                tv_MenuDashboard.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuNotification.setBackgroundColor(Color.parseColor("#EEEEEE"));
                tv_MenuSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuLogout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                changeFragment(new NotificationFragment(), "FragmentNotification");
                break;

            case R.id.menuSetting:
                drawer.closeDrawer(GravityCompat.START);
//                clearBackStack();
                tv_MenuDashboard.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuSetting.setBackgroundColor(Color.parseColor("#EEEEEE"));
                tv_MenuLogout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                changeFragment(new SettingFragment(), "FragmentSetting");
                break;


            case R.id.menuLogout:
                drawer.closeDrawer(GravityCompat.START);
                clearBackStack();
                tv_MenuDashboard.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_MenuLogout.setBackgroundColor(Color.parseColor("#EEEEEE"));


                // Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_roboto_regular));
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                TextView tv_LogoutMsg = (TextView) dialog.findViewById(R.id.tv_LogoutMsg);
                TextView tv_LogoutYes = (TextView) dialog.findViewById(R.id.tv_LogoutYes);
                TextView tv_LogoutNo = (TextView) dialog.findViewById(R.id.tv_LogoutNo);
                tv_LogoutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != NetworkUtil.TYPE_NOT_CONNECTED) {
                            LogoutUser();
                        } else {
                            setSnackBar(getResources().
                                    getString(R.string.msg_no_internet_connection));
                        }
                        dialog.dismiss();
                    }
                });

                tv_LogoutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        changeFragment(new DashboardFragment(), "FragmentDashboard");
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @SuppressLint("NewApi")
    public void enableBackButton(boolean enable, final String title) {
        this.backButtonEnabled = enable;
        final int count = getSupportFragmentManager().getBackStackEntryCount();
        if (enable) {
            toolbar.setNavigationIcon(R.drawable.ic_action_back_new);
            setToolbarTitle(title);
            imageViewToolbarImage.setVisibility(View.GONE);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count >= 1) {
                        hideSoftKeyboard(v);
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                }
            });
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
            setToolbarTitle(title);
            imageViewToolbarImage.setVisibility(View.VISIBLE);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                    //setDrawerEnabled(true);

                }
            });
        }
    }


    public void setToolbarTitle(String title) {
        // toolbar.setTitle(title);
        txtViewToolBarText.setText(title);
    }

    public Toolbar getToolbarObject(){
        return toolbar;
    }

    public void changeFragment(Fragment targetFragment, String tag) {
        currentFragment = targetFragment;
        if (tag != null && !tag.isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frmDashboard, currentFragment)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frmDashboard, currentFragment)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        }
    }

    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            }
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (drawer.isDrawerOpen(navigationView)) {
                toggleDrawer(false);
                if (drawerClose) {
                    drawerClose = false;
                    appExitConfirmation();

                } else {
                    appExitConfirmation();
                }
            } else {
                appExitConfirmation();
            }
        }
    }

    private void appExitConfirmation() {
        if (ON_BACK_PRESSED + 2000 > System.currentTimeMillis()) {
            enableBackButton(false, "");
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        } else {
            Toast.makeText(mContext, getResources().getString(R.string.back_button_press), Toast.LENGTH_SHORT).show();
            ON_BACK_PRESSED = System.currentTimeMillis();
        }
    }
//    private void dialogBox(){
//        final Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_exit);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//        dialog.getWindow().setAttributes(lp);
//        TextView tv_ExitMsg = (TextView) dialog.findViewById(R.id.tv_ExitMsg);
//        TextView tv_ExitYes = (TextView) dialog.findViewById(R.id.tv_ExitYes);
//        TextView tv_ExitNo = (TextView) dialog.findViewById(R.id.tv_ExitNo);
////        tv_ExitMsg.setTypeface(tf);
////        tv_ExitYes.setTypeface(tf);
////        tv_ExitNo.setTypeface(tf);
//        tv_ExitYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory(Intent.CATEGORY_HOME);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
//                finish();
//            }
//        });
//
//        tv_ExitNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public void setSnackBar(String snackBarText) {
        final Snackbar bar = Snackbar.make(this.findViewById(android.R.id.content), snackBarText, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.WHITE)
                .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        TextView tv = bar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        bar.show();
    }

    public void toggleDrawer(boolean isOpen) {
        if (isOpen) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
            // drawer = true;
        }
    }

    private void clearSharedPref() {
        sharedPreference.clearAllPreferenceValues();
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void LogoutUser() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show();
        String url = Uri.parse(RestUrl.LOGOUT_CUSTOMER)
                .buildUpon()
                .appendQueryParameter(RestKeys.UPDATE_FCM_CUSTOMER_ID, sharedPreference.getCustomerId())
                .appendQueryParameter(RestKeys.UPDATE_FCM_API_TOKEN, sharedPreference.getCustomerApi())
                .build().toString();
        StringRequest logout = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customProgressDialog.hide();
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getString(RestKeys.SUCCESS).equals(RestKeys.SUCCESS_VALUE)) {
                        clearSharedPref();
                        setSnackBar(getString(R.string.logout_message));
                    } else {
                        clearSharedPref();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hide();
            }
        });
        new VolleySingleTone(this).getRequestQueue().add(logout);
    }
}