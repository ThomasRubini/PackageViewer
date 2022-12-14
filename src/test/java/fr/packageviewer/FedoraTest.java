package fr.packageviewer;

import fr.packageviewer.distribution.FedoraDistribution;

public class FedoraTest extends DistroTest<FedoraDistribution> {
	@Override
	protected FedoraDistribution createInstance() {
		return new FedoraDistribution();
	}
}
