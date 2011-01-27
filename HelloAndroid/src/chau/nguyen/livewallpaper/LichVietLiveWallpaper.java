package chau.nguyen.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LichVietLiveWallpaper extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new LichVietEngine();
	}
	
	
	private class LichVietEngine extends Engine {
		private boolean visible;
		private Handler handler;
		private Paint paint;
		
		private Runnable drawContent = new Runnable() {
			
			@Override
			public void run() {
				drawContent();
			}
		};
		
		public LichVietEngine() {
			handler = new Handler();
			paint = new Paint();
			paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
			
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			this.visible = visible;
			if (this.visible) {
				drawContent();
			} else {
				handler.removeCallbacks(drawContent);
			}
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			handler.removeCallbacks(drawContent);
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawContent);
		}
		
		private void drawContent() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					//draw
				}
			} finally {
				if (canvas != null) holder.unlockCanvasAndPost(canvas);
			}
			handler.removeCallbacks(drawContent);
			
			if (visible) {
				handler.postDelayed(drawContent, 5000);
			}
		}
		
	}

}
