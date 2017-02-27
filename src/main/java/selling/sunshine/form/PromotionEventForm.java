package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class PromotionEventForm {
	
	@NotNull
	private String promotionEventTitle;

	@NotNull
	private String promotionEventNickname;

	@NotNull
	private String startTime;

	@NotNull
	private String endTime;

	@NotNull
	private String promotionEventType;
	
	private PromotionConfigForm[] promotionConfigList;

	public String getPromotionEventTitle() {
		return promotionEventTitle;
	}

	public void setPromotionEventTitle(String promotionEventTitle) {
		this.promotionEventTitle = promotionEventTitle;
	}

	public String getPromotionEventNickname() {
		return promotionEventNickname;
	}

	public void setPromotionEventNickname(String promotionEventNickname) {
		this.promotionEventNickname = promotionEventNickname;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public PromotionConfigForm[] getPromotionConfigList() {
		return promotionConfigList;
	}

	public void setPromotionConfigList(PromotionConfigForm[] promotionConfigList) {
		this.promotionConfigList = promotionConfigList;
	}

	public String getPromotionEventType() {
		return promotionEventType;
	}

	public void setPromotionEventType(String promotionEventType) {
		this.promotionEventType = promotionEventType;
	}
}
