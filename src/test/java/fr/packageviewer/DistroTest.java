package fr.packageviewer;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class DistroTest<T extends Distribution> {

	protected abstract T createInstance();

	protected List<SearchedPackage> helperSearchPackage(String packageName) {
		Distribution distribution = createInstance();
		Future<List<SearchedPackage>> future = distribution.searchPackage(packageName);
		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	protected Package helperGetPackageTree(String packageName, int depth) {
		Distribution distribution = createInstance();
		Future<Package> future = distribution.getPackageTree(packageName, depth);
		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testBasicQueryDoNotFail() {
		helperGetPackageTree("bash", 0);
	}

	@Test
	public void testBashPackageHasNameBash() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 0).get();
		Assertions.assertEquals(pack.getName(), "bash");
	}

	@Test
	public void testQueryWithDepth0HasNoDeps() {
		Distribution arch = new ArchDistribution();
		Package pack = helperGetPackageTree("bash", 0);
		Assertions.assertEquals(pack.getDeps().size(), 0);
	}

	@Test
	public void testQueryWithDepth1hasOneLevelOfDeps() {
		Package pack = helperGetPackageTree("bash", 1);
		Assertions.assertNotEquals(pack.getDeps().size(), 0);
		for (Package dep : pack.getDeps()) {
			Assertions.assertEquals(dep.getDeps().size(), 0);
		}
	}

	@Test
	public void testBashDescriptionIsNotEmpty() {
		Package pack = helperGetPackageTree("bash", 1);
		Assertions.assertFalse(pack.getDescription().isEmpty());
	}

	@Test
	public void testBashVersionIsNotEmpty() {
		Package pack = helperGetPackageTree("bash", 1);
		Assertions.assertFalse(pack.getVersion().isEmpty());
	}

	@Test
	public void testInvalidPackageReturnsNull() {
		Package pack = helperGetPackageTree("lndhsgudw", 1);
		Assertions.assertNull(pack);
	}

	@Test
	public void testThatBashSearchReturnsResults() {
		Assertions.assertNotEquals(helperSearchPackage("bash").size(), 0);
	}

	@Test
	public void testThatBashSearchContainsBash() {
		for (SearchedPackage pack : helperSearchPackage("bash")) {
			if (pack.getName().equals("bash")) {
				Assertions.assertTrue(true);
				return;
			}
		}
		Assertions.fail("No package named 'bash' in results");
	}
}
