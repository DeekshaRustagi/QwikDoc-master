package main.java.cz2006project.mojojo.Boundary.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Application.SampleApplication;





public class ActivitiesFragment extends Fragment {
    ViewPager activityPager;
    FragmentPagerAdapter fragmentPagerAdapter;


    /**
     * Default constructor for the LeaveFragment
     */
    public ActivitiesFragment() {
        // Required empty public constructor
    }


    public static ActivitiesFragment newInstance() {
       ActivitiesFragment fragment = new ActivitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This method creates the view for the Appointments Fragment by calling the fragmentPagerAdapter
     * and instantiating Upcoming Leave, Create Leave and Past Leave
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        int p = getActivity().getResources().getColor(R.color.eventsColorPrimary);
        int s = getActivity().getResources().getColor(R.color.eventsColorPrimaryDark);
        SampleApplication.setCustomTheme((ActionBarActivity) getActivity(), p, s);

        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ListActivityFragment.newInstance(true);
                    case 1:
                        return AddActivityFragment();


                }
                return new ListActivityFragment();
            }

            /**
             * This method creates the title for Upcoming Leave, Create Leave and Past Leave Fragments
             */
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "List of Act";
                    case 1:
                        return "Add new Act";

                }

                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        activityPager = (ViewPager) view.findViewById(R.id.appointments_pager);
        activityPager.setAdapter(fragmentPagerAdapter);
        activityPager.setOffscreenPageLimit(2);
        return view;
    }



}


