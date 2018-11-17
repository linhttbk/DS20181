package a20181.ds.com.ds20181;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;

import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.customs.DisableTouchView;
import a20181.ds.com.ds20181.fragments.RecordContentFragment;
import a20181.ds.com.ds20181.fragments.RecordFileFragment;
import a20181.ds.com.ds20181.models.FileFilm;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppConstant {
    private String TAG = "SocketIO";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.disableTouchView)
    DisableTouchView layoutProgress;
    @BindView(R.id.swrBase)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.imgAdd)
    ImageView imgAdd;
    @BindView(R.id.imgUndo)
    ImageView imgUndo;
    @BindView(R.id.imgRedo)
    ImageView imgRedo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bus.register(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        swipeRefreshLayout.setEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, RecordFileFragment.newInstance());
        ft.disallowAddToBackStack();
        ft.commit();
//        showContentRecord();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showContentRecord(FileFilm fileFilm) {
        switchFragment(RecordContentFragment.newInstance(fileFilm));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showLoading(boolean isShow) {
        layoutProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(System.currentTimeMillis() + "");
        transaction.commit();
    }

    public void switchFragmentNoAddBackStack(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    public void showAllIcon(boolean isShown) {
        showIcon(imgAdd, isShown);
        showIcon(imgRedo, isShown);
        showIcon(imgUndo, isShown);
    }

    public void showIcon(ImageView icon, boolean isShown) {
        icon.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.imgAdd)
    public void onAddClick() {
        bus.post(AppAction.ADD_CLICK);
    }

    @OnClick(R.id.imgUndo)
    public void onUndoClick() {
        bus.post(AppAction.UNDO_CLICK);
    }

    @OnClick(R.id.imgRedo)
    public void onRedoClick() {
        bus.post(AppAction.REDO_CLICK);
    }

    @Subscribe
    public void onAppAction(AppAction action) {

    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();

    }

}
