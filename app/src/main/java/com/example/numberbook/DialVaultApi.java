package com.example.numberbook;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DialVaultApi {

    @POST("storeEntry.php")
    Call<VaultResponse> pushEntry(@Body RingEntry ringEntry);

    @GET("fetchAllEntries.php")
    Call<List<RingEntry>> pullAllEntries();

    @GET("lookupEntry.php")
    Call<List<SearchResult>> searchVault(@Query("searchTerm") String searchTerm);
}