import java.io.Serializable;
import java.util.*;

public abstract class Kolekcjoner extends Osoba implements Sprzedajacy, Kupujacy {
    public enum RolaKolekcjonera { SPRZEDAJACY, KUPUJACY }
    public enum Poziom { POCZATKUJACY, DOSWIADCZONY }
    public enum Region { KRAJOWY, MIEDZYNARODOWY }

    public static List<Kolekcjoner> wszyscyKolekcjonerzy = new ArrayList<>();

    private static int idLicznik = 0;
    private int idKolekcjonera;

    private Kolekcja kolekcja;
    private List<Listing> listingi = new ArrayList<>();
    private List<Oferta> oferty = new ArrayList<>();
    private Map<Integer, Listing> listingiId = new HashMap<>();
    private Set<RolaKolekcjonera> role = new HashSet<>();
    private List<Powiadomienie> powiadomienia = new ArrayList<>();
    private Poziom poziom;
    private Region region;

    public Kolekcjoner(String imie, String nazwisko, Poziom poziom, Region region) {
        super(imie, nazwisko);
        if (poziom == null) {
            throw new IllegalArgumentException("Poziom nie moze byc null");
        }
        if (region == null) {
            throw new IllegalArgumentException("Region nie moze byc null");
        }
        this.idKolekcjonera = ++idLicznik;
        this.kolekcja = new Kolekcja();
        this.poziom = poziom;
        this.region = region;
        wszyscyKolekcjonerzy.add(this);
    }

    public abstract String getTyp();

    public void dodajRole(RolaKolekcjonera rola) {
        if (rola == null) {
            throw new IllegalArgumentException("Rola nie moze byc null");
        }
        role.add(rola);
    }

    public void usunRole(RolaKolekcjonera rola) {
        role.remove(rola);
    }

    public boolean maRole(RolaKolekcjonera rola) {
        return role.contains(rola);
    }

    @Override
    public void dodajListing(Listing listing) {
        if (listing == null) {
            throw new IllegalArgumentException("Listing nie moze byc null");
        }
        if (listingi.contains(listing)) {
            return;
        }
        long aktywne = listingi.stream()
                .filter(l -> l.getStatus() == Listing.Status.AKTYWNY)
                .count();
        if (aktywne >= getLimitListingow()) {
            throw new IllegalStateException("Osiagnieto limit listingow");
        }
        listingi.add(listing);
        listingiId.put(listing.getId(), listing);
        dodajRole(RolaKolekcjonera.SPRZEDAJACY);
    }

    @Override
    public void dodajOferte(Oferta oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("Oferta nie moze byc null");
        }
        if (!oferty.contains(oferta)) {
            oferty.add(oferta);
            dodajRole(RolaKolekcjonera.KUPUJACY);
        }
    }

    public void dodajPowiadomienie(Powiadomienie powiadomienie) {
        if (powiadomienie != null) {
            powiadomienia.add(powiadomienie);
        }
    }

    public List<Powiadomienie> getPowiadomienia() { return powiadomienia; }

    public Listing znajdzListing(int id) {
        return listingiId.get(id);
    }

    public void pokazKolekcje() {
        if (kolekcja.getKolekcja().isEmpty()) {
            System.out.println("Brak gier w kolekcji");
            return;
        }
        for (Gra gra : kolekcja.getKolekcja()) {
            System.out.println(gra.getTytul());
        }
    }

    @Override
    public void wyswietlInfo() {
        System.out.println("Imie: " + imie + " Nazwisko: " + nazwisko
                + " Id: " + idKolekcjonera + " Typ: " + getTyp()
                + " Poziom: " + poziom + " Region: " + region
                + " Role: " + role);
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko + " (" + getTyp() + ")";
    }

    @Override
    public List<Listing> getListingi() { return listingi; }
    @Override
    public List<Oferta> getOferty() { return oferty; }
    public Kolekcja getKolekcja() { return kolekcja; }
    public int getId() { return idKolekcjonera; }
    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public Set<RolaKolekcjonera> getRole() { return Collections.unmodifiableSet(role); }
    public Poziom getPoziom() { return poziom; }
    public Region getRegion() { return region; }

    public void setKolekcja(Kolekcja kolekcja) { this.kolekcja = kolekcja; }

    public void setImie(String imie) {
        if (imie == null || imie.isBlank()) {
            throw new IllegalArgumentException("Imie nie moze byc puste");
        }
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        if (nazwisko == null || nazwisko.isBlank()) {
            throw new IllegalArgumentException("Nazwisko nie moze byc puste");
        }
        this.nazwisko = nazwisko;
    }

    public void setPoziom(Poziom poziom) {
        if (poziom == null) {
            throw new IllegalArgumentException("Poziom nie moze byc null");
        }
        this.poziom = poziom;
    }

    public void setRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region nie moze byc null");
        }
        this.region = region;
    }
}
