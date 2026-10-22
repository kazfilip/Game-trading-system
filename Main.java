import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Main extends JFrame {

    private Kolekcjoner zalogowany;
    private CardLayout ukladKart;
    private JPanel panelGlowny;
    private JComboBox<Kolekcjoner> listaKolekcjonerow;

    private JComboBox<Listing> listaListingow;
    private DefaultListModel<Gra> modelGierOferty;
    private JList<Gra> listaGierOferty;
    private JTextField poleDoplaty;
    private JLabel etykietaBleduOferty;

    private DefaultListModel<Gra> modelGierListingu;
    private JList<Gra> listaGierListingu;
    private JLabel etykietaBleduListingu;
    private JLabel etykietaLimitu;

    private JComboBox<Gra> listaGierRecenzji;
    private JComboBox<Integer> listaOcen;
    private JTextArea poleTresciRecenzji;
    private JLabel etykietaBleduRecenzji;

    private JPanel panelAktywnychListingow;
    private JTable tabelaListingow;
    private List<Listing> aktywneListingi = new ArrayList<>();

    private JPanel panelSzczegolowListingu;
    private JPanel panelKolekcji;
    private JPanel panelMoichListingow;
    private JPanel panelMoichOfert;
    private JPanel panelPowiadomien;

public Main() {
        super("System Wymiany Gier Retro");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zapiszIZamknij();
            }
        });
        setMinimumSize(new Dimension(760, 540));
        setSize(900, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        wczytajLubUtworzDane();
        zalogowany = Kolekcjoner.wszyscyKolekcjonerzy.get(0);
        zbudujInterfejs();
        setVisible(true);
    }

    private void wczytajLubUtworzDane() {
        try {
            StanAplikacji.wczytaj();
        } catch (Exception e) {
            Kolekcjoner.wszyscyKolekcjonerzy.clear();
            Gra.wszystkieGry.clear();
            Listing.wszystkieListingi.clear();
            Oferta.wszystkieOferty.clear();
        }
        if (Kolekcjoner.wszyscyKolekcjonerzy.isEmpty()) {
            inicjalizujDaneDemo();
        }
    }
    
    private void inicjalizujDaneDemo() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Wydawnictwo nintendo = new Wydawnictwo("Nintendo", "Japonia");
            Wydawnictwo sega = new Wydawnictwo("Sega", "Japonia");
            Wydawnictwo atari = new Wydawnictwo("Atari", "USA");

            Gra mario = new Gra("Super Mario Bros", format.parse("1985-09-13"), 450, nintendo);
            mario.setOpis("Klasyczna platformowka");
            Gra zelda = new Gra("Zelda: Link to the Past", format.parse("1991-11-21"), 380, nintendo);
            Gra sonic = new Gra("Sonic the Hedgehog", format.parse("1991-06-23"), 200, sega);
            sonic.setOpis("Szybka platformowka");
            Gra tetris = new Gra("Tetris", format.parse("1989-06-06"), 150, atari);
            Gra doom = new Gra("Doom", format.parse("1993-12-10"), 120, atari);
            doom.setOpis("Strzelanka pierwszoosobowa");

            Kolekcjoner jan = new KolekcjonerNormal("Jan", "Kowalski",
                    Kolekcjoner.Poziom.POCZATKUJACY, Kolekcjoner.Region.KRAJOWY);
            Kolekcjoner anna = new KolekcjonerPremium("Anna", "Nowak",
                    Kolekcjoner.Poziom.DOSWIADCZONY, Kolekcjoner.Region.MIEDZYNARODOWY,
                    format.parse("2027-12-31"));
            Kolekcjoner piotr = new KolekcjonerNormal("Piotr", "Wisniewski",
                    Kolekcjoner.Poziom.POCZATKUJACY, Kolekcjoner.Region.KRAJOWY);

            jan.getKolekcja().dodajGre(mario);
            jan.getKolekcja().dodajGre(tetris);
            jan.getKolekcja().dodajGre(doom);
            anna.getKolekcja().dodajGre(zelda);
            piotr.getKolekcja().dodajGre(sonic);

            new Listing(anna, Arrays.asList(zelda));
            new Listing(piotr, Arrays.asList(sonic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zapiszIZamknij() {
        try {
            StanAplikacji.zapisz();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Blad zapisu");
        }
        dispose();
        System.exit(0);
    }

    private void zbudujInterfejs() {
        setLayout(new BorderLayout());
        add(zbudujPasekGorny(), BorderLayout.NORTH);

        ukladKart = new CardLayout();
        panelGlowny = new JPanel(ukladKart);
        add(panelGlowny, BorderLayout.CENTER);

        panelGlowny.add(zbudujMenuGlowne(), "menu");
        panelGlowny.add(zbudujListeListingow(), "listingi");
        panelGlowny.add(zbudujSzczegolyListingu(), "szczegoly");
        panelGlowny.add(zbudujFormularzListingu(), "listing");
        panelGlowny.add(zbudujFormularzOferty(), "oferta");
        panelGlowny.add(zbudujPanelKolekcji(), "kolekcja");
        panelGlowny.add(zbudujFormularzRecenzji(), "recenzja");
        panelGlowny.add(zbudujPanelMoichListingow(), "moje_listingi");
        panelGlowny.add(zbudujPanelMoichOfert(), "moje_oferty");
        panelGlowny.add(zbudujPanelPowiadomien(), "powiadomienia");

        ukladKart.show(panelGlowny, "menu");
    }

    private JPanel zbudujPasekGorny() {
        JPanel pasek = new JPanel(new BorderLayout(10, 0));
        pasek.setBackground(new Color(44, 71, 112));
        pasek.setBorder(new EmptyBorder(8, 16, 8, 16));

        JLabel tytul = new JLabel("System Wymiany Gier Retro");
        tytul.setForeground(Color.WHITE);
        tytul.setFont(new Font("Arial", Font.BOLD, 16));
        pasek.add(tytul, BorderLayout.WEST);

        JPanel prawy = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        prawy.setOpaque(false);

        JLabel etykietaKolekcjonera = new JLabel("Zalogowany:");
        etykietaKolekcjonera.setForeground(new Color(190, 210, 235));
        etykietaKolekcjonera.setFont(new Font("Arial", Font.PLAIN, 11));

        listaKolekcjonerow = new JComboBox<>();
        for (Kolekcjoner kolekcjoner : Kolekcjoner.wszyscyKolekcjonerzy) {
            listaKolekcjonerow.addItem(kolekcjoner);
        }
        listaKolekcjonerow.addActionListener(e -> {
            Kolekcjoner wybrany = (Kolekcjoner) listaKolekcjonerow.getSelectedItem();
            if (wybrany != null) {
                zalogowany = wybrany;
                ukladKart.show(panelGlowny, "menu");
            }
        });

        prawy.add(etykietaKolekcjonera);
        prawy.add(listaKolekcjonerow);
        pasek.add(prawy, BorderLayout.EAST);
        return pasek;
    }

    private JPanel zbudujMenuGlowne() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        panel.add(zbudujKolumneMenu("Panel wymiany",
                new String[]{ "Przegladaj listingi", "Zloz oferte", "Stworz listing" },
                new Runnable[]{
                    () -> { odswiezListeListingow(); ukladKart.show(panelGlowny, "listingi"); },
                    () -> { odswiezFormularzOferty(null); ukladKart.show(panelGlowny, "oferta"); },
                    () -> { odswiezFormularzListingu(); ukladKart.show(panelGlowny, "listing"); }
                }));

        panel.add(zbudujKolumneMenu("Moja strefa",
                new String[]{ "Moja kolekcja", "Moje listingi", "Moje oferty", "Powiadomienia" },
                new Runnable[]{
                    () -> { odswiezKolekcje(); ukladKart.show(panelGlowny, "kolekcja"); },
                    () -> { odswiezMojeListingi(); ukladKart.show(panelGlowny, "moje_listingi"); },
                    () -> { odswiezMojeOferty(); ukladKart.show(panelGlowny, "moje_oferty"); },
                    () -> { odswiezPowiadomienia(); ukladKart.show(panelGlowny, "powiadomienia"); }
                }));
        return panel;
    }

    private JPanel zbudujKolumneMenu(String naglowek, String[] etykiety, Runnable[] akcje) {
        JPanel kolumna = new JPanel();
        kolumna.setLayout(new BoxLayout(kolumna, BoxLayout.Y_AXIS));
        kolumna.setBorder(BorderFactory.createTitledBorder(naglowek));
        for (int i = 0; i < etykiety.length; i++) {
            JButton przycisk = new JButton(etykiety[i]);
            przycisk.setFont(new Font("Arial", Font.PLAIN, 13));
            przycisk.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            przycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
            przycisk.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            final Runnable akcja = akcje[i];
            przycisk.addActionListener(e -> akcja.run());
            kolumna.add(przycisk);
        }
        return kolumna;
    }

    private JPanel zbudujNaglowek(String tytul, String powrot) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBorder(new EmptyBorder(0, 0, 8, 0));
        JLabel etykietaTytulu = new JLabel(tytul);
        etykietaTytulu.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(etykietaTytulu, BorderLayout.WEST);
        JButton przyciskWstecz = new JButton("Wstecz");
        przyciskWstecz.setFont(new Font("Arial", Font.PLAIN, 11));
        przyciskWstecz.addActionListener(e -> ukladKart.show(panelGlowny, powrot));
        panel.add(przyciskWstecz, BorderLayout.EAST);
        return panel;
    }

    private JPanel zbudujListeListingow() {
        panelAktywnychListingow = new JPanel(new BorderLayout(0, 8));
        panelAktywnychListingow.setBorder(new EmptyBorder(16, 20, 16, 20));
        panelAktywnychListingow.add(zbudujNaglowek("Aktywne listingi", "menu"), BorderLayout.NORTH);

        tabelaListingow = new JTable() {
            @Override
            public boolean isCellEditable(int wiersz, int kolumna) {
                return false;
            }
        };
        tabelaListingow.setRowHeight(24);
        tabelaListingow.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelAktywnychListingow.add(new JScrollPane(tabelaListingow), BorderLayout.CENTER);

        JPanel dolny = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton przyciskSzczegoly = new JButton("Szczegoly i oferty");
        przyciskSzczegoly.addActionListener(e -> {
            int wybranyWiersz = tabelaListingow.getSelectedRow();
            if (wybranyWiersz < 0) {
                pokazKomunikat("Zaznacz listing");
                return;
            }
            odswiezSzczegoly(aktywneListingi.get(wybranyWiersz));
            ukladKart.show(panelGlowny, "szczegoly");
        });

        JButton przyciskZlozOferte = new JButton("Zloz oferte na zaznaczony");
        przyciskZlozOferte.addActionListener(e -> {
            int wybranyWiersz = tabelaListingow.getSelectedRow();
            if (wybranyWiersz < 0) {
                pokazKomunikat("Zaznacz listing");
                return;
            }
            odswiezFormularzOferty(aktywneListingi.get(wybranyWiersz));
            ukladKart.show(panelGlowny, "oferta");
        });

        dolny.add(przyciskSzczegoly);
        dolny.add(przyciskZlozOferte);
        panelAktywnychListingow.add(dolny, BorderLayout.SOUTH);
        return panelAktywnychListingow;
    }

    private void odswiezListeListingow() {
        aktywneListingi.clear();
        for (Listing listing : Listing.wszystkieListingi) {
            if (listing.getStatus() == Listing.Status.AKTYWNY && listing.getWystawiajacy() != zalogowany) {
                aktywneListingi.add(listing);
            }
        }
        String[] kolumny = { "ID", "Gry", "Wystawia", "Typ" };
        Object[][] dane = new Object[aktywneListingi.size()][4];
        for (int i = 0; i < aktywneListingi.size(); i++) {
            Listing listing = aktywneListingi.get(i);
            dane[i][0] = listing.getId();
            dane[i][1] = nazwyGier(listing.getWystawioneGry());
            dane[i][2] = listing.getWystawiajacy().getImie() + " " + listing.getWystawiajacy().getNazwisko();
            dane[i][3] = listing.getWystawiajacy().getTyp();
        }
        tabelaListingow.setModel(new DefaultTableModel(dane, kolumny));
    }

    private JPanel zbudujSzczegolyListingu() {
        panelSzczegolowListingu = new JPanel(new BorderLayout(0, 8));
        panelSzczegolowListingu.setBorder(new EmptyBorder(16, 20, 16, 20));
        return panelSzczegolowListingu;
    }

    private void odswiezSzczegoly(Listing listing) {
        panelSzczegolowListingu.removeAll();

        JPanel naglowek = new JPanel(new BorderLayout(8, 0));
        naglowek.setBorder(new EmptyBorder(0, 0, 8, 0));
        JLabel tytul = new JLabel("Listing " + listing.getId());
        tytul.setFont(new Font("Arial", Font.BOLD, 16));
        naglowek.add(tytul, BorderLayout.WEST);
        JButton przyciskWstecz = new JButton("Wstecz");
        przyciskWstecz.addActionListener(e -> {
            odswiezListeListingow();
            ukladKart.show(panelGlowny, "listingi");
        });
        naglowek.add(przyciskWstecz, BorderLayout.EAST);
        panelSzczegolowListingu.add(naglowek, BorderLayout.NORTH);

        JPanel informacje = new JPanel(new GridLayout(3, 2, 6, 4));
        informacje.setBorder(BorderFactory.createTitledBorder("Listing"));
        informacje.add(new JLabel("Gry:"));
        informacje.add(new JLabel(nazwyGier(listing.getWystawioneGry())));
        informacje.add(new JLabel("Wystawia:"));
        informacje.add(new JLabel(listing.getWystawiajacy().getImie() + " " + listing.getWystawiajacy().getNazwisko()));
        informacje.add(new JLabel("Status:"));
        informacje.add(new JLabel(listing.getStatus().toString()));

        JPanel panelOfert = new JPanel();
        panelOfert.setLayout(new BoxLayout(panelOfert, BoxLayout.Y_AXIS));
        panelOfert.setBorder(BorderFactory.createTitledBorder("Oferty"));

        List<Oferta> oferty = ofertyNaListing(listing);
        if (oferty.isEmpty()) {
            JLabel brak = new JLabel("Brak ofert");
            brak.setForeground(Color.GRAY);
            brak.setBorder(new EmptyBorder(4, 4, 4, 4));
            panelOfert.add(brak);
        }
        for (Oferta oferta : oferty) {
            panelOfert.add(zbudujWierszOferty(oferta, listing, false));
        }

        JPanel srodek = new JPanel();
        srodek.setLayout(new BoxLayout(srodek, BoxLayout.Y_AXIS));
        srodek.add(informacje);
        srodek.add(Box.createVerticalStrut(8));
        srodek.add(panelOfert);

        panelSzczegolowListingu.add(new JScrollPane(srodek), BorderLayout.CENTER);

        JPanel dol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton przyciskZlozOferte = new JButton("Zloz oferte na ten listing");
        przyciskZlozOferte.addActionListener(e -> {
            odswiezFormularzOferty(listing);
            ukladKart.show(panelGlowny, "oferta");
        });
        dol.add(przyciskZlozOferte);
        panelSzczegolowListingu.add(dol, BorderLayout.SOUTH);

        panelSzczegolowListingu.revalidate();
        panelSzczegolowListingu.repaint();
    }

    private JPanel zbudujFormularzListingu() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        panel.add(zbudujNaglowek("Stworz listing", "menu"), BorderLayout.NORTH);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dane listingu"));
        GridBagConstraints ograniczenia = new GridBagConstraints();
        ograniczenia.insets = new Insets(6, 6, 6, 6);
        ograniczenia.anchor = GridBagConstraints.WEST;

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 0;
        ograniczenia.gridwidth = 2;
        etykietaLimitu = new JLabel(" ");
        etykietaLimitu.setForeground(Color.GRAY);
        formularz.add(etykietaLimitu, ograniczenia);

        ograniczenia.gridy = 1;
        ograniczenia.gridwidth = 1;
        ograniczenia.anchor = GridBagConstraints.NORTHWEST;
        formularz.add(new JLabel("Wybierz gry:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.BOTH;
        ograniczenia.weighty = 1;
        ograniczenia.weightx = 1;
        modelGierListingu = new DefaultListModel<>();
        listaGierListingu = new JList<>(modelGierListingu);
        listaGierListingu.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        formularz.add(new JScrollPane(listaGierListingu), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.gridy = 2;
        ograniczenia.weighty = 0;
        ograniczenia.fill = GridBagConstraints.NONE;
        etykietaBleduListingu = new JLabel(" ");
        etykietaBleduListingu.setForeground(Color.RED);
        formularz.add(etykietaBleduListingu, ograniczenia);

        ograniczenia.gridy = 3;
        ograniczenia.anchor = GridBagConstraints.EAST;
        JButton przyciskWystaw = new JButton("Wystaw listing");
        przyciskWystaw.addActionListener(e -> wystawListing());
        formularz.add(przyciskWystaw, ograniczenia);

        panel.add(formularz, BorderLayout.CENTER);
        return panel;
    }

    private void odswiezFormularzListingu() {
        modelGierListingu.clear();
        for (Gra gra : zalogowany.getKolekcja().getKolekcja()) {
            if (!Listing.czyGraZajeta(gra)) {
                modelGierListingu.addElement(gra);
            }
        }
        long liczbaAktywnych = zalogowany.getListingi().stream()
                .filter(listing -> listing.getStatus() == Listing.Status.AKTYWNY)
                .count();
        if (zalogowany instanceof KolekcjonerNormal) {
            etykietaLimitu.setText("Aktywne listingi: " + liczbaAktywnych + " / " + zalogowany.getLimitListingow());
        } else {
            etykietaLimitu.setText("Aktywne listingi: " + liczbaAktywnych);
        }
        etykietaBleduListingu.setText(" ");
    }

    private void wystawListing() {
        List<Gra> wybrane = listaGierListingu.getSelectedValuesList();
        if (wybrane.isEmpty()) {
            etykietaBleduListingu.setText("Wybierz gre");
            return;
        }
        try {
            new Listing(zalogowany, wybrane);
            pokazKomunikat("Listing wystawiony");
            ukladKart.show(panelGlowny, "menu");
        } catch (IllegalArgumentException | IllegalStateException wyjatek) {
            etykietaBleduListingu.setText(wyjatek.getMessage());
        }
    }

    private JPanel zbudujFormularzOferty() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        panel.add(zbudujNaglowek("Zloz oferte", "menu"), BorderLayout.NORTH);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dane oferty"));
        GridBagConstraints ograniczenia = new GridBagConstraints();
        ograniczenia.insets = new Insets(6, 6, 6, 6);
        ograniczenia.anchor = GridBagConstraints.WEST;

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 0;
        formularz.add(new JLabel("Listing:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.HORIZONTAL;
        ograniczenia.weightx = 1;
        listaListingow = new JComboBox<>();
        formularz.add(listaListingow, ograniczenia);

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 1;
        ograniczenia.weightx = 0;
        ograniczenia.fill = GridBagConstraints.NONE;
        ograniczenia.anchor = GridBagConstraints.NORTHWEST;
        formularz.add(new JLabel("Moje gry:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.BOTH;
        ograniczenia.weighty = 1;
        modelGierOferty = new DefaultListModel<>();
        listaGierOferty = new JList<>(modelGierOferty);
        listaGierOferty.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        formularz.add(new JScrollPane(listaGierOferty), ograniczenia);

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 2;
        ograniczenia.weighty = 0;
        ograniczenia.fill = GridBagConstraints.NONE;
        ograniczenia.anchor = GridBagConstraints.WEST;
        formularz.add(new JLabel("Doplata:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.HORIZONTAL;
        poleDoplaty = new JTextField("0");
        formularz.add(poleDoplaty, ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.gridy = 3;
        etykietaBleduOferty = new JLabel(" ");
        etykietaBleduOferty.setForeground(Color.RED);
        formularz.add(etykietaBleduOferty, ograniczenia);

        ograniczenia.gridy = 4;
        ograniczenia.anchor = GridBagConstraints.EAST;
        JButton przyciskZloz = new JButton("Zloz oferte");
        przyciskZloz.addActionListener(e -> zlozOferte());
        formularz.add(przyciskZloz, ograniczenia);

        panel.add(formularz, BorderLayout.CENTER);
        return panel;
    }

    private void odswiezFormularzOferty(Listing preselekcja) {
        listaListingow.removeAllItems();
        for (Listing listing : Listing.wszystkieListingi) {
            if (listing.getStatus() == Listing.Status.AKTYWNY && listing.getWystawiajacy() != zalogowany) {
                listaListingow.addItem(listing);
            }
        }
        if (preselekcja != null) {
            listaListingow.setSelectedItem(preselekcja);
        }
        modelGierOferty.clear();
        for (Gra gra : zalogowany.getKolekcja().getKolekcja()) {
            if (!Listing.czyGraZajeta(gra)) {
                modelGierOferty.addElement(gra);
            }
        }
        poleDoplaty.setText("0");
        etykietaBleduOferty.setText(" ");
    }

    private void zlozOferte() {
        List<Gra> wybrane = listaGierOferty.getSelectedValuesList();
        if (wybrane.isEmpty()) {
            etykietaBleduOferty.setText("Wybierz gre");
            return;
        }
        int doplata;
        try {
            doplata = Integer.parseInt(poleDoplaty.getText().trim());
        } catch (NumberFormatException wyjatek) {
            etykietaBleduOferty.setText("Doplata musi byc liczba");
            return;
        }
        Listing wybranyListing = (Listing) listaListingow.getSelectedItem();
        if (wybranyListing == null) {
            etykietaBleduOferty.setText("Wybierz listing");
            return;
        }
        try {
            new Oferta(wybranyListing, zalogowany, wybrane, doplata);
            pokazKomunikat("Oferta zlozona");
            ukladKart.show(panelGlowny, "menu");
        } catch (IllegalArgumentException | IllegalStateException wyjatek) {
            etykietaBleduOferty.setText(wyjatek.getMessage());
        }
    }

    private JPanel zbudujPanelKolekcji() {
        panelKolekcji = new JPanel(new BorderLayout(0, 8));
        panelKolekcji.setBorder(new EmptyBorder(16, 20, 16, 20));
        panelKolekcji.add(zbudujNaglowek("Moja kolekcja", "menu"), BorderLayout.NORTH);
        return panelKolekcji;
    }

    private void odswiezKolekcje() {
        if (panelKolekcji.getComponentCount() > 1) {
            panelKolekcji.remove(1);
        }
        JPanel srodek = new JPanel();
        srodek.setLayout(new BoxLayout(srodek, BoxLayout.Y_AXIS));

        JPanel gora = new JPanel(new BorderLayout());
        gora.setBorder(new EmptyBorder(0, 0, 8, 0));
        gora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel informacje = new JLabel(zalogowany.getImie() + " " + zalogowany.getNazwisko()
                + " | " + zalogowany.getTyp()
                + " | " + zalogowany.getPoziom()
                + " | " + zalogowany.getRegion());
        informacje.setForeground(Color.GRAY);
        gora.add(informacje, BorderLayout.WEST);
        JButton przyciskRecenzja = new JButton("Dodaj recenzje");
        przyciskRecenzja.addActionListener(e -> {
            odswiezFormularzRecenzji();
            ukladKart.show(panelGlowny, "recenzja");
        });
        gora.add(przyciskRecenzja, BorderLayout.EAST);
        srodek.add(gora);

        List<Gra> gry = zalogowany.getKolekcja().getKolekcja();
        if (gry.isEmpty()) {
            JLabel brak = new JLabel("Brak gier");
            brak.setForeground(Color.GRAY);
            srodek.add(brak);
        } else {
            String[] kolumny = { "Tytul", "Wydawnictwo", "Wartosc", "Opis", "Status" };
            Object[][] dane = new Object[gry.size()][5];
            for (int i = 0; i < gry.size(); i++) {
                Gra gra = gry.get(i);
                dane[i][0] = gra.getTytul();
                dane[i][1] = gra.getWydawnictwo().getNazwa();
                dane[i][2] = gra.getWartosc();
                dane[i][3] = gra.getOpis();
                dane[i][4] = Listing.czyGraZajeta(gra) ? "Zajeta" : "Dostepna";
            }
            JTable tabela = new JTable(dane, kolumny) {
                @Override
                public boolean isCellEditable(int wiersz, int kolumna) {
                    return false;
                }
            };
            tabela.setRowHeight(22);
            srodek.add(new JScrollPane(tabela));
        }

        panelKolekcji.add(srodek, BorderLayout.CENTER);
        panelKolekcji.revalidate();
        panelKolekcji.repaint();
    }

    private JPanel zbudujFormularzRecenzji() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        panel.add(zbudujNaglowek("Dodaj recenzje", "kolekcja"), BorderLayout.NORTH);

        JPanel formularz = new JPanel(new GridBagLayout());
        formularz.setBorder(BorderFactory.createTitledBorder("Dane recenzji"));
        GridBagConstraints ograniczenia = new GridBagConstraints();
        ograniczenia.insets = new Insets(6, 6, 6, 6);
        ograniczenia.anchor = GridBagConstraints.WEST;

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 0;
        formularz.add(new JLabel("Gra:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.HORIZONTAL;
        ograniczenia.weightx = 1;
        listaGierRecenzji = new JComboBox<>();
        formularz.add(listaGierRecenzji, ograniczenia);

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 1;
        ograniczenia.weightx = 0;
        ograniczenia.fill = GridBagConstraints.NONE;
        formularz.add(new JLabel("Ocena:"), ograniczenia);

        ograniczenia.gridx = 1;
        listaOcen = new JComboBox<>(new Integer[]{ 1, 2, 3, 4, 5 });
        formularz.add(listaOcen, ograniczenia);

        ograniczenia.gridx = 0;
        ograniczenia.gridy = 2;
        ograniczenia.anchor = GridBagConstraints.NORTHWEST;
        formularz.add(new JLabel("Tresc:"), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.fill = GridBagConstraints.BOTH;
        ograniczenia.weighty = 1;
        poleTresciRecenzji = new JTextArea(5, 20);
        poleTresciRecenzji.setLineWrap(true);
        poleTresciRecenzji.setWrapStyleWord(true);
        formularz.add(new JScrollPane(poleTresciRecenzji), ograniczenia);

        ograniczenia.gridx = 1;
        ograniczenia.gridy = 3;
        ograniczenia.weighty = 0;
        ograniczenia.fill = GridBagConstraints.NONE;
        etykietaBleduRecenzji = new JLabel(" ");
        etykietaBleduRecenzji.setForeground(Color.RED);
        formularz.add(etykietaBleduRecenzji, ograniczenia);

        ograniczenia.gridy = 4;
        ograniczenia.anchor = GridBagConstraints.EAST;
        JButton przyciskDodaj = new JButton("Dodaj recenzje");
        przyciskDodaj.addActionListener(e -> dodajRecenzje());
        formularz.add(przyciskDodaj, ograniczenia);

        panel.add(formularz, BorderLayout.CENTER);
        return panel;
    }

    private void odswiezFormularzRecenzji() {
        listaGierRecenzji.removeAllItems();
        for (Gra gra : zalogowany.getKolekcja().getKolekcja()) {
            listaGierRecenzji.addItem(gra);
        }
        listaOcen.setSelectedItem(5);
        poleTresciRecenzji.setText("");
        etykietaBleduRecenzji.setText(" ");
    }

    private void dodajRecenzje() {
        Gra wybranaGra = (Gra) listaGierRecenzji.getSelectedItem();
        if (wybranaGra == null) {
            etykietaBleduRecenzji.setText("Wybierz gre");
            return;
        }
        Integer ocena = (Integer) listaOcen.getSelectedItem();
        String tresc = poleTresciRecenzji.getText().trim();
        try {
            new Recenzja(zalogowany, wybranaGra, ocena, tresc);
            pokazKomunikat("Recenzja dodana");
            odswiezKolekcje();
            ukladKart.show(panelGlowny, "kolekcja");
        } catch (IllegalArgumentException wyjatek) {
            etykietaBleduRecenzji.setText(wyjatek.getMessage());
        }
    }

    private JPanel zbudujPanelMoichListingow() {
        panelMoichListingow = new JPanel(new BorderLayout(0, 8));
        panelMoichListingow.setBorder(new EmptyBorder(16, 20, 16, 20));
        panelMoichListingow.add(zbudujNaglowek("Moje listingi", "menu"), BorderLayout.NORTH);
        return panelMoichListingow;
    }

    private void odswiezMojeListingi() {
        if (panelMoichListingow.getComponentCount() > 1) {
            panelMoichListingow.remove(1);
        }
        JPanel srodek = new JPanel();
        srodek.setLayout(new BoxLayout(srodek, BoxLayout.Y_AXIS));

        List<Listing> mojeListingi = new ArrayList<>();
        for (Listing listing : Listing.wszystkieListingi) {
            if (listing.getWystawiajacy() == zalogowany) {
                mojeListingi.add(listing);
            }
        }

        if (mojeListingi.isEmpty()) {
            JLabel brak = new JLabel("Brak listingow");
            brak.setForeground(Color.GRAY);
            srodek.add(brak);
        } else {
            for (Listing listing : mojeListingi) {
                JPanel sekcja = new JPanel();
                sekcja.setLayout(new BoxLayout(sekcja, BoxLayout.Y_AXIS));
                sekcja.setBorder(BorderFactory.createTitledBorder(
                        "Listing " + listing.getId() + " - " + nazwyGier(listing.getWystawioneGry())
                        + " - " + listing.getStatus()));

                List<Oferta> oferty = ofertyNaListing(listing);
                if (oferty.isEmpty()) {
                    JLabel brak = new JLabel("Brak ofert");
                    brak.setForeground(Color.GRAY);
                    sekcja.add(brak);
                } else {
                    for (Oferta oferta : oferty) {
                        sekcja.add(zbudujWierszOferty(oferta, listing,
                                listing.getStatus() == Listing.Status.AKTYWNY));
                    }
                }
                srodek.add(sekcja);
                srodek.add(Box.createVerticalStrut(6));
            }
        }

        panelMoichListingow.add(new JScrollPane(srodek), BorderLayout.CENTER);
        panelMoichListingow.revalidate();
        panelMoichListingow.repaint();
    }

    private JPanel zbudujPanelMoichOfert() {
        panelMoichOfert = new JPanel(new BorderLayout(0, 8));
        panelMoichOfert.setBorder(new EmptyBorder(16, 20, 16, 20));
        panelMoichOfert.add(zbudujNaglowek("Moje oferty", "menu"), BorderLayout.NORTH);
        return panelMoichOfert;
    }

    private void odswiezMojeOferty() {
        if (panelMoichOfert.getComponentCount() > 1) {
            panelMoichOfert.remove(1);
        }
        JPanel srodek = new JPanel(new BorderLayout());

        List<Oferta> mojeOferty = new ArrayList<>();
        for (Oferta oferta : Oferta.wszystkieOferty) {
            if (oferta.getSkladajacy() == zalogowany) {
                mojeOferty.add(oferta);
            }
        }

        if (mojeOferty.isEmpty()) {
            JLabel brak = new JLabel("Brak ofert");
            brak.setForeground(Color.GRAY);
            srodek.add(brak, BorderLayout.NORTH);
        } else {
            String[] kolumny = { "ID", "Listing", "Oferowane gry", "Doplata", "Status" };
            Object[][] dane = new Object[mojeOferty.size()][5];
            for (int i = 0; i < mojeOferty.size(); i++) {
                Oferta oferta = mojeOferty.get(i);
                dane[i][0] = oferta.getId();
                dane[i][1] = oferta.getListing().getId();
                dane[i][2] = nazwyGier(oferta.getOferowane());
                dane[i][3] = oferta.getDoplata();
                dane[i][4] = oferta.getStatus();
            }
            JTable tabela = new JTable(dane, kolumny) {
                @Override
                public boolean isCellEditable(int wiersz, int kolumna) {
                    return false;
                }
            };
            tabela.setRowHeight(22);
            srodek.add(new JScrollPane(tabela), BorderLayout.CENTER);
        }

        panelMoichOfert.add(srodek, BorderLayout.CENTER);
        panelMoichOfert.revalidate();
        panelMoichOfert.repaint();
    }

    private JPanel zbudujPanelPowiadomien() {
        panelPowiadomien = new JPanel(new BorderLayout(0, 8));
        panelPowiadomien.setBorder(new EmptyBorder(16, 20, 16, 20));
        panelPowiadomien.add(zbudujNaglowek("Powiadomienia", "menu"), BorderLayout.NORTH);
        return panelPowiadomien;
    }

    private void odswiezPowiadomienia() {
        if (panelPowiadomien.getComponentCount() > 1) {
            panelPowiadomien.remove(1);
        }
        JPanel srodek = new JPanel();
        srodek.setLayout(new BoxLayout(srodek, BoxLayout.Y_AXIS));

        List<Powiadomienie> powiadomienia = zalogowany.getPowiadomienia();
        if (powiadomienia.isEmpty()) {
            JLabel brak = new JLabel("Brak powiadomien");
            brak.setForeground(Color.GRAY);
            srodek.add(brak);
        } else {
            for (Powiadomienie powiadomienie : powiadomienia) {
                JPanel wiersz = new JPanel(new BorderLayout(8, 0));
                wiersz.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                wiersz.setBorder(new EmptyBorder(2, 4, 2, 4));

                JLabel tekst = new JLabel(powiadomienie.getTresc());
                tekst.setFont(new Font("Arial", powiadomienie.czyPrzeczytane() ? Font.PLAIN : Font.BOLD, 12));
                wiersz.add(tekst, BorderLayout.CENTER);

                if (!powiadomienie.czyPrzeczytane()) {
                    JButton przyciskOznacz = new JButton("Oznacz");
                    przyciskOznacz.addActionListener(e -> {
                        powiadomienie.oznaczJakoPrzeczytane();
                        odswiezPowiadomienia();
                    });
                    wiersz.add(przyciskOznacz, BorderLayout.EAST);
                }
                srodek.add(wiersz);
                srodek.add(new JSeparator());
            }
        }

        panelPowiadomien.add(new JScrollPane(srodek), BorderLayout.CENTER);
        panelPowiadomien.revalidate();
        panelPowiadomien.repaint();
    }

    private JPanel zbudujWierszOferty(Oferta oferta, Listing listing, boolean pokazPrzyciski) {
        JPanel wiersz = new JPanel(new BorderLayout(8, 0));
        wiersz.setBorder(new EmptyBorder(4, 4, 4, 4));
        wiersz.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        String opis = oferta.getSkladajacy().getImie() + " " + oferta.getSkladajacy().getNazwisko()
                + " - " + nazwyGier(oferta.getOferowane())
                + " - doplata " + oferta.getDoplata()
                + " - " + oferta.getStatus();
        wiersz.add(new JLabel(opis), BorderLayout.CENTER);

        if (pokazPrzyciski && oferta.getStatus() == Oferta.Status.AKTYWNA) {
            JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));

            JButton przyciskAkceptuj = new JButton("Akceptuj");
            przyciskAkceptuj.addActionListener(e -> {
                oferta.akceptuj();
                pokazKomunikat("Wymiana zakonczona");
                odswiezMojeListingi();
            });

            JButton przyciskOdrzuc = new JButton("Odrzuc");
            przyciskOdrzuc.addActionListener(e -> {
                oferta.odrzuc();
                pokazKomunikat("Oferta odrzucona");
                odswiezMojeListingi();
            });

            przyciski.add(przyciskAkceptuj);
            przyciski.add(przyciskOdrzuc);
            wiersz.add(przyciski, BorderLayout.EAST);
        }
        return wiersz;
    }

    private List<Oferta> ofertyNaListing(Listing listing) {
        return listing.getOferty();
    }

    private String nazwyGier(List<Gra> gry) {
        StringBuilder wynik = new StringBuilder();
        for (Gra gra : gry) {
            if (wynik.length() > 0) {
                wynik.append(", ");
            }
            wynik.append(gra.getTytul());
        }
        return wynik.toString();
    }

    private void pokazKomunikat(String wiadomosc) {
        JOptionPane.showMessageDialog(this, wiadomosc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
