package geocell.comparator;

import java.util.Comparator;

import geocell.model.Tuple;

public class DoubleTupleComparator implements Comparator<Tuple<int[], Double>> {

	@Override
	public int compare(Tuple<int[], Double> o1, Tuple<int[], Double> o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		if(o1 == null) {
			return -1;
		}
		if(o2 == null) {
			return 1;
		}
		return o1.getSecond().compareTo(o2.getSecond());
	}

}