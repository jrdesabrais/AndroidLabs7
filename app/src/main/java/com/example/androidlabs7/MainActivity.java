package com.example.androidlabs7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<JSONObject> characters = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> characterNames = new ArrayList<>();
    private FrameLayout slideUpPanel;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        slideUpPanel = findViewById(R.id.slideUpFragment);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, characterNames);
        listView.setAdapter(adapter);

        fetchCharacters();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject character = characters.get(position);

            Bundle bundle = new Bundle();
            try {
                bundle.putString("name", character.getString("name"));
                bundle.putString("height", character.getString("height"));
                bundle.putString("mass", character.getString("mass"));
                bundle.putString("birthYear", character.getString("birth_year"));
                bundle.putString("gender", character.getString("gender"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (fragmentContainer != null) {

                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
            } else if (slideUpPanel != null) {

                ft.replace(R.id.slideUpFragment, fragment);
                ft.commit();

                slideUpPanel.setTranslationY(slideUpPanel.getHeight());
                slideUpPanel.animate()
                        .translationY(0)
                        .setDuration(300)
                        .start();

                slideUpPanel.setOnClickListener(v ->
                        slideUpPanel.animate()
                                .translationY(slideUpPanel.getHeight())
                                .setDuration(300)
                                .start()
                );
            }
        });
    }

    private void fetchCharacters() {
        new Thread(() -> {
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject person = results.getJSONObject(i);
                    characters.add(person);
                    characterNames.add(person.getString("name"));
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
