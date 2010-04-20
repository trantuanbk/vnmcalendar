package chau.nguyen;

import java.util.Calendar;

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
	public static int getRandomBackgroundId() {
		int index = Calendar.getInstance().get(Calendar.MILLISECOND) % backroundIds.length;
		return backroundIds[index];
	}
}
