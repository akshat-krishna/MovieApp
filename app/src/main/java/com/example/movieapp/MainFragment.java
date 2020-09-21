package com.example.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MainFragment extends Fragment  {
    private onFragmentBtnSelected listener;
    TextView t;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        Button click=view.findViewById(R.id.button);
        t=view.findViewById(R.id.textView);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonSelected();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentBtnSelected) {
            listener = (onFragmentBtnSelected) context;
        } else
            throw new ClassCastException(context.toString() + " must implement listener");
    }

    public void setOb(ArrayList<Movie> movie) {
        t.setText(String.valueOf(movie.size()));

    }


    public interface onFragmentBtnSelected{
        public void onButtonSelected();
    }
}
