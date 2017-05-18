package be.ehb.roadtracker.ui.helpers;

/**
 * Created by Simon Pollé on 18/05/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.Route;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.MyViewHolder>
{

    private List<Route> routes;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView distanceTravelled, totalCost;

        public MyViewHolder(View view)
        {
            super(view);
            distanceTravelled = (TextView) view.findViewById(R.id.distanceTravelled);
            totalCost = (TextView) view.findViewById(R.id.totalCost);
        }
    }

    public RoutesAdapter(List<Route> routes)
    {
        this.routes = routes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.route_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Route route = routes.get(position);
        holder.distanceTravelled.setText("Distance travelled: " + String.valueOf(route.getDistance_travelled()));
        holder.totalCost.setText("Total cost: " + String.valueOf(route.getTotal_cost()));
    }

    @Override
    public int getItemCount()
    {
        return routes.size();
    }
}