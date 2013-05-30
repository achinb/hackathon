package hack.db;

public abstract class DBTask {

    private String query;

    public DBTask(String query) {
        this.query = query;
    }

    protected  abstract void run(String data);

    public String getQuery() {
        return query;
    }
}
