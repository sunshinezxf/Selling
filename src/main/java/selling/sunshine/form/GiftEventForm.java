package selling.sunshine.form;


import javax.validation.constraints.NotNull;

public class GiftEventForm {

	@NotNull
	private String giftEventTitle;

	@NotNull
	private String giftEventNickname;

	@NotNull
	private String startTime;

	@NotNull
	private String endTime;

	private EventQuestionForm[] questionList;


	public String getGiftEventTitle() {
		return giftEventTitle;
	}

	public void setGiftEventTitle(String giftEventTitle) {
		this.giftEventTitle = giftEventTitle;
	}

	public String getGiftEventNickname() {
		return giftEventNickname;
	}

	public void setGiftEventNickname(String giftEventNickname) {
		this.giftEventNickname = giftEventNickname;
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

	public EventQuestionForm[] getQuestionList() {
		return questionList;
	}

	public void setQuestionList(EventQuestionForm[] questionList) {
		this.questionList = questionList;
	}

	

	

}
