package geocell;

import java.util.List;

import geocell.model.LocationCapable;

public interface LocationCapableRepositorySearch<T extends LocationCapable> {

	List<T> search(List<String> geocells);
	
}
