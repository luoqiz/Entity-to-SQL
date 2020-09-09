package top.luoqiz.idea.plugin.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * @author luoqiz
 */
public class LogUtils {

	public static void showError(String content) {
		Notifications.Bus.notify(new Notification("Entity to SQL", "Entity to SQL", content, NotificationType.ERROR));
	}

	public static void showInfo(String content) {
		Notifications.Bus.notify(new Notification("Entity to SQL", "Entity to SQL", content, NotificationType.INFORMATION));
	}
}
