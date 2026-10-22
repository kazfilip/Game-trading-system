import java.util.List;

public interface Sprzedajacy {
    List<Listing> getListingi();
    void dodajListing(Listing listing);
    int getLimitListingow();
}
