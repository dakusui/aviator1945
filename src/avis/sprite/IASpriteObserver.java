package avis.sprite;

public interface IASpriteObserver {
	public void disposed(ASprite sprite);
	public void setVisible(ASprite sprite, boolean visible);
	public static final IASpriteObserver NULL_OBSERVER = new IASpriteObserver() {
		public void disposed(ASprite sprite) {
		}
		public void setVisible(ASprite sprite, boolean visible) {
		}
		public void priorityChanged(ASprite sprite) {
		}
	};
	public void priorityChanged(ASprite sprite);
}
