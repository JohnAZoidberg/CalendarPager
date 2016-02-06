package de.struckmeierfliesen.ds.calendarpager;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * The adapter for the calendar. Does not need to be touched by the user.
 */
public class DayAdapter extends FragmentStatePagerAdapter {
    public static final int DAY_FRAGMENTS = 365*2;

    private SparseArray<DayFragment> registeredFragments = new SparseArray<>();
    private Class<? extends DayFragment> fragmentClass;

    public DayAdapter(FragmentManager fm, Class<? extends DayFragment> fragmentClass) {
        super(fm);
        this.fragmentClass = fragmentClass;
    }

    @Override
    public DayFragment getItem(int position) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("This is not supposed to happen :(");
    }

    @Override
    public int getCount() {
        return DAY_FRAGMENTS;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DayFragment fragment = (DayFragment) super.instantiateItem(container, position);
        try {
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            registeredFragments.put(position, fragment);
        } catch (IllegalStateException e) {
            String msg = "SCREENSHOT! ERROR!\n" +
                    "IllegalStateException: " +
                    "fragment.isAdded() => " + fragment.isAdded() + "\n" +
                    "registeredFragments contains => " + registeredFragments.get(position).equals(fragment) + "\n" +
                    "Bitte mit Screenshot melden!";
            //DateUtil.logToFile(msg, e);
            Toast.makeText(fragment.getContext(),
                    msg,
                    Toast.LENGTH_LONG).show();
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public DayFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
