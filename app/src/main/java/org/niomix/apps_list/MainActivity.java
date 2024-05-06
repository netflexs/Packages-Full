package org.niomix.apps_list;
// MainActivity.java
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView appListView;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appListView = findViewById(R.id.appListView);
        packageManager = getPackageManager();

        loadInstalledApps();
    }

    private void loadInstalledApps() {
        List<AppInfo> appsList = new ArrayList<>();
        LayoutInflater myInflater = LayoutInflater.from(this);
        Toast mytoast = new Toast(this);

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedApps = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA);

        for (ResolveInfo resolveInfo : installedApps) {
            String appName = resolveInfo.loadLabel(packageManager).toString();
            String packageName = resolveInfo.activityInfo.packageName;
            Drawable icon = resolveInfo.loadIcon(packageManager);

            appsList.add(new AppInfo(appName, packageName, icon));
        }
        // Now, appsList contains all installed apps

        // Create adapter to populate ListView
        AppAdapter adapter = new AppAdapter(appsList);
        appListView.setAdapter(adapter);
    }

    private class AppAdapter extends ArrayAdapter<AppInfo> {

        AppAdapter(List<AppInfo> appsList) {
            super(MainActivity.this, R.layout.list_item_app, appsList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_app, parent, false);
            }

            ImageView appIcon = convertView.findViewById(R.id.appIcon);
            TextView appName = convertView.findViewById(R.id.appName);
            TextView appPackage = convertView.findViewById(R.id.appPackage);
            TextView copyButton = convertView.findViewById(R.id.copyButton);

            final AppInfo appInfo = getItem(position);

            if (appInfo != null) {
                appIcon.setImageDrawable(appInfo.icon);
                appName.setText(appInfo.name);
                appPackage.setText(appInfo.packageName);

                // Uninstall button click listener
                copyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Package name: ", appInfo.packageName);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(MainActivity.this,
                                "Package Name coppied to clipboard : " + appInfo.packageName, Toast.LENGTH_LONG).show();
                    }
                });
            }

            return convertView;
        }

        private void uninstallApp(String packageName) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }

    private static class AppInfo {
        String name;
        String packageName;
        Drawable icon;

        AppInfo(String name, String packageName, Drawable icon) {
            this.name = name;
            this.packageName = packageName;
            this.icon = icon;
        }
    }
}
