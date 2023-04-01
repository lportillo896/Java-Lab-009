public class User {
    private final String username;
    private final String passHash;

    public User(String username, String passHash) {
        this.username = username;
        this.passHash = passHash;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPassHash() {
        return this.passHash;
    }
}
