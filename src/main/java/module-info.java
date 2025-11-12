module ca.qc.bdeb.sim.tp2camelotvelo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.security.jgss;


    opens ca.qc.bdeb.sim.tp2camelotvelo to javafx.fxml;
    exports ca.qc.bdeb.sim.tp2camelotvelo;
}