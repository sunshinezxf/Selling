package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class EventQuestionForm {

	@NotNull
	private String content;
	
	@NotNull
	private int rank;
	
	@NotNull
	private String type;
	
	@NotNull
	private String[] questionOptionList;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getQuestionOptionList() {
		return questionOptionList;
	}

	public void setQuestionOptionList(String[] questionOptionList) {
		this.questionOptionList = questionOptionList;
	}

}
