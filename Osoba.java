import java.io.Serializable;

public abstract class Osoba implements Serializable {
    protected String imie;
    protected String nazwisko;

    public Osoba(String imie, String nazwisko) {
        if (imie == null || imie.isBlank()) {
            throw new IllegalArgumentException("Imie nie moze byc puste");
        }
        if (nazwisko == null || nazwisko.isBlank()) {
            throw new IllegalArgumentException("Nazwisko nie moze byc puste");
        }
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    public abstract void wyswietlInfo();
}
