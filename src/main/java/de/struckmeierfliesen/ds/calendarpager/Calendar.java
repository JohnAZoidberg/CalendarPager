package de.struckmeierfliesen.ds.calendarpager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * This class serves as a tool to help create a ViewPager for use in a calendar-like app.
 *
 * @author Daniel Schäfer ds@struckmeierflisen.de
 * @version 0.1.0
 *
 */
public class Calendar {
    private AppCompatActivity activity;
    private ViewPager dayViewPager;
    private DayAdapter dayAdapter;

    /**
     * Starting date is today
     */
    private Date date = new Date();

    // Optional TextView to display the date
    private TextView changeDateButton = null;
    // The text colors for the above button in hex format
    private int[] changeDateButtonColors = null;

    /**
     * Use this constructor to set up the Calendar-ViewPager.
     *
     * @param activity      Any {@link AppCompatActivity}.
     *                      It is merely used to get the {@link android.support.v4.app.FragmentManager}
     *                      and for access to a {@Context}.
     * @param dayViewPager  The {@link ViewPager} which will contain the fragments.
     * @param fragmentClass The custom class of the Fragments in the ViewPager.
     *                      Must be a subclass of {@link DayFragment}!
     */
    public Calendar(AppCompatActivity activity, ViewPager dayViewPager, Class<? extends DayFragment> fragmentClass) {
        this.activity = activity;
        this.dayViewPager = dayViewPager;
        dayAdapter = new DayAdapter(this.activity.getSupportFragmentManager(), fragmentClass);
        dayViewPager.setOffscreenPageLimit(1);
        dayViewPager.setAdapter(dayAdapter);
        showDate(date);
        dayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                DayFragment registeredFragment = dayAdapter.getRegisteredFragment(position);
                if (registeredFragment == null) {
                    Log.d("Testing", "registeredFragment == null in onPageSelected (MainActivity)");
                    return;
                }

                Date selectedDate = registeredFragment.getDate();
                setDate(selectedDate);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Asks the fragments currently in memory to update their contents.
     * Calls {@link DayFragment#updateContents()} of each fragment which needs to be overriden.
     */
    public void updateContents() {
        int position = dayViewPager.getCurrentItem();
        for (int i = 0; i < 3; i++) {
            DayFragment registeredFragment = dayAdapter.getRegisteredFragment(position - 1 + i);
            if (registeredFragment != null) {
                registeredFragment.updateContents();
            }
        }
    }

    /**
     * If you have a {@link TextView} that should display the current date
     * and act as a button to change it, specify it here.
     *
     * @param changeDateButton The {@link TextView} which acts as this pseudo-button.
     * @param generalColor The text color of all dates except for today.
     * @param todayColor The text color when the date is set to today.
     */
    public void setChangeDateButton(TextView changeDateButton, int generalColor, int todayColor) {
        this.changeDateButton = changeDateButton;
        this.changeDateButtonColors = new int[] {generalColor, todayColor};
        this.changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putIntArray("date", Calendar.extractIntsFromDate(Calendar.this.date));
                dateFragment.setArguments(args);
                dateFragment.show(activity.getSupportFragmentManager(), "datePicker");
            }
        });
        updateButton();
    }

    /**
     * @return the {@link Date} for the currently shown Fragment
     */
    public Date getDate() {
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
        updateButton();
    }

    private void updateButton() {
        if (changeDateButton != null && changeDateButtonColors != null) {
            changeDateButton.setText(new StringBuilder().append(DateUtil.getDayAbbrev(date)).append(" ").append(DateFormat.format("dd.MM.yy", date)).append("  ").toString());
            if (!DateUtil.isSameDay(new Date(), date)) {
                changeDateButton.setTextColor(changeDateButtonColors[0]);
            } else {
                changeDateButton.setTextColor(changeDateButtonColors[1]);
            }
        }
    }

    private void showDate(Date date) {
        setDate(date);

        int dayDifference = DateUtil.getDayDifference(new Date(), date);
        if (Math.abs(dayDifference) < DayAdapter.DAY_FRAGMENTS / 2) {
            dayViewPager.setCurrentItem(DayAdapter.DAY_FRAGMENTS / 2 - dayDifference);
        } else {
            if (DateUtil.isSameDay(date, this.date)) {
                showDate(new Date()); // TODO is this necessary? What for?
            }
            Toast.makeText(activity,
                    activity.getString(R.string.day_scroll_limit, DayAdapter.DAY_FRAGMENTS / 2),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int[] dateArray = getArguments().getIntArray("date");
            // Use the current date as the default date in the picker
            final java.util.Calendar c = java.util.Calendar.getInstance();
            int year;
            int month;
            int day;
            if (dateArray != null) {
                day = dateArray[0];
                month = dateArray[1];
                year = dateArray[2];
            } else {
                year = c.get(java.util.Calendar.YEAR);
                month = c.get(java.util.Calendar.MONTH);
                day = c.get(java.util.Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            Calendar.this.showDate(date);
        }
    }

    private static int[] extractIntsFromDate(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        int month = cal.get(java.util.Calendar.MONTH);
        int year = cal.get(java.util.Calendar.YEAR);
        return new int[]{day, month, year};
    }
}
