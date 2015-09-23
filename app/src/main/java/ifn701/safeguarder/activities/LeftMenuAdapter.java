/**
 * References http://www.android4devs.com/2015/01/recycler-view-handling-onitemtouch-for.html
 */
package ifn701.safeguarder.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.webservices.ImageViewUrlLoader;

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.LeftMenuViewHolder> {
    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    public static final int ZONE_SETTING = 2;

    Context context;
    Drawable patientIcon;

    private int[] icons = {R.drawable.ic_sms_failed_black_24dp, R.drawable.ic_visibility_black_24dp,
    R.drawable.ic_action, R.drawable.ic_check_circle_black_24dp, R.drawable.ic_help_black_24dp,R.drawable.ic_logout_black_24dp};
    private int[] titles = {R.string.left_menu_notification, R.string.left_menu_observation,
    R.string.left_menu_zone_setting, R.string.left_menu_event_fiilter, R.string.left_menu_help,R.string.left_menu_logout};

    private boolean[] splitterPositions = {false, true, false, true, false, true};

    public LeftMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public LeftMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

            LeftMenuViewHolder vhItem = new LeftMenuViewHolder(v, viewType);

            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);

            LeftMenuViewHolder vhHeader = new LeftMenuViewHolder(v, viewType);

            return vhHeader;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(LeftMenuViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(titles[position - 1]);
            holder.imageView.setImageResource(icons[position - 1]);
            if(!splitterPositions[position - 1]) {
                holder.spliterView.setVisibility(View.GONE);
            }
        } else {
            UserInfoPreferences userPrefs = new UserInfoPreferences(context);
            new ImageViewUrlLoader(holder.profile).execute(userPrefs.getProfilePicture());
            holder.Name.setText(userPrefs.getFullname());
        }
    }

    @Override
    public int getItemCount() {
        return icons.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        //items
        TextView textView;
        ImageView imageView;
        ImageView spliterView;

        //header
        ImageView profile;
        TextView Name;
        TextView email;

        public LeftMenuViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                spliterView =  (ImageView) itemView.findViewById(R.id.splitterRow);
                Holderid = 1;
            } else {
                Name = (TextView) itemView.findViewById(R.id.name);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }
    }
}