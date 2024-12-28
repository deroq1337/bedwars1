package com.github.deroq1337.bedwars.data.game.config;

import com.github.deroq1337.bedwars.data.config.Config;
import com.github.deroq1337.bedwars.data.config.ConfigPath;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class MainConfig extends Config {

    public MainConfig() {
        super(new File("plugins/bedwars/configs/config.yml"));
        load();
    }

    @ConfigPath("min_players")
    private final int minPlayers = 3;

    @ConfigPath("team.count")
    private final int teamCount = 4;

    @ConfigPath("team.size")
    private final int teamSize = 2;

    @ConfigPath("team.inventory.slots")
    private final List<Integer> teamSlots = List.of(1, 3, 5, 7);

    @ConfigPath("voting.map.slot")
    private final int mapVotingSlot = 4;

    @ConfigPath("voting.map.candidates.inventory.size")
    private final int mapVotingInventorySize = 9;

    @ConfigPath("voting.map.candidates.inventory.slots")
    private final List<Integer> mapVotingInventorySlots = List.of(3, 5);
}
