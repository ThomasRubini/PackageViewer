package fr.packageviewer;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.pack.Package;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArchTest extends DistroTest<ArchDistribution> {
	@Override
	protected ArchDistribution createInstance() {
		return new ArchDistribution();
	}

	@Test
	public void testBashIsInCore() {
		Package pack = helperGetPackageTree("bash", 0);
		Assertions.assertEquals(pack.getRepo(), "core");
	}
}
