package be.ehb.roadtracker.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import be.ehb.roadtracker.R;
import be.ehb.roadtracker.domain.Route;
import be.ehb.roadtracker.presenters.RoutePresenterImpl;
import be.ehb.roadtracker.ui.helpers.EndlessRecyclerViewScrollListener;
import be.ehb.roadtracker.ui.helpers.RoutesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class ListRoutes extends Fragment implements RoutePresenterImpl.RoutePresenterFindAllListener
{
    @BindView(R.id.not_found)
    TextView notFound;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private List<Route> routes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoutesAdapter mAdapter;
    private RoutePresenterImpl presenter;
    private EndlessRecyclerViewScrollListener scrollListener;


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
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new RoutesAdapter(routes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        presenter = new RoutePresenterImpl(view.getContext(), this);
        presenter.findAll(1);
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // After 2.5 seconds
                        progressBar.setVisibility(View.GONE);
                    }
                }, 900);
                presenter.findAll(page + 1);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void successfull(List<Route> response)
    {
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

