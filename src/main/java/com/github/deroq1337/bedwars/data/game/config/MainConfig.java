package com.github.deroq1337.bedwars.data.game.config;

import lombok.Getter;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.Path;
import net.cubespace.Yamler.Config.YamlConfig;

import java.io.File;
import java.util.List;

@Getter
public class MainConfig extends YamlConfig {

    public MainConfig() {
        this.CONFIG_FILE = new File("plugins/bedwars/configs/config.yml");
        try {
            init();
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Path("min_players")
    private int minPlayers = 3;

    @Path("team.count")
    private int teamCount = 4;

    @Path("team.size")
    private int teamSize = 2;

    @Path("team.inventory.slots")
    private List<Integer> teamSlots = List.of(1, 3, 5, 7);

    @Path("voting.map.slot")
    private int mapVotingSlot = 4;

    @Path("voting.map.candidates.inventory.size")
    private int mapVotingInventorySize = 9;

    @Path("voting.map.candidates.inventory.slots")
    private List<Integer> mapVotingInventorySlots = List.of(3, 5);
}
