import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency
	 * preserving under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//a decomposition is dependency preserving, if local functional dependencies are
		//sufficient to enforce the global properties
		//To check a particular functional dependency a -> b is preserved, 
		//you can run the following algorithm
		//result = a
		//while result has not stabilized
		//	for each table in the decomposition
		//		t = result intersect table 
		//		t = closure(t) intersect table
		//		result = result union t
		//if b is contained in result, the dependency is preserved
		for (FunctionalDependency fd: fds) {
			AttributeSet result = new AttributeSet(fd.left);
			boolean changed = true;
			while (changed) {
				changed = false;
				// for t1
				AttributeSet t = new AttributeSet(result);
				t.retainAll(t1);
				t = closure(t, fds);
				t.retainAll(t1);
				if (result.addAll(t)) {
					changed = true;
				}
				
				// for t2
				t = new AttributeSet(result);
				t.retainAll(t2);
				t = closure(t, fds);
				t.retainAll(t2);
				if (result.addAll(t)) {
					changed = true;
				}
			}
			if (!result.contains(fd.right)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether a decomposition of a table is lossless
	 * under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//Lossless decompositions do not lose information, the natural join is equal to the 
		//original table.
		//a decomposition is lossless if the common attributes for a superkey for one of the
		//tables.
		
		// get t1 intersect t2
		AttributeSet intersect = new AttributeSet(t1);
		intersect.retainAll(t2);
		AttributeSet closure = closure(intersect, fds);
		return closure.containsAll(t1) || closure.containsAll(t2);
	}

	//recommended helper method
	//finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		AttributeSet closure = new AttributeSet(attrs);
		boolean changed = true;
		while (changed) {
			changed = false;
			for (FunctionalDependency fd: fds) {
				if (closure.containsAll(fd.left) && closure.add(fd.right)) {
					changed = true;
					
				}
			}
		}
		return closure;
	}
}
