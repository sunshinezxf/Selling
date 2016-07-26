package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class ScaleForm {
	@NotNull
	private int scale;

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

}
