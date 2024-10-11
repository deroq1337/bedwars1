package com.github.lukas2o11.bedwars.game.countdown;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.exceptions.EmptyGameStateException;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class BedWarsCountdown {

    private final BedWarsGame game;
    private final int start;
    private final int interval;
    private final List<Integer> specialTicks;

    public BedWarsCountdown(final BedWarsGame game, final int start, final int interval, final int... specialTicks) {
        this.game = game;
        this.start = start;
        this.interval = interval;
        this.specialTicks = Arrays.stream(specialTicks).boxed().toList();
    }

    private int current;
    private Optional<BukkitTask> task = Optional.empty();
    private boolean started = false;
    private boolean running = false;

    public void start() {
        this.current = start;
        this.started = true;
        this.running = true;
        this.task = Optional.of(Bukkit.getScheduler().runTaskTimer(game.getBedWars(), countdownTask(), interval, interval));
    }

    public abstract void onTick();

    public abstract void onSpecialTick();

    public void pause() {
        this.running = false;
    }

    public void unpause() {
        this.running = true;
    }

    public void cancel() {
        this.running = false;
        this.started = false;
        this.current = start;

        task.ifPresent(BukkitTask::cancel);
        task = Optional.empty();
    }

    private Runnable countdownTask() {
        return () -> {
            if (!running) {
                return;
            }

            if (current <= 0) {
                onEnd();
                return;
            }

            if (specialTicks.contains(current)) {
                onSpecialTick();
            }

            onTick();
            current--;
        };
    }

    private void onEnd() {
        cancel();

        final BedWarsGameState gameState = game.getGameState().orElseThrow(() -> new EmptyGameStateException("Countdown end error: GameState in BedWarsGame is empty"));
        gameState.leave();
        gameState.getNextState().ifPresent(BedWarsGameState::enter);
    }
}
