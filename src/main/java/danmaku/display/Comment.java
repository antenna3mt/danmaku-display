package danmaku.display;

import java.awt.*;

public class Comment {
    private String color;
    private String content;

    Comment(String color, String content) {
        this.color = color;
        this.content = content;
    }

    public String Color() {
        return this.color;
    }

    public String Content() {
        return this.content;
    }
}
