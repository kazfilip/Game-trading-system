import java.io.Serializable;
import java.util.*;

public class Listing implements Serializable {
    public enum Status { AKTYWNY, ZAMKNIETY }

    public static List<Listing> wszystkieListingi = new ArrayList<>();
    private static int licznikId = 0;

    private int id;
    private Kolekcjoner wystawiajacy;
    private List<Gra> wystawioneGry;
    private Status status;
    private List<Oferta> oferty = new ArrayList<>();

    public Listing(Kolekcjoner wystawiajacy, List<Gra> wystawioneGry) {
        if (wystawiajacy == null) {
            throw new IllegalArgumentException("Wystawiajacy nie moze byc null");
        }
        if (wystawioneGry == null || wystawioneGry.isEmpty()) {
            throw new IllegalArgumentException("Lista gier nie moze byc pusta");
        }
        for (Gra gra : wystawioneGry) {
            if (czyGraZajeta(gra)) {
                throw new IllegalStateException("Gra jest juz uzyta gdzie indziej");
            }
        }
        this.id = ++licznikId;
        this.wystawiajacy = wystawiajacy;
        this.wystawioneGry = new ArrayList<>(wystawioneGry);
        this.status = Status.AKTYWNY;
        wszystkieListingi.add(this);
        wystawiajacy.dodajListing(this);
    }

    public static boolean czyGraZajeta(Gra gra) {
        for (Listing listing : wszystkieListingi) {
            if (listing.status == Status.AKTYWNY && listing.wystawioneGry.contains(gra)) {
                return true;
            }
        }
        for (Oferta oferta : Oferta.wszystkieOferty) {
            if (oferta.getStatus() == Oferta.Status.AKTYWNA && oferta.getOferowane().contains(gra)) {
                return true;
            }
        }
        return false;
    }
    public void dodajOferte(Oferta oferta) {
        if (!oferty.contains(oferta)) {
            oferty.add(oferta);
        }
    }
    public void zamknij() {
        this.status = Status.ZAMKNIETY;
    }

    @Override
    public String toString() {
        StringBuilder gry = new StringBuilder();
        for (Gra gra : wystawioneGry) {
            if (gry.length() > 0) {
                gry.append(", ");
            }
            gry.append(gra.getTytul());
        }
        return "Listing " + id + " - " + gry + " - " + wystawiajacy.getImie() + " " + wystawiajacy.getNazwisko();
    }
    public List<Oferta> getOferty(){ return oferty;}
    public int getId() { return id; }
    public Kolekcjoner getWystawiajacy() { return wystawiajacy; }
    public List<Gra> getWystawioneGry() { return wystawioneGry; }
    public Status getStatus() { return status; }
}
