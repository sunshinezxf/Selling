package selling.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 4/8/16.
 */
public class GoodsForm {
    @NotNull
    private String name;
    @NotNull
    private String price;
    @NotNull
    private String description;
    private boolean block;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
