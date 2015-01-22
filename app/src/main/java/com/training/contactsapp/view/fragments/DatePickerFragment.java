package com.training.contactsapp.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by davidd on 1/16/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public ProcessDate processDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        processDate = (ProcessDate) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String formattedMonthOfYear = String.valueOf(monthOfYear + 1);
        if (monthOfYear < 9) {
            formattedMonthOfYear = "0" + formattedMonthOfYear;
        }

        String formattedDayOfMonth = String.valueOf(dayOfMonth);
        if (dayOfMonth < 10)
            formattedDayOfMonth = "0" + formattedDayOfMonth;

        processDate.onDateSetInTheDialog(year + "-" + formattedMonthOfYear + "-" + formattedDayOfMonth);
    }

    public interface ProcessDate {
        public void onDateSetInTheDialog(String date);
    }

}
