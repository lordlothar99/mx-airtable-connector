package jsonstructure.export;

public class SortNumberProvider {
    private static Long sortNumber = 0L;

    public static Long next() {
        SortNumberProvider.sortNumber = SortNumberProvider.sortNumber + 1;
        return SortNumberProvider.sortNumber;
    }
}
