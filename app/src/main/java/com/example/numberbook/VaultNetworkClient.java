package com.example.numberbook;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VaultNetworkClient {

    private static final String VAULT_BASE_URL = "http://10.0.2.2/dialvault-api/api/";
    private static Retrofit vaultInstance;

    public static Retrofit getClient() {
        if (vaultInstance == null) {
            vaultInstance = new Retrofit.Builder()
                    .baseUrl(VAULT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return vaultInstance;
    }
}