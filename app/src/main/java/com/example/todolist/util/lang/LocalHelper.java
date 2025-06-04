package com.example.todolist.util.lang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.example.todolist.R;

import java.util.Locale;

public class LocalHelper {
    public static Context setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        prefs.edit().putString("language", languageCode).apply();
        return context.createConfigurationContext(config);
    }

    public static Context applySavedLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String language = prefs.getString("language", Locale.getDefault().getLanguage());
        return setLocale(context, language);
    }

    public static void showChangeLanguageDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.select_lang))
                .setItems(new CharSequence[]{"Polski", "English"}, ((dialog, which) -> {
                    String selectedLanguage = which == 0 ? "pl" : "en";
                    changeLanguage(context, selectedLanguage);
                }))
                .setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void changeLanguage(Context context, String languageCode) {
        setLocale(context, languageCode);
        if (context instanceof Activity activity) {
            activity.recreate();
        }
    }

    public static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getString("language", Locale.getDefault().getLanguage());
    }
}