
package com.example.movet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {
    private ActiveUserData activeUserData = ActiveUserData.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textHello = v.findViewById(R.id.text3);
        textHello.setText("Hello " + activeUserData.getUsername() + "!");

        TextView slogan = v.findViewById(R.id.text4);
        slogan.setText("Strong mind Strong body");

        return (v);

    }

}
