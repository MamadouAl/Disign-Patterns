package editeur;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Fenetre extends JFrame {
	JTextArea texte;
	JMenuItem annuler;
	JMenuItem refaire;
	Font policeInitiale;
	JCheckBoxMenuItem menuGras;
	JCheckBoxMenuItem menuItalique;
	JCheckBoxMenuItem menuRouge;

	private boolean gras = false;;
	private boolean italique = false;
	private boolean rouge = false;
	
	private Memoire memoire;
	private EcouteurDocument ecouteurDocument;
	private EcouteurStyle ecouteurStyle;
		
	public Fenetre() {
		super("Editeur");
		initMenus();
		initComposant();
		initFenetre();
		initMemoire();
	}
	
	private void initMenus() {
		JMenuBar barre = new JMenuBar();
		JMenu fichier = new JMenu("Fichier");
		JMenu edition = new JMenu("Edition");
		JMenu style = new JMenu("Style");
		
		JMenuItem quitter = new JMenuItem("Quitter");
		quitter.addActionListener(ae -> System.exit(0));
		fichier.add(quitter);
		barre.add(fichier);
		
		annuler = new JMenuItem("Annuler");
		refaire = new JMenuItem("Refaire");
		refaire.setEnabled(true);
		edition.add(annuler);
		edition.add(refaire);
		barre.add(edition);
		annuler.setEnabled(false);
		refaire.setEnabled(false);
		annuler.addActionListener(new EcouteurAnnuler());
		refaire.addActionListener(new EcouteurRefaire());
		
		menuGras = new JCheckBoxMenuItem("gras");
		menuItalique = new JCheckBoxMenuItem("italique");
		menuRouge = new JCheckBoxMenuItem("rouge");
		ecouteurStyle = new EcouteurStyle();
		menuGras.addActionListener(ecouteurStyle);
		menuItalique.addActionListener(ecouteurStyle);
		menuRouge.addActionListener(ecouteurStyle);
		style.add(menuGras);
		style.add(menuItalique);
		style.add(menuRouge);
		barre.add(style);
		
		setJMenuBar(barre);
	}

	private void initFenetre() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void initComposant() {
		texte = new JTextArea(20, 80);
		texte.setWrapStyleWord(true);
		JScrollPane ascenseurs = new JScrollPane(texte);
		add(ascenseurs);
		ecouteurDocument = new EcouteurDocument();
		texte.getDocument().addDocumentListener(ecouteurDocument);
		policeInitiale = texte.getFont();
	}
	
	private void initMemoire() {
		Memento debut = new Memento();
		memoire = new Memoire(debut);
	}
	
	
	private void changerStyle(boolean gras, boolean italique, boolean rouge) {
		int style = 0;
		if (gras) {
			style = style | Font.BOLD;
		}
		if (italique) {
			style = style | Font.ITALIC;
		}
		Font police = policeInitiale.deriveFont(style);
		
		if (rouge) {
			texte.setForeground(Color.RED);			
		}
		else {
			texte.setForeground(Color.BLACK);
		}
		texte.setFont(police);
	}
	
	
	class EcouteurDocument implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			enregistrerEtat();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			enregistrerEtat();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			enregistrerEtat();
		}
	}
	class EcouteurStyle implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gras = menuGras.isSelected();
			italique = menuItalique.isSelected();
			rouge = menuRouge.isSelected();
			changerStyle(gras, italique, rouge);
			enregistrerEtat();
		}
		
	}
	
	class EcouteurAnnuler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO
			// récupérer l'état précédent
			Memento etatPrecedent = memoire.annuler();
			lireEtat(etatPrecedent);
			if (memoire.auDebut()) {
				annuler.setEnabled(false);
			}
			refaire.setEnabled(true);
		}
		
	}
	class EcouteurRefaire implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO
			// récupérer l'état suivant
			Memento etatSuivant = memoire.refaire();
			lireEtat(etatSuivant);
			if (memoire.aLaFin()) {
				refaire.setEnabled(false);
			}
			annuler.setEnabled(true);
		}
	}

	private void enregistrerEtat() {
		// TODO
		// enregistrer l'état courant
		Memento etatCourant = new Memento(texte.getText(), gras, italique, rouge);
		memoire.ajouter(etatCourant);
		annuler.setEnabled(true);
		refaire.setEnabled(false);
	}
	
	private void lireEtat(Memento etat) {
		texte.getDocument().removeDocumentListener(ecouteurDocument);
		texte.setText(etat.text);
		texte.getDocument().addDocumentListener(ecouteurDocument);
		changerStyle(etat.gras, etat.italique, etat.rouge);
		menuGras.setSelected(etat.gras);
		menuItalique.setSelected(etat.italique);
		menuRouge.setSelected(etat.rouge);
	}
	class Memento {
		private String text;
		private boolean gras;
		private boolean italique;
		private boolean rouge;
		public Memento(String text, boolean gras, boolean italique, boolean rouge) {
			// TODO
			// crée une "sauvegarde" d'un état
			this.text = text;
			this.gras = gras;
			this.italique = italique;
			this.rouge = rouge;
		}
		
		public Memento() {
			// TODO
			// crée une "sauvegarde" de l'état courant
			text="";
			gras = false;
			italique = false;
			rouge = false;
		}	
	}
	
	
}

