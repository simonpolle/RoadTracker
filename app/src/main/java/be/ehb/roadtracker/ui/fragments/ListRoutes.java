package be.ehb.roadtracker.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.presenters.LoginPresenterImpl;
import be.ehb.roadtracker.presenters.RoutePresenterImpl;
import be.ehb.roadtracker.ui.helpers.RoutesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class ListRoutes extends Fragment implements RoutePresenterImpl.RoutePresenterListener
{
    @BindView(R.id.not_found)
    TextView notFound;

    private List<Route> routes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoutesAdapter mAdapter;
    private RoutePresenterImpl presenter;

    public ListRoutes()
    {
    }

    public static ListRoutes newInstance(int position)
    {
        Bundle args = new Bundle();
        ListRoutes fragment = new ListRoutes();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_routes, container, false);
        ButterKnife.bind(this, view);
        initializeView(view);
        return view;
    }

    public void initializeView(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new RoutesAdapter(routes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        presenter = new RoutePresenterImpl(view.getContext(), this);
        presenter.findAll();
    }

    @Override
    public void successfull(List<Route> response)
    {
        routes.clear();
        routes.addAll(response);
        mAdapter.notifyDataSetChanged();
        notFound.setEnabled(false);
    }

    @Override
    public void unsuccessfull()
    {
        routes.clear();
        mAdapter.notifyDataSetChanged();
        notFound.setEnabled(true);
        notFound.setText("No routes found");
    }
}

