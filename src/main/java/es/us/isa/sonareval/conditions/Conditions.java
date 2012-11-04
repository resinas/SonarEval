package es.us.isa.sonareval.conditions;

public class Conditions {

	public static Condition greaterThan(final double d) {
		return new Condition() {
			public boolean eval(Double value) {
				return value > d;
			}
		};
	}

	public static Condition lowerThan(final double d) {
		return new Condition() {

			public boolean eval(Double value) {
				return value < d;
			}
		};
	}

	public static Condition greaterEqualThan(final double d) {
		return new Condition() {

			public boolean eval(Double value) {
				return value >= d;
			}
		};
	}

	public static Condition lowerEqualThan(final double d) {
		return new Condition() {

			public boolean eval(Double value) {
				return value <= d;
			}
		};
	}
	
	public static Condition equal(final double d) {
		return new Condition() {
			
			public boolean eval(Double value) {
				return value == d;
			}
		};
	}
	
	public static Condition between(final double start, final double end) {
		return new Condition() {
			
			public boolean eval(Double value) {
				return value >= start && value <= end;
			}
		};
	}
}
