package es.us.isa.sonareval.rubrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.wsclient.services.Resource;

import es.us.isa.sonareval.evaluators.MetricEvaluator;

public class Rubric {
	
	private List<MetricEvaluator> evaluators;
	
	public Rubric() {
		evaluators = new ArrayList<MetricEvaluator>();
	}
	
	public void addEvaluator(MetricEvaluator ... eval) {		
		evaluators.addAll(Arrays.asList(eval));
	}
	
	public double evalResource(Resource r) {
		double calification = 0;
		for (MetricEvaluator ev: evaluators) {
			//Measure m = r.getMeasure(ev.getMetricName());
			calification += ev.eval(r);
		}
		
		return calification/evaluators.size();
	}
	
	public List<String> getResourcesForQuery() {
		List<String> l = new ArrayList<String>();
		for (MetricEvaluator ev: evaluators) {
			l.addAll(Arrays.asList(ev.getMetricsForQuery()));
		}
		
		return l;
	}

}
