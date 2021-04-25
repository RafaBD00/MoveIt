
package com.example.movet;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.Objects;

public class FragmentNotes extends Fragment {
    private final ActiveUserData activeUserData = ActiveUserData.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        TextView textView = v.findViewById(R.id.textView2);
        textView.setText("Notes");
        EditText field = v.findViewById(R.id.EditNote);
        SharedPreferences preference = Objects.requireNonNull(getContext()).getSharedPreferences("SaveNote", Context.MODE_PRIVATE);
        String text = preference.getString(activeUserData.getUsername(), "Write");
        field.setText(text);


        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences settings = Objects.requireNonNull(getContext()).getSharedPreferences("SaveNote", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = settings.edit();
                editor.putString(activeUserData.getUsername(), field.getText().toString());
                editor.apply();
            }});
        return (v);
    }
}
