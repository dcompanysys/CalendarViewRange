package br.com.brn.calendarviewrange;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import br.com.brn.calendarviewrange.listeners.DialogCompleteListener;

/**
 * Created by psinetron on 03/12/2018.
 * http://slybeaver.ru
 */
public class CalendarDialog extends DialogFragment implements DialogCompleteListener {


    private CalendarData calendarData = new CalendarData();
    private Callback callback = null;

    public CalendarDialog setStartDate(@Nullable Date startDate) {
        calendarData.setSelectedStartDate(startDate);
        return this;
    }

    public CalendarDialog setEndDate(@Nullable Date endDate) {
        calendarData.setSelectedEndDate(endDate);
        return this;
    }

    public CalendarDialog setSingle(boolean single) {
        calendarData.setSingle(single);
        return this;
    }

    public CalendarDialog setFirstMonday(boolean firsMonday) {
        calendarData.setFirstMonday(firsMonday);
        return this;
    }

    public CalendarDialog setCallback(@Nullable Callback callback) {
        this.callback = callback;
        return this;
    }

    public CalendarDialog setTimeTheme(@Nullable Integer themeResource) {
        calendarData.setTimeTheme(themeResource);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SlyCalendarDialogStyle);
    }

    @Nullable
    public Date getCalendarFirstDate() {
        return calendarData.getSelectedStartDate();
    }

    @Nullable
    public Date getCalendarSecondDate() {
        return calendarData.getSelectedEndDate();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CalendarView calendarView = (CalendarView) getActivity().getLayoutInflater().inflate(R.layout.slycalendar_main, container);
        calendarView.setCalendarData(calendarData);
        calendarView.setCallback(callback);
        calendarView.setCompleteListener(this);
        return calendarView;
    }

    @Override
    public void complete() {
        this.dismiss();
    }


    public interface Callback {
        void onCancelled();

        void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes);
    }


    public CalendarDialog setBackgroundColor(Integer backgroundColor) {
        calendarData.setBackgroundColor(backgroundColor);
        return this;
    }

    public CalendarDialog setHeaderColor(Integer headerColor) {
        calendarData.setHeaderColor(headerColor);
        return this;
    }

    public CalendarDialog setHeaderTextColor(Integer headerTextColor) {
        calendarData.setHeaderTextColor(headerTextColor);
        return this;
    }

    public CalendarDialog setTextColor(Integer textColor) {
        calendarData.setTextColor(textColor);
        return this;
    }

    public CalendarDialog setSelectedColor(Integer selectedColor) {
        calendarData.setSelectedColor(selectedColor);
        return this;
    }

    public CalendarDialog setSelectedTextColor(Integer selectedTextColor) {
        calendarData.setSelectedTextColor(selectedTextColor);
        return this;
    }

    public CalendarDialog setShowTimeButon(Boolean show) {
        calendarData.setShowTime(show);
        return this;
    }

}
