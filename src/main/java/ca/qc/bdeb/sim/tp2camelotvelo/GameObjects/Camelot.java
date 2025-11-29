package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Input;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * Représente le joueur (le camelot) qui peut se déplacer, sauter
 * et lancer des journaux avec différentes trajectoires.
 *
 * Le déplacement est influencé par :
 *  - l'accélération horizontale selon les touches
 *  - la gravité
 *  - des vitesses min/max imposées
 *
 * Des sprites alternés créent une animation de course.
 */
public class Camelot extends GameObject {

    /** Indique si le joueur touche actuellement le sol. */
    private boolean toucheLeSol;

    /** Temps cumulé servant à changer les frames d'animation. */
    private double tempsTotal = 0;

    /** Sprite 1 de l'animation. */
    private final Image img1 = new Image("camelot1.png");

    /** Sprite 2 de l'animation. */
    private final Image img2 = new Image("camelot2.png");

    /** Nombre de journaux que le joueur peut lancer. */
    private int nbrJournaux;

    /**
     * Constructeur. Initialise position, vitesse, accélération et sprite.
     */
    public Camelot(){

        // Vitesse initiale vers la droite
        this.velocite = new Point2D(400, 0);

        // Accélération initiale (gravité)
        this.acceleration = new Point2D(0, ACCELERATION_GRAVITE);

        // Sprite de base
        this.imgView = new ImageView(img1);
        imgView.setFitWidth(172);
        imgView.setFitHeight(144);

        // Position initiale
        this.position = new Point2D(0.20 * MainJavaFX.WIDTH, MainJavaFX.HEIGHT - imgView.getFitHeight());

        this.toucheLeSol = true;
    }

    /**
     * Méthode héritée mais inutilisée (une version plus complète existe plus bas).
     */
    @Override
    public void draw(Camera camera) {
    }

    /**
     * Dessine le camelot avec animation selon le temps écoulé.
     *
     * @param deltaTemps Temps écoulé depuis la dernière frame.
     * @param camera Caméra utilisée pour convertir les coordonnées.
     */
    public void draw(double deltaTemps, Camera camera) {
        tempsTotal += deltaTemps;

        // Animations : 4 images/sec -> alterne entre img1 et img2
        var coordoEcran = camera.coordoEcran(position);

        int index = (int) Math.floor(tempsTotal * 4) % 2;
        if (index == 0) imgView.setImage(img1);
        else imgView.setImage(img2);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());

        imgView.toFront(); // Toujours visible devant le décor
    }

    /**
     * Met à jour la physique et le mouvement du joueur.
     */
    @Override
    public void update(double deltaTemps) {

        // Lire les touches enfoncées
        inputReads();

        // Appliquer accélération et vitesse
        updatePhysique(deltaTemps);

        // Empêcher de tomber sous le sol ou dépasser les limites
        restrictionsPosition();
    }

    /**
     * Empêche le joueur de traverser le sol et impose une limite de vitesse horizontale.
     */
    private void restrictionsPosition() {

        // Vérifie si le camelot touche le sol
        if (position.getY() + imgView.getFitHeight() >= MainJavaFX.HEIGHT) {

            // Replace exactement sur le sol
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - imgView.getFitHeight());

            toucheLeSol = true;

            // Supprime la vitesse verticale
            velocite = new Point2D(velocite.getX(), 0);
        }

        // Limite la vitesse horizontale entre 200 et 600
        velocite = new Point2D(
                Math.clamp(velocite.getX(), 200, 600),
                velocite.getY()
        );
    }

    /**
     * Gère les entrées clavier et modifie l'accélération en conséquence.
     */
    private void inputReads() {
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean jump = Input.isKeyPressed(KeyCode.UP) || Input.isKeyPressed(KeyCode.SPACE);

        double accelX;

        // Déplacement gauche/droite
        if (gauche) {
            accelX = -300;
        } else if (droite) {
            accelX = 300;
        } else {
            // Stabilisation automatique vers vitesse 400
            if (velocite.getX() < 400) accelX = 300;
            else if (velocite.getX() > 400) accelX = -300;
            else accelX = 0;
        }

        // Appliquer gravité + accélération horizontale
        acceleration = new Point2D(accelX, ACCELERATION_GRAVITE);

        // Saut
        if (jump && toucheLeSol){
            velocite = new Point2D(velocite.getX(), -500); // impulsion vers le haut
            toucheLeSol = false;
        }
    }

    /**
     * Applique les lois de la physique :
     * v = v + a*dt
     * pos = pos + v*dt
     */
    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    /**
     * Lance un journal vers le haut.
     *
     * @return Le journal lancé, ou null si aucun disponible.
     */
    public Journal lancerHaut(){
        var shiftEnfonce = Input.isKeyPressed(KeyCode.SHIFT);
        if (!peutLancerJournal()) return null;

        // Vecteur de lancement
        var q = new Point2D(150, -1100);
        if (shiftEnfonce) q = q.multiply(1.5); // Tir plus fort

        return creerJournal(q);
    }

    /**
     * Lance un journal vers l’avant.
     */
    public Journal lancerAvant(){
        var shiftEnfonce = Input.isKeyPressed(KeyCode.SHIFT);
        if (!peutLancerJournal()) return null;

        var q = new Point2D(900, -900);
        if (shiftEnfonce) q = q.multiply(1.5);

        return creerJournal(q);
    }

    /**
     * Crée un journal avec une vitesse initiale dépendant de la masse.
     */
    private Journal creerJournal(Point2D q){
        if (!peutLancerJournal()) return null;

        // Masse entre 1 et 2 kg
        var masse = 1 + Math.random();

        // Position du journal : centre du camelot corrigé par les dimensions du sprite
        var posDepart = position.add(
                imgView.getFitWidth() / 2 - 52,
                imgView.getFitHeight() / 2 - 31
        );

        // v initiale = v du joueur + impulsion
        var velociteInitiale = velocite.add(q.multiply(1/masse));

        return new Journal(posDepart, velociteInitiale, masse);
    }

    /**
     * Vérifie si le joueur possède au moins un journal à lancer.
     */
    public boolean peutLancerJournal(){
        return !(nbrJournaux <= 0);
    }

    /** Ajoute des journaux au joueur. */
    public void ajouterJournaux(int quantite){
        this.nbrJournaux += quantite;
        Math.clamp(nbrJournaux, 0, this.nbrJournaux);
    }

    /** Retire des journaux au joueur. */
    public void retirerJournaux(int quantite){
        this.nbrJournaux -= quantite;
        Math.clamp(nbrJournaux, 0, this.nbrJournaux);
    }

    /** @return Le nombre de journaux restants. */
    public int getNbrJournaux() {
        return nbrJournaux;
    }

    /** Définit un nombre précis de journaux. */
    public void setNbrJournaux(int nbrJournaux){
        this.nbrJournaux = nbrJournaux;
        Math.clamp(nbrJournaux, 0, this.nbrJournaux);
    }

    /** Réinitialise les journaux (valeur par défaut : 12). */
    public void  resetJournal(){
        this.nbrJournaux = 12;
        Math.clamp(nbrJournaux, 0, this.nbrJournaux);
    }
}
