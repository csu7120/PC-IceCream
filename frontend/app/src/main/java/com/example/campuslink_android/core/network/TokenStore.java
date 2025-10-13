package com.example.campuslink_android.core.network;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {
    private static final String P="auth_pref", K="jwt";
    private final SharedPreferences sp;
    public TokenStore(Context ctx){ sp = ctx.getSharedPreferences(P, Context.MODE_PRIVATE); }
    public void save(String t){ sp.edit().putString(K, t).apply(); }
    public String get(){ return sp.getString(K, null); }
    public void clear(){ sp.edit().remove(K).apply(); }
}
