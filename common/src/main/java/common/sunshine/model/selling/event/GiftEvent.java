package common.sunshine.model.selling.event;

import java.util.List;

/**
 * Created by sunshine on 8/24/16.
 */
public class GiftEvent extends Event {
	 private List<EventQuestion> eventQuestions;

	public List<EventQuestion> getEventQuestions() {
		return eventQuestions;
	}

	public void setEventQuestions(List<EventQuestion> eventQuestions) {
		this.eventQuestions = eventQuestions;
	}
	 
	 
}
