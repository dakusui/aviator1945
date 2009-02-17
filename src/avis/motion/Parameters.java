package avis.motion;

import java.io.Serializable;

/**
 * 初期パラメタ指定に用いるクラス。
 * 
 * @author hukai
 * 
 */
public class Parameters implements Serializable {
	/**
	 * シリアル・バージョン
	 */
	private static final long serialVersionUID = -3912714539655192867L;
	/**
	 * 加速度
	 */
	public double acceleration = 0;
	/**
	 * オブジェクトの有効/無効状態を管理するフラグ
	 */
	public boolean alive = true;
	/**
	 * 進行方向
	 */
	public double direction = 0;
	/**
	 * グループ識別オブジェクト
	 */
	public Group groupId = null;
	/**
	 * 最大の速さ,最小の速さ
	 */
	public double maxV = Double.MAX_VALUE, minV = Double.MIN_VALUE;
	/**
	 * 速さ
	 */
	public double velocity = 0;
	/**
	 * オブジェクトの衝突判定に用いる幅と高さ
	 */
	public double width = 20, height = 20;
	/**
	 * 現在の位置
	 */
	public double x = 0, y = 0;
}