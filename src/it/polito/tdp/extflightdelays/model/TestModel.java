package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo(400);
		
		System.out.println(model.testLink(11, 297));
		System.out.println(model.testLink(10, 295));
		
		System.out.println(model.findWalk(11, 297));
		System.out.println(model.findWalk(10, 295));

	}

}
