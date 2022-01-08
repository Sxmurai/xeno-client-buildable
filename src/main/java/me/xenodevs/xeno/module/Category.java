package me.xenodevs.xeno.module;

public enum Category {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    CLIENT("Client"),
    MISC("Misc"),
    HUD("HUD");

	public String name;
	
	Category(String string) {
		name = string;
	}

}
