package me.xenodevs.xeno.alt;

public final class Alt {
    private String mask = "";
    private final String username;
    private String password;
    public int mode;

    public Alt(String username, String password, int mode) {
        this(username, password, "", mode);
    }

    public Alt(String username, String password, String mask, int mode) { // Mode - 0 == Mojang, 1 == Microsoft, 2 == Cracked
        this.username = username;
        this.password = password;
        this.mask = mask;
        this.mode = mode;
    }

    public String getMask() {
        return this.mask;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

