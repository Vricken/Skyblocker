package me.xmrvizzy.skyblocker.skyblock.tabhud.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.xmrvizzy.skyblocker.skyblock.tabhud.util.PlayerListMgr;
import me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component.PlainTextComponent;
import me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component.PlayerComponent;
import me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component.TableComponent;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// this widget shows a list of players with their skins.
// responsible for non-private-island areas

public class PlayerListWidget extends Widget {

    private static final MutableText TITLE = Text.literal("Players").formatted(Formatting.GREEN,
            Formatting.BOLD);

    private ArrayList<PlayerListEntry> list = new ArrayList<>();

    public PlayerListWidget() {
        super(TITLE, Formatting.GREEN.getColorValue());

        // hard cap to 4x20 entries.
        // 5x20 is too wide (and not possible in theory. in reality however...)
        int listlen = Math.min(PlayerListMgr.getSize(), 160);

        // list isn't fully loaded, so our hack won't work...
        if (listlen < 80) {
            this.addComponent(new PlainTextComponent(Text.literal("List loading...").formatted(Formatting.GRAY)));
            this.pack();
            return;
        }

        // unintuitive int ceil division stolen from
        // https://stackoverflow.com/questions/7139382/java-rounding-up-to-an-int-using-math-ceil#21830188
        int tblW = ((listlen - 80) - 1) / 20 + 1;

        TableComponent tc = new TableComponent(tblW, (listlen - 80 >= 20) ? 20 : listlen - 80,
                Formatting.GREEN.getColorValue());

        for (int i = 80; i < listlen; i++) {
            list.add(PlayerListMgr.getRaw(i));
        }

        Collections.sort(list, new Comparator<PlayerListEntry>() {
            @Override
            public int compare(PlayerListEntry o1, PlayerListEntry o2) {
                return o1.getProfile().getName().toLowerCase().compareTo(o2.getProfile().getName().toLowerCase());
            }
        });

        int x = 0, y = 0;

        for (PlayerListEntry ple : list) {
            tc.addToCell(x, y, new PlayerComponent(ple));
            y++;
            if (y >= 20) {
                y = 0;
                x++;
            }
        }

        this.addComponent(tc);
        this.pack();
    }
}