package sgcarvalet.adam.com.sgcarvalet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;

import sgcarvalet.adam.com.MySQLiteHelper.MySQLiteHelper;


public class NavHome extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private Context context;
    MySQLiteHelper MysqlDB;

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_home);
        context = this;
        MysqlDB     = new MySQLiteHelper(this);
        cekTemp();
        getDataUser();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    public void getDataUser(){
        String Query = "SELECT * FROM tbl_user WHERE id='1'";
        SQLiteDatabase db = MysqlDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor.moveToFirst()) {
            Log.e("Get id sqlite",cursor.getString(0));
            Log.e("Get nama sqlite",cursor.getString(1));
        }
    }
    public void cekTemp(){
        String Query = "SELECT * FROM tbl_temp WHERE temp=? AND id_temp=?";
        SQLiteDatabase db = MysqlDB.getWritableDatabase();
        Cursor cursor = db.rawQuery(Query,new String[] { "no","1" });
            if (cursor.moveToFirst()) {
                showSiderDialog();
            }
    }
    private void showSiderDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View slideView = li.inflate(R.layout.slidersg, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(slideView);
        alertDialogBuilder
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db =MysqlDB.getWritableDatabase();
                        String strSQL = "UPDATE tbl_temp SET temp = 'yes' WHERE id_temp ='1'";
                        db.execSQL(strSQL);
                        db.close();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new Fragment();
        fragment=null;
        switch (position){

            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();

                break;

            case 1:
                Intent in = new Intent(NavHome.this,MapsView.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;

            case 2:
                fragment = new History();
                break;
            case 3:
                fragment = new SettingUser();

                break;

            case 4:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure want to quit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                MySQLiteHelper DB = new MySQLiteHelper(context.getApplicationContext());
//                                DB.resetTables();
                                SQLiteDatabase db =MysqlDB.getWritableDatabase();
                                int del = db.delete("tbl_user",null,null);
                                String strSQL = "UPDATE tbl_temp SET temp = 'no' WHERE id_temp ='1'";

                                if (del>0){
                                    Log.e("DELETED :","Sukses Logout");
                                }else{
                                    Log.e("DELETED :","Gagal di hapus");
                                }
                                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name= 'tbl_user'");
                                db.execSQL("DELETE FROM tbl_user");
                                db.execSQL(strSQL);
                                db.close();
                                finish();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManagers = getSupportFragmentManager();
            fragmentManagers.beginTransaction()
                    .replace(R.id.container, fragment).commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.icon);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }



    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NavHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }




}
