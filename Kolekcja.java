import java.io.Serializable;
import java.util.*;

public class Kolekcja implements Serializable {
    private ArrayList<Gra> kolekcja;

    public Kolekcja() {
        this.kolekcja = new ArrayList<>();
    }

    public int getWartoscKolekcji() {
        int wartosc = 0;
        for (Gra gra : kolekcja) {
            wartosc += gra.getWartosc();
        }
        return wartosc;
    }

    public void dodajGre(Gra gra) {
        if (gra == null) {
            throw new IllegalArgumentException("Gra nie moze byc null");
        }
        if (!kolekcja.contains(gra)) {
            kolekcja.add(gra);
        }
    }

    public void dodajGre(String tytul, Date data, int wartosc, Wydawnictwo wydawnictwo) {
        kolekcja.add(new Gra(tytul, data, wartosc, wydawnictwo));
    }

    public void usunGre(Gra gra) {
        if (kolekcja.contains(gra)) {
            kolekcja.remove(gra);
        }
    }

    public Gra getGra(int indeks) {
        return kolekcja.get(indeks);
    }

    public Gra getGra(String tytul) {
        for (Gra gra : kolekcja) {
            if (gra.getTytul().equals(tytul)) {
                return gra;
            }
        }
        return null;
    }

    public List<Gra> getKolekcja() { return kolekcja; }
}
