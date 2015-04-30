package sgcarvalet.adam.com.sgcarvalet;



//import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

import sgcarvalet.adam.com.sgcarvalet.adam.com.model.static_user;

/**
 * Created by adam on 1/7/15.
 */
public class ContentAll extends FragmentActivity{

    private TabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentall);


//        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
//
//        TabHost.TabSpec tabMaps     = mTabHost.newTabSpec("tid1");
//        TabHost.TabSpec tabHistory  = mTabHost.newTabSpec("tid1");
//        TabHost.TabSpec tabSetting  = mTabHost.newTabSpec("tid1");
//
//        tabMaps.setIndicator("Check Location").setContent(new Intent(this,Maps.class));
//        tabHistory.setIndicator("History").setContent(new Intent(this,History.class));
//        tabSetting.setIndicator("Settings").setContent(new Intent(this,SettingUser.class));
//
//        mTabHost.addTab(tabMaps);
//        mTabHost.addTab(tabHistory);
//        mTabHost.addTab(tabSetting);
//

    }


}
