module ca.qc.bdeb.sim.tp2camelotvelo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    exports ca.qc.bdeb.sim.tp2camelotvelo;
    exports ca.qc.bdeb.sim.tp2camelotvelo.Decor;
    exports ca.qc.bdeb.sim.tp2camelotvelo.Utilities;
    exports ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

    opens ca.qc.bdeb.sim.tp2camelotvelo to javafx.fxml, javafx.graphics;
    opens ca.qc.bdeb.sim.tp2camelotvelo.Decor to javafx.fxml, javafx.graphics;
    opens ca.qc.bdeb.sim.tp2camelotvelo.Utilities to javafx.fxml, javafx.graphics;
    opens ca.qc.bdeb.sim.tp2camelotvelo.GameObjects to javafx.fxml, javafx.graphics;
}