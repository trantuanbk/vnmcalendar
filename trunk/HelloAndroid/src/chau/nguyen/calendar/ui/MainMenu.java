package chau.nguyen.calendar.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import chau.nguyen.R;
import chau.nguyen.VNMCalendarViewActivity;
import chau.nguyen.VNMDayActivity;
import chau.nguyen.VNMMonthActivity;

public class MainMenu extends LinearLayout {
	private ImageButton monthDaySwitcherButton;
	private SwitchViewOption switchOption;
	private VNMCalendarViewActivity vnmCalendarActivity;

	public MainMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MainMenu(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.switchOption = SwitchViewOption.SWITCH_DAY_MONTH;
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.main_menu, this, true);
		
		this.monthDaySwitcherButton = (ImageButton)findViewById(R.id.monthDaySwitcherButton);
		//this.optionButton = (ImageButton)findViewById(R.id.optionButton);
		this.monthDaySwitcherButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (switchOption) {
				case SWITCH_MONTH_DAY:
					Intent dayIntent = new Intent(v.getContext(), VNMDayActivity.class);
					vnmCalendarActivity.startActivity(dayIntent);
					break;
					
				default:
					Intent monthIntent = new Intent(v.getContext(), VNMMonthActivity.class);
					vnmCalendarActivity.startActivity(monthIntent);
					break;
				}
			}
		});
	}
	
	
	public VNMCalendarViewActivity getVnmCalendarActivity() {
		return vnmCalendarActivity;
	}

	public void setVnmCalendarActivity(VNMCalendarViewActivity vnmCalendarActivity) {
		this.vnmCalendarActivity = vnmCalendarActivity;
	}

	public SwitchViewOption getSwitchOption() {
		return switchOption;
	}

	public void setSwitchOption(SwitchViewOption switchOption) {
		this.switchOption = switchOption;
	}



	public enum SwitchViewOption {
		SWITCH_MONTH_DAY,
		SWITCH_DAY_MONTH
	}
}
