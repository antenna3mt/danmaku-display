package danmaku.display;

import java.util.Vector;

// danmaku.display.Manager Singleton
public enum Manager {
    INSTANCE;
    boolean logged_in = false;
    boolean fetching = false;
    String token;

    enum Activity {
        INSTANCE;
        boolean valid = false;
        int id;
        String name;
        int total_count;
        int approved_count;
        int denied_count;
        int displayed_count;
    }

    public Activity GetActivity(String token) {
        if (!Activity.INSTANCE.valid) {
            // TODO
            // api call to get the activity
        }
        return Activity.INSTANCE;
    }

    private Vector<Comment> FetchRemote(String token) {
        // TODO
        return null;
    }

    public void Login(String token) {
        // TODO
    }

    public void SwitchFetching(boolean f) {
        if (f) OnFetching();
        else OffFetching();
    }

    public void OnFetching() {
        this.fetching = true;
        // TODO
    }

    public void OffFetching() {
        this.fetching = false;
        // TODO
    }

    public boolean IsFetching() {
        return this.fetching;
    }

    public Vector<Comment> Fetch(int len) {
        // TODO
        return null;
    }

    public Comment FetchOne() {
        // TODO
        return null;
    }
}
