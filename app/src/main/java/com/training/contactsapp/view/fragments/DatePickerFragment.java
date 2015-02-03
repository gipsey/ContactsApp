package com.training.contactsapp.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by davidd on 1/16/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String DATE = "yyyy-mm-dd";

    public ProcessDate processDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        processDate = (ProcessDate) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = 0, month = 0, dayOfMonth = 0;

        String date = getArguments().getString(DATE);
        if (date == null || date.isEmpty()) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            String[] splitDate = null;
            try {
                splitDate = date.split("-");
                year = Integer.parseInt(splitDate[0]);
                month = Integer.parseInt(splitDate[1]) - 1;
                dayOfMonth = Integer.parseInt(splitDate[2]);
            } catch (Exception e) {
                Log.e(getClass().getName() + ".onCreateDialog", "Cannot parse the data, so using today's date");
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }

        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
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
