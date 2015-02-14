package ece1779.commonObjects;

public class Images {

	private int imgId;
	private String original;
	private String transFirst;
	private String transSecond;
	private String transThird;

	public Images(int imgId, String original, String transFirst,
			String transSecond, String transThird) {

		this.imgId = imgId;
		this.original = original;
		this.transFirst = transFirst;
		this.transSecond = transSecond;
		this.transThird = transThird;
	}

	public String getTransFirst() {
		return transFirst;
	}

	public void setTransFirst(String transFirst) {
		this.transFirst = transFirst;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getTransSecond() {
		return transSecond;
	}

	public void setTransSecond(String transSecond) {
		this.transSecond = transSecond;
	}

	public String getTransThird() {
		return transThird;
	}

	public void setTransThird(String transThird) {
		this.transThird = transThird;
	}

}
