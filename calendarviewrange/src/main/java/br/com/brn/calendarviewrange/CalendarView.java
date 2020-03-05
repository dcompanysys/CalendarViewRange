package br.com.brn.calendarviewrange;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.brn.calendarviewrange.listeners.DateSelectListener;
import br.com.brn.calendarviewrange.listeners.DialogCompleteListener;

/**
 * Created by psinetron on 29/11/2018.
 * http://slybeaver.ru
 */
public class CalendarView extends FrameLayout implements DateSelectListener {

    private CalendarData calendarData;

    private CalendarDialog.Callback callback = null;

    private DialogCompleteListener completeListener = null;

    private AttributeSet attrs = null;
    private int defStyleAttr = 0;


    public CalendarView(Context context) {
        super(context);
        init(null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;

    }

    public void setCallback(@Nullable CalendarDialog.Callback callback) {
        this.callback = callback;
    }

    public void setCompleteListener(@Nullable DialogCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void setCalendarData(CalendarData calendarData) {
        this.calendarData = calendarData;
        init(attrs, defStyleAttr);
        showCalendar();
    }

    private void init(@Nullable AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.slycalendar_frame, this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyle, 0);

       if(calendarData.getShowTime() == null) {
           calendarData.setShowTime(typedArray.getBoolean(R.styleable.CalendarView_showTimeButton, true));
       }
        if (calendarData.getBackgroundColor() == null) {
            calendarData.setBackgroundColor(typedArray.getColor(R.styleable.CalendarView_backgroundColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defBackgroundColor)));
        }
        if (calendarData.getHeaderColor() == null) {
            calendarData.setHeaderColor(typedArray.getColor(R.styleable.CalendarView_headerColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderColor)));
        }
        if (calendarData.getHeaderTextColor() == null) {
            calendarData.setHeaderTextColor(typedArray.getColor(R.styleable.CalendarView_headerTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderTextColor)));
        }
        if (calendarData.getTextColor() == null) {
            calendarData.setTextColor(typedArray.getColor(R.styleable.CalendarView_textColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defTextColor)));
        }
        if (calendarData.getSelectedColor() == null) {
            calendarData.setSelectedColor(typedArray.getColor(R.styleable.CalendarView_selectedColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedColor)));
        }
        if (calendarData.getSelectedTextColor() == null) {
            calendarData.setSelectedTextColor(typedArray.getColor(R.styleable.CalendarView_selectedTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedTextColor)));
        }

        typedArray.recycle();

        final ViewPager vpager = findViewById(R.id.content);
        vpager.setAdapter(new MonthPagerAdapter(calendarData, this));
        vpager.setCurrentItem(vpager.getAdapter().getCount() / 2);

        showCalendar();
    }

    private void showCalendar() {

        paintCalendar();
        showTime();

        findViewById(R.id.txtCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onCancelled();
                }
                if (completeListener != null) {
                    completeListener.complete();
                }
            }
        });

        findViewById(R.id.txtSave).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    Calendar start = null;
                    Calendar end = null;
                    if (calendarData.getSelectedStartDate() != null) {
                        start = Calendar.getInstance();
                        start.setTime(calendarData.getSelectedStartDate());
                    }
                    if (calendarData.getSelectedEndDate() != null) {
                        end = Calendar.getInstance();
                        end.setTime(calendarData.getSelectedEndDate());
                    }
                    callback.onDataSelected(start, end, calendarData.getSelectedHour(), calendarData.getSelectedMinutes());
                }
                if (completeListener != null) {
                    completeListener.complete();
                }
            }
        });


        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = null;
        if (calendarData.getSelectedStartDate() != null) {
            calendarStart.setTime(calendarData.getSelectedStartDate());
        } else {
            calendarStart.setTime(calendarData.getShowDate());
        }

        if (calendarData.getSelectedEndDate() != null) {
            calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(calendarData.getSelectedEndDate());
        }

        ((TextView) findViewById(R.id.txtYear)).setText(String.valueOf(calendarStart.get(Calendar.YEAR)));


        if (calendarEnd == null) {
            ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                    new SimpleDateFormat("EE, dd MMMM", Locale.getDefault()).format(calendarStart.getTime())
            );
        } else {
            if (calendarStart.get(Calendar.MONTH) == calendarEnd.get(Calendar.MONTH)) {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            } else {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            }
        }


        findViewById(R.id.btnMonthPrev).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vpager = findViewById(R.id.content);
                vpager.setCurrentItem(vpager.getCurrentItem()-1);
            }
        });

        findViewById(R.id.btnMonthNext).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vpager = findViewById(R.id.content);
                vpager.setCurrentItem(vpager.getCurrentItem()+1);
            }
        });

        TextView timeButon = findViewById(R.id.txtTime);

        if(calendarData.getShowTime()) {
            timeButon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int style = R.style.SlyCalendarTimeDialogTheme;
                    if (calendarData.getTimeTheme() != null) {
                        style = calendarData.getTimeTheme();
                    }

                    TimePickerDialog tpd = new TimePickerDialog(getContext(), style, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendarData.setSelectedHour(hourOfDay);
                            calendarData.setSelectedMinutes(minute);
                            showTime();
                        }
                    }, calendarData.getSelectedHour(), calendarData.getSelectedMinutes(), true);
                    tpd.show();
                }
            });
        }else {
            timeButon.setVisibility(GONE);
        }

        ViewPager vpager = findViewById(R.id.content);
        vpager.getAdapter().notifyDataSetChanged();
        vpager.invalidate();

    }

    @Override
    public void dateSelect(Date selectedDate) {
        if (calendarData.getSelectedStartDate() == null || calendarData.isSingle()) {
            calendarData.setSelectedStartDate(selectedDate);
            showCalendar();
            return;
        }
        if (calendarData.getSelectedEndDate() == null) {
            if (selectedDate.getTime() < calendarData.getSelectedStartDate().getTime()) {
                calendarData.setSelectedEndDate(calendarData.getSelectedStartDate());
                calendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() == calendarData.getSelectedStartDate().getTime()) {
                calendarData.setSelectedEndDate(null);
                calendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() > calendarData.getSelectedStartDate().getTime()) {
                calendarData.setSelectedEndDate(selectedDate);
                showCalendar();
                return;
            }
        }
        if (calendarData.getSelectedEndDate() != null) {
            calendarData.setSelectedEndDate(null);
            calendarData.setSelectedStartDate(selectedDate);
            showCalendar();
        }
    }

    @Override
    public void dateLongSelect(Date selectedDate) {
        calendarData.setSelectedEndDate(null);
        calendarData.setSelectedStartDate(selectedDate);
        showCalendar();
    }

    private void paintCalendar() {
        findViewById(R.id.mainFrame).setBackgroundColor(calendarData.getBackgroundColor());
        findViewById(R.id.headerView).setBackgroundColor(calendarData.getHeaderColor());
        ((TextView) findViewById(R.id.txtYear)).setTextColor(calendarData.getHeaderTextColor());
        ((TextView) findViewById(R.id.txtSelectedPeriod)).setTextColor(calendarData.getHeaderTextColor());
        ((TextView) findViewById(R.id.txtTime)).setTextColor(calendarData.getHeaderColor());

    }


    private void showTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendarData.getSelectedHour());
        calendar.set(Calendar.MINUTE, calendarData.getSelectedMinutes());
        ((TextView) findViewById(R.id.txtTime)).setText(
                new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime())
        );

    }

}
