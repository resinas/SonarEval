package es.us.isa.sonareval;

import static es.us.isa.sonareval.conditions.Conditions.*;
import static es.us.isa.sonareval.evaluators.ConditionEvaluator.createForMetric;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import es.us.isa.sonareval.evaluators.LCom4Evaluator;
import es.us.isa.sonareval.rubrics.Rubric;

public class Launch {
	public static void main(String [] args) {
		Sonar sonar = Sonar.create("http://clinker.isa.us.es/sonar", "admin", "s9mGextIT504yKNrr3s8");
		
		Resource struts = sonar.find(ResourceQuery.createForMetrics("isg2.grupo2:footy-manager", "coverage", "lines", "violations"));
		Measure measure = struts.getMeasure("coverage");
		System.out.println("coverage:" + measure.getFormattedValue());

		ResourceQuery q = new ResourceQuery();
		q.setMetrics("lcom4_distribution", "lines", "violations");
		List<Resource> resList = sonar.findAll(q);
		for (Resource r: resList) {
			Measure m = r.getMeasure("lcom4_distribution");
			System.out.println(m);
			System.out.println(r.getLongName()+" lcom4:" + m.getVariation1());
		}
		
		Rubric designRubric = createDesignRubric();
		Rubric testRubric = createTestRubric();
		Rubric docRubric = createDocRubric();
		
		List<String> metricsForQuery = designRubric.getResourcesForQuery();
		metricsForQuery.addAll(testRubric.getResourcesForQuery());
		metricsForQuery.addAll(docRubric.getResourcesForQuery());
		metricsForQuery.add("ncloc");
		System.out.println(metricsForQuery);
		
		ResourceQuery query = new ResourceQuery();
		query.setMetrics(metricsForQuery.toArray(new String[0]));
		resList = sonar.findAll(query);
		
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
		StringBuilder sb = new StringBuilder();
		String separator = ";";
		for (Resource res: resList){
			double design = designRubric.evalResource(res);
			double test = testRubric.evalResource(res);
			double doc = docRubric.evalResource(res);
			sb.append(res.getLongName());
			sb.append(separator);
			sb.append(nf.format(design));
			sb.append(separator);
			sb.append(nf.format(test));
			sb.append(separator);
			sb.append(nf.format(doc)); 
			sb.append(separator);
			sb.append(res.getMeasureValue("ncloc"));
//			sb.append(separator);
//			sb.append(nf.format(design*0.5 + test*0.2 + doc * 0.15));
			sb.append("\n");
		}
		
		System.out.println(sb);
	

}

//	private static void showRubric(Sonar sonar, Rubric rubric) {
//		List<Resource> resList;
//		resList = sonar.findAll(rubric.createQuery());
//		
//		for (Resource res: resList){
//			System.out.println(res.getLongName() + " eval: " + rubric.evalResource(res)); 
//		}
//	}

	private static Rubric createDesignRubric() {
		Rubric designRubric = new Rubric();
		
//		designRubric.addEvaluator(createForMetric("violations_density")
//				.when(greaterEqualThan(95)).value(10.0)
//				.when(between(80, 95)).value(5.0)
//				.when(lowerEqualThan(80)).value(0.0).build()
//			);
		
		designRubric.addEvaluator(createForMetric("package_tangle_index")
				.when(equal(0)).value(10.0)
				.when(between(0,40)).value(5.0).build()
			);
		
		designRubric.addEvaluator(createForMetric("duplicated_lines")
				.when(equal(0)).value(10.0).build()				
				);
		
		designRubric.addEvaluator(new LCom4Evaluator());
		
		designRubric.addEvaluator(createForMetric("function_complexity")
				.when(lowerThan(3)).value(10.0)
				.when(greaterEqualThan(3)).value(0.0).build()
				);
		return designRubric;
	}
	
	private static Rubric createTestRubric() {
		Rubric testRubric = new Rubric();
		
		testRubric.addEvaluator(createForMetric("line_coverage")
				.when(greaterEqualThan(40)).value(10.0)
				.when(between(20,40)).value(5.0)
				.when(lowerEqualThan(20)).value(0.0).build()
				);
		
		testRubric.addEvaluator(createForMetric("test_success_density")
				.when(equal(100)).value(10.0).build()				
				);
		
		return testRubric;
	}
	
	private static Rubric createDocRubric() {
		Rubric docRubric = new Rubric();
		
		docRubric.addEvaluator(createForMetric("comment_lines_density")
				.when(greaterEqualThan(25)).value(10.0)
				.when(between(10,25)).value(5.0).build()
				);
		
		return docRubric;
	}
}
