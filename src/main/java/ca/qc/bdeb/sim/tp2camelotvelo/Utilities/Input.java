package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import javafx.animation.PauseTransition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe utilitaire pour gérer l'état des touches du clavier.
 * Permet de savoir si une touche est actuellement pressée ou relâchée.
 */
public class Input {

    /** Ensemble des touches actuellement pressées. */
    private static final Set<KeyCode> touches = new HashSet<>();


    /** Indique si une touche peut actuellement être pressée. */
    private static final Map<KeyCode, Boolean> canPress = new HashMap<>();

    /** Temps de cooldown associé à chaque touche (en secondes). */
    private static final Map<KeyCode, Double> cooldowns = new HashMap<>();

    /**
     * Vérifie si une touche spécifique est actuellement pressée.
     *
     * @param code code de la touche à vérifier
     * @return true si la touche est pressée, false sinon
     */
    public static boolean isKeyPressed(KeyCode code) {
        return touches.contains(code); // Retourne vrai si la touche est dans l'ensemble
    }

    /**
     * Met à jour l'état d'une touche (pressée ou relâchée).
     *
     * @param code code de la touche à mettre à jour
     * @param appuie true si la touche est pressée, false si relâchée
     */
    public static void setKeyPressed(KeyCode code, boolean appuie) {
        if (appuie) {
            touches.add(code); // Ajoute la touche à l'ensemble
        } else {
            touches.remove(code); // Supprime la touche de l'ensemble
        }
    }

    /**
     * Définit un cooldown pour une touche.
     *
     * @param key touche à configurer
     * @param seconds durée du cooldown en secondes
     */
    public static void setCooldown(KeyCode key, double seconds) {
        cooldowns.put(key, seconds); // Enregistre la durée du cooldown
        canPress.put(key, true);     // Touche disponible immédiatement
    }

    /**
     * Tente d'utiliser une touche si elle est disponible et pressée.
     * Déclenche le cooldown si l'action est effectuée.
     *
     * @param key touche à vérifier
     * @return true si la touche a pu être activée, false sinon
     */
    public static boolean tryPress(KeyCode key) {

        // Vérifie si la touche peut être pressée et si elle est enfoncée
        if (canPress.getOrDefault(key, true) && Input.isKeyPressed(key)) {

            canPress.put(key, false); // Bloque la touche

            // Crée un PauseTransition pour réactiver la touche après le cooldown
            var cooldown = new PauseTransition(Duration.seconds(cooldowns.getOrDefault(key, 0.5)));
            cooldown.setOnFinished(e -> canPress.put(key, true)); // Réactive la touche

            cooldown.play(); // Démarre le cooldown
            return true;
        }
        return false; // Touche non disponible
    }
}
