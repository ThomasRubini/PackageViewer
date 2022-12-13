package fr.packageviewer;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class ArchTest {

	@Test
	public void testBasicQueryDoNotFail(){
		Distribution arch = new ArchDistribution();
		arch.getPackageTree("bash", 0);
	}

	@Test
	public void testBashPackageHasNameBash() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 0).get();
		Assertions.assertEquals(pack.getName(), "bash");
	}
	@Test
	public void testQueryWithDepth0HasNoDeps() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 0).get();
		Assertions.assertEquals(pack.getDeps().size(), 0);
	}
	@Test
	public void testQueryWithDepth1hasOneLevelOfDeps() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 1).get();
		Assertions.assertNotEquals(pack.getDeps().size(), 0);
		for(Package dep : pack.getDeps()){
			Assertions.assertEquals(dep.getDeps().size(), 0);
		}
	}

	@Test
	public void testBashIsInCore() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 1).get();
		Assertions.assertEquals(pack.getRepo(), "core");
	}

	@Test
	public void testBashDescriptionIsNotEmpty() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 1).get();
		Assertions.assertFalse(pack.getDescription().isEmpty());
	}
	@Test
	public void testBashVersionIsNotEmpty() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("bash", 1).get();
		Assertions.assertFalse(pack.getVersion().isEmpty());
	}

	@Test
	public void testInvalidPackageReturnsNull() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Package pack = arch.getPackageTree("lndhsgudw", 1).get();
		Assertions.assertNull(pack);
	}

	@Test
	public void testThatBashSearchReturnsResults() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		Assertions.assertNotEquals(arch.searchPackage("bash").get().size(), 0);
	}
	@Test
	public void testThatBashSearchContainsBash() throws ExecutionException, InterruptedException {
		Distribution arch = new ArchDistribution();
		for(SearchedPackage pack : arch.searchPackage("bash").get()){
			if(pack.getName().equals("bash")){
				Assertions.assertTrue(true);
				return;
			}
		}
		Assertions.fail("No package named 'bash' in results");
	}
}
