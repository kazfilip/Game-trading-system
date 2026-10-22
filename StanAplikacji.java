import java.io.*;
import java.util.*;

public class StanAplikacji implements Serializable {
    private List<Gra> gry;
    private List<Kolekcjoner> kolekcjonerzy;
    private List<Listing> listingi;
    private List<Oferta> oferty;

    private static final String PLIK = "stan.txt";

    public static void zapisz() throws IOException {
        StanAplikacji stan = new StanAplikacji();
        stan.gry = Gra.wszystkieGry;
        stan.kolekcjonerzy = Kolekcjoner.wszyscyKolekcjonerzy;
        stan.listingi = Listing.wszystkieListingi;
        stan.oferty = Oferta.wszystkieOferty;
        try (ObjectOutputStream wyjscie = new ObjectOutputStream(new FileOutputStream(PLIK))) {
            wyjscie.writeObject(stan);
        }
    }

    public static void wczytaj() throws IOException, ClassNotFoundException {
        try (ObjectInputStream wejscie = new ObjectInputStream(new FileInputStream(PLIK))) {
            StanAplikacji stan = (StanAplikacji) wejscie.readObject();
            Gra.wszystkieGry = stan.gry;
            Kolekcjoner.wszyscyKolekcjonerzy = stan.kolekcjonerzy;
            Listing.wszystkieListingi = stan.listingi;
            Oferta.wszystkieOferty = stan.oferty;
        }
    }
}
