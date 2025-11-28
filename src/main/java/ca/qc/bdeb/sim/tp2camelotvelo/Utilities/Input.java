package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe utilitaire pour gérer l'état des touches du clavier.
 * Permet de savoir si une touche est actuellement pressée ou relâchée.
 */
public class Input {

    /** Ensemble des touches actuellement pressées. */
    private static final Set<KeyCode> touches = new HashSet<>();

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
}
