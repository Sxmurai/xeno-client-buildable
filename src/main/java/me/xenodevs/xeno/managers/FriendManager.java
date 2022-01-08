package me.xenodevs.xeno.managers;

import java.util.ArrayList;
import java.util.List;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.friends.Friend;

public class FriendManager {

	public static List<Friend> friends;

	public FriendManager(){
		friends = new ArrayList<>();
		
		Xeno.logger.info("Initialized Friend Manager");
	}

	public static List<String> getFriendsByName() {
		ArrayList<String> friendsName = new ArrayList<>();
		friends.forEach(friend -> friendsName.add(friend.getName()));

		return friendsName;
	}

	public static boolean isFriend(String name) {
		for (Friend f : friends) {
			if (f.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean isEnemy(String name) {
		return (isFriend(name) ? false : true);
	}

	public static Friend getFriendByName(String name) {
		Friend fr = null;
		for (Friend f : friends) {
			if (f.getName().equalsIgnoreCase(name)) {
				fr = f;
			}
		}

		return fr;
	}

	public void addFriend(String name) {
		friends.add(new Friend(name));
		
		Xeno.config.saveFriendConfig();
	}

	public static void removeFriend(String name) {
		friends.remove(getFriendByName(name));
	}
	
	public static void clearFriends() {
		friends.clear();
	}
}