package editeur;

import java.util.ArrayList;
import java.util.List;

import editeur.Fenetre.Memento;

class Memoire {
    private List<Memento> pileEtats;
    private int indiceEtatCourant;

    public Memoire(Memento etatInitial) {
        pileEtats = new ArrayList<>();
        pileEtats.add(etatInitial);
        indiceEtatCourant = 0;
    }

    public void ajouter(Memento etat) {
        // Ajoute un nouvel état à la pile après l'état courant
        while (indiceEtatCourant < pileEtats.size() - 1) {
            pileEtats.remove(pileEtats.size() - 1);
        }
        pileEtats.add(etat);
        indiceEtatCourant++;
    }

    public Memento annuler() {
        // Reviens en arrière d'un état
        if (auDebut()) {
            return pileEtats.get(indiceEtatCourant);
        }
        return pileEtats.get(--indiceEtatCourant);
    }

    public Memento refaire() {
        // Fait progresser l'indice de l'état courant et renvoie l'état atteint
        if (aLaFin()) {
            return pileEtats.get(indiceEtatCourant);
        }
        return pileEtats.get(++indiceEtatCourant);
    }

    public boolean auDebut() {
        // Renvoie vrai si on est sur le premier état
        return indiceEtatCourant == 0;
    }

    public boolean aLaFin() {
        // Renvoie vrai si on est sur le dernier état
        return indiceEtatCourant == pileEtats.size() - 1;
    }
}
