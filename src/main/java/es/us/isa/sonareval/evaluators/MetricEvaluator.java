package es.us.isa.sonareval.evaluators;

import org.sonar.wsclient.services.Resource;

public interface MetricEvaluator {

	public String getMetricName();
	
	public double eval(Resource m);
	
	public String[] getMetricsForQuery();
}
