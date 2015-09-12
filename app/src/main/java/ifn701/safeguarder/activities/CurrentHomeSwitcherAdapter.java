package ifn701.safeguarder.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ifn701.safeguarder.R;

/**
 * Created by lua on 10/09/2015.
 */
public class CurrentHomeSwitcherAdapter extends RecyclerView.Adapter<CurrentHomeSwitcherAdapter.CurrentHomeSwitcherViewHolder> {

    @Override
    public CurrentHomeSwitcherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_home_switch_bar,
                parent, false);

        return null;
    }

    @Override
    public void onBindViewHolder(CurrentHomeSwitcherViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class CurrentHomeSwitcherViewHolder extends  RecyclerView.ViewHolder{

        public CurrentHomeSwitcherViewHolder(View itemView) {
            super(itemView);


        }
    }
}
