module ca.qc.bdeb.sim.tp2camelotvelo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.security.jgss;
    requires jdk.compiler;


    opens ca.qc.bdeb.sim.tp2camelotvelo to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2camelotvelo;
    exports ca.qc.bdeb.sim.tp2camelotvelo.Decor;
    opens ca.qc.bdeb.sim.tp2camelotvelo.Decor to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2camelotvelo.Utilities;
    opens ca.qc.bdeb.sim.tp2camelotvelo.Utilities to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;
    opens ca.qc.bdeb.sim.tp2camelotvelo.GameObjects to javafx.fxml;
}