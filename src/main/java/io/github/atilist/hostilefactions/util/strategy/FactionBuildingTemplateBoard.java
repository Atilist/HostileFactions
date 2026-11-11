package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.construction.FactionBuildingTemplate;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructureTemplate;

public class FactionBuildingTemplateBoard {
    public static final FactionBuildingTemplate HUB = new FactionBuildingTemplate(
            new FactionStructureTemplate[]{
                    FactionStructureBoard.SMALL_HUB_FOUNDATION,
                    FactionStructureBoard.SMALL_HUB_HOUSE
            }
    );
}
