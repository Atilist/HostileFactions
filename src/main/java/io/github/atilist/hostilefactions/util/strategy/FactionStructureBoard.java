package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructureBlockEntry;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructureTemplate;
import net.minecraft.block.Block;

public class FactionStructureBoard {
    public static final FactionStructureTemplate SMALL_HUB_FOUNDATION = new FactionStructureTemplate(
            new String[][]{
                    new String[] {
                            "PPPPP", "     "
                    },
                    new String[] {
                            "PPPPP", "     "
                    },
                    new String[] {
                            "PPPPP", "  -  "
                    },
                    new String[] {
                            "PPPPP", "     "
                    },
                    new String[] {
                            "PPPPP", "     "
                    },
            },
            new FactionStructureBlockEntry[]{
                    new FactionStructureBlockEntry('P', Block.PLANKS),
                    new FactionStructureBlockEntry('-', null)
            }
    );
}
