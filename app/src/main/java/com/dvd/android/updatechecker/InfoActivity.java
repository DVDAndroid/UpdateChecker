package com.dvd.android.updatechecker;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class InfoActivity extends PreferenceActivity {

    PackageInfo pInfo;

    @SuppressWarnings({
            "deprecation",
            "static-access"
    })
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.info);

        ActionBar actionBar = getActionBar();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        SharedPreferences prefs = getPreferenceManager()
                .getDefaultSharedPreferences(this);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Utils.NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            findViewById(android.R.id.content).setPadding(
                    config.getPixelInsetRight(), config.getPixelInsetTop(true),
                    config.getPixelInsetRight(), 0);
        }

        if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                Utils.KEY_ICON_JB)) {

            actionBar.setIcon(R.drawable.ic_launcher_settings_jb);

        }

        if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                Utils.KEY_ICON_KK)) {

            actionBar.setIcon(R.drawable.ic_launcher_settings_kk);

        }

        if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                Utils.KEY_ICON_L)) {

            actionBar.setIcon(R.drawable.ic_launcher_settings_l);

        }

        Preference ver = findPreference("ver");
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        ver.setSummary(prefs.getString("ver", pInfo.versionName + " - "
                + pInfo.versionCode));

        if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

            prefs.edit()
                    .putString(Utils.KEY_LIST_PREFERENCE_COLOR,
                            prefs.getString("colorInfo", null)).apply();

        }

        String line = prefs.getString("new_ver_line", null);
        if (! line.equals("NO")) { updateAvailable(line); }

        boolean land = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (Build.VERSION.SDK_INT < 19 | land | ! config.hasNavigtionBar()) {
            getPreferenceScreen().removePreference(findPreference("null"));

            findViewById(android.R.id.content).setPadding(0,
                    config.getPixelInsetTop(true), config.getPixelInsetRight(),
                    config.getPixelInsetBottom());

        }

        applyColor(prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));

    }

    public void applyColor(String color) {

        if (color.equals(Utils.KEY_MATERIAL_DARK)) {
            int actionBarColor = Color.parseColor("#ff37464E");

            this.getActionBar().setBackgroundDrawable(
                    new ColorDrawable(actionBarColor));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SystemBarTintManager tintManager = new SystemBarTintManager(
                        this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(actionBarColor);
            }

        } else {
            if (color.equals(Utils.KEY_MATERIAL_LIGHT)) {
                int actionBarColor = Color.parseColor("#ff78909C");

                this.getActionBar().setBackgroundDrawable(
                        new ColorDrawable(actionBarColor));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemBarTintManager tintManager = new SystemBarTintManager(
                            this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintColor(actionBarColor);
                }

            } else {
                int actionBarColor = this.getResources().getColor(
                        Integer.valueOf(color));
                this.getActionBar().setBackgroundDrawable(
                        new ColorDrawable(actionBarColor));

                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
                    SystemBarTintManager tintManager = new SystemBarTintManager(
                            this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintColor(actionBarColor);
                }
            }
        }

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        if (preference.getKey().equals(Utils.KEY_SYSTEMBARTINT)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/jgilfelt/SystemBarTint"));
            startActivity(browserIntent);
        }

        if (preference.getKey().equals(Utils.KEY_ANDROID_OPENSOURCE)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://android.googlesource.com/"));
            startActivity(browserIntent);
        }

        if (preference.getKey().equals(Utils.KEY_STACKOVERFLOW)) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://stackoverflow.com"));
            startActivity(browserIntent);

        }

        if (preference.getKey().equals(Utils.KEY_EGGSTER)) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://repo.xposed.info/module/areeb.xposed.eggster"));
            startActivity(browserIntent);
        }

        if (preference.getKey().equals(Utils.KEY_GITHUB)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/dvdandroid/UpdateChecker"));
            startActivity(browserIntent);
        }

        if (preference.getKey().equals(Utils.KEY_CHECKING)) {

            @SuppressWarnings({
                    "static-access",
                    "deprecation"
            })
            final SharedPreferences prefs = getPreferenceManager()
                    .getDefaultSharedPreferences(this);

            String url = "https://sites.google.com/site/dvdandroid99/ver.txt?attredirects=0&d=1";
            String path = "ver.txt";

            doVerDownload(url, path, prefs);
        }
        return false;
    }

    public void doVerDownload(final String urlLink, final String fileName,
                              final SharedPreferences prefs) throws IllegalArgumentException {
        Thread dx = new Thread() {
            ProgressDialog dialog = ProgressDialog.show(InfoActivity.this, "",
                    getString(R.string.checking), true);

            @SuppressWarnings("unused")
            @Override
            public void run() {

                try {
                    URL url = new URL(urlLink);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // int fileLength = connection.getContentLength();
                    InputStream input = new BufferedInputStream(
                            url.openStream());
                    OutputStream output = new FileOutputStream(getFilesDir()
                            + "/" + fileName);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != - 1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    dialog.dismiss();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface arg0) {

                            try {
                                pInfo = getPackageManager().getPackageInfo(
                                        getPackageName(), 0);
                            } catch (NameNotFoundException e1) {
                                e1.printStackTrace();
                            }

                            BufferedReader br = null;
                            try {
                                br = new BufferedReader(
                                        new FileReader(new File(getFilesDir()
                                                + "/" + fileName)));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            String versione = null;
                            try {
                                versione = br.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (! versione.equals(pInfo.versionName)) {
                                updateAvailable(versione);
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        InfoActivity.this);

                                alertDialogBuilder.setTitle("");
                                alertDialogBuilder
                                        .setMessage(getApplicationContext()
                                                .getString(R.string.no_upds));
                                alertDialogBuilder.setPositiveButton(
                                        android.R.string.ok, null);

                                AlertDialog alertDialog = alertDialogBuilder
                                        .create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }

                        }
                    });

                } catch (FileNotFoundException e) {
                    dialog.dismiss();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(),
                                    "an error is occurred", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    });
                } catch (IllegalArgumentException e) {
                    dialog.dismiss();

                } catch (IOException e) {
                    dialog.dismiss();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.no_int),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
                }

            }
        };
        dx.start();
    }

    public void updateAvailable(final String line) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                InfoActivity.this);

        alertDialogBuilder.setTitle("");

        String text = String.format(getResources().getString(R.string.upd_yes),
                String.valueOf(pInfo.versionName), line);

        alertDialogBuilder.setMessage(text);

        @SuppressWarnings({
                "static-access",
                "deprecation"
        })
        final SharedPreferences prefs = getPreferenceManager()
                .getDefaultSharedPreferences(this);

        alertDialogBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (prefs.getString("st", null).equals("null")) {

                            String url = "https://sites.google.com/site/dvdandroid99/UpdateChecker.apk?attredirects=0&d=1";
                            String path = "UpdateChecker_new_apk.apk";

                            doApkDownload(url, path, prefs);

                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    InfoActivity.this);

                            alertDialogBuilder.setTitle("");

                            String text = String.format(getResources()
                                    .getString(R.string.alpha_beta), prefs
                                    .getString("st", null));

                            alertDialogBuilder.setMessage(text);

                            alertDialogBuilder.setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            String url = "https://sites.google.com/site/dvdandroid99/UpdateChecker.apk?attredirects=0&d=1";
                                            String path = "UpdateChecker_new_apk.apk";

                                            doApkDownload(url, path, prefs);
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton(
                                    android.R.string.no, null);
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton(android.R.string.no, null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void doApkDownload(final String urlLink, final String fileName,
                              final SharedPreferences prefs) throws IllegalArgumentException {
        Thread dx = new Thread() {
            ProgressDialog dialog = ProgressDialog.show(InfoActivity.this, "",
                    "Download APK...", true);

            @SuppressWarnings("unused")
            @Override
            public void run() {

                try {
                    URL url = new URL(urlLink);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // int fileLength = connection.getContentLength();
                    InputStream input = new BufferedInputStream(
                            url.openStream());
                    OutputStream output = new FileOutputStream(
                            Environment.getExternalStorageDirectory()
                                    + "/download/" + fileName);

                    byte data[] = new byte[2048];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != - 1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    dialog.dismiss();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface arg0) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(
                                            Environment.getExternalStorageDirectory()
                                                    + "/download/" + fileName)),
                                    "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    });

                } catch (FileNotFoundException e) {
                    dialog.dismiss();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(),
                                    "an error is occurred", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    });
                } catch (IllegalArgumentException e) {
                    dialog.dismiss();

                } catch (IOException e) {
                    dialog.dismiss();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.no_int),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        };
        dx.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @SuppressWarnings({
            "static-access",
            "deprecation"
    })
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        SharedPreferences prefs = getPreferenceManager()
                .getDefaultSharedPreferences(this);

        if (id == R.id.sett) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName(
                    "com.dvd.android.updatechecker",
                    "com.dvd.android.updatechecker.SettingsActivity"));
            startActivity(intent);
        }

        if (id == R.id.action_add) {

            Intent shortcutIntent = new Intent(getApplicationContext(),
                    MainActivity.class);
            Intent shortcutIntent1 = new Intent(getApplicationContext(),
                    UpdateActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            shortcutIntent1.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            Intent addIntent1 = new Intent();

            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
            addIntent1.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.verifica);

            if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                    "1")) {

                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(
                                getApplicationContext(),
                                R.drawable.ic_launcher_settings_jb));

            }

            if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                    "2")) {

                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(
                                getApplicationContext(),
                                R.drawable.ic_launcher_settings_kk));

            }

            if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
                    "3")) {

                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(
                                getApplicationContext(),
                                R.drawable.ic_launcher_settings_l));

            }

            addIntent1.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource
                            .fromContext(getApplicationContext(),
                                    R.drawable.ic_launcher_gsm));

            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent1);

            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            addIntent1
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            getApplicationContext().sendBroadcast(addIntent);

            String text = String.format(
                    getResources().getString(R.string.add_short_text),
                    getString(R.string.app_name));

            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                    .show();

            if (prefs.getBoolean(Utils.KEY_CHECK_BOX_NO_ADD, false)) {
                getApplicationContext().sendBroadcast(addIntent1);

                String text1 = String.format(
                        getResources().getString(R.string.add_short_text),
                        getString(R.string.title_activity_update));

                Toast.makeText(getApplicationContext(), text1,
                        Toast.LENGTH_LONG).show();
            }

            return true;
        }

        if (id == R.id.action_remove) {

            Intent shortcutIntent = new Intent(getApplicationContext(),
                    MainActivity.class);
            Intent shortcutIntent1 = new Intent(getApplicationContext(),
                    UpdateActivity.class);

            shortcutIntent.setAction(Intent.ACTION_MAIN);
            shortcutIntent1.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            Intent addIntent1 = new Intent();

            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                    getApplicationContext().getString(R.string.app_name));
            addIntent1.putExtra(
                    Intent.EXTRA_SHORTCUT_NAME,
                    getApplicationContext().getString(
                            R.string.title_activity_update));

            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent1);

            addIntent
                    .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            addIntent1
                    .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(addIntent);
            getApplicationContext().sendBroadcast(addIntent1);

        }

        if (id == R.id.sys_info) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName(
                    "com.dvd.android.updatechecker",
                    "com.dvd.android.updatechecker.SysInfoActivity"));
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}