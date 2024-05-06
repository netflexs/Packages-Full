import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> appInfoList;

    public CustomAdapter(Context context, List<AppInfo> appInfoList) {
        this.context = context;
        this.appInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_app_info, parent, false);
        }

        AppInfo appInfo = appInfoList.get(position);

        ImageView iconImageView = convertView.findViewById(R.id.iconImageView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView packageNameTextView = convertView.findViewById(R.id.packageNameTextView);

        iconImageView.setImageDrawable(appInfo.getIcon());
        nameTextView.setText(appInfo.getAppName());
        packageNameTextView.setText(appInfo.getPackageName());

        return convertView;
    }
}
