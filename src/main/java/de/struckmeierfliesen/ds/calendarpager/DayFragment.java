package de.struckmeierfliesen.ds.calendarpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

/**
 * The fragment the Calendar uses which the user needs to subclass.
 */
abstract public class DayFragment extends Fragment {
    private Date date;

    /**
     * Subclass needs to invoke this method:
     * {@code
     * super.onCreateView(inflater, container, savedInstanceState);
     * }
     *
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return null but the subclass has to return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        int position = args.getInt("position");
        date = DateUtil.addDays(new Date(), position - DayAdapter.DAY_FRAGMENTS / 2);
        return null;
    }

    /**
     *
     * @return Return the current displayed date of the fragment.
     */
    final public Date getDate() {
        if (date == null) throw new RuntimeException("The date cannot be fetched before onCreateView was called!");
        return date;
    }

    /**
     * Ask the fragment to perform an update on the displayed data.
     */
    abstract public void updateContents();
}
