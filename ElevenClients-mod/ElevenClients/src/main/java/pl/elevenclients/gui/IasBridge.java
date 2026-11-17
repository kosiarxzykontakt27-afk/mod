package pl.elevenclients.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

/** Otwiera ekran moda In-Game Account Switcher (jesli jest zainstalowany) - przez refleksje, bez zaleznosci przy buildzie. */
public final class IasBridge {
    private static final String[] CANDIDATES = {
            "ru.vidtu.ias.screen.AccountScreen",
            "ru.vidtu.ias.gui.AccountScreen",
            "ru.vidtu.ias.screen.AccountListScreen",
            "the_fireplace.ias.gui.AccountListScreen"
    };
    public static volatile String status = "";

    private IasBridge() {}

    public static boolean installed() { return FabricLoader.getInstance().isModLoaded("ias"); }

    public static void open(Screen parent) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!installed()) { status = "Zainstaluj mod In-Game Account Switcher (id: ias)"; return; }
        for (String cn : CANDIDATES) {
            try {
                Class<?> c = Class.forName(cn, true, MinecraftClient.class.getClassLoader());
                Screen s = (Screen) c.getConstructor(Screen.class).newInstance(parent);
                status = "";
                mc.setScreen(s);
                return;
            } catch (ClassNotFoundException ignored) {
            } catch (Throwable t) {
                pl.elevenclients.ElevenClients.LOGGER.warn("IAS: nie udalo sie otworzyc {}", cn, t);
            }
        }
        status = "Nie znaleziono ekranu IAS - uzyj klawisza z moda IAS";
    }
}
