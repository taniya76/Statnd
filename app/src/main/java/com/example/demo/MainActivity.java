package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.demo.Adapters.MainAdapter;
import com.example.demo.Fragment.cartFragment;
import com.example.demo.Models.MainModel;
import com.example.demo.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        ArrayList<MainModel> list = new ArrayList<>();
        list.add(new MainModel(R.drawable.notebooks,"NoteBooks","5","Made with high-quality, 100 gsm acid-free paper in light ivory color."));
        list.add(new MainModel(R.drawable.coloredpapers,"Colored Papers","3","Our papers are designed in a variety of beautiful and fun colors available in a selection of page styles."));
        list.add(new MainModel(R.drawable.colorpencil,"Color Pencils","2","This contains 50 rich,vibrant ,pre-sharpened colored pencils that are ready for use."));
        list.add(new MainModel(R.drawable.folders,"Folder","4","12-pack of folders for storing and transporting letter-size documents."));
        list.add(new MainModel(R.drawable.holder1,"Holder","2","Make your pens, pencils, and markers in a place and desk looks more cleanly."));
        list.add(new MainModel(R.drawable.organizer,"Organizer","3","We used military-grade aluminum to ensure our products will last your entire lifetime and longer."));
        list.add(new MainModel(R.drawable.organizer2,"Plastic Organizer","5","The desk organizer does not have to assemble the trouble, just need to have it."));
        list.add(new MainModel(R.drawable.kits,"Stationary Kit","10","A perfect gift to provide a comfortable work or study space for your families and friends."));
        list.add(new MainModel(R.drawable.pen,"Pens","3","Ball point pens ISO 12757-1 H B."));
        list.add(new MainModel(R.drawable.pencils,"Pencil Set","5","Soft, smudge-free, latex-free eraser secured to the end for conveniently wiping away mistakes."));
        list.add(new MainModel(R.drawable.tapes,"Colorful Tapes","4","You will get 12 pack colored artist tapes."));
        list.add(new MainModel(R.drawable.img1,"Pencil Set","10","This contains 50 rich,vibrant ,pre-sharpened colored pencils that are ready for use."));
        list.add(new MainModel(R.drawable.img2,"Kit","5","The desk organizer does not have to assemble the trouble, just need to have it."));
        list.add(new MainModel(R.drawable.img3,"Art Kit","10","A perfect gift to provide a comfortable work or study space for your families and friends."));
        list.add(new MainModel(R.drawable.img4,"Chart Papers","3","Ball point pens ISO 12757-1 H B."));
        list.add(new MainModel(R.drawable.img5,"Pencil Set","5","Soft, smudge-free, latex-free eraser secured to the end for conveniently wiping away mistakes."));
        list.add(new MainModel(R.drawable.img6,"Stationary","4","You will get 12 pack colored artist tapes."));
        list.add(new MainModel(R.drawable.img7,"Color Paper","3","We used military-grade aluminum to ensure our products will last your entire lifetime and longer."));
        list.add(new MainModel(R.drawable.img8,"Papers","5","The desk organizer does not have to assemble the trouble, just need to have it."));
        list.add(new MainModel(R.drawable.img9,"Pencil Kit","10","A perfect gift to provide a comfortable work or study space for your families and friends."));
        list.add(new MainModel(R.drawable.img10,"Pens","3","Ball point pens ISO 12757-1 H B."));
        list.add(new MainModel(R.drawable.img11,"Full Kit","15","Soft, smudge-free, latex-free eraser secured to the end for conveniently wiping away mistakes."));
        list.add(new MainModel(R.drawable.img12,"Aesthetic Green Art Kit","10","You will get 12 pack colored artist tapes."));

        adapter=new MainAdapter(list,this);
        binding.recylerview.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recylerview.setLayoutManager(layoutManager);


        binding.shoppingCart.setVisibility(View.GONE);
        binding.OrdersFloating.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        binding.addFab.shrink();

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible) {
                    binding.shoppingCart.show();
                    binding.OrdersFloating.show();
                    binding.addFab.extend();
                    isAllFabsVisible = true;
                } else {
                    binding.shoppingCart.hide();
                    binding.OrdersFloating.hide();
                    // Set the FAB to shrink after use closes all the sub FABs
                    binding.addFab.shrink();
                    isAllFabsVisible = false;
                }
            }
        });

        binding.shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivity,new cartFragment()) .addToBackStack("tag");
                transaction.commit();
            }
        });

        binding.OrdersFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Going to my Orders", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,OrderActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}