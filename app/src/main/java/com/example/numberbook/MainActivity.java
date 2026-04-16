package com.example.numberbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnDialLoad, btnDialSync, btnVaultLookup;
    private EditText etVaultSearch;
    private RecyclerView dialRingList;
    private DialRingAdapter ringAdapter;
    private SearchResultAdapter searchAdapter;
    private List<RingEntry> ringCollection = new ArrayList<>();
    private DialVaultApi dialVaultApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDialLoad    = findViewById(R.id.btnDialLoad);
        btnDialSync    = findViewById(R.id.btnDialSync);
        btnVaultLookup = findViewById(R.id.btnVaultLookup);
        etVaultSearch  = findViewById(R.id.etVaultSearch);
        dialRingList   = findViewById(R.id.dialRingList);

        dialRingList.setLayoutManager(new LinearLayoutManager(this));
        ringAdapter   = new DialRingAdapter(ringCollection);
        searchAdapter = new SearchResultAdapter(new ArrayList<>());
        dialRingList.setAdapter(ringAdapter);

        dialVaultApi = VaultNetworkClient.getClient().create(DialVaultApi.class);

        btnDialLoad.setOnClickListener(v -> verifyAccessAndFetchRings());
        btnDialSync.setOnClickListener(v -> pushRingsToVault());
        btnVaultLookup.setOnClickListener(v -> queryVaultEntries());
    }

    private void verifyAccessAndFetchRings() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            fetchRingsFromDevice();
        } else {
            ringPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private final androidx.activity.result.ActivityResultLauncher<String> ringPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    fetchRingsFromDevice();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void fetchRingsFromDevice() {
        ringCollection.clear();

        Cursor ringCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (ringCursor != null) {
            while (ringCursor.moveToNext()) {
                String displayName = ringCursor.getString(
                        ringCursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        )
                );
                String dialNumber = ringCursor.getString(
                        ringCursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                );
                ringCollection.add(new RingEntry(displayName, dialNumber));
            }
            ringCursor.close();
        }

        dialRingList.setAdapter(ringAdapter);
        ringAdapter.refreshVault(ringCollection);
        Toast.makeText(this, "Contacts loaded: " + ringCollection.size(), Toast.LENGTH_SHORT).show();
    }

    private void pushRingsToVault() {
        for (RingEntry entry : ringCollection) {
            dialVaultApi.pushEntry(entry).enqueue(new Callback<VaultResponse>() {
                @Override
                public void onResponse(@NonNull Call<VaultResponse> call, @NonNull Response<VaultResponse> response) {
                }

                @Override
                public void onFailure(@NonNull Call<VaultResponse> call, @NonNull Throwable networkError) {
                    Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Toast.makeText(this, "Sync launched", Toast.LENGTH_SHORT).show();
    }

    private void queryVaultEntries() {
        String searchTerm = etVaultSearch.getText().toString().trim();

        if (searchTerm.isEmpty()) {
            Toast.makeText(this, "Enter a name or number", Toast.LENGTH_SHORT).show();
            return;
        }

        dialVaultApi.searchVault(searchTerm).enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchResult>> call, @NonNull Response<List<SearchResult>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchAdapter.refreshResults(response.body());
                    dialRingList.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SearchResult>> call, @NonNull Throwable networkError) {
                Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}