package pl.rpieja.flat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class ChargesSortDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!(getActivity() instanceof ChargesActivity)) return null;

        ChargesActivity activity = (ChargesActivity) getActivity();
        return new ChargesSortDialog(activity, activity.chargesViewModel)
                .create(activity.viewPager.getCurrentItem());
    }
}
