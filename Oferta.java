import java.io.Serializable;
import java.util.*;

public class Oferta implements Serializable {
    public enum Status { AKTYWNA, ZAAKCEPTOWANA, ODRZUCONA }

    public static List<Oferta> wszystkieOferty = new ArrayList<>();
    private static int licznikId = 0;

    private int id;
    private Listing listing;
    private Kolekcjoner skladajacy;
    private List<Gra> oferowaneGry;
    private Status status;
    private int doplata;

    public Oferta(Listing listing, Kolekcjoner skladajacy, List<Gra> oferowaneGry, int doplata) {
        if (listing == null) {
            throw new IllegalArgumentException("Listing nie moze byc null");
        }
        if (skladajacy == null) {
            throw new IllegalArgumentException("Skladajacy nie moze byc null");
        }
        if (oferowaneGry == null || oferowaneGry.isEmpty()) {
            throw new IllegalArgumentException("Lista gier nie moze byc pusta");
        }
        if (listing.getStatus() != Listing.Status.AKTYWNY) {
            throw new IllegalStateException("Listing jest juz zamkniety");
        }
        if (skladajacy.equals(listing.getWystawiajacy())) {
            throw new IllegalArgumentException("Nie mozna skladac oferty na wlasny listing");
        }
        if (doplata < 0) {
            throw new IllegalArgumentException("Doplata nie moze byc ujemna");
        }
        for (Gra gra : oferowaneGry) {
            if (Listing.czyGraZajeta(gra)) {
                throw new IllegalStateException("Gra jest juz uzyta gdzie indziej");
            }
        }
        this.id = ++licznikId;
        this.listing = listing;
        this.skladajacy = skladajacy;
        this.oferowaneGry = new ArrayList<>(oferowaneGry);
        this.doplata = doplata;
        this.status = Status.AKTYWNA;
        wszystkieOferty.add(this);
        listing.dodajOferte(this);
        skladajacy.dodajOferte(this);
        listing.getWystawiajacy().dodajOferte(this);
        listing.getWystawiajacy().dodajPowiadomienie(new Powiadomienie("Nowa oferta na listing " + listing.getId()));
    }

    public void akceptuj() {
        if (this.status != Status.AKTYWNA) {
            throw new IllegalStateException("Oferta nie jest aktywna");
        }
        this.status = Status.ZAAKCEPTOWANA;
        Kolekcjoner wystawiajacy = listing.getWystawiajacy();
        for (Gra gra : listing.getWystawioneGry()) {
            wystawiajacy.getKolekcja().usunGre(gra);
            skladajacy.getKolekcja().dodajGre(gra);
        }
        for (Gra gra : oferowaneGry) {
            skladajacy.getKolekcja().usunGre(gra);
            wystawiajacy.getKolekcja().dodajGre(gra);
        }
        listing.zamknij();
    }

    public void odrzuc() {
        if (this.status != Status.AKTYWNA) {
            throw new IllegalStateException("Oferta nie jest aktywna");
        }
        this.status = Status.ODRZUCONA;
    }

    public int getId() { return id; }
    public Listing getListing() { return listing; }
    public Kolekcjoner getSkladajacy() { return skladajacy; }
    public List<Gra> getOferowane() { return oferowaneGry; }
    public Status getStatus() { return status; }
    public int getDoplata() { return doplata; }
}
