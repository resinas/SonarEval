package es.us.isa.sonareval.evaluators;


import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import es.us.isa.sonareval.conditions.Condition;

public class ConditionEvaluator implements MetricEvaluator {
	private static final Log log = LogFactory.getLog(ConditionEvaluator.class);

	public static ConditionEvaluatorBuilder createForMetric(String name) {
		return new ConditionEvaluatorBuilder(name);
	}
	
	private Map<Double, Condition> conditions;
	private String metricName;
	
	public ConditionEvaluator(String name, Map<Double, Condition> conditions) {
		
		metricName = name;
		this.conditions = conditions;
		
	}

	public String getMetricName() {
		return metricName;
	}

	public double eval(Resource r) {
		double evaluation = 0;
		Measure m = r.getMeasure(metricName);
		if (m == null) {
			log.warn("Problems with metric "+metricName+" for "+r.getName());
			return evaluation;
		}
		for (Entry<Double, Condition> entry: conditions.entrySet()) {
			if (entry.getValue().eval(m.getValue()))
				evaluation = entry.getKey();
		}

		return evaluation;
	}
	
	public String[] getMetricsForQuery() {
		return new String[]{metricName};
	}

}
