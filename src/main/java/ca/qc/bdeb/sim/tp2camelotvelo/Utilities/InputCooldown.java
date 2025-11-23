package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

public class InputCooldown {
    private final Map<KeyCode, Boolean> canPress = new HashMap<>();
    private final Map<KeyCode, Double> cooldowns = new HashMap<>();

    // Définir un cooldown pour une touche
    public void setCooldown(KeyCode key, double seconds) {
        cooldowns.put(key, seconds);
        canPress.put(key, true);
    }

    // Vérifie si on peut utiliser la touche
    public boolean tryPress(KeyCode key) {
        if (canPress.getOrDefault(key, true) && Input.isKeyPressed(key)) {
            canPress.put(key, false);
            PauseTransition cooldown = new PauseTransition(Duration.seconds(cooldowns.getOrDefault(key, 0.5)));
            cooldown.setOnFinished(e -> canPress.put(key, true));
            cooldown.play();
            return true;
        }
        return false;
    }
}
