package com.example.androidlabs7;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    public DetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle args = getArguments();
        if (args != null) {
            ((TextView) view.findViewById(R.id.name)).setText(args.getString("name"));
            ((TextView) view.findViewById(R.id.height)).setText(args.getString("height"));
            ((TextView) view.findViewById(R.id.mass)).setText(args.getString("mass"));
        }

        return view;
    }
}
