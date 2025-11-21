package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Input {
    private static final Set<KeyCode> touches = new HashSet<>();

    public static boolean isKeyPressed(KeyCode code){
        return touches.contains(code);
    }

    public static void setKeyPressed(KeyCode code, boolean appuie){
        if (appuie){
            touches.add(code);
        } else touches.remove(code);
    }
}
