package chau.nguyen.calendar.ui;

import chau.nguyen.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainMenu extends LinearLayout {
	private ImageButton monthDaySwitcherButton;
	private ImageButton optionButton;

	public MainMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MainMenu(Context context) {
		super(context);
		init();
	}

	private void init() {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.main_menu, this, true);
		
		this.monthDaySwitcherButton = (ImageButton)findViewById(R.id.monthDaySwitcherButton);
		//this.optionButton = (ImageButton)findViewById(R.id.optionButton);
	}
}
