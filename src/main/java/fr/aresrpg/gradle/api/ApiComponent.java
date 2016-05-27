package fr.aresrpg.gradle.api;

import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.internal.component.SoftwareComponentInternal;
import org.gradle.api.internal.component.Usage;

import java.util.Collections;
import java.util.Set;

public class ApiComponent implements SoftwareComponentInternal{
	public static final String NAME = "api";
	private final ApiUsage usage;

	public ApiComponent(SoftwareComponentInternal parent , PublishArtifact artifact) {
		this.usage = new ApiUsage(parent.getUsages().iterator().next() , artifact);
	}

	@Override
	public Set<Usage> getUsages() {
		return Collections.singleton(usage);
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static class ApiUsage implements Usage{
		private final Usage parent;
		private final PublishArtifact artifact;

		public ApiUsage(Usage parent, PublishArtifact artifact) {
			this.parent = parent;
			this.artifact = artifact;
		}

		@Override
		public Set<PublishArtifact> getArtifacts() {
			return Collections.singleton(artifact);
		}

		@Override
		public Set<ModuleDependency> getDependencies() {
			return parent.getDependencies();
		}

		@Override
		public String getName() {
			return parent.getName();
		}
	}
}
