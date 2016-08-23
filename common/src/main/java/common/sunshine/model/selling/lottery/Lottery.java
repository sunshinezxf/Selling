package common.sunshine.model.selling.lottery;

/**
 * Created by sunshine on 8/23/16.
 */
public class Lottery {
    private String lotteryId;

    private String title;

    private String description;

    public Lottery(String title, String description) {
        this.title = title;

        this.description = description;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
