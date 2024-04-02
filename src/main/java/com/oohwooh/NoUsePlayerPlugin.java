package com.oohwooh;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Arrays;

@Slf4j
@PluginDescriptor(
	name = "No Use Player"
)
public class NoUsePlayerPlugin extends Plugin
{
	@Inject
	private Client client;

	// ~~stole from~~ inspired by https://github.com/mad-s/easy-unnote/blob/main/src/main/java/easyunnote/EasyUnnotePlugin.java
	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		// The menu is not rebuilt when it is open, so don't swap or else it will
		// repeatedly swap entries
		if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen()) {
			return;
		}
		final Widget selectedWidget = client.getSelectedWidget();
		if (selectedWidget == null) {
			return;
		}
		final int itemId = selectedWidget.getItemId();
		if (itemId <= 0 || !client.isWidgetSelected()) {
			return;
		}

		// TODO: Add soa, heroes quest, bond exceptions.
		// For now though, people can just turn the plugin off

		MenuEntry[] menuEntries = client.getMenuEntries();
		MenuEntry[] newEntries = Arrays.stream(menuEntries)
				.filter(e -> {
					switch (e.getType()) {
						case WIDGET_TARGET_ON_PLAYER:
							return false;
						default:
							return true;
					}
				})
				.toArray(MenuEntry[]::new);

		client.setMenuEntries(newEntries);
	}
}
