package fr.aresrpg.gradle.api.task;

import org.gradle.jvm.tasks.Jar;

import javax.inject.Inject;

public class ApiJarTask extends Jar{
	public static final String NAME = "apijar";

	@Inject
	public ApiJarTask(){
		setClassifier("api");
	}
}
