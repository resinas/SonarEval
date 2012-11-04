package es.us.isa.sonareval.evaluators;

import java.util.Map;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

public class LCom4Evaluator implements MetricEvaluator {
	
	private static final String LCOM4 = "lcom4_distribution";
	private static final String NUMCLASSES = "classes";

	public String getMetricName() {
		return LCOM4;
	}
	
	public String[] getMetricsForQuery() {
		return new String[]{LCOM4, NUMCLASSES};
	}


	public double eval(Resource m) {
		Measure lcom4 = m.getMeasure(LCOM4);
		Measure numClasses = m.getMeasure(NUMCLASSES);
		double lcomClasses = 0;
		Map<String, String> data = lcom4.getDataAsMap(";");
		for (String s: data.values()) {
			lcomClasses += Float.parseFloat(s);
		}
		
		double index = lcomClasses/numClasses.getValue();
		double result = 0;
		
		if (index < 20)
			result = 10;
		
		return result;
	}

}
