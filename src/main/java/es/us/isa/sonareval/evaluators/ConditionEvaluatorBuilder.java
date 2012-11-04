package es.us.isa.sonareval.evaluators;

import java.util.HashMap;
import java.util.Map;

import es.us.isa.sonareval.conditions.Condition;

public class ConditionEvaluatorBuilder {
	private String metricname;
	private Map<Double, Condition> conditions;
	
	public ConditionEvaluatorBuilder(String name) {
		metricname = name;
		conditions = new HashMap<Double, Condition>();
	}
	
	public ConditionEvaluatorBuilder(String name, Map<Double, Condition> conds) {
		metricname = name;
		conditions = conds;
	}
	
	public ConditionEvaluator build() {
		return new ConditionEvaluator(metricname, conditions);
	}
	
	public WhenBuilder when(Condition cond) {
		return new WhenBuilder(this, cond);
	}
	
	private void addCondition(Condition cond, Double val) {
		conditions.put(val, cond);
	}
	
	public class WhenBuilder {
		private ConditionEvaluatorBuilder builder;
		private Condition condition;
		public WhenBuilder(ConditionEvaluatorBuilder b, Condition cond) {
			builder = b;
			condition = cond;
		}
		
		public ConditionEvaluatorBuilder value(Double val) {
			builder.addCondition(condition, val);
			
			return builder;
		}
		
	}

}