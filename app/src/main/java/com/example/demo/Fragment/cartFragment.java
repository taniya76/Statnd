package com.example.demo.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.Adapters.cartAdapter;
import com.example.demo.Models.cartModel;
import com.example.demo.R;

import java.util.ArrayList;


public class cartFragment extends Fragment {

    ArrayList<cartModel> list;
    RecyclerView recommendRV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        recommendRV=view.findViewById(R.id.cartImage);
        list=new ArrayList<>();
        list.add(new cartModel(R.drawable.img11,"10"));
        list.add(new cartModel(R.drawable.img12,"3"));
        list.add(new cartModel(R.drawable.img13,"3"));
        list.add(new cartModel(R.drawable.img14,"FREE"));
        list.add(new cartModel(R.drawable.img15,"3"));
        list.add(new cartModel(R.drawable.img16,"5"));
        list.add(new cartModel(R.drawable.img17,"4"));
        list.add(new cartModel(R.drawable.img18,"1"));
        list.add(new cartModel(R.drawable.img19,"1"));
        list.add(new cartModel(R.drawable.img20,"7"));


        cartAdapter adapter=new cartAdapter(getContext(),list);
        LinearLayoutManager layout=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recommendRV.setNestedScrollingEnabled(false);
        recommendRV.setAdapter(adapter);
        recommendRV.setLayoutManager(layout);

        return view;
    }


}