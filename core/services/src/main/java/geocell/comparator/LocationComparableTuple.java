package geocell.comparator;


import geocell.model.LocationCapable;
import geocell.model.Tuple;


/**
 * This class is used to merge lists of Tuple<T, Double>. Lists are sorted following Double value but are equals only if T.key (same entity) are equals.
 *
 * @author Alexandre Gellibert
 *
 * @param <T>
 */
public class LocationComparableTuple<T extends LocationCapable> extends Tuple<T ,Double> implements Comparable<LocationComparableTuple<T>>{

    public LocationComparableTuple(T first, Double second) {
        super(first, second);
    }

    @Override
    public int compareTo(LocationComparableTuple<T> o) {
        if(o == null) {
            return -1;
        }
        int doubleCompare = this.getSecond().compareTo(o.getSecond());
        if(doubleCompare == 0) {
            return this.getFirst().getKeyString().compareTo(o.getFirst().getKeyString());
        } else {
            return doubleCompare;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LocationComparableTuple<LocationCapable> other = (LocationComparableTuple<LocationCapable>) obj;
        if (getFirst() == null) {
            return other.getFirst() == null;
        } else return getFirst().getKeyString().equals(other.getFirst().getKeyString());
    }


    @Override
    public int hashCode() {
        return getFirst().getKeyString().hashCode();
    }

}