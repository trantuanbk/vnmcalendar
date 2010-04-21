package chau.nguyen;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class BackgroundManager {
	private static int[] backroundIds = new int[] {
													R.drawable.body, 
													R.drawable.body1, 
													R.drawable.body2, 
													R.drawable.body3, 
													R.drawable.body4,
													R.drawable.body5,
													R.drawable.body6,
													R.drawable.body7,
													R.drawable.body8,
													};
	private static Drawable[] backgroundDrawables = null;
	private static int currentIndex = -1; 
	
	public static void init(Context context) {
		if (backgroundDrawables != null) return;
		backgroundDrawables = new Drawable[backroundIds.length];
		Resources resources = context.getResources();
		for (int i = 0; i < backroundIds.length; i++) {
			backgroundDrawables[i] = resources.getDrawable(backroundIds[i]);
		}
	}
	
	public static Drawable getRandomBackground() {
		int randomIndex = Calendar.getInstance().get(Calendar.MILLISECOND) % backgroundDrawables.length;
		if (randomIndex == currentIndex) randomIndex = ++randomIndex % backgroundDrawables.length;
		currentIndex = randomIndex;
		return backgroundDrawables[currentIndex];
	}
}
