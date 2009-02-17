package avis.motion;

import java.io.Serializable;

/**
 * �����p�����^�w��ɗp����N���X�B
 * 
 * @author hukai
 * 
 */
public class Parameters implements Serializable {
	/**
	 * �V���A���E�o�[�W����
	 */
	private static final long serialVersionUID = -3912714539655192867L;
	/**
	 * �����x
	 */
	public double acceleration = 0;
	/**
	 * �I�u�W�F�N�g�̗L��/������Ԃ��Ǘ�����t���O
	 */
	public boolean alive = true;
	/**
	 * �i�s����
	 */
	public double direction = 0;
	/**
	 * �O���[�v���ʃI�u�W�F�N�g
	 */
	public Group groupId = null;
	/**
	 * �ő�̑���,�ŏ��̑���
	 */
	public double maxV = Double.MAX_VALUE, minV = Double.MIN_VALUE;
	/**
	 * ����
	 */
	public double velocity = 0;
	/**
	 * �I�u�W�F�N�g�̏Փ˔���ɗp���镝�ƍ���
	 */
	public double width = 20, height = 20;
	/**
	 * ���݂̈ʒu
	 */
	public double x = 0, y = 0;
}