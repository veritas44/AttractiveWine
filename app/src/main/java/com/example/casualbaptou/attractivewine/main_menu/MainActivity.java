package com.example.casualbaptou.attractivewine.main_menu;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.casualbaptou.attractivewine.DownloadEveryCocktailsIntent;
import com.example.casualbaptou.attractivewine.NetworkConnection;
import com.example.casualbaptou.attractivewine.R;
import com.example.casualbaptou.attractivewine.URLRefs;
import com.example.casualbaptou.attractivewine.cocktail_display_menu.CocktailDisplayActivity;
import com.example.casualbaptou.attractivewine.recipe_display.RecipeDisplayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static String COCKTAILS_UPDATE = "com.example.casualbaptou.attractivewine.update.cocktailUpdates";
    public static Context mainContext;

    private String TAG = "Main activity :";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;


        findViewById(R.id.mainLoading).setAlpha(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        new URLRefs().getAllCocktailNames();    //update the total cocktail list

        setButtonActions();

        if (NetworkConnection.getInstance(this).isAvailable())
            startCocktailAPIreading();
        else
        {
            if(!URLRefs.cocktailIsSaved(URLRefs.FileNames[0], this))
            {
                findViewById(R.id.no_connection).setAlpha(1);

            }
            else
            {
                RelativeLayout RL = findViewById(R.id.mainLoading);
                RL.setVisibility(View.GONE);
                RL.findViewById(R.id.mainLoading).setAlpha(0);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){



        switch (item.getItemId()){
            case R.id.language:
                   createPopUp();
                return true;
            case R.id.savePref:
                if(!NetworkConnection.getInstance(this).isAvailable())
                {
                    Toast.makeText(getApplicationContext(),R.string.no_network,Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(!NetworkConnection.getInstance(this).isWifi()) {
                    return new Pop_up().createPopUp();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.start_dl,Toast.LENGTH_SHORT).show();
                    DownloadEveryCocktailsIntent.startActionGetCocktail(this);
                }
                return true;
            case R.id.rinit:
                PreferenceManager.getDefaultSharedPreferences(mainContext).
                        edit().clear().apply();
                return true;
        }


        return super.onOptionsItemSelected(item);

    }



    private void startCocktailAPIreading() {
        MainCocktailLoaderIntent.startActionGetCocktail(this);
        IntentFilter intentFilter = new IntentFilter(COCKTAILS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new onCocktailAPIUpdate(), intentFilter);
    }

    public class onCocktailAPIUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Button displayCocktailList = findViewById(R.id.CocktailList);
            displayCocktailList.setActivated(true);

            if(!MainCocktailLoaderIntent.isLoaded){
                findViewById(R.id.no_connection).setAlpha(1);
                return;
            }

            RelativeLayout RL = findViewById(R.id.mainLoading);
            RL.setVisibility(View.GONE);
            RL.findViewById(R.id.mainLoading).setAlpha(0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private void setButtonActions() {
        Button displayCocktailList = findViewById(R.id.CocktailList);
        displayCocktailList.setActivated(false);
        Button pickRandomCocktail = findViewById(R.id.Random);
        Button favoritedCocktails = findViewById(R.id.Favorite);


        displayCocktailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "display cocktail list pressed");
                Intent cocktailListDisplay = new Intent(mainContext, CocktailDisplayActivity.class);
                cocktailListDisplay.putExtra("EXTRA_isFavorite", "false");
                startActivity(cocktailListDisplay);
            }
        });

        pickRandomCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "pick random pressed");
                Intent intent = new Intent(mainContext, RecipeDisplayer.class);
                intent.putExtra("EXTRA_cocktail_ID", "");
                startActivity(intent);
            }
        });

        favoritedCocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "see favorited pressed");
                Intent cocktailListDisplay = new Intent(mainContext, CocktailDisplayActivity.class);
                cocktailListDisplay.putExtra("EXTRA_isFavorite", "true");
                startActivity(cocktailListDisplay);
            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("         ", "channnnnge");
        recreate();
    }

    public void createPopUp(){

        String []language = {"Francais", "Anglais"};


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
        builder.setTitle(MainActivity.mainContext.getString(R.string.selectLanguage));

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Resources res = MainActivity.mainContext.getResources();
                Configuration config = res.getConfiguration();
                switch(which){
                    case 0:
                        //changeLanguage(MainActivity.mainContext.getResources(),"fr");
                        config.setLocale(Locale.FRANCE);
                        res.updateConfiguration(config, res.getDisplayMetrics());
                        onConfigurationChanged(config);
                        break;
                    case 1:
                        config.setLocale(Locale.ENGLISH);
                        res.updateConfiguration(config, res.getDisplayMetrics());
                        onConfigurationChanged(config);
                        break;

                    default:
                        break;
                }
            }
        };

        builder.setItems(language, dialogClickListener);
        builder.show();
    }

}
